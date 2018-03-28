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

package studio.raptor.ddal.core.parser.oracle;

import studio.raptor.ddal.core.constants.DatabaseType;

/**
 * @author Sam
 * @since 3.0.0
 */
public class OracleSQLParseTest {

    private DatabaseType databaseType = DatabaseType.Oracle;

//    /**
//     * Oracle解析测试。 表名带schema， 期望的解析结果应去掉schema
//     */
//    @Test
//    public void parseUpdateWithSchema() {
//        String sql = "update test.customer set name = 'jack' where id = 1";
//
//        SQLParseEngine parseEngine =
//            SQLParserFactory
//                .create(databaseType, sql, Collections.emptyList(),
//                    ShardConfig.getInstance().getVirtualDb("crmdb"));
//        SQLParsedResult parsedResult = parseEngine.parse();
//        Assert.assertNotNull(parsedResult);
//        Assert.assertNotNull(parsedResult.getRouteContext());
//        Assert.assertNotNull(parsedResult.getRouteContext().getSqlBuilder());
//        Assert.assertThat(parsedResult.getRouteContext().getSqlBuilder().toSQL(), Is.is("UPDATE customer SET name = 'jack' WHERE id = 1"));
//    }
//
//
//    /**
//     * Oracle解析测试。
//     */
//    @Test
//    public void parseUpdateNoSchema() {
//        String sql = "update customer set name = 'jack' where id = 1";
//
//        SQLParseEngine parseEngine =
//                SQLParserFactory.create(databaseType, sql, Collections.emptyList(), ShardConfig.getInstance().getVirtualDb("crmdb"));
//        SQLParsedResult parsedResult = parseEngine.parse();
//        Assert.assertNotNull(parsedResult);
//        Assert.assertNotNull(parsedResult.getRouteContext());
//        Assert.assertNotNull(parsedResult.getRouteContext().getSqlBuilder());
//        Assert.assertThat(parsedResult.getRouteContext().getSqlBuilder().toSQL(), Is.is("UPDATE customer SET name = 'jack' WHERE id = 1"));
//    }
//
//
//    @Test
//    public void parseInsertNoSchema() {
//        String sql = "insert into customer (id, name, gender, create_date) values (11, '萧景琰', 1, '2016-11-01 11:01:00')";
//
//        SQLParseEngine parseEngine =
//                SQLParserFactory.create(databaseType, sql, Collections.emptyList(), ShardConfig.getInstance().getVirtualDb("crmdb"));
//        SQLParsedResult parsedResult = parseEngine.parse();
//        Assert.assertNotNull(parsedResult);
//        Assert.assertNotNull(parsedResult.getRouteContext());
//        Assert.assertNotNull(parsedResult.getRouteContext().getSqlBuilder());
//        Assert.assertThat(parsedResult.getRouteContext().getSqlBuilder().toSQL(), Is.is("INSERT INTO customer (id, name, gender, create_date) VALUES (11, '萧景琰', 1, '2016-11-01 11:01:00')"));
//    }
//
//
//    @Test
//    public void parseInsertHasSchema() {
//        String sql = "insert into test.customer (id, name, gender, create_date) values (11, '萧景琰', 1, '2016-11-01 11:01:00')";
//        SQLParseEngine parseEngine =
//                SQLParserFactory.create(databaseType, sql, Collections.emptyList(), ShardConfig.getInstance().getVirtualDb("crmdb"));
//        SQLParsedResult parsedResult = parseEngine.parse();
//        Assert.assertNotNull(parsedResult);
//        Assert.assertNotNull(parsedResult.getRouteContext());
//        Assert.assertNotNull(parsedResult.getRouteContext().getSqlBuilder());
//        Assert.assertThat(parsedResult.getRouteContext().getSqlBuilder().toSQL(), Is.is("INSERT INTO customer (id, name, gender, create_date) VALUES (11, '萧景琰', 1, '2016-11-01 11:01:00')"));
//    }
}
