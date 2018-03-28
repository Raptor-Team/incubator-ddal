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
import studio.raptor.sqlparser.dialect.oracle.ast.OracleSQLObjectImpl;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;

public class OracleFileSpecification extends OracleSQLObjectImpl {

  private List<SQLExpr> fileNames = new ArrayList<SQLExpr>();

  private SQLExpr size;

  private boolean autoExtendOff = false;

  private SQLExpr autoExtendOn;

  @Override
  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, fileNames);
      acceptChild(visitor, size);
      acceptChild(visitor, autoExtendOn);
    }
    visitor.endVisit(this);
  }

  public SQLExpr getAutoExtendOn() {
    return autoExtendOn;
  }

  public void setAutoExtendOn(SQLExpr autoExtendOn) {
    this.autoExtendOn = autoExtendOn;
  }

  public SQLExpr getSize() {
    return size;
  }

  public void setSize(SQLExpr size) {
    this.size = size;
  }

  public boolean isAutoExtendOff() {
    return autoExtendOff;
  }

  public void setAutoExtendOff(boolean autoExtendOff) {
    this.autoExtendOff = autoExtendOff;
  }

  public List<SQLExpr> getFileNames() {
    return fileNames;
  }

  public void setFileNames(List<SQLExpr> fileNames) {
    this.fileNames = fileNames;
  }

}
