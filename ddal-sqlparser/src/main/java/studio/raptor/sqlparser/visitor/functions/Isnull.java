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
package studio.raptor.sqlparser.visitor.functions;

import static studio.raptor.sqlparser.visitor.SQLEvalVisitor.EVAL_ERROR;
import static studio.raptor.sqlparser.visitor.SQLEvalVisitor.EVAL_VALUE;
import static studio.raptor.sqlparser.visitor.SQLEvalVisitor.EVAL_VALUE_NULL;

import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.expr.SQLMethodInvokeExpr;
import studio.raptor.sqlparser.visitor.SQLEvalVisitor;

public class Isnull implements Function {

  public final static Isnull instance = new Isnull();

  public Object eval(SQLEvalVisitor visitor, SQLMethodInvokeExpr x) {
    final List<SQLExpr> parameters = x.getParameters();
    if (parameters.size() == 0) {
      return EVAL_ERROR;
    }

    SQLExpr condition = parameters.get(0);
    condition.accept(visitor);
    Object itemValue = condition.getAttributes().get(EVAL_VALUE);
    if (itemValue == EVAL_VALUE_NULL) {
      return Boolean.TRUE;
    } else if (itemValue == null) {
      return null;
    } else {
      return Boolean.FALSE;
    }
  }
}
