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

/**
 * An interface to be implemented by objects which can detect whether a reload
 * operation is required.
 *
 * @author Sam
 * @since 3.0.0
 */
public interface ReloadingDetector {

  /**
   * 配置是否需要重新加载。
   *
   * @return 需要重新加载时，返回true.
   */
  boolean isReloadingRequired();

  /**
   * 配置重新加载之后调用。
   */
  void reloadingPerformed();
}
