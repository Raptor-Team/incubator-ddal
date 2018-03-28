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
package studio.raptor.sqlparser.dialect.odps.parser;

import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.dialect.odps.ast.OdpsCreateTableStatement;
import studio.raptor.sqlparser.parser.ParserException;
import studio.raptor.sqlparser.parser.SQLCreateTableParser;
import studio.raptor.sqlparser.parser.SQLExprParser;
import studio.raptor.sqlparser.parser.Token;

public class OdpsCreateTableParser extends SQLCreateTableParser {

  public OdpsCreateTableParser(String sql) {
    super(new OdpsExprParser(sql));
  }

  public OdpsCreateTableParser(SQLExprParser exprParser) {
    super(exprParser);
  }

  public SQLCreateTableStatement parseCreateTable(boolean acceptCreate) {
    OdpsCreateTableStatement stmt = new OdpsCreateTableStatement();

    if (acceptCreate) {
      accept(Token.CREATE);
    }

    accept(Token.TABLE);

    if (lexer.token() == Token.IF || identifierEquals("IF")) {
      lexer.nextToken();
      accept(Token.NOT);
      accept(Token.EXISTS);

      stmt.setIfNotExiists(true);
    }

    stmt.setName(this.exprParser.name());

    if (identifierEquals("LIFECYCLE")) {
      lexer.nextToken();
      stmt.setLifecycle(this.exprParser.expr());
    }

    if (lexer.token() == Token.LIKE) {
      lexer.nextToken();
      SQLName name = this.exprParser.name();
      stmt.setLike(name);
    } else if (lexer.token() == Token.AS) {
      lexer.nextToken();

      OdpsSelectParser selectParser = new OdpsSelectParser(this.exprParser);
      SQLSelect select = selectParser.select();

      stmt.setSelect(select);
    } else {
      accept(Token.LPAREN);

      if (lexer.isKeepComments() && lexer.hasComment()) {
        stmt.addBodyBeforeComment(lexer.readAndResetComments());
      }

      for (; ; ) {
        if (lexer.token() != Token.IDENTIFIER) {
          throw new ParserException("expect identifier");
        }

        SQLColumnDefinition column = this.exprParser.parseColumn();
        stmt.getTableElementList().add(column);

        if (lexer.isKeepComments() && lexer.hasComment()) {
          column.addAfterComment(lexer.readAndResetComments());
        }

        if (!(lexer.token() == (Token.COMMA))) {
          break;
        } else {
          lexer.nextToken();

          if (lexer.isKeepComments() && lexer.hasComment()) {
            column.addAfterComment(lexer.readAndResetComments());
          }
        }
      }
      accept(Token.RPAREN);
    }

    if (lexer.token() == Token.COMMENT) {
      lexer.nextToken();
      stmt.setComment(this.exprParser.primary());
    }

    if (lexer.token() == Token.PARTITIONED) {
      lexer.nextToken();
      accept(Token.BY);
      accept(Token.LPAREN);

      for (; ; ) {
        if (lexer.token() != Token.IDENTIFIER) {
          throw new ParserException("expect identifier");
        }

        SQLColumnDefinition column = this.exprParser.parseColumn();
        stmt.addPartitionColumn(column);

        if (lexer.isKeepComments() && lexer.hasComment()) {
          column.addAfterComment(lexer.readAndResetComments());
        }

        if (!(lexer.token() == (Token.COMMA))) {
          break;
        } else {
          lexer.nextToken();
          if (lexer.isKeepComments() && lexer.hasComment()) {
            column.addAfterComment(lexer.readAndResetComments());
          }
        }
      }

      accept(Token.RPAREN);
    }

    if (identifierEquals("LIFECYCLE")) {
      lexer.nextToken();
      stmt.setLifecycle(this.exprParser.expr());
    }

    return stmt;
  }
}
