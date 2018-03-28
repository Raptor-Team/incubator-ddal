/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.raptor.sqlparser.dialect.db2.parser;

import java.util.List;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.dialect.db2.ast.stmt.DB2ValuesStatement;
import studio.raptor.sqlparser.parser.Lexer;
import studio.raptor.sqlparser.parser.SQLStatementParser;
import studio.raptor.sqlparser.parser.Token;


public class DB2StatementParser extends SQLStatementParser {

  public DB2StatementParser(String sql) {
    super(new DB2ExprParser(sql));
  }

  public DB2StatementParser(Lexer lexer) {
    super(new DB2ExprParser(lexer));
  }

  public DB2SelectParser createSQLSelectParser() {
    return new DB2SelectParser(this.exprParser);
  }

  public boolean parseStatementListDialect(List<SQLStatement> statementList) {
    if (lexer.token() == Token.VALUES) {
      lexer.nextToken();
      DB2ValuesStatement stmt = new DB2ValuesStatement();
      stmt.setExpr(this.exprParser.expr());
      statementList.add(stmt);
      return true;
    }

    return false;
  }
}
