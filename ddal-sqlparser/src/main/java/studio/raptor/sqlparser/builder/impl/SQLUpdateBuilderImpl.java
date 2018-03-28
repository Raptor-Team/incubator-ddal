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
package studio.raptor.sqlparser.builder.impl;

import java.util.List;
import java.util.Map;
import studio.raptor.sqlparser.SQLUtils;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOperator;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLUpdateSetItem;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.builder.SQLUpdateBuilder;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlUpdateStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleUpdateStatement;
import studio.raptor.sqlparser.dialect.postgresql.ast.stmt.PGUpdateStatement;
import studio.raptor.sqlparser.dialect.sqlserver.ast.stmt.SQLServerUpdateStatement;
import studio.raptor.sqlparser.util.JdbcConstants;

public class SQLUpdateBuilderImpl extends SQLBuilderImpl implements SQLUpdateBuilder {

  private SQLUpdateStatement stmt;
  private String dbType;

  public SQLUpdateBuilderImpl(String dbType) {
    this.dbType = dbType;
  }

  public SQLUpdateBuilderImpl(String sql, String dbType) {
    List<SQLStatement> stmtList = SQLUtils.parseStatements(sql, dbType);

    if (stmtList.size() == 0) {
      throw new IllegalArgumentException("not support empty-statement :" + sql);
    }

    if (stmtList.size() > 1) {
      throw new IllegalArgumentException("not support multi-statement :" + sql);
    }

    SQLUpdateStatement stmt = (SQLUpdateStatement) stmtList.get(0);
    this.stmt = stmt;
    this.dbType = dbType;
  }

  public SQLUpdateBuilderImpl(SQLUpdateStatement stmt, String dbType) {
    this.stmt = stmt;
    this.dbType = dbType;
  }

  @Override
  public SQLUpdateBuilderImpl limit(int rowCount) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SQLUpdateBuilderImpl limit(int rowCount, int offset) {
    throw new UnsupportedOperationException();
  }

  @Override
  public SQLUpdateBuilderImpl from(String table) {
    return from(table, null);
  }

  @Override
  public SQLUpdateBuilderImpl from(String table, String alias) {
    SQLUpdateStatement update = getSQLUpdateStatement();
    SQLExprTableSource from = new SQLExprTableSource(new SQLIdentifierExpr(table), alias);
    update.setTableSource(from);
    return this;
  }

  @Override
  public SQLUpdateBuilderImpl where(String expr) {
    SQLUpdateStatement update = getSQLUpdateStatement();

    SQLExpr exprObj = SQLUtils.toSQLExpr(expr, dbType);
    update.setWhere(exprObj);

    return this;
  }

  @Override
  public SQLUpdateBuilderImpl whereAnd(String expr) {
    SQLUpdateStatement update = getSQLUpdateStatement();

    SQLExpr exprObj = SQLUtils.toSQLExpr(expr, dbType);
    SQLExpr newCondition = SQLUtils
        .buildCondition(SQLBinaryOperator.BooleanAnd, exprObj, false, update.getWhere());
    update.setWhere(newCondition);

    return this;
  }

  @Override
  public SQLUpdateBuilderImpl whereOr(String expr) {
    SQLUpdateStatement update = getSQLUpdateStatement();

    SQLExpr exprObj = SQLUtils.toSQLExpr(expr, dbType);
    SQLExpr newCondition = SQLUtils
        .buildCondition(SQLBinaryOperator.BooleanOr, exprObj, false, update.getWhere());
    update.setWhere(newCondition);

    return this;
  }

  public SQLUpdateBuilderImpl set(String... items) {
    SQLUpdateStatement update = getSQLUpdateStatement();
    for (String item : items) {
      SQLUpdateSetItem updateSetItem = SQLUtils.toUpdateSetItem(item, dbType);
      update.addItem(updateSetItem);
    }

    return this;
  }

  public SQLUpdateBuilderImpl setValue(Map<String, Object> values) {
    for (Map.Entry<String, Object> entry : values.entrySet()) {
      setValue(entry.getKey(), entry.getValue());
    }

    return this;
  }

  public SQLUpdateBuilderImpl setValue(String column, Object value) {
    SQLUpdateStatement update = getSQLUpdateStatement();

    SQLExpr columnExpr = SQLUtils.toSQLExpr(column, dbType);
    SQLExpr valueExpr = toSQLExpr(value, dbType);

    SQLUpdateSetItem item = new SQLUpdateSetItem();
    item.setColumn(columnExpr);
    item.setValue(valueExpr);
    update.addItem(item);

    return this;
  }

  public SQLUpdateStatement getSQLUpdateStatement() {
    if (stmt == null) {
      stmt = createSQLUpdateStatement();
    }
    return stmt;
  }

  public SQLUpdateStatement createSQLUpdateStatement() {
    if (JdbcConstants.MYSQL.equals(dbType)) {
      return new MySqlUpdateStatement();
    }

    if (JdbcConstants.ORACLE.equals(dbType)) {
      return new OracleUpdateStatement();
    }

    if (JdbcConstants.POSTGRESQL.equals(dbType)) {
      return new PGUpdateStatement();
    }

    if (JdbcConstants.SQL_SERVER.equals(dbType)) {
      return new SQLServerUpdateStatement();
    }

    return new SQLUpdateStatement();
  }

  public String toString() {
    return SQLUtils.toSQLString(stmt, dbType);
  }
}
