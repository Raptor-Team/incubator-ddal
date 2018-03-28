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

package studio.raptor.ddal.core.parser.visitor;

import org.junit.Test;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.parser.SQLParserUtils;
import studio.raptor.sqlparser.parser.SQLStatementParser;
import studio.raptor.sqlparser.util.JdbcConstants;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class OracleStatementVisitorTest {

  @Test
  public void testNativeVisitor() {
    String sql = "SELECT DISTINCT P.POOL_ID \"poolId\", P.NAME \"poolName\" FROM PROD_SPEC_2_POOL P2P JOIN AREA_2_POOL A2P ON P2P.POOL_ID = A2P.POOL_ID JOIN CHANNEL_2_POOL C2P ON A2P.POOL_ID = C2P.POOL_ID JOIN PHONE_NUMBER_POOL P ON P.POOL_ID = P2P.POOL_ID WHERE P2P.PROD_SPEC_ID = ? AND A2P.AREA_ID in ( ? , ? ) AND C2P.CHANNEL_ID = ? AND ROWNUM <1000 ORDER BY P.NAME";
    SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.ORACLE);
    SQLStatement statement = parser.parseStatement();

    ParseResult parseResult = new ParseResult(DatabaseType.Oracle, statement);
    OracleStatementVisitor visitor = new OracleStatementVisitor(parseResult);
    statement.accept(visitor);
  }
}
