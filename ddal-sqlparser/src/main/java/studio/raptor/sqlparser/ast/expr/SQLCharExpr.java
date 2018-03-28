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

import studio.raptor.sqlparser.SQLUtils;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLCharExpr extends SQLTextLiteralExpr implements SQLValuableExpr {

  public SQLCharExpr() {

  }

  public SQLCharExpr(String text) {
    super(text);
  }

  @Override
  public void output(StringBuffer buf) {
    if ((this.text == null) || (this.text.length() == 0)) {
      buf.append("NULL");
    } else {
      buf.append("'");
      buf.append(this.text.replaceAll("'", "''"));
      buf.append("'");
    }
  }

  protected void accept0(SQLASTVisitor visitor) {
    visitor.visit(this);
    visitor.endVisit(this);
  }

  @Override
  public Object getValue() {
    return this.text;
  }

  public String toString() {
    return SQLUtils.toSQLString(this);
  }
}
