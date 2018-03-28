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

package studio.raptor.ddal.config.event;

import studio.raptor.ddal.config.reloading.ReloadingController;

/**
 * @author Sam
 * @since 3.0.0
 */
public class LocalShardConfigChangeEvent {

  private final Object ctrlParam;

  // 保存重载控制器的引用
  private final ReloadingController controller;

  public LocalShardConfigChangeEvent(ReloadingController controller, Object ctrlParam) {
    this.controller = controller;
    this.ctrlParam = ctrlParam;
  }

  public Object getCtrlParam() {
    return ctrlParam;
  }

  ReloadingController getController() {
    return controller;
  }
}
