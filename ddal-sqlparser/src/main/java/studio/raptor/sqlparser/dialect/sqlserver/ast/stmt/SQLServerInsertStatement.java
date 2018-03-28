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
package studio.raptor.sqlparser.dialect.sqlserver.ast.stmt;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.dialect.sqlserver.ast.SQLServerObject;
import studio.raptor.sqlparser.dialect.sqlserver.ast.SQLServerOutput;
import studio.raptor.sqlparser.dialect.sqlserver.ast.SQLServerTop;
import studio.raptor.sqlparser.dialect.sqlserver.visitor.SQLServerASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLServerInsertStatement extends SQLInsertStatement implements SQLServerObject {

  private List<ValuesClause> valuesList = new ArrayList<ValuesClause>();

  private boolean defaultValues;

  private SQLServerTop top;

  private SQLServerOutput output;

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

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    this.accept0((SQLServerASTVisitor) visitor);
  }

  @Override
  public void accept0(SQLServerASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, getTop());
      this.acceptChild(visitor, getTableSource());
      this.acceptChild(visitor, getColumns());
      this.acceptChild(visitor, getOutput());
      this.acceptChild(visitor, getValuesList());
      this.acceptChild(visitor, getQuery());
    }

    visitor.endVisit(this);
  }

  public boolean isDefaultValues() {
    return defaultValues;
  }

  public void setDefaultValues(boolean defaultValues) {
    this.defaultValues = defaultValues;
  }

  public SQLServerOutput getOutput() {
    return output;
  }

  public void setOutput(SQLServerOutput output) {
    this.output = output;
  }

  public SQLServerTop getTop() {
    return top;
  }

  public void setTop(SQLServerTop top) {
    this.top = top;
  }

}
