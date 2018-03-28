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
package studio.raptor.sqlparser.ast.expr;

import static studio.raptor.sqlparser.visitor.SQLEvalVisitor.EVAL_VALUE_NULL;

import studio.raptor.sqlparser.ast.SQLExprImpl;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLNullExpr extends SQLExprImpl implements SQLLiteralExpr, SQLValuableExpr {

  public SQLNullExpr() {

  }

  public void output(StringBuffer buf) {
    buf.append("NULL");
  }

  protected void accept0(SQLASTVisitor visitor) {
    visitor.visit(this);

    visitor.endVisit(this);
  }

  public int hashCode() {
    return 0;
  }

  public boolean equals(Object o) {
    return o instanceof SQLNullExpr;
  }

  @Override
  public Object getValue() {
    return EVAL_VALUE_NULL;
  }
}
