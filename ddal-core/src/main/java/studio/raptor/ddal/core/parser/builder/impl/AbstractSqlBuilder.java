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

package studio.raptor.ddal.core.parser.builder.impl;

import static studio.raptor.sqlparser.SQLUtils.LCASE_NOPRETTY_FORMAT_OPTION;

import studio.raptor.ddal.core.constants.EngineConstants;
import studio.raptor.ddal.core.parser.builder.SqlBuilder;
import studio.raptor.ddal.core.parser.result.merger.Limit;
import studio.raptor.sqlparser.SQLUtils;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLJoinTableSource;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;

/**
 * @author Sam
 * @since 3.0.0
 */
abstract class AbstractSqlBuilder implements SqlBuilder {

  void rewriteSchema(SQLTableSource tableSource, String schema) {
    if (null == tableSource) {
      return;
    }
    if (tableSource instanceof SQLExprTableSource) {
      SQLExpr sqlExpr = ((SQLExprTableSource) tableSource).getExpr();
      if (sqlExpr instanceof SQLPropertyExpr) {
        sqlExpr = new SQLPropertyExpr(new SQLIdentifierExpr(schema),
            ((SQLPropertyExpr) sqlExpr).getName());
      } else {
        sqlExpr = new SQLPropertyExpr(new SQLIdentifierExpr(schema),
            ((SQLIdentifierExpr) sqlExpr).getName());
      }
      ((SQLExprTableSource) tableSource).setExpr(sqlExpr);
    } else if (tableSource instanceof SQLJoinTableSource) {
      rewriteSchema(((SQLJoinTableSource) tableSource).getLeft(), schema);
      rewriteSchema(((SQLJoinTableSource) tableSource).getRight(), schema);
    }
  }

  void rewriteTable(SQLTableSource tableSource, String replaceTableName) {
    if (null == tableSource) {
      return;
    }
    if (tableSource instanceof SQLExprTableSource) {
      SQLExpr sqlExpr = ((SQLExprTableSource) tableSource).getExpr();
      if (sqlExpr instanceof SQLPropertyExpr) {
        String tableName = ((SQLPropertyExpr) sqlExpr).getName();
        if(!tableName.startsWith(EngineConstants.REWRITE_PH_TBL_PREFIX)
            && tableName.equalsIgnoreCase(replaceTableName)) {
          ((SQLPropertyExpr) sqlExpr).setName(
              String.format(
                  EngineConstants.REWRITE_PH_TBL_TEMPLATE,
                  EngineConstants.REWRITE_PH_TBL_PREFIX, tableName.toUpperCase(), EngineConstants.REWRITE_PH_TBL_SUFFIX));
        }
      }
      if (sqlExpr instanceof SQLIdentifierExpr) {
        String tableName = ((SQLIdentifierExpr) sqlExpr).getName();
        if(!tableName.startsWith(EngineConstants.REWRITE_PH_TBL_PREFIX)
            && tableName.equalsIgnoreCase(replaceTableName)) {
          ((SQLIdentifierExpr) sqlExpr).setName(
              String.format(
                  EngineConstants.REWRITE_PH_TBL_TEMPLATE,
                  EngineConstants.REWRITE_PH_TBL_PREFIX, tableName.toUpperCase(), EngineConstants.REWRITE_PH_TBL_SUFFIX));
        }
      }
      ((SQLExprTableSource) tableSource).setExpr(sqlExpr);
    }
    if (tableSource instanceof SQLJoinTableSource) {
      rewriteTable(((SQLJoinTableSource) tableSource).getLeft(), replaceTableName);
      rewriteTable(((SQLJoinTableSource) tableSource).getRight(), replaceTableName);
    }
  }

  public void rewriteLimit(Limit limit) {
    throw new UnsupportedOperationException();
  }

  abstract SQLStatement getSqlStatement();

  abstract String getDbType();

  @Override
  public String toString() {
    return SQLUtils.toSQLString(getSqlStatement(), getDbType(), LCASE_NOPRETTY_FORMAT_OPTION);
  }

  /**
   * 索引模式-副本模式特殊处理,不再 嵌套两层 SELECT * FROM；后续环节会处理
   * @return
   */
  public String toStringForIndexTable(){
    return SQLUtils.toSQLString(getSqlStatement(), getDbType(), LCASE_NOPRETTY_FORMAT_OPTION);
  }
}
