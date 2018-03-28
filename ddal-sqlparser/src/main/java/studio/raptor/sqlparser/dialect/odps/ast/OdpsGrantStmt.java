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
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.statement.SQLGrantStatement;
import studio.raptor.sqlparser.ast.statement.SQLObjectType;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OdpsGrantStmt extends SQLGrantStatement {

  private SQLObjectType subjectType;

  private boolean isSuper = false;

  private boolean isLabel = false;
  private SQLExpr label;
  private List<SQLName> columns = new ArrayList<SQLName>();
  ;
  private SQLExpr expire;

  public OdpsGrantStmt() {
    super(JdbcConstants.ODPS);
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  protected void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, on);
      acceptChild(visitor, to);
    }
    visitor.endVisit(this);
  }

  public SQLObject getOn() {
    return on;
  }

  public void setOn(SQLExpr on) {
    if (on != null) {
      on.setParent(this);
    }
    this.on = on;
  }

  public SQLExpr getTo() {
    return to;
  }

  public void setTo(SQLExpr to) {
    this.to = to;
  }

  public List<SQLExpr> getPrivileges() {
    return privileges;
  }

  public SQLObjectType getSubjectType() {
    return subjectType;
  }

  public void setSubjectType(SQLObjectType subjectType) {
    this.subjectType = subjectType;
  }

  public boolean isSuper() {
    return isSuper;
  }

  public void setSuper(boolean isSuper) {
    this.isSuper = isSuper;
  }

  public boolean isLabel() {
    return isLabel;
  }

  public void setLabel(boolean isLabel) {
    this.isLabel = isLabel;
  }

  public SQLExpr getLabel() {
    return label;
  }

  public void setLabel(SQLExpr label) {
    this.label = label;
  }

  public List<SQLName> getColumns() {
    return columns;
  }

  public void setColumnList(List<SQLName> columns) {
    this.columns = columns;
  }

  public SQLExpr getExpire() {
    return expire;
  }

  public void setExpire(SQLExpr expire) {
    this.expire = expire;
  }

}
