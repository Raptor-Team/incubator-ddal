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
package studio.raptor.sqlparser.dialect.oracle.ast.expr;

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLObjectImpl;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleAnalyticWindowing extends SQLObjectImpl implements OracleExpr {

  private Type type;
  private SQLExpr expr;

  public OracleAnalyticWindowing() {

  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    this.accept0((OracleASTVisitor) visitor);
  }

  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.expr);
    }
    visitor.endVisit(this);
  }

  public SQLExpr getExpr() {
    return this.expr;
  }

  public void setExpr(SQLExpr expr) {
    this.expr = expr;
  }

  public Type getType() {
    return this.type;
  }

  public void setType(Type type) {
    this.type = type;
  }

  public static enum Type {
    ROWS, RANGE;
  }
}
