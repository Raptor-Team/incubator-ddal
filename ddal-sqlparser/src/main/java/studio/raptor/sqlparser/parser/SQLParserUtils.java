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
package studio.raptor.sqlparser.parser;

import studio.raptor.sqlparser.dialect.db2.parser.DB2ExprParser;
import studio.raptor.sqlparser.dialect.db2.parser.DB2Lexer;
import studio.raptor.sqlparser.dialect.db2.parser.DB2StatementParser;
import studio.raptor.sqlparser.dialect.mysql.parser.MySqlExprParser;
import studio.raptor.sqlparser.dialect.mysql.parser.MySqlLexer;
import studio.raptor.sqlparser.dialect.mysql.parser.MySqlStatementParser;
import studio.raptor.sqlparser.dialect.odps.parser.OdpsExprParser;
import studio.raptor.sqlparser.dialect.odps.parser.OdpsLexer;
import studio.raptor.sqlparser.dialect.odps.parser.OdpsStatementParser;
import studio.raptor.sqlparser.dialect.oracle.parser.OracleExprParser;
import studio.raptor.sqlparser.dialect.oracle.parser.OracleLexer;
import studio.raptor.sqlparser.dialect.oracle.parser.OracleStatementParser;
import studio.raptor.sqlparser.dialect.phoenix.parser.PhoenixExprParser;
import studio.raptor.sqlparser.dialect.phoenix.parser.PhoenixLexer;
import studio.raptor.sqlparser.dialect.phoenix.parser.PhoenixStatementParser;
import studio.raptor.sqlparser.dialect.postgresql.parser.PGExprParser;
import studio.raptor.sqlparser.dialect.postgresql.parser.PGLexer;
import studio.raptor.sqlparser.dialect.postgresql.parser.PGSQLStatementParser;
import studio.raptor.sqlparser.dialect.sqlserver.parser.SQLServerExprParser;
import studio.raptor.sqlparser.dialect.sqlserver.parser.SQLServerLexer;
import studio.raptor.sqlparser.dialect.sqlserver.parser.SQLServerStatementParser;
import studio.raptor.sqlparser.util.JdbcUtils;

public class SQLParserUtils {

  public static SQLStatementParser createSQLStatementParser(String sql, String dbType) {
    if (JdbcUtils.ORACLE.equalsIgnoreCase(dbType) || JdbcUtils.ALI_ORACLE.equalsIgnoreCase(dbType)) {
      return new OracleStatementParser(sql);
    }

    if (JdbcUtils.MYSQL.equalsIgnoreCase(dbType)) {
      Lexer lexer = new MySqlLexer(sql);
      lexer.setAllowComment(false);
      lexer.nextToken();
      while (lexer.token() == Token.HINT) {
        lexer.nextToken();
      }
      return new MySqlStatementParser(lexer);
    }

    if (JdbcUtils.MARIADB.equalsIgnoreCase(dbType)) {
      return new MySqlStatementParser(sql);
    }

    if (JdbcUtils.POSTGRESQL.equalsIgnoreCase(dbType)
        || JdbcUtils.ENTERPRISEDB.equalsIgnoreCase(dbType)) {
      return new PGSQLStatementParser(sql);
    }

    if (JdbcUtils.SQL_SERVER.equalsIgnoreCase(dbType) || JdbcUtils.JTDS.equalsIgnoreCase(dbType)) {
      return new SQLServerStatementParser(sql);
    }

    if (JdbcUtils.H2.equalsIgnoreCase(dbType)) {
      return new MySqlStatementParser(sql);
    }

    if (JdbcUtils.DB2.equalsIgnoreCase(dbType)) {
      return new DB2StatementParser(sql);
    }

    if (JdbcUtils.ODPS.equalsIgnoreCase(dbType)) {
      return new OdpsStatementParser(sql);
    }

    if (JdbcUtils.PHOENIX.equalsIgnoreCase(dbType)) {
      return new PhoenixStatementParser(sql);
    }

    return new SQLStatementParser(sql, dbType);
  }

  public static SQLExprParser createExprParser(String sql, String dbType) {
    if (JdbcUtils.ORACLE.equals(dbType) || JdbcUtils.ALI_ORACLE.equals(dbType)) {
      return new OracleExprParser(sql);
    }

    if (JdbcUtils.MYSQL.equals(dbType) || //
        JdbcUtils.MARIADB.equals(dbType) || //
        JdbcUtils.H2.equals(dbType)) {
      return new MySqlExprParser(sql);
    }

    if (JdbcUtils.POSTGRESQL.equals(dbType)
        || JdbcUtils.ENTERPRISEDB.equals(dbType)) {
      return new PGExprParser(sql);
    }

    if (JdbcUtils.SQL_SERVER.equals(dbType) || JdbcUtils.JTDS.equals(dbType)) {
      return new SQLServerExprParser(sql);
    }

    if (JdbcUtils.DB2.equals(dbType)) {
      return new DB2ExprParser(sql);
    }

    if (JdbcUtils.ODPS.equals(dbType)) {
      return new OdpsExprParser(sql);
    }

    if (JdbcUtils.PHOENIX.equals(dbType)) {
      return new PhoenixExprParser(sql);
    }

    return new SQLExprParser(sql);
  }

  public static Lexer createLexer(String sql, String dbType) {
    if (JdbcUtils.ORACLE.equals(dbType) || JdbcUtils.ALI_ORACLE.equals(dbType)) {
      return new OracleLexer(sql);
    }

    if (JdbcUtils.MYSQL.equals(dbType) || //
        JdbcUtils.MARIADB.equals(dbType) || //
        JdbcUtils.H2.equals(dbType)) {
      return new MySqlLexer(sql);
    }

    if (JdbcUtils.POSTGRESQL.equals(dbType)
        || JdbcUtils.ENTERPRISEDB.equals(dbType)) {
      return new PGLexer(sql);
    }

    if (JdbcUtils.SQL_SERVER.equals(dbType) || JdbcUtils.JTDS.equals(dbType)) {
      return new SQLServerLexer(sql);
    }

    if (JdbcUtils.DB2.equals(dbType)) {
      return new DB2Lexer(sql);
    }

    if (JdbcUtils.ODPS.equals(dbType)) {
      return new OdpsLexer(sql);
    }

    if (JdbcUtils.PHOENIX.equals(dbType)) {
      return new PhoenixLexer(sql);
    }

    return new Lexer(sql);
  }
}
