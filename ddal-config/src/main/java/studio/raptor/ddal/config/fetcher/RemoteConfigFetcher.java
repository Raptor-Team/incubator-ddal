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

package studio.raptor.ddal.config.fetcher;

import com.google.common.base.Preconditions;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.cmdb.Config;
import studio.raptor.cmdb.ConfigFile;
import studio.raptor.cmdb.ConfigService;
import studio.raptor.cmdb.core.enums.ConfigFileFormat;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.common.ConfigConstant;

/**
 * 远程配置获取接口
 *
 * @author Charley
 * @since 1.0
 */
public abstract class RemoteConfigFetcher extends ConfigFetcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalConfigFetcher.class);

  @Override
  public String fetchingMode() {
    return ConfigConstant.FETCH_MODE_REMOTE;
  }

  @Override
  public String getFileString(String filePath) throws GenericException {
    Preconditions.checkNotNull(filePath, "filePath can not be null");
    LOGGER.info("Get file string from remote system, file namespace: {}", filePath);
    ConfigFile configFile = ConfigService.getConfigFile(filePath, ConfigFileFormat.XML);
    return configFile.getContent();
  }

  @Override
  public Map<String, String> getProperties(String filePath) throws GenericException {
    Preconditions.checkNotNull(filePath, "filePath can not be null");
    LOGGER.info("Get properties from remote system, file namespace: {}", filePath);
    return config2Map(ConfigService.getConfig(filePath));
  }

  private static Map<String, String> config2Map(Config config) {
    Set<String> keys = config.getPropertyNames();

    Map<String, String> routeMapping = new HashMap<String, String>(keys.size());
    for (String key : keys) {
      String value = config.getProperty(key, null);
      routeMapping.put(key, value);
    }
    return routeMapping;
  }
}
