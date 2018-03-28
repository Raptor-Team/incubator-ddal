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

package studio.raptor.ddal.config.exception;

import studio.raptor.ddal.common.exception.ErrorCodeDefinition;

/**
 * error code for config exception
 *
 * @author Sam
 * @since 3.0.0
 */
public interface ConfigErrCodes {

  ErrorCodeDefinition CONFIG_101 = new ErrorCodeDefinition("CONFIG", 101, "load %s error",
      "pls check file path");
  ErrorCodeDefinition CONFIG_102 = new ErrorCodeDefinition("CONFIG", 102, "validate %s error",
      "pls check file path or format");
  ErrorCodeDefinition CONFIG_103 = new ErrorCodeDefinition("CONFIG", 103, "parse %s error",
      "pls check file path or format");
  ErrorCodeDefinition CONFIG_104 = new ErrorCodeDefinition("CONFIG", 104,
      "can not load config files from zookeeper",
      "config file not exists or zookeeper is not avaliable");
  ErrorCodeDefinition CONFIG_105 = new ErrorCodeDefinition("CONFIG", 105,
      "the table [%s] is not in the config file", "pls check the file");
  ErrorCodeDefinition CONFIG_106 = new ErrorCodeDefinition("CONFIG", 106,
      "class of ConfigFetcher's implementation not found",
      "pls check the dir resources/META-INF/services");
  ErrorCodeDefinition CONFIG_107 = new ErrorCodeDefinition("CONFIG", 107,
      "Cannot load a configuration from a directory", "");
  ErrorCodeDefinition CONFIG_108 = new ErrorCodeDefinition("CONFIG", 108,
      "Unable to load the configuration from the URL %s", "");
  ErrorCodeDefinition CONFIG_109 = new ErrorCodeDefinition("CONFIG", 109,
      "Table '%s' not exists in shard-config.xml", "");
}
