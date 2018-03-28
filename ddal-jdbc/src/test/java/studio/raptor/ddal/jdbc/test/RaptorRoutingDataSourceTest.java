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

package studio.raptor.ddal.jdbc.test;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.jdbc.RaptorDataSource;
import studio.raptor.ddal.jdbc.RaptorRoutingDataSource;

/**
 * Raptor datasource test.
 *
 * @author Sam
 * @since 3.0
 */
public class RaptorRoutingDataSourceTest extends AutoPrepareTestingEnv {

  @Test
  public void testRoutingDataSourceByDefaultFile() throws Exception {
    RaptorRoutingDataSource router = new RaptorRoutingDataSource("mysql");
    RaptorDataSource dataSource1 = router.route("aa");
    RaptorDataSource dataSource2 = router.route("bb");
    Assert.assertNotNull(dataSource1);
    Assert.assertNotNull(dataSource2);
    Assert.assertNotEquals(dataSource1, dataSource2);
  }
}
