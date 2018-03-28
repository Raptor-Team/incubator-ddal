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
package studio.raptor.sqlparser.dialect.oracle.visitor;

import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLNumberExpr;
import studio.raptor.sqlparser.visitor.ParameterizedOutputVisitorUtils;
import studio.raptor.sqlparser.visitor.ParameterizedVisitor;

public class OracleParameterizedOutputVisitor extends OracleOutputVisitor implements
    ParameterizedVisitor {

  public OracleParameterizedOutputVisitor() {
    this(new StringBuilder());
    this.parameterized = true;
  }

  public OracleParameterizedOutputVisitor(Appendable appender) {
    super(appender);
    this.parameterized = true;
  }

  public OracleParameterizedOutputVisitor(Appendable appender, boolean printPostSemi) {
    super(appender, printPostSemi);
  }

  public boolean visit(SQLBinaryOpExpr x) {
    x = ParameterizedOutputVisitorUtils.merge(this, x);

    return super.visit(x);
  }

  public boolean visit(SQLNumberExpr x) {
    if (!ParameterizedOutputVisitorUtils.checkParameterize(x)) {
      return super.visit(x);
    }

    print('?');
    incrementReplaceCunt();
    return false;
  }

}
