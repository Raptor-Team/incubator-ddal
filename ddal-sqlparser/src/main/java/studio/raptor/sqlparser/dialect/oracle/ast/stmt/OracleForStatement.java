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
package studio.raptor.sqlparser.dialect.oracle.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;

public class OracleForStatement extends OracleStatementImpl {

  private SQLName index;

  private SQLExpr range;

  private List<SQLStatement> statements = new ArrayList<SQLStatement>();

  @Override
  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, index);
      acceptChild(visitor, range);
      acceptChild(visitor, statements);
    }
    visitor.endVisit(this);
  }

  public SQLName getIndex() {
    return index;
  }

  public void setIndex(SQLName index) {
    this.index = index;
  }

  public SQLExpr getRange() {
    return range;
  }

  public void setRange(SQLExpr range) {
    this.range = range;
  }

  public List<SQLStatement> getStatements() {
    return statements;
  }

  public void setStatements(List<SQLStatement> statements) {
    this.statements = statements;
  }

}
