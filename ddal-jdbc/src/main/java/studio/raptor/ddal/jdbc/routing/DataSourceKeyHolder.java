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

package studio.raptor.ddal.jdbc.routing;

/**
 * 数据源名称保持
 *
 * @author Charley
 * @since 1.0
 */
public class DataSourceKeyHolder {

  //线程本地环境
  private static final ThreadLocal<String> keyHolder = new ThreadLocal<>();

  //设置数据源
  public static void set(String sourceKey) {
    keyHolder.remove();
    keyHolder.set(sourceKey);
  }

  //获取数据源
  public static String get() {
    return keyHolder.get();
  }

  //清除数据源
  public static void clear() {
    keyHolder.remove();
  }
}
