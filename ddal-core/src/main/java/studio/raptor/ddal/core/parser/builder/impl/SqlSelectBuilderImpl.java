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



import studio.raptor.ddal.core.parser.builder.SqlBuilder;
import studio.raptor.ddal.core.parser.result.merger.Limit;
import studio.raptor.sqlparser.ast.SQLLimit;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLIntegerExpr;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.ast.statement.SQLSelectQuery;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import studio.raptor.sqlparser.util.JdbcConstants;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SqlSelectBuilderImpl extends AbstractSqlBuilder {

  private ThreadLocal<SQLSelectStatement> stmt;
  private String dbType;
  //TODO 临时添加针对于ORACLE的默认值，Mysql后期考虑
  private Limit limit = new Limit(0,1000);

  public SqlSelectBuilderImpl(SQLSelectStatement stmt, String dbType) {
    this.stmt = new ThreadLocal<>();
    this.stmt.set(stmt);
    this.dbType = dbType;
  }

  @Override
  public void rewriteTable(String table) {
    rewriteTable(getQueryBlock().getFrom(), table);
  }

  @Override
  public void rewriteSchema(String schema) {
    rewriteSchema(getQueryBlock().getFrom(), schema);
  }

  public SqlBuilder from(String table, String alias) {
    SQLSelectQueryBlock queryBlock = getQueryBlock();
    SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
    queryBlock.setFrom(from);
    return this;
  }

  private SQLSelectQueryBlock getQueryBlock() {
    SQLSelect select = stmt.get().getSelect();
    SQLSelectQuery query = select.getQuery();
    if (!(query instanceof SQLSelectQueryBlock)) {
      throw new IllegalStateException("not support from, class : " + query.getClass().getName());
    }
    return (SQLSelectQueryBlock) query;
  }

  @Override
  public void rewriteLimit(Limit limit) {
    this.limit = limit;
    SQLSelectQueryBlock queryBlock = getQueryBlock();
    if (queryBlock instanceof MySqlSelectQueryBlock) {
      MySqlSelectQueryBlock mySqlQueryBlock = (MySqlSelectQueryBlock) queryBlock;
      SQLLimit sqlLimit = new SQLLimit();
      sqlLimit.setRowCount(new SQLIntegerExpr(this.limit.getRowCount()));
      if (this.limit.getOffset() > 0) {
        sqlLimit.setOffset(new SQLIntegerExpr(this.limit.getOffset()));
      }
      mySqlQueryBlock.setLimit(sqlLimit);
    }
  }

  @Override
  SQLStatement getSqlStatement() {
    return this.stmt.get();
  }

  @Override
  String getDbType() {
    return this.dbType;
  }

  @Override
  public String toString() {
    String sql = super.toString();
    if(dbType.equalsIgnoreCase(JdbcConstants.ORACLE) && null != this.limit) {
      sql = String.format(
          "SELECT * FROM (SELECT XX.*, ROWNUM AS RN FROM (%s) XX WHERE ROWNUM <= %d ) XXX WHERE RN > %d",
          sql, this.limit.getRowCount() + this.limit.getOffset(), this.limit.getOffset());
    }
    return sql;
  }
}
