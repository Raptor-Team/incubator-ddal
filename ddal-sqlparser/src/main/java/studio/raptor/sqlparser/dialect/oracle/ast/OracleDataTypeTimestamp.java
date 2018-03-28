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

import studio.raptor.sqlparser.ast.SQLDataTypeImpl;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OracleDataTypeTimestamp extends SQLDataTypeImpl implements OracleSQLObject {

  private boolean withTimeZone = false;
  private boolean withLocalTimeZone = false;

  public OracleDataTypeTimestamp() {
    this.setName("TIMESTAMP");
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

  public boolean isWithTimeZone() {
    return withTimeZone;
  }

  public void setWithTimeZone(boolean withTimeZone) {
    this.withTimeZone = withTimeZone;
  }

  public boolean isWithLocalTimeZone() {
    return withLocalTimeZone;
  }

  public void setWithLocalTimeZone(boolean withLocalTimeZone) {
    this.withLocalTimeZone = withLocalTimeZone;
  }

}
