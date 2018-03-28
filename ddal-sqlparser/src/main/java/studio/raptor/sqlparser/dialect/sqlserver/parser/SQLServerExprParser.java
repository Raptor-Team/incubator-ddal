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
package studio.raptor.sqlparser.dialect.sqlserver.parser;

import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLIntegerExpr;
import studio.raptor.sqlparser.ast.expr.SQLNullExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.dialect.sqlserver.ast.SQLServerOutput;
import studio.raptor.sqlparser.dialect.sqlserver.ast.SQLServerTop;
import studio.raptor.sqlparser.dialect.sqlserver.ast.expr.SQLServerObjectReferenceExpr;
import studio.raptor.sqlparser.parser.Lexer;
import studio.raptor.sqlparser.parser.SQLExprParser;
import studio.raptor.sqlparser.parser.Token;
import studio.raptor.sqlparser.util.JdbcConstants;

public class SQLServerExprParser extends SQLExprParser {

  public final static String[] AGGREGATE_FUNCTIONS = {"AVG", "COUNT", "MAX", "MIN", "ROW_NUMBER",
      "STDDEV", "SUM"};

  public SQLServerExprParser(Lexer lexer) {
    super(lexer);
    this.dbType = JdbcConstants.SQL_SERVER;
    this.aggregateFunctions = AGGREGATE_FUNCTIONS;
  }

  public SQLServerExprParser(String sql) {
    this(new SQLServerLexer(sql));
    this.lexer.nextToken();
    this.dbType = JdbcConstants.SQL_SERVER;
  }

  public SQLExpr primary() {

    if (lexer.token() == Token.LBRACKET) {
      lexer.nextToken();
      SQLExpr name = this.name();
      accept(Token.RBRACKET);
      return primaryRest(name);
    }

    return super.primary();
  }

  public SQLServerSelectParser createSelectParser() {
    return new SQLServerSelectParser(this);
  }

  public SQLExpr primaryRest(SQLExpr expr) {
    if (lexer.token() == Token.DOTDOT) {
      expr = nameRest((SQLName) expr);
    }

    return super.primaryRest(expr);
  }

  protected SQLExpr dotRest(SQLExpr expr) {
    boolean backet = false;

    if (lexer.token() == Token.LBRACKET) {
      lexer.nextToken();
      backet = true;
    }

    expr = super.dotRest(expr);

    if (backet) {
      accept(Token.RBRACKET);
    }

    return expr;
  }

  public SQLName nameRest(SQLName expr) {
    if (lexer.token() == Token.DOTDOT) {
      lexer.nextToken();

      boolean backet = false;
      if (lexer.token() == Token.LBRACKET) {
        lexer.nextToken();
        backet = true;
      }
      String text = lexer.stringVal();
      lexer.nextToken();

      if (backet) {
        accept(Token.RBRACKET);
      }

      SQLServerObjectReferenceExpr owner = new SQLServerObjectReferenceExpr(expr);
      expr = new SQLPropertyExpr(owner, text);
    }

    return super.nameRest(expr);
  }

  public SQLServerTop parseTop() {
    if (lexer.token() == Token.TOP) {
      SQLServerTop top = new SQLServerTop();
      lexer.nextToken();

      boolean paren = false;
      if (lexer.token() == Token.LPAREN) {
        paren = true;
        lexer.nextToken();
      }

      top.setExpr(primary());

      if (paren) {
        accept(Token.RPAREN);
      }

      if (lexer.token() == Token.PERCENT) {
        lexer.nextToken();
        top.setPercent(true);
      }

      return top;
    }

    return null;
  }

  protected SQLServerOutput parserOutput() {
    if (identifierEquals("OUTPUT")) {
      lexer.nextToken();
      SQLServerOutput output = new SQLServerOutput();

      final List<SQLSelectItem> selectList = output.getSelectList();
      for (; ; ) {
        final SQLSelectItem selectItem = parseSelectItem();
        selectList.add(selectItem);

        if (lexer.token() != Token.COMMA) {
          break;
        }

        lexer.nextToken();
      }

      if (lexer.token() == Token.INTO) {
        lexer.nextToken();
        output.setInto(new SQLExprTableSource(this.name()));
        if (lexer.token() == (Token.LPAREN)) {
          lexer.nextToken();
          this.exprList(output.getColumns(), output);
          accept(Token.RPAREN);
        }
      }
      return output;
    }
    return null;
  }

  public SQLSelectItem parseSelectItem() {
    SQLExpr expr;
    if (lexer.token() == Token.IDENTIFIER) {
      expr = new SQLIdentifierExpr(lexer.stringVal());
      lexer.nextTokenComma();

      if (lexer.token() != Token.COMMA) {
        expr = this.primaryRest(expr);
        expr = this.exprRest(expr);
      }
    } else {
      expr = this.expr();
    }
    final String alias = as();
    return new SQLSelectItem(expr, alias);
  }

  public SQLColumnDefinition createColumnDefinition() {
    SQLColumnDefinition column = new SQLColumnDefinition();
    return column;
  }

  public SQLColumnDefinition parseColumnRest(SQLColumnDefinition column) {
    if (lexer.token() == Token.IDENTITY) {
      lexer.nextToken();

      SQLColumnDefinition.Identity identity = new SQLColumnDefinition.Identity();
      if (lexer.token() == Token.LPAREN) {
        lexer.nextToken();

        SQLIntegerExpr seed = (SQLIntegerExpr) this.primary();
        accept(Token.COMMA);
        SQLIntegerExpr increment = (SQLIntegerExpr) this.primary();
        accept(Token.RPAREN);

        identity.setSeed((Integer) seed.getNumber());
        identity.setIncrement((Integer) increment.getNumber());
      }

      if (lexer.token() == Token.NOT) {
        lexer.nextToken();

        if (lexer.token() == Token.NULL) {
          lexer.nextToken();
          column.setDefaultExpr(new SQLNullExpr());
        } else {
          accept(Token.FOR);
          identifierEquals("REPLICATION ");
          identity.setNotForReplication(true);
        }
      }

      column.setIdentity(identity);
    }

    return super.parseColumnRest(column);
  }
}
