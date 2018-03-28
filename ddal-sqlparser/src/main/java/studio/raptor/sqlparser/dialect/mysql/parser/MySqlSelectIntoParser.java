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
package studio.raptor.sqlparser.dialect.mysql.parser;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLSetQuantifier;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLLiteralExpr;
import studio.raptor.sqlparser.ast.expr.SQLVariantRefExpr;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.ast.statement.SQLSelectQuery;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;
import studio.raptor.sqlparser.ast.statement.SQLUnionQuery;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlForceIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlIgnoreIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlIndexHintImpl;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlUseIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlOutFileExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlUnionQuery;
import studio.raptor.sqlparser.parser.ParserException;
import studio.raptor.sqlparser.parser.SQLExprParser;
import studio.raptor.sqlparser.parser.SQLSelectParser;
import studio.raptor.sqlparser.parser.Token;

/**
 */
public class MySqlSelectIntoParser extends SQLSelectParser {

  private List<SQLExpr> argsList;

  public MySqlSelectIntoParser(SQLExprParser exprParser) {
    super(exprParser);
  }

  public MySqlSelectIntoParser(String sql) {
    this(new MySqlExprParser(sql));
  }

  public MySqlSelectIntoStatement parseSelectInto() {
    SQLSelect select = select();
    MySqlSelectIntoStatement stmt = new MySqlSelectIntoStatement();
    stmt.setSelect(select);
    stmt.setVarList(argsList);
    return stmt;

  }

  @Override
  public SQLSelectQuery query() {
    if (lexer.token() == (Token.LPAREN)) {
      lexer.nextToken();

      SQLSelectQuery select = query();
      accept(Token.RPAREN);

      return queryRest(select);
    }

    MySqlSelectQueryBlock queryBlock = new MySqlSelectQueryBlock();

    if (lexer.token() == Token.SELECT) {
      lexer.nextToken();

      if (lexer.token() == Token.HINT) {
        this.exprParser.parseHints(queryBlock.getHints());
      }

      if (lexer.token() == Token.COMMENT) {
        lexer.nextToken();
      }

      if (lexer.token() == (Token.DISTINCT)) {
        queryBlock.setDistionOption(SQLSetQuantifier.DISTINCT);
        lexer.nextToken();
      } else if (identifierEquals("DISTINCTROW")) {
        queryBlock.setDistionOption(SQLSetQuantifier.DISTINCTROW);
        lexer.nextToken();
      } else if (lexer.token() == (Token.ALL)) {
        queryBlock.setDistionOption(SQLSetQuantifier.ALL);
        lexer.nextToken();
      }

      if (identifierEquals("HIGH_PRIORITY")) {
        queryBlock.setHignPriority(true);
        lexer.nextToken();
      }

      if (identifierEquals("STRAIGHT_JOIN")) {
        queryBlock.setStraightJoin(true);
        lexer.nextToken();
      }

      if (identifierEquals("SQL_SMALL_RESULT")) {
        queryBlock.setSmallResult(true);
        lexer.nextToken();
      }

      if (identifierEquals("SQL_BIG_RESULT")) {
        queryBlock.setBigResult(true);
        lexer.nextToken();
      }

      if (identifierEquals("SQL_BUFFER_RESULT")) {
        queryBlock.setBufferResult(true);
        lexer.nextToken();
      }

      if (identifierEquals("SQL_CACHE")) {
        queryBlock.setCache(true);
        lexer.nextToken();
      }

      if (identifierEquals("SQL_NO_CACHE")) {
        queryBlock.setCache(false);
        lexer.nextToken();
      }

      if (identifierEquals("SQL_CALC_FOUND_ROWS")) {
        queryBlock.setCalcFoundRows(true);
        lexer.nextToken();
      }

      parseSelectList(queryBlock);

      argsList = parseIntoArgs();
    }

    parseFrom(queryBlock);

    parseWhere(queryBlock);

    parseGroupBy(queryBlock);

    queryBlock.setOrderBy(this.exprParser.parseOrderBy());

    if (lexer.token() == Token.LIMIT) {
      queryBlock.setLimit(this.exprParser.parseLimit());
    }

    if (lexer.token() == Token.PROCEDURE) {
      lexer.nextToken();
      throw new ParserException("TODO");
    }

    parseInto(queryBlock);

    if (lexer.token() == Token.FOR) {
      lexer.nextToken();
      accept(Token.UPDATE);

      queryBlock.setForUpdate(true);
    }

    if (lexer.token() == Token.LOCK) {
      lexer.nextToken();
      accept(Token.IN);
      acceptIdentifier("SHARE");
      acceptIdentifier("MODE");
      queryBlock.setLockInShareMode(true);
    }

    return queryRest(queryBlock);
  }

  /**
   * parser the select into arguments
   */
  protected List<SQLExpr> parseIntoArgs() {

    List<SQLExpr> args = new ArrayList<SQLExpr>();
    if (lexer.token() == (Token.INTO)) {
      accept(Token.INTO);
      //lexer.nextToken();
      for (; ; ) {
        SQLExpr var = exprParser.primary();
        if (var instanceof SQLIdentifierExpr) {
          var = new SQLVariantRefExpr(
              ((SQLIdentifierExpr) var).getName());
        }
        args.add(var);
        if (lexer.token() == Token.COMMA) {
          accept(Token.COMMA);
          continue;
        } else {
          break;
        }
      }
    }
    return args;
  }


  protected void parseInto(SQLSelectQueryBlock queryBlock) {
    if (lexer.token() == (Token.INTO)) {
      lexer.nextToken();

      if (identifierEquals("OUTFILE")) {
        lexer.nextToken();

        MySqlOutFileExpr outFile = new MySqlOutFileExpr();
        outFile.setFile(expr());

        queryBlock.setInto(outFile);

        if (identifierEquals("FIELDS") || identifierEquals("COLUMNS")) {
          lexer.nextToken();

          if (identifierEquals("TERMINATED")) {
            lexer.nextToken();
            accept(Token.BY);
          }
          outFile.setColumnsTerminatedBy((SQLLiteralExpr) expr());

          if (identifierEquals("OPTIONALLY")) {
            lexer.nextToken();
            outFile.setColumnsEnclosedOptionally(true);
          }

          if (identifierEquals("ENCLOSED")) {
            lexer.nextToken();
            accept(Token.BY);
            outFile.setColumnsEnclosedBy((SQLLiteralExpr) expr());
          }

          if (identifierEquals("ESCAPED")) {
            lexer.nextToken();
            accept(Token.BY);
            outFile.setColumnsEscaped((SQLLiteralExpr) expr());
          }
        }

        if (identifierEquals("LINES")) {
          lexer.nextToken();

          if (identifierEquals("STARTING")) {
            lexer.nextToken();
            accept(Token.BY);
            outFile.setLinesStartingBy((SQLLiteralExpr) expr());
          } else {
            identifierEquals("TERMINATED");
            lexer.nextToken();
            accept(Token.BY);
            outFile.setLinesTerminatedBy((SQLLiteralExpr) expr());
          }
        }
      } else {
        queryBlock.setInto(this.exprParser.name());
      }
    }
  }

  protected SQLTableSource parseTableSourceRest(SQLTableSource tableSource) {
    if (identifierEquals("USING")) {
      return tableSource;
    }

    if (lexer.token() == Token.USE) {
      lexer.nextToken();
      MySqlUseIndexHint hint = new MySqlUseIndexHint();
      parseIndexHint(hint);
      tableSource.getHints().add(hint);
    }

    if (identifierEquals("IGNORE")) {
      lexer.nextToken();
      MySqlIgnoreIndexHint hint = new MySqlIgnoreIndexHint();
      parseIndexHint(hint);
      tableSource.getHints().add(hint);
    }

    if (identifierEquals("FORCE")) {
      lexer.nextToken();
      MySqlForceIndexHint hint = new MySqlForceIndexHint();
      parseIndexHint(hint);
      tableSource.getHints().add(hint);
    }

    return super.parseTableSourceRest(tableSource);
  }

  private void parseIndexHint(MySqlIndexHintImpl hint) {
    if (lexer.token() == Token.INDEX) {
      lexer.nextToken();
    } else {
      accept(Token.KEY);
    }

    if (lexer.token() == Token.FOR) {
      lexer.nextToken();

      if (lexer.token() == Token.JOIN) {
        lexer.nextToken();
        hint.setOption(MySqlIndexHint.Option.JOIN);
      } else if (lexer.token() == Token.ORDER) {
        lexer.nextToken();
        accept(Token.BY);
        hint.setOption(MySqlIndexHint.Option.ORDER_BY);
      } else {
        accept(Token.GROUP);
        accept(Token.BY);
        hint.setOption(MySqlIndexHint.Option.GROUP_BY);
      }
    }

    accept(Token.LPAREN);
    if (lexer.token() == Token.PRIMARY) {
      lexer.nextToken();
      hint.getIndexList().add(new SQLIdentifierExpr("PRIMARY"));
    } else {
      this.exprParser.names(hint.getIndexList());
    }
    accept(Token.RPAREN);
  }

  protected MySqlUnionQuery createSQLUnionQuery() {
    return new MySqlUnionQuery();
  }

  public SQLUnionQuery unionRest(SQLUnionQuery union) {
    if (lexer.token() == Token.LIMIT) {
      MySqlUnionQuery mysqlUnionQuery = (MySqlUnionQuery) union;
      mysqlUnionQuery.setLimit(this.exprParser.parseLimit());
    }
    return super.unionRest(union);
  }

  public MySqlExprParser getExprParser() {
    return (MySqlExprParser) exprParser;
  }
}
