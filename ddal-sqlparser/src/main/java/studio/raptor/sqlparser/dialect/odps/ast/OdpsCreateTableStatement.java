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
package studio.raptor.sqlparser.dialect.odps.ast;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OdpsCreateTableStatement extends SQLCreateTableStatement {

  protected SQLExpr comment;
  protected List<SQLColumnDefinition> partitionColumns = new ArrayList<SQLColumnDefinition>(2);
  protected SQLExpr lifecycle;
  private SQLExprTableSource like;

  public OdpsCreateTableStatement() {
    super(JdbcConstants.ODPS);
  }

  public SQLExprTableSource getLike() {
    return like;
  }

  public void setLike(SQLExprTableSource like) {
    this.like = like;
  }

  public void setLike(SQLName like) {
    this.setLike(new SQLExprTableSource(like));
  }

  public SQLExpr getComment() {
    return comment;
  }

  public void setComment(SQLExpr comment) {
    this.comment = comment;
  }

  public List<SQLColumnDefinition> getPartitionColumns() {
    return partitionColumns;
  }

  public void addPartitionColumn(SQLColumnDefinition column) {
    if (column != null) {
      column.setParent(this);
    }
    this.partitionColumns.add(column);
  }

  public SQLExpr getLifecycle() {
    return lifecycle;
  }

  public void setLifecycle(SQLExpr lifecycle) {
    this.lifecycle = lifecycle;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  protected void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, tableSource);
      this.acceptChild(visitor, tableElementList);
      this.acceptChild(visitor, partitionColumns);
      this.acceptChild(visitor, lifecycle);
      this.acceptChild(visitor, select);
    }
    visitor.endVisit(this);
  }

}
