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

package studio.raptor.ddal.config.common;

public class ConfigConstant {

  public static final String DEFAULT_CHARSET = "UTF-8";

  /*
   * 默认CPU处理器个数
   */
  public static final int DEFAULT_CPU_CORES = Runtime.getRuntime().availableProcessors();

  public static final String FETCH_MODE_LOCAL = "L";
  public static final String FETCH_MODE_REMOTE = "R";

  // 数据库密码是否需要解密的开关
  public static final String PROP_KEY_CONFIG_DECRYPT_ENABLED = "config.decrypt";
  // 解密密钥文件路径
  public static final String PROP_KEY_CONFIG_DECRYPT_KEY_FILE = "config.decrypt.key.file";
  // 解密密钥文件串
  public static final String PROP_KEY_CONFIG_DECRYPT_KEY = "config.decrypt.key";

  // 解密密钥文件的默认目录名，该目录请在用户目录下创建。
  public static final String CONFIG_DEFAULT_DECRYPT_KEY_FILE_DIR = ".raptor_ddal";
  // 解密密钥文件默认文件名
  public static final String CONFIG_DEFAULT_DECRYPT_KEY_FILE_NAME = "decrypt.key";
}
