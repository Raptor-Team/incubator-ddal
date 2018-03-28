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

package studio.raptor.ddal.common;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import studio.raptor.ddal.common.algorithm.DefaultHashRangeSingleKeyShardAlgorithmTest;
import studio.raptor.ddal.common.algorithm.DefaultModMultiKeyAlgorithmTest;
import studio.raptor.ddal.common.algorithm.DefaultModSingleKeyAlgorithmTest;
import studio.raptor.ddal.common.algorithm.ShardValueTest;
import studio.raptor.ddal.common.bytes.ByteUnitTest;
import studio.raptor.ddal.common.bytes.BytesToolsTest;
import studio.raptor.ddal.common.exception.GenericExceptionTest;
import studio.raptor.ddal.common.helper.PathHelperTest;
import studio.raptor.ddal.common.jdbc.JdbcMethodInvocationTest;
import studio.raptor.ddal.common.jdbc.ResultSetUtilTest;
import studio.raptor.ddal.common.sql.SQLUtilTest;
import studio.raptor.ddal.common.util.IpUtilTest;
import studio.raptor.ddal.common.util.RuntimeUtilTest;
import studio.raptor.ddal.common.util.SplitUtilTest;
import studio.raptor.ddal.common.util.parser.CharTypesTest;

/**
 * Test suites.
 *
 * Created by Sam on 17/11/2016.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
    //util
    IpUtilTest.class,
    RuntimeUtilTest.class,
    SplitUtilTest.class,

    //util/parser
    CharTypesTest.class,

    // sql
    SQLUtilTest.class,

    // helper
    PathHelperTest.class,
    // algorithm
    DefaultHashRangeSingleKeyShardAlgorithmTest.class,
    DefaultModSingleKeyAlgorithmTest.class,
    DefaultModMultiKeyAlgorithmTest.class,
    ShardValueTest.class,

    BytesToolsTest.class,
    ByteUnitTest.class,
    GenericExceptionTest.class,
    PhaseTest.class,

    // jdbc
    JdbcMethodInvocationTest.class,
    ResultSetUtilTest.class,
})
public class DDALCommonTestSuites {

}
