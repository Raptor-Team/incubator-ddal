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

package studio.raptor.ddal.config.main;

import org.junit.Test;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.config.ServerConfig;

import static org.junit.Assert.assertNotNull;

public class ServerConfigTest {

  @Test
  public void testServerConfig() throws GenericException {
    ServerConfig serverConfig = ServerConfig.getInstance();
    System.out.println(serverConfig);
    assertNotNull(serverConfig.getParams());
  }
}
