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

package studio.raptor.ddal.tests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import studio.raptor.ddal.tests.aggregation.AvgAggregationTest;
import studio.raptor.ddal.tests.aggregation.CountAggregationTest;
import studio.raptor.ddal.tests.aggregation.DistinctAggregationTest;
import studio.raptor.ddal.tests.aggregation.GroupByAggregationTest;
import studio.raptor.ddal.tests.aggregation.MaxAggregationTest;
import studio.raptor.ddal.tests.aggregation.MinAggregationTest;
import studio.raptor.ddal.tests.aggregation.OrderByAggregationTest;
import studio.raptor.ddal.tests.aggregation.SumAggregationTest;
import studio.raptor.ddal.tests.delete.GlobalTableDeleteTest;
import studio.raptor.ddal.tests.delete.ShardTableDeleteTest;
import studio.raptor.ddal.tests.dimension.MultiDimensionDatabaseShardTest;
import studio.raptor.ddal.tests.hint.HintShardKVRouteTest;
import studio.raptor.ddal.tests.index.IndexIUDTest;
import studio.raptor.ddal.tests.index.IndexSelectTest;
import studio.raptor.ddal.tests.insert.GlobalTableInsertTest;
import studio.raptor.ddal.tests.insert.ShardTableInsertTest;
import studio.raptor.ddal.tests.join.JoinGlobalTableTest;
import studio.raptor.ddal.tests.page.HintPaginationTest;
import studio.raptor.ddal.tests.page.MySQLLimitTest;
import studio.raptor.ddal.tests.select.GlobalTableSelectTest;
import studio.raptor.ddal.tests.select.ShardTableSelectTest;
import studio.raptor.ddal.tests.sequence.SequenceTest;
import studio.raptor.ddal.tests.update.MultiShardUpdateTest;
import studio.raptor.ddal.tests.update.SingleShardUpdateTest;
import studio.raptor.ddal.tests.where.BetweenClauseTest;
import studio.raptor.ddal.tests.where.EqualClauseTest;
import studio.raptor.ddal.tests.where.InClauseTest;
import studio.raptor.ddal.tests.wildcard.WildcardTest;

/**
 * @author Sam
 * @since 3.0.0
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    // aggregation
    AvgAggregationTest.class,
    CountAggregationTest.class,
    DistinctAggregationTest.class,
    GroupByAggregationTest.class,
    MaxAggregationTest.class,
    MinAggregationTest.class,
    OrderByAggregationTest.class,
    SumAggregationTest.class,
    // hint
    HintShardKVRouteTest.class,
    //insert
    GlobalTableInsertTest.class,
    ShardTableInsertTest.class,
    // join
    JoinGlobalTableTest.class,
    // page
    HintPaginationTest.class,
    MySQLLimitTest.class,
    // select
    GlobalTableSelectTest.class,
    ShardTableSelectTest.class,
    // update
    SingleShardUpdateTest.class,
    MultiShardUpdateTest.class,
    // where
    BetweenClauseTest.class,
    EqualClauseTest.class,
    InClauseTest.class,
    // delete
    GlobalTableDeleteTest.class,
    ShardTableDeleteTest.class,
    //MultiKeyShard
    MultiDimensionDatabaseShardTest.class,
    //Wildcard
    WildcardTest.class,
    //Sequence
    SequenceTest.class,
    //Index
    IndexIUDTest.class,
    IndexSelectTest.class
})
public class TestSuites {
}
