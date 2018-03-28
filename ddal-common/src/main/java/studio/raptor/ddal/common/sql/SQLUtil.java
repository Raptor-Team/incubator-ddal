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

package studio.raptor.ddal.common.sql;

import com.google.common.base.CharMatcher;
import java.sql.SQLException;
import studio.raptor.ddal.common.util.StringUtil;

/**
 * SQL util
 *
 * @author jack, Sam
 * @since 1.0
 */

public class SQLUtil {

  private SQLUtil() {
  }

  /**
   * 去掉SQL表达式的特殊字符.
   *
   * @param value SQL表达式
   * @return 去掉SQL特殊字符的表达式
   */
  public static String getExactlyValue(final String value) {
    return null == value ? null : CharMatcher.anyOf("[]`'\"").removeFrom(value);
  }

  /**
   * 删除表名前schema的工具类。表名前的schema一般是schema.table, 处理之后的结果是
   * schema.table --> table
   *
   * @param tableName 带schema的表名
   * @return 不带schema的表名
   */
  public static String rmTableSchema(final String tableName) {
    int idx;
    if ((idx = tableName.indexOf('.')) != -1) {
      return tableName.substring(idx + 1, tableName.length());
    }
    return tableName;
  }

  public static int findStartOfStatement(String sql) {
    int statementStartPos = 0;
    if (StringUtil.startsWithIgnoreCaseAndWs(sql, "/*")) {
      statementStartPos = sql.indexOf("*/");
      if (statementStartPos == -1) {
        statementStartPos = 0;
      } else {
        statementStartPos += 2;
        while(sql.charAt(statementStartPos) == ' ') {
          statementStartPos++;
        }
      }
    } else if (StringUtil.startsWithIgnoreCaseAndWs(sql, "--")
        || StringUtil.startsWithIgnoreCaseAndWs(sql, "#")) {
      statementStartPos = sql.indexOf('\n');
      if (statementStartPos == -1) {
        statementStartPos = sql.indexOf('\r');
        if (statementStartPos == -1) {
          statementStartPos = 0;
        }
      }
    }
    return statementStartPos;
  }

  /**
   * 计算SQL指纹。
   *
   * SQL自定义的注释也加入指纹计算。
   *
   * @param sql SQL语句
   * @return SQL指纹
   */
  public static String getFingerprint(String sql) {

    return null;
  }


  /**
   * Checks if the given SQL query with the given first non-ws char is a DML
   * statement.
   *
   * @param sql the SQL to check
   * @param firstStatementChar the UC first non-ws char of the statement
   * @throws SQLException if the statement contains DML
   */
  public static boolean checkForDml(String sql, char firstStatementChar)
      throws SQLException {
    if ((firstStatementChar == 'I') || (firstStatementChar == 'U')
        || (firstStatementChar == 'D') || (firstStatementChar == 'A')
        || (firstStatementChar == 'C') || (firstStatementChar == 'T')
        || (firstStatementChar == 'R')) {
      String noCommentSql = StringUtil.stripComments(sql,
          "'\"", "'\"", true, false, true, true);

      if (StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "INSERT") //$NON-NLS-1$
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "UPDATE") //$NON-NLS-1$
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "DELETE") //$NON-NLS-1$
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "DROP") //$NON-NLS-1$
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "CREATE") //$NON-NLS-1$
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "ALTER")
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "TRUNCATE")
          || StringUtil.startsWithIgnoreCaseAndWs(noCommentSql, "RENAME")
          ) { //$NON-NLS-1$
        return true;
      }
    }
    return false;
  }
}
