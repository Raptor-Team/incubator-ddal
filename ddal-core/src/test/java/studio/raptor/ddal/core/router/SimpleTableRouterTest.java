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
public class SimpleTableRouterTest {

    private static ShardConfig shardConfig;
    private static String virtualDBName = "crmdb";
    private static VirtualDb virtualDb;

    @BeforeClass
    public static void setUp() throws Exception {
        shardConfig = ShardConfig.getInstance();
        virtualDb = shardConfig.getVirtualDbs().get(virtualDBName);
    }

//    @Test
//    public void testHasTableAndDatabaseShard() {
//        String sql1 = "select * from party where id=1 and num=2";
//        ParseResult pr1 = GenericParser.parse(DatabaseType.MySQL, sql1, Collections.emptyList());
//        RouteResult rr1 = Router.route(virtualDb, pr1);
//        Assert.assertEquals(1, rr1.getRouteNodes().size());
//
//        String sql2 = "select * from party where num=2";
//        ParseResult pr2 = GenericParser.parse(DatabaseType.MySQL, sql2, Collections.emptyList());
//        RouteResult rr2 = Router.route(virtualDb, pr2);
//        Assert.assertEquals(2, rr2.getRouteNodes().size());
//
//        String sql3 = "select * from party where id=1";
//        ParseResult pr3 = GenericParser.parse(DatabaseType.MySQL, sql3, Collections.emptyList());
//        RouteResult rr3 = Router.route(virtualDb, pr3);
//        Assert.assertEquals(4, rr3.getRouteNodes().size());
//
//        String sql4 = "select * from party";
//        ParseResult pr4 = GenericParser.parse(DatabaseType.MySQL, sql4, Collections.emptyList());
//        RouteResult rr4 = Router.route(virtualDb, pr4);
//        Assert.assertEquals(8, rr4.getRouteNodes().size());
//    }
//
//    @Test
//    public void testOnlyHasTableShard() {
//        String sql1 = "select * from order where num=2";
//        ParseResult pr1 = GenericParser.parse(DatabaseType.MySQL, sql1, Collections.emptyList());
//        RouteResult rr1 = Router.route(virtualDb, pr1);
//        Assert.assertEquals(1, rr1.getRouteNodes().size());
//
//        String sql2 = "select * from order";
//        ParseResult pr2 = GenericParser.parse(DatabaseType.MySQL, sql2, Collections.emptyList());
//        RouteResult rr2 = Router.route(virtualDb, pr2);
//        Assert.assertEquals(4, rr2.getRouteNodes().size());
//    }

}
