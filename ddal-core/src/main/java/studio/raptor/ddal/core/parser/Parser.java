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

package studio.raptor.ddal.core.parser;

import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.visitor.MySqlStatementVisitor;
import studio.raptor.ddal.core.parser.visitor.OracleStatementVisitor;
import studio.raptor.ddal.core.parser.visitor.StatementVisitor;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.parser.SQLParserUtils;
import studio.raptor.sqlparser.parser.SQLStatementParser;

/**
 * 解析器入口
 *
 * @author Charley
 * @since 1.0
 */
public class Parser {

  /**
   * 解析
   *
   * @param databaseType 数据库类型
   * @param sql SQL语句
   */
  public static ParseResult parse(final DatabaseType databaseType, final String sql) {
    //Sequence解析
    if(SequenceParser.fastCheck(sql)){
      return SequenceParser.parse(databaseType, sql);
    }

    SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, databaseType.name());
    SQLStatement statement = parser.parseStatement();
    ParseResult parseResult = new ParseResult(databaseType, statement);

    StatementVisitor visitor;
    switch (databaseType){
      case MySQL:
        visitor = new MySqlStatementVisitor(parseResult);
        break;
      case Oracle:
        visitor = new OracleStatementVisitor(parseResult);
        break;
      default:
        throw new RuntimeException(
            String.format("Unsupported comments of database type [%s]", databaseType));
    }
    statement.accept(visitor);
    return parseResult;
  }
}
