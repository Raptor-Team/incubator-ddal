/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.ddal.config.reloading;

import studio.raptor.ddal.common.event.NativeEventBus;
import studio.raptor.ddal.config.event.LocalShardConfigChangeEvent;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ReloadingController {

  private final ReloadingDetector detector;
  /**
   * A flag whether this controller is in reloading state.
   */
  private boolean reloadingState;

  public ReloadingController(ReloadingDetector detector) {
    if (null == detector) {
      throw new IllegalArgumentException("ReloadingDetector must not be null!");
    }
    this.detector = detector;
  }

  public ReloadingDetector getDetector() {
    return detector;
  }

  /**
   * Performs a check whether a reload operation is necessary.
   *
   * @param data additional data for an event notification
   * @return a flag whether a reload operation is necessary
   */
  public boolean checkForReloading(Object data) {
    boolean sendEvent = false;
    synchronized (this) {
      if (isInReloadingState()) {
        return true;
      }
      if (getDetector().isReloadingRequired()) {
        sendEvent = true;
        reloadingState = true;
      }
    }
    if (sendEvent) {
      NativeEventBus.get().post(new LocalShardConfigChangeEvent(this, null));
      return true;
    }
    return false;
  }

  /**
   * Tests whether this controller is in <em>reloading state</em>. A return
   * value of <b>true</b> means that a previous invocation of
   * {@code checkForReloading()} has detected the necessity for a reload
   * operation, but {@code resetReloadingState()} has not been called yet. In
   * this state no further reloading checks are possible.
   *
   * @return a flag whether this controller is in reloading state
   */
  public synchronized boolean isInReloadingState() {
    return reloadingState;
  }


  /**
   * Resets the reloading state. This tells the controller that reloading has
   * been performed and new checks are possible again. If this controller is
   * not in reloading state, this method has no effect.
   */
  public synchronized void resetReloadingState() {
    if (isInReloadingState()) {
      getDetector().reloadingPerformed();
      reloadingState = false;
    }
  }
}
