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

package studio.raptor.ddal.core.router;


import org.junit.BeforeClass;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.shard.VirtualDb;

/**
 * 常规路由测试
 *
 * @author Charley
 */
public class SimpleDatabaseRouterTest {

  private static ShardConfig shardConfig;
  private static String virtualDBName = "crmdb";
  private static int vDBShardingNum;
  private static VirtualDb virtualDb;

  @BeforeClass
  public static void setUp() throws Exception {
    shardConfig = ShardConfig.getInstance();
    virtualDb = shardConfig.getVirtualDbs().get(virtualDBName);
    vDBShardingNum = virtualDb.getShards().size();
  }

//  /**
//   * DDL类型语句路由测试
//   */
//  @Test(expected = GenericException.class)
//  public void testDDL() {
//    String sql = "CREATE Table GlobalTest( Id_P int, OrderNo int)";
//    Parser.parse(DatabaseType.MySQL, sql);
//  }
//
//  /**
//   * DCL类型语句路由测试
//   */
//  @Test
//  public void testDCL() {
//    String sql = "COMMIT";
//    ParseResult pr = GenericParser.parse(DatabaseType.MySQL, sql, Collections.emptyList());
//    RouteResult rr = Router.route(virtualDb, pr);
//    Assert.assertEquals(0, rr.getRouteNodes().size());
//  }
//
//  /**
//   * Insert语句路由测试
//   */
//  @Test(expected = GenericException.class)
//  public void testInsert() {
//    // 全局表
//    String globalSql = "insert into area (id, name) values (10, 'Beijing')";
//    ParseResult globalPr = GenericParser.parse(DatabaseType.MySQL, globalSql, Collections.emptyList());
//    RouteResult globalRr = Router.route(virtualDb, globalPr);
//    Assert.assertEquals(vDBShardingNum, globalRr.getRouteNodes().size());
//
//    // 分片表
//    String ID_P1 = "1";
//    String shardingSql1 = "insert into customer (id, name, gender, create_date) values (" + ID_P1
//        + ",'Gates', 'Bill', 'Xuanwumen 10', 'Beijing')";
//    ParseResult shardingPr1 = GenericParser.parse(DatabaseType.MySQL, shardingSql1, Collections.emptyList());
//    RouteResult shardingRr1 = Router.route(virtualDb, shardingPr1);
//    Assert.assertEquals("shard_1", shardingRr1.getRouteNodes().get(0).getShard().getName());
//
//    // 预编译
//    String shardingPreparedSql1 = "insert into customer (id, name, gender, create_date) values ( ?,'Gates', 'Bill', 'Xuanwumen 10', 'Beijing')";
//    ParseResult shardingPreparedPr1 = GenericParser.parse(DatabaseType.MySQL, shardingPreparedSql1, Collections.singletonList((Object) 3));
//    RouteResult shardingPreparedRr1 = Router.route(virtualDb, shardingPreparedPr1);
//    Assert.assertEquals("shard_1", shardingPreparedRr1.getRouteNodes().get(0).getShard().getName());
//
//    String ID_P2 = "2";
//    String shardingSql2 = "insert into customer (id, name, gender, create_date) values (" + ID_P2
//        + ",'Gates', 'Bill', 'Xuanwumen 10', 'Beijing')";
//    ParseResult shardingPr2 = GenericParser.parse(DatabaseType.MySQL, shardingSql2, Collections.emptyList());
//    RouteResult shardingRr2 = Router.route(virtualDb, shardingPr2);
//    Assert.assertEquals("shard_0", shardingRr2.getRouteNodes().get(0).getShard().getName());
//
//    String shardingSql3 = "insert into customer values ('Gates', 'Bill', 'Xuanwumen 10', 'Beijing')";
//    ParseResult shardingPr3 = GenericParser.parse(DatabaseType.MySQL, shardingSql3, Collections.emptyList());
//    RouteResult shardingRr3 = Router.route(virtualDb, shardingPr3);
//  }
//
//  @Test
//  public void testSelect() {
//    // 全局表
//    String globalSql = "select * from area";
//    ParseResult globalPr = GenericParser.parse(DatabaseType.MySQL, globalSql, Collections.emptyList());
//    RouteResult globalRr = Router.route(virtualDb, globalPr);
//    Assert.assertEquals(1, globalRr.getRouteNodes().size());
//
//    // 全局表与分片表
//    String globalSql1 = "select * from area, customer";
//    ParseResult globalPr1 = GenericParser.parse(DatabaseType.MySQL, globalSql1, Collections.emptyList());
//    RouteResult globalRr1 = Router.route(virtualDb, globalPr1);
//    Assert.assertEquals(2, globalRr1.getRouteNodes().size());
//
//    // 分片表
//    String shardingSql = "select * from customer";
//    ParseResult shardingPr = GenericParser.parse(DatabaseType.MySQL, shardingSql, Collections.emptyList());
//    RouteResult shardingRr = Router.route(virtualDb, shardingPr);
//    Assert.assertEquals(2, shardingRr.getRouteNodes().size());
//
//    // 值
//    String shardingSql1 = "select * from customer where id=1";
//    ParseResult shardingPr1 = GenericParser.parse(DatabaseType.MySQL, shardingSql1, Collections.emptyList());
//    RouteResult shardingRr1 = Router.route(virtualDb, shardingPr1);
//    Assert.assertEquals("shard_1", shardingRr1.getRouteNodes().get(0).getShard().getName());
//
//    // 值
//    String shardingSql2 = "select * from customer where id=2";
//    ParseResult shardingPr2 = GenericParser.parse(DatabaseType.MySQL, shardingSql2, Collections.emptyList());
//    RouteResult shardingRr2 = Router.route(virtualDb, shardingPr2);
//    Assert.assertEquals("shard_0", shardingRr2.getRouteNodes().get(0).getShard().getName());
//
//    // 全字段
//    String shardingSql3 = "select id,LastName,FirstName, FirstName, Address, City, Birthday from customer";
//    ParseResult shardingPr3 = GenericParser.parse(DatabaseType.MySQL, shardingSql3, Collections.emptyList());
//    RouteResult shardingRr3 = Router.route(virtualDb, shardingPr3);
//    Assert.assertEquals(2, shardingRr3.getRouteNodes().size());
//
//    // 关键字AS
//    String shardingSql4 = "select id as Id,LastName as Name from customer";
//    ParseResult shardingPr4 = GenericParser.parse(DatabaseType.MySQL, shardingSql4, Collections.emptyList());
//    RouteResult shardingRr4 = Router.route(virtualDb, shardingPr4);
//    Assert.assertEquals(2, shardingRr4.getRouteNodes().size());
//
//    // 操作符 <=
//    String shardingSql5 = "select * from customer where id <= 20";
//    ParseResult shardingPr5 = GenericParser.parse(DatabaseType.MySQL, shardingSql5, Collections.emptyList());
//    RouteResult shardingRr5 = Router.route(virtualDb, shardingPr5);
//    Assert.assertEquals(2, shardingRr5.getRouteNodes().size());
//
//    // 操作符 >=
//    String shardingSql6 = "select * from customer where id >= 40";
//    ParseResult shardingPr6 = GenericParser.parse(DatabaseType.MySQL, shardingSql6, Collections.emptyList());
//    RouteResult shardingRr6 = Router.route(virtualDb, shardingPr6);
//    Assert.assertEquals(2, shardingRr6.getRouteNodes().size());
//
//    // 操作符 !=
//    String shardingSql7 = "select * from customer where id != 20";
//    ParseResult shardingPr7 = GenericParser.parse(DatabaseType.MySQL, shardingSql7, Collections.emptyList());
//    RouteResult shardingRr7 = Router.route(virtualDb, shardingPr7);
//    Assert.assertEquals(2, shardingRr7.getRouteNodes().size());
//
//    // 操作符 IN
//    String shardingSql8 = "select * from customer where id in (1,3,5)";
//    ParseResult shardingPr8 = GenericParser.parse(DatabaseType.MySQL, shardingSql8, Collections.emptyList());
//    RouteResult shardingRr8 = Router.route(virtualDb, shardingPr8);
//    Assert.assertEquals("shard_1", shardingRr8.getRouteNodes().get(0).getShard().getName());
//
//    // 操作符 IN
//    String shardingSql9 = "select * from customer where id in (2,4,6)";
//    ParseResult shardingPr9 = GenericParser.parse(DatabaseType.MySQL, shardingSql9, Collections.emptyList());
//    RouteResult shardingRr9 = Router.route(virtualDb, shardingPr9);
//    Assert.assertEquals("shard_0", shardingRr9.getRouteNodes().get(0).getShard().getName());
//
//    // 操作符 IN
//    String shardingSql10 = "select * from customer where id in (1,2,3,4,5,6)";
//    ParseResult shardingPr10 = GenericParser.parse(DatabaseType.MySQL, shardingSql10, Collections.emptyList());
//    RouteResult shardingRr10 = Router.route(virtualDb, shardingPr10);
//    Assert.assertEquals(2, shardingRr10.getRouteNodes().size());
//
//    // 操作符 NOT IN
//    String shardingSql11 = "select * from customer where id not in (1,2,3)";
//    ParseResult shardingPr11 = GenericParser.parse(DatabaseType.MySQL, shardingSql11, Collections.emptyList());
//    RouteResult shardingRr11 = Router.route(virtualDb, shardingPr11);
//    Assert.assertEquals(2, shardingRr11.getRouteNodes().size());
//
//    // 操作符 BETWEEN
//    String shardingSql12 = "select * from customer where id between 1 and 5";
//    ParseResult shardingPr12 = GenericParser.parse(DatabaseType.MySQL, shardingSql12, Collections.emptyList());
//    RouteResult shardingRr12 = Router.route(virtualDb, shardingPr12);
//    Assert.assertEquals(2, shardingRr12.getRouteNodes().size());
//
//    // 函数 count(*)
//    String shardingSql13 = "select count(*) from customer";
//    ParseResult shardingPr13 = GenericParser.parse(DatabaseType.MySQL, shardingSql13, Collections.emptyList());
//    RouteResult shardingRr13 = Router.route(virtualDb, shardingPr13);
//    Assert.assertEquals(2, shardingRr13.getRouteNodes().size());
//
//    // 函数 Max(), Min(), Sum(), Avg()
//    String shardingSql14 = "select Max(id), Min(Age), Sum(Age), Avg(Age) from customer";
//    ParseResult shardingPr14 = GenericParser.parse(DatabaseType.MySQL, shardingSql14, Collections.emptyList());
//    RouteResult shardingRr14 = Router.route(virtualDb, shardingPr14);
//    Assert.assertEquals(2, shardingRr14.getRouteNodes().size());
//
//    // 函数 distinct
//    String shardingSql15 = "select distinct Sum(Age) from customer";
//    ParseResult shardingPr15 = GenericParser.parse(DatabaseType.MySQL, shardingSql15, Collections.emptyList());
//    RouteResult shardingRr15 = Router.route(virtualDb, shardingPr15);
//    Assert.assertEquals(2, shardingRr15.getRouteNodes().size());
//
//    // 函数 order by
//    String shardingSql16 = "select * from customer order by id ASC,Age DESC";
//    ParseResult shardingPr16 = GenericParser.parse(DatabaseType.MySQL, shardingSql16, Collections.emptyList());
//    RouteResult shardingRr16 = Router.route(virtualDb, shardingPr16);
//    Assert.assertEquals(2, shardingRr16.getRouteNodes().size());
//  }
//
//  @Test
//  public void testUpdate() {
//    String sql = "update customer set firstName = 'Fred' where LastName = 'Wilson'";
//    ParseResult pr = GenericParser.parse(DatabaseType.MySQL, sql, Collections.emptyList());
//    RouteResult rr = Router.route(virtualDb, pr);
//    Assert.assertEquals(2, rr.getRouteNodes().size());
//  }
}
