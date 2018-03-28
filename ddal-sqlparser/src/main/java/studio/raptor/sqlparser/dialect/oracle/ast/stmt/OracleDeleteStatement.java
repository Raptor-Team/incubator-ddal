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
import studio.raptor.sqlparser.ast.SQLHint;
import studio.raptor.sqlparser.ast.statement.SQLDeleteStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.OracleReturningClause;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleDeleteStatement extends SQLDeleteStatement {

  private final List<SQLHint> hints = new ArrayList<SQLHint>();
  private boolean only = false;
  private OracleReturningClause returning = null;

  public OracleDeleteStatement() {
    super(JdbcConstants.ORACLE);
  }

  public OracleReturningClause getReturning() {
    return returning;
  }

  public void setReturning(OracleReturningClause returning) {
    this.returning = returning;
  }

  public List<SQLHint> getHints() {
    return this.hints;
  }

  protected void accept0(SQLASTVisitor visitor) {
    accept0((OracleASTVisitor) visitor);
  }

  protected void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.hints);
      acceptChild(visitor, this.tableSource);
      acceptChild(visitor, this.getWhere());
      acceptChild(visitor, returning);
    }

    visitor.endVisit(this);
  }

  public boolean isOnly() {
    return this.only;
  }

  public void setOnly(boolean only) {
    this.only = only;
  }

}
