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
import studio.raptor.sqlparser.ast.SQLHint;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleUpdateStatement extends SQLUpdateStatement implements OracleStatement {

  private final List<SQLHint> hints = new ArrayList<SQLHint>(1);
  private boolean only = false;
  private String alias;

  private List<SQLExpr> returningInto = new ArrayList<SQLExpr>();

  public OracleUpdateStatement() {
    super(JdbcConstants.ORACLE);
  }

  public List<SQLExpr> getReturningInto() {
    return returningInto;
  }

  public void setReturningInto(List<SQLExpr> returningInto) {
    this.returningInto = returningInto;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof OracleASTVisitor) {
      accept0((OracleASTVisitor) visitor);
      return;
    }

    super.accept(visitor);
  }

  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.hints);
      acceptChild(visitor, tableSource);
      acceptChild(visitor, items);
      acceptChild(visitor, where);
      acceptChild(visitor, returning);
      acceptChild(visitor, returningInto);
    }

    visitor.endVisit(this);
  }

  public String getAlias() {
    return this.alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }

  public boolean isOnly() {
    return this.only;
  }

  public void setOnly(boolean only) {
    this.only = only;
  }

  public List<SQLHint> getHints() {
    return this.hints;
  }
}
