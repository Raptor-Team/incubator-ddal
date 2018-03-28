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
public class MySQLStatementVisitorTest {

  @Test
  public void testVisitor() {
    String sql = "CREATE TABLE ddl_create_table ( `cno` int(6) UNSIGNED not null COMMENT '课程编号')";
    SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, JdbcConstants.MYSQL);
    SQLStatement statement = parser.parseStatement();

    ParseResult parseResult = new ParseResult(DatabaseType.MySQL, statement);
    MySqlStatementVisitor visitor = new MySqlStatementVisitor(parseResult);

    statement.accept(visitor);
  }
}
