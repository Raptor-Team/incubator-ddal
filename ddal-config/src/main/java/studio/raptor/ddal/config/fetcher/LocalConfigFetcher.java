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
import java.util.Properties;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.common.ConfigConstant;
import studio.raptor.ddal.config.common.FileLoader;

/**
 * 本地配置获取接口
 *
 * @author Charley
 * @since 1.0
 */
public abstract class LocalConfigFetcher extends ConfigFetcher {

  private static final Logger LOGGER = LoggerFactory.getLogger(LocalConfigFetcher.class);

  @Override
  public String fetchingMode() {
    return ConfigConstant.FETCH_MODE_LOCAL;
  }

  @Override
  public String getFileString(String filePath) throws GenericException {
    Preconditions.checkNotNull(filePath, "filePath can not be null");
    LOGGER.info("Get file string from local system, file name: {}", filePath);
    return FileLoader.readLocalFile(filePath);
  }

  @Override
  public Map<String, String> getProperties(String filePath) throws Exception {
    Preconditions.checkNotNull(filePath, "filePath can not be null");
    LOGGER.info("Get properties from local system, file name: {}", filePath);
    return properties2Map(FileLoader.loadLocalProps(filePath));
  }

  private static Map<String, String> properties2Map(Properties properties) {
    Set<String> keys = properties.stringPropertyNames();

    Map<String, String> routeMapping = new HashMap<>(keys.size());
    for (String key : keys) {
      String value = properties.getProperty(key);
      routeMapping.put(key, value);
    }
    return routeMapping;
  }

}
