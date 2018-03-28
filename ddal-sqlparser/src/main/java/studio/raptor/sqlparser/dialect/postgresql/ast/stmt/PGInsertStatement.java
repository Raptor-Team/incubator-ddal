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
package studio.raptor.sqlparser.dialect.postgresql.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.dialect.postgresql.ast.PGWithClause;
import studio.raptor.sqlparser.dialect.postgresql.visitor.PGASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class PGInsertStatement extends SQLInsertStatement implements PGSQLStatement {

  private PGWithClause with;
  private List<ValuesClause> valuesList = new ArrayList<ValuesClause>();
  private SQLExpr returning;
  private boolean defaultValues = false;

  public SQLExpr getReturning() {
    return returning;
  }

  public void setReturning(SQLExpr returning) {
    this.returning = returning;
  }

  public PGWithClause getWith() {
    return with;
  }

  public void setWith(PGWithClause with) {
    this.with = with;
  }

  public ValuesClause getValues() {
    if (valuesList.size() == 0) {
      return null;
    }
    return valuesList.get(0);
  }

  public void setValues(ValuesClause values) {
    if (valuesList.size() == 0) {
      valuesList.add(values);
    } else {
      valuesList.set(0, values);
    }
  }

  public List<ValuesClause> getValuesList() {
    return valuesList;
  }

  public void addValueCause(ValuesClause valueClause) {
    valueClause.setParent(this);
    valuesList.add(valueClause);
  }

  public boolean isDefaultValues() {
    return defaultValues;
  }

  public void setDefaultValues(boolean defaultValues) {
    this.defaultValues = defaultValues;
  }

  protected void accept0(SQLASTVisitor visitor) {
    accept0((PGASTVisitor) visitor);
  }

  @Override
  public void accept0(PGASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, with);
      this.acceptChild(visitor, tableSource);
      this.acceptChild(visitor, columns);
      this.acceptChild(visitor, valuesList);
      this.acceptChild(visitor, query);
      this.acceptChild(visitor, returning);
    }

    visitor.endVisit(this);
  }
}
