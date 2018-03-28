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

import java.util.Iterator;
import java.util.ServiceLoader;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.exception.ConfigErrCodes;

/**
 * Config获取器持有者
 *
 * @author Charley
 * @since 1.0
 */
public class FetcherHolder {

  private static ConfigFetcher configFetcher;

  public static ConfigFetcher get() {
    if (null == configFetcher) {
      ServiceLoader<ConfigFetcher> serviceLoader = ServiceLoader.load(ConfigFetcher.class);
      Iterator<ConfigFetcher> fetcherIt = serviceLoader.iterator();
      if (fetcherIt.hasNext()) {
        configFetcher = fetcherIt.next();
      } else {
        throw new GenericException(ConfigErrCodes.CONFIG_106);
      }
    }
    return configFetcher;
  }
}
