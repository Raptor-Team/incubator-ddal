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
package studio.raptor.sqlparser.dialect.oracle.ast;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLDataTypeImpl;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleDataTypeIntervalDay extends SQLDataTypeImpl implements OracleSQLObject {

  protected final List<SQLExpr> fractionalSeconds = new ArrayList<SQLExpr>();
  private boolean toSecond = false;

  public OracleDataTypeIntervalDay() {
    this.setName("INTERVAL DAY");
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    this.accept0((OracleASTVisitor) visitor);
  }

  @Override
  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, getArguments());
    }
    visitor.endVisit(this);
  }

  public boolean isToSecond() {
    return toSecond;
  }

  public void setToSecond(boolean toSecond) {
    this.toSecond = toSecond;
  }

  public List<SQLExpr> getFractionalSeconds() {
    return fractionalSeconds;
  }

}
