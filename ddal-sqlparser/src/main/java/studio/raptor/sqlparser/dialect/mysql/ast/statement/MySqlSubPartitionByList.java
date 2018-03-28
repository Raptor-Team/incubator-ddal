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

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLSubPartitionBy;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlObject;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class MySqlSubPartitionByList extends SQLSubPartitionBy implements MySqlObject {

  private SQLExpr expr;

  private List<SQLColumnDefinition> columns = new ArrayList<SQLColumnDefinition>();

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof MySqlASTVisitor) {
      accept0((MySqlASTVisitor) visitor);
    } else {
      throw new IllegalArgumentException(
          "not support visitor type : " + visitor.getClass().getName());
    }
  }

  @Override
  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, expr);
      acceptChild(visitor, columns);
      acceptChild(visitor, subPartitionsCount);
    }
    visitor.endVisit(this);
  }

  public SQLExpr getExpr() {
    return expr;
  }

  public void setExpr(SQLExpr expr) {
    if (expr != null) {
      expr.setParent(this);
    }
    this.expr = expr;
  }

  public List<SQLColumnDefinition> getColumns() {
    return columns;
  }

  public void addColumn(SQLColumnDefinition column) {
    if (column != null) {
      column.setParent(this);
    }
    this.columns.add(column);
  }

}
