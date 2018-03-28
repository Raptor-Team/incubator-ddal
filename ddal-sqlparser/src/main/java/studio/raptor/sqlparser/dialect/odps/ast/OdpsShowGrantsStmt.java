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

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OdpsShowGrantsStmt extends SQLStatementImpl implements SQLStatement {

  private SQLExpr user;

  private SQLExpr objectType;

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  public void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, user);
      acceptChild(visitor, objectType);
    }
    visitor.endVisit(this);
  }

  public SQLExpr getUser() {
    return user;
  }

  public void setUser(SQLExpr user) {
    if (user != null) {
      user.setParent(this);
    }
    this.user = user;
  }

  public SQLExpr getObjectType() {
    return objectType;
  }

  public void setObjectType(SQLExpr objectType) {
    if (objectType != null) {
      objectType.setParent(this);
    }
    this.objectType = objectType;
  }
}
