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

package studio.raptor.ddal.core.parser.mysql;

/**
 * @author Sam
 * @since 3.0.0
 */
public class MySQLHintParseTest {


  /**
   * 测试分页注释， SQL主语句不带limit关键字
   */
//  @Test
//  public void parseLimitException() {
//    String sql = "/*! hint page(offset=10, count=8)*/select * from customer";
//    SQLParseEngine parseEngine =
//        SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//            ShardConfig.getInstance().getVirtualDb("crmdb"));
//    SQLParsedResult parsedResult = parseEngine.parse();
//    Assert.assertNotNull(parsedResult);
//    Assert.assertNotNull(parsedResult.getMergeContext());
//    Assert.assertFalse(parsedResult.getMergeContext().hasLimit());
//  }
//
//
//  /**
//   * 测试分页注释， SQL主语句不带limit关键字
//   */
//  @Test
//  public void parseLimitWrongLimitHint() {
//    String sql = "/*!hint page(offset=10)*/select * from customer";
//
//    SQLParseEngine parseEngine =
//        SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//            ShardConfig.getInstance().getVirtualDb("crmdb"));
//    try {
//      parseEngine.parse();
//    } catch (RuntimeException e) {
//      Assert.assertThat(e.getMessage(), Is.is("Invalid hint config of page [offset=10]"));
//      e.printStackTrace();
//    }
//
//    sql = "/*!hint page(offset=10, count=)*/select * from customer";
//
//    parseEngine = SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//        ShardConfig.getInstance().getVirtualDb("crmdb"));
//    try {
//      parseEngine.parse();
//    } catch (RuntimeException e) {
//      e.printStackTrace();
//      Assert.assertThat(e.getMessage(), Is.is("Invalid hint config of page [offset=10, count=]"));
//    }
//  }
//
//  /**
//   * 测试分页注释， SQL主语句不带limit关键字
//   */
//  @Test
//  public void parseLimit() {
//    String sql = "/*!hint page(offset=10, count=8)*/select * from customer";
//
//    SQLParseEngine parseEngine =
//        SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//            ShardConfig.getInstance().getVirtualDb("crmdb"));
//    SQLParsedResult parsedResult = parseEngine.parse();
//    Assert.assertNotNull(parsedResult);
//    Assert.assertNotNull(parsedResult.getMergeContext());
//    Assert.assertTrue(parsedResult.getMergeContext().hasLimit());
//    Assert.assertThat(parsedResult.getMergeContext().getLimit().getOffset(), Is.is(10));
//    Assert.assertThat(parsedResult.getMergeContext().getLimit().getRowCount(), Is.is(8));
//  }
//
//  /**
//   * 测试分页注释， SQL主语句带limit关键字
//   * 优先使用注释中的limit参数
//   */
//  @Test
//  public void parseLimitWithLimitBlock() {
//    String sql = "/*!hint page(offset=10, count=8)*/select * from customer limit 5, 8";
//
//    SQLParseEngine parseEngine =
//        SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//            ShardConfig.getInstance().getVirtualDb("crmdb"));
//    SQLParsedResult parsedResult = parseEngine.parse();
//    Assert.assertNotNull(parsedResult);
//    Assert.assertNotNull(parsedResult.getMergeContext());
//    Assert.assertTrue(parsedResult.getMergeContext().hasLimit());
//    Assert.assertThat(parsedResult.getMergeContext().getLimit().getOffset(), Is.is(10));
//    Assert.assertThat(parsedResult.getMergeContext().getLimit().getRowCount(), Is.is(8));
//  }


//  /**
//   * 测试隐藏分片注释
//   */
//  @Test
//  public void parseHiddenShard() {
//    String sql = "/*!hint shard(customer_id=8920021)*/select * from customer";
//
//    SQLParseEngine parseEngine =
//        SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//            ShardConfig.getInstance().getVirtualDb("crmdb"));
//    SQLParsedResult parsedResult = parseEngine.parse();
//    Assert.assertNotNull(parsedResult);
//    Assert.assertNotNull(parsedResult.getRouteContext());
//    Assert.assertNotNull(parsedResult.getRouteContext().getHintContext());
//    Assert.assertNotNull(parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs());
//    Assert
//        .assertThat(parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs().size(),
//            Is.is(1));
//    Assert.assertThat(parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs()
//        .containsKey("customer_id"), Is.is(true));
//    Assert.assertThat(String.valueOf(
//        parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs()
//            .get("customer_id")), Is.is("8920021"));
//  }
//
//  /**
//   * 测试隐藏分片注释，带星号
//   */
//  @Test
//  public void parseHiddenShardHavingAsteriskValue() {
//    String sql = "/*!hint shard(customer_id=89200****21)*/select * from customer";
//
//    SQLParseEngine parseEngine =
//        SQLParserFactory.create(DatabaseType.MySQL, sql, Collections.emptyList(),
//            ShardConfig.getInstance().getVirtualDb("crmdb"));
//    SQLParsedResult parsedResult = parseEngine.parse();
//    Assert.assertNotNull(parsedResult);
//    Assert.assertNotNull(parsedResult.getRouteContext());
//    Assert.assertNotNull(parsedResult.getRouteContext().getHintContext());
//    Assert.assertNotNull(parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs());
//    Assert
//        .assertThat(parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs().size(),
//            Is.is(1));
//    Assert.assertThat(parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs()
//        .containsKey("customer_id"), Is.is(true));
//    Assert.assertThat(String.valueOf(
//        parsedResult.getRouteContext().getHintContext().getHiddenShardValuePairs()
//            .get("customer_id")), Is.is("89200****21"));
//  }
}
