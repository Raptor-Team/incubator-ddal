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
import studio.raptor.sqlparser.ast.statement.SQLErrorLoggingClause;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.OracleReturningClause;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleOutputVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleInsertStatement extends SQLInsertStatement implements OracleStatement {

  private OracleReturningClause returning;
  private SQLErrorLoggingClause errorLogging;
  private List<SQLHint> hints = new ArrayList<SQLHint>();

  public List<SQLHint> getHints() {
    return hints;
  }

  public void setHints(List<SQLHint> hints) {
    this.hints = hints;
  }

  public OracleReturningClause getReturning() {
    return returning;
  }

  public void setReturning(OracleReturningClause returning) {
    this.returning = returning;
  }

  public SQLErrorLoggingClause getErrorLogging() {
    return errorLogging;
  }

  public void setErrorLogging(SQLErrorLoggingClause errorLogging) {
    this.errorLogging = errorLogging;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    this.accept0((OracleASTVisitor) visitor);
  }

  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, getTableSource());
      this.acceptChild(visitor, getColumns());
      this.acceptChild(visitor, getValues());
      this.acceptChild(visitor, getQuery());
      this.acceptChild(visitor, returning);
      this.acceptChild(visitor, errorLogging);
    }

    visitor.endVisit(this);
  }

  public void output(StringBuffer buf) {
    new OracleOutputVisitor(buf).visit(this);
  }
}
