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
import studio.raptor.sqlparser.ast.SQLExprImpl;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleOuterExpr extends SQLExprImpl implements OracleExpr {

  private SQLExpr expr;

  public OracleOuterExpr() {

  }

  public OracleOuterExpr(SQLExpr expr) {

    this.expr = expr;
  }

  public SQLExpr getExpr() {
    return this.expr;
  }

  public void setExpr(SQLExpr expr) {
    this.expr = expr;
  }

  public void output(StringBuffer buf) {
    this.expr.output(buf);
    buf.append("(+)");
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

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((expr == null) ? 0 : expr.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    OracleOuterExpr other = (OracleOuterExpr) obj;
    if (expr == null) {
      if (other.expr != null) {
        return false;
      }
    } else if (!expr.equals(other.expr)) {
      return false;
    }
    return true;
  }
}
