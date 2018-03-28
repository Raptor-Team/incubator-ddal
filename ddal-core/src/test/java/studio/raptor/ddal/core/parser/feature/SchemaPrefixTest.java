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

package studio.raptor.ddal.core.parser.feature;

import studio.raptor.ddal.config.model.shard.VirtualDb;

/**
 * SQL解析带 schema 特性
 * Created by Sam on 9/30/16.
 */
public class SchemaPrefixTest {

    private static VirtualDb virtualDb;

//    @BeforeClass
//    public static void setUp() throws Exception {
//        ShardConfig shardConfig = ShardConfig.getInstance();
//        virtualDb = shardConfig.getVirtualDb("crmdb");
//    }
//    @Test
//    public void normalSqlSchemaPrefix() {
//        SQLParsedResult sqlParsedResult = SQLParserFactory.create(DatabaseType.MySQL,
//                "SELECT o.* FROM customer o WHERE o.user_id=? AND o.order_id=?",
//                new LinkedList<>(), virtualDb).parse();
//        Assert.assertThat(
//                new SQLExecutionUnit("ds_0", sqlParsedResult.getRouteContext().getSqlBuilder()).getSql(),
//                Is.is("SELECT o.* FROM t_order o WHERE o.user_id = ? AND o.order_id = ?"));
//    }
//
//    @Test
//    public void joinSqlSchemaPrefix() {
//        SQLParsedResult sqlParsedResult = SQLParserFactory.create(DatabaseType.MySQL,
//                "SELECT i.* FROM customer o JOIN area i ON o.order_id=i.order_id WHERE o.user_id=? AND o.order_id=?",
//                new LinkedList<>(), virtualDb).parse();
//        Assert.assertThat(
//                new SQLExecutionUnit("ds_0", sqlParsedResult.getRouteContext().getSqlBuilder()).getSql(),
//                Is.is("SELECT i.* FROM t_order o JOIN t_order_item i ON o.order_id = i.order_id WHERE o.user_id = ? AND o.order_id = ?"));
//    }
}
