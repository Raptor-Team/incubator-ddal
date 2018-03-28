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

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLLimit;
import studio.raptor.sqlparser.ast.SQLOrderBy;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class MySqlUpdateStatement extends SQLUpdateStatement implements MySqlStatement {

  private SQLOrderBy orderBy;
  private SQLLimit limit;

  private boolean lowPriority = false;
  private boolean ignore = false;
  private boolean commitOnSuccess = false;
  private boolean rollBackOnFail = false;
  private boolean queryOnPk = false;
  private SQLExpr targetAffectRow;

  public MySqlUpdateStatement() {
    super(JdbcConstants.MYSQL);
  }

  public SQLLimit getLimit() {
    return limit;
  }

  public void setLimit(SQLLimit limit) {
    if (limit != null) {
      limit.setParent(this);
    }
    this.limit = limit;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof MySqlASTVisitor) {
      accept0((MySqlASTVisitor) visitor);
    } else {
      throw new IllegalArgumentException(
          "not support visitor type : " + visitor.getClass().getName());
    }
  }

  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, tableSource);
      acceptChild(visitor, items);
      acceptChild(visitor, where);
      acceptChild(visitor, orderBy);
      acceptChild(visitor, limit);
    }
    visitor.endVisit(this);
  }

  public boolean isLowPriority() {
    return lowPriority;
  }

  public void setLowPriority(boolean lowPriority) {
    this.lowPriority = lowPriority;
  }

  public boolean isIgnore() {
    return ignore;
  }

  public void setIgnore(boolean ignore) {
    this.ignore = ignore;
  }

  public boolean isCommitOnSuccess() {
    return commitOnSuccess;
  }

  public void setCommitOnSuccess(boolean commitOnSuccess) {
    this.commitOnSuccess = commitOnSuccess;
  }

  public boolean isRollBackOnFail() {
    return rollBackOnFail;
  }

  public void setRollBackOnFail(boolean rollBackOnFail) {
    this.rollBackOnFail = rollBackOnFail;
  }

  public boolean isQueryOnPk() {
    return queryOnPk;
  }

  public void setQueryOnPk(boolean queryOnPk) {
    this.queryOnPk = queryOnPk;
  }

  public SQLExpr getTargetAffectRow() {
    return targetAffectRow;
  }

  public void setTargetAffectRow(SQLExpr targetAffectRow) {
    if (targetAffectRow != null) {
      targetAffectRow.setParent(this);
    }
    this.targetAffectRow = targetAffectRow;
  }

  public SQLOrderBy getOrderBy() {
    return orderBy;
  }

  public void setOrderBy(SQLOrderBy orderBy) {
    this.orderBy = orderBy;
  }

}
