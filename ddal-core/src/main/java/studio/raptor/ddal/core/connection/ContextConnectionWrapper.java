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

package studio.raptor.ddal.core.connection;

/**
 * 执行上下文使用的后段连接包装类。
 *
 * @author Sam
 * @since 3.0.0
 */
public class ContextConnectionWrapper {

  private BackendConnection readWriteConnection;
  private BackendConnection readonlyConnection;
  /**
   * 当前执行上下文即将使用的连接，由数据源选择器设置。
   */
  private BackendConnection currentConnection;

  public BackendConnection getReadWriteConnection() {
    return readWriteConnection;
  }

  public void setReadWriteConnection(BackendConnection readWriteConnection) {
    this.readWriteConnection = readWriteConnection;
  }

  public BackendConnection getReadonlyConnection() {
    return readonlyConnection;
  }

  public void setReadonlyConnection(BackendConnection readonlyConnection) {
    this.readonlyConnection = readonlyConnection;
  }

  public BackendConnection getCurrentConnection() {
    return currentConnection;
  }

  public void setCurrentConnection(BackendConnection currentConnection) {
    this.currentConnection = currentConnection;
  }
}
