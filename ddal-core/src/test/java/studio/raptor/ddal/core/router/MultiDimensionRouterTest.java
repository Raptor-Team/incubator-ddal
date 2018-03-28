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
 * 二维路由测试
 *
 * @author Charley
 */
public class MultiDimensionRouterTest {

  private static ShardConfig shardConfig;
  private static String virtualDBName = "crmdb";
  private static VirtualDb virtualDb;

  @BeforeClass
  public static void setUp() throws Exception {
    shardConfig = ShardConfig.getInstance();
    virtualDb = shardConfig.getVirtualDb(virtualDBName);
  }

//  /**
//   * 22分片
//   */
//  public void testDimensionBy22() throws Exception {
//    // (0, 0)
//    String sql1 = "select * from customer where id = 0 and staff_number = 0";
//    ParseResult pr1 = Parser.parse(DatabaseType.MySQL, sql1, Collections.emptyList());
//    RouteResult rr1 = Router.route(virtualDb, pr1);
//    Assert.assertEquals("shard_0", rr1.getRouteNodes().get(0).getShard().getName());
//
//    // (1, 0)
//    String sql2 = "select * from customer where id = 1 and staff_number = 0";
//    ParseResult pr2 = Parser.parse(DatabaseType.MySQL, sql2, Collections.emptyList());
//    RouteResult rr2 = Router.route(virtualDb, pr2);
//    Assert.assertEquals("shard_1", rr2.getRouteNodes().get(0).getShard().getName());
//
//    // (0, 1)
//    String sql3 = "select * from customer where id = 0 and staff_number = 1";
//    ParseResult pr3 = Parser.parse(DatabaseType.MySQL, sql3, Collections.emptyList());
//    RouteResult rr3 = Router.route(virtualDb, pr3);
//    Assert.assertEquals("shard_2", rr3.getRouteNodes().get(0).getShard().getName());
//
//    // (1, 1)
//    String sql4 = "select * from customer where id = 1 and staff_number = 1";
//    ParseResult pr4 = Parser.parse(DatabaseType.MySQL, sql4, Collections.emptyList());
//    RouteResult rr4 = Router.route(virtualDb, pr4);
//    Assert.assertEquals("shard_3", rr4.getRouteNodes().get(0).getShard().getName());
//  }

//    /**
//     * 23分片
//     *
//     * @throws Exception
//     */
//    public void testDimensionBy23() throws Exception {
//        // (0, 0)
//        String sql1 = "select * from 23staff where staff_id = 0 and staff_phone = 0";
//        SQLParsedResult pr1 = SQLParserFactory.create(DatabaseType.MySQL, sql1, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr1 = Router.route(virtualDb, pr1);
//      Assert.assertEquals("shard_0", rr1.getRouteNodes().get(0).getShard().getName());
//
//        // (1, 0)
//        String sql2 = "select * from 23staff where staff_id = 1 and staff_phone = 0";
//        SQLParsedResult pr2 = SQLParserFactory.create(DatabaseType.MySQL, sql2, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr2 = Router.route(virtualDb, pr2);
//      Assert.assertEquals("shard_1", rr2.getRouteNodes().get(0).getShard().getName());
//
//        // (0, 1)
//        String sql3 = "select * from 23staff where staff_id = 0 and staff_phone = 1";
//        SQLParsedResult pr3 = SQLParserFactory.create(DatabaseType.MySQL, sql3, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr3 = Router.route(virtualDb, pr3);
//      Assert.assertEquals("shard_2", rr3.getRouteNodes().get(0).getShard().getName());
//
//        // (1, 1)
//        String sql4 = "select * from 23staff where staff_id = 1 and staff_phone = 1";
//        SQLParsedResult pr4 = SQLParserFactory.create(DatabaseType.MySQL, sql4, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr4 = Router.route(virtualDb, pr4);
//      Assert.assertEquals("shard_3", rr4.getRouteNodes().get(0).getShard().getName());
//
//        // (0, 2)
//        String sql5 = "select * from 23staff where staff_id = 0 and staff_phone = 2";
//        SQLParsedResult pr5 = SQLParserFactory.create(DatabaseType.MySQL, sql5, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr5 = Router.route(virtualDb, pr5);
//      Assert.assertEquals("shard_4", rr5.getRouteNodes().get(0).getShard().getName());
//
//        // (1, 2)
//        String sql6 = "select * from 23staff where staff_id = 1 and staff_phone = 2";
//        SQLParsedResult pr6 = SQLParserFactory.create(DatabaseType.MySQL, sql6, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr6 = Router.route(virtualDb, pr6);
//      Assert.assertEquals("shard_5", rr6.getRouteNodes().get(0).getShard().getName());
//    }
//
//    /**
//     * 32分片
//     *
//     * @throws Exception
//     */
//    public void testDimensionBy32() throws Exception {
//        // (0, 0)
//        String sql1 = "select * from 32staff where staff_id = 0 and staff_phone = 0";
//        SQLParsedResult pr1 = SQLParserFactory.create(DatabaseType.MySQL, sql1, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr1 = Router.route(virtualDb, pr1);
//      Assert.assertEquals("shard_0", rr1.getRouteNodes().get(0).getShard().getName());
//
//        // (1, 0)
//        String sql2 = "select * from 32staff where staff_id = 0 and staff_phone = 1";
//        SQLParsedResult pr2 = SQLParserFactory.create(DatabaseType.MySQL, sql2, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr2 = Router.route(virtualDb, pr2);
//      Assert.assertEquals("shard_1", rr2.getRouteNodes().get(0).getShard().getName());
//
//        // (2, 0)
//        String sql3 = "select * from 32staff where staff_id = 0 and staff_phone = 2";
//        SQLParsedResult pr3 = SQLParserFactory.create(DatabaseType.MySQL, sql3, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr3 = Router.route(virtualDb, pr3);
//      Assert.assertEquals("shard_2", rr3.getRouteNodes().get(0).getShard().getName());
//
//        // (0, 1)
//        String sql4 = "select * from 32staff where staff_id = 1 and staff_phone = 0";
//        SQLParsedResult pr4 = SQLParserFactory.create(DatabaseType.MySQL, sql4, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr4 = Router.route(virtualDb, pr4);
//      Assert.assertEquals("shard_3", rr4.getRouteNodes().get(0).getShard().getName());
//
//        // (1, 1)
//        String sql5 = "select * from 32staff where staff_id = 1 and staff_phone = 1";
//        SQLParsedResult pr5 = SQLParserFactory.create(DatabaseType.MySQL, sql5, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr5 = Router.route(virtualDb, pr5);
//      Assert.assertEquals("shard_4", rr5.getRouteNodes().get(0).getShard().getName());
//
//        // (2, 1)
//        String sql6 = "select * from 32staff where staff_id = 1 and staff_phone = 2";
//        SQLParsedResult pr6 = SQLParserFactory.create(DatabaseType.MySQL, sql6, Collections.emptyList(), virtualDb).parse();
//        RouteResult rr6 = Router.route(virtualDb, pr6);
//      Assert.assertEquals("shard_5", rr6.getRouteNodes().get(0).getShard().getName());
//    }
}
