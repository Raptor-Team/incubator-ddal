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

package studio.raptor.ddal.config.config;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.exception.ConfigErrCodes;
import studio.raptor.ddal.config.exception.ConfigNotFoundException;

public class SystemProperties extends AbstractConfig {

  private static final Logger LOGGER = LoggerFactory.getLogger(SystemProperties.class);

  private Map<String, String> mapper = new HashMap<>();

  private boolean isLoaded = false;

  private static SystemProperties instance = new SystemProperties();

  public static SystemProperties getInstance() {
    return instance;
  }

  private SystemProperties() throws GenericException {
    try{
      this.mapper = this.getPropsMap(this.configFetcher.getSystemPropPath());
      isLoaded = true;
    }catch(Exception e){
      if(e instanceof ConfigNotFoundException){
        LOGGER.warn("The system.properties file not found!");
      }else{
        throw new GenericException(ConfigErrCodes.CONFIG_101, e, "", this.configFetcher.getSystemPropPath());
      }
    }

  }

  @Override
  public void reload() {
    throw new UnsupportedOperationException("SystemProperties reloading is not supported.");
  }

  public Map<String, String> getMapper() {
    return mapper;
  }

  public String get(String key) {
    return getMapper().get(key);
  }

  public boolean isLoaded() {
    return isLoaded;
  }
}
