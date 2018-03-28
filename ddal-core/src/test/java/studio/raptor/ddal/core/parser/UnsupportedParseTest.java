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

package studio.raptor.ddal.core.parser;

import org.junit.BeforeClass;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.shard.VirtualDb;

public final class UnsupportedParseTest {

    private static VirtualDb virtualDb;

    @BeforeClass
    public static void setUp() throws Exception {
        ShardConfig shardConfig = ShardConfig.getInstance();
        virtualDb = shardConfig.getVirtualDb("crmdb");
    }
    
//    @Test(expected = GenericException.class)
//    public void assertCreate() throws GenericException {
//      SQLParserFactory
//          .create(DatabaseType.MySQL, "CREATE TABLE `customer` (id BIGINT(10))",
//              Collections.emptyList(), virtualDb);
//    }
//
//    @Test(expected = GenericException.class)
//    public void assertDrop() throws GenericException {
//        SQLParserFactory.create(DatabaseType.MySQL, "DROP TABLE `customer`", Collections.emptyList(), virtualDb);
//    }
//
//    @Test(expected = GenericException.class)
//    public void assertTruncate() throws GenericException {
//        SQLParserFactory.create(DatabaseType.MySQL, "TRUNCATE `customer`", Collections.emptyList(), virtualDb);
//    }
//
//    @Test(expected = GenericException.class)
//    public void assertAlter() throws GenericException {
//        SQLParserFactory.create(DatabaseType.MySQL, "ALTER TABLE `customer` ADD COLUMN `other` VARCHAR(45)", Collections.emptyList(), virtualDb);
//    }
//
//    @Test(expected = GenericException.class)
//    public void assertNegativeLimitRowCount() throws GenericException {
//        SQLParserFactory.create(DatabaseType.MySQL, "select * from customer limit -2,-1", Collections.emptyList(), virtualDb).parse();
//    }
    
}
