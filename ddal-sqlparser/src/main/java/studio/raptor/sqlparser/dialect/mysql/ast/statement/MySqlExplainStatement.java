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
package studio.raptor.sqlparser.dialect.mysql.ast.statement;

import studio.raptor.sqlparser.SQLUtils;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLExplainStatement;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class MySqlExplainStatement extends SQLExplainStatement implements MySqlStatement {

  private boolean describe;

  private SQLName tableName;
  private SQLName columnName;
  private SQLExpr wild;

  private String format;

  private SQLExpr connectionId;

  public MySqlExplainStatement() {
    super(JdbcConstants.MYSQL);
  }

  @Override
  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      // tbl_name [col_name | wild]
      if (tableName != null) {
        acceptChild(visitor, tableName);
        if (columnName != null) {
          acceptChild(visitor, columnName);
        } else if (wild != null) {
          acceptChild(visitor, wild);
        }
      } else {
        // {explainable_stmt | FOR CONNECTION connection_id}
        if (connectionId != null) {
          acceptChild(visitor, connectionId);
        } else {
          acceptChild(visitor, statement);
        }
      }
    }

    visitor.endVisit(this);
  }

  protected void accept0(SQLASTVisitor visitor) {
    accept0((MySqlASTVisitor) visitor);
  }

  public String toString() {
    return SQLUtils.toMySqlString(this);
  }

  public boolean isDescribe() {
    return describe;
  }

  public void setDescribe(boolean describe) {
    this.describe = describe;
  }

  public SQLName getTableName() {
    return tableName;
  }

  public void setTableName(SQLName tableName) {
    this.tableName = tableName;
  }

  public SQLName getColumnName() {
    return columnName;
  }

  public void setColumnName(SQLName columnName) {
    this.columnName = columnName;
  }

  public SQLExpr getWild() {
    return wild;
  }

  public void setWild(SQLExpr wild) {
    this.wild = wild;
  }

  public String getFormat() {
    return format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public SQLExpr getConnectionId() {
    return connectionId;
  }

  public void setConnectionId(SQLExpr connectionId) {
    this.connectionId = connectionId;
  }

}
