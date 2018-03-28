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
package studio.raptor.sqlparser.ast.statement;

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLDeleteStatement extends SQLStatementImpl {

  protected SQLTableSource tableSource;
  protected SQLExpr where;
  protected SQLTableSource from;

  public SQLDeleteStatement() {

  }

  public SQLDeleteStatement(String dbType) {
    super(dbType);
  }

  public SQLTableSource getTableSource() {
    return tableSource;
  }

  public void setTableSource(SQLTableSource tableSource) {
    if (tableSource != null) {
      tableSource.setParent(this);
    }
    this.tableSource = tableSource;
  }

  public SQLExprTableSource getExprTableSource() {
    return (SQLExprTableSource) getTableSource();
  }

  public void setTableSource(SQLExpr expr) {
    this.setTableSource(new SQLExprTableSource(expr));
  }

  public SQLName getTableName() {
    if (this.tableSource instanceof SQLExprTableSource) {
      SQLExprTableSource exprTableSource = (SQLExprTableSource) this.tableSource;
      return (SQLName) exprTableSource.getExpr();
    }

    if (tableSource instanceof SQLSubqueryTableSource) {
      SQLSelectQuery selectQuery = ((SQLSubqueryTableSource) tableSource).getSelect().getQuery();
      if (selectQuery instanceof SQLSelectQueryBlock) {
        SQLTableSource subQueryTableSource = ((SQLSelectQueryBlock) selectQuery).getFrom();
        if (subQueryTableSource instanceof SQLExprTableSource) {
          SQLExpr subQueryTableSourceExpr = ((SQLExprTableSource) subQueryTableSource).getExpr();
          return (SQLName) subQueryTableSourceExpr;
        }
      }
    }

    return null;
  }

  public void setTableName(String name) {
    setTableName(new SQLIdentifierExpr(name));
  }

  public void setTableName(SQLName tableName) {
    this.setTableSource(new SQLExprTableSource(tableName));
  }

  public SQLExpr getWhere() {
    return where;
  }

  public void setWhere(SQLExpr where) {
    if (where != null) {
      where.setParent(this);
    }
    this.where = where;
  }

  public String getAlias() {
    return this.tableSource.getAlias();
  }

  public void setAlias(String alias) {
    this.tableSource.setAlias(alias);
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, tableSource);
      acceptChild(visitor, where);
    }

    visitor.endVisit(this);
  }

  public SQLTableSource getFrom() {
    return from;
  }

  public void setFrom(SQLTableSource from) {
    if (from != null) {
      from.setParent(this);
    }
    this.from = from;
  }
}
