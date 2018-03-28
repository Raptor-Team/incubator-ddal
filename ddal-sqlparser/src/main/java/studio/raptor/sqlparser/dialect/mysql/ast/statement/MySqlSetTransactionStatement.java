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
package studio.raptor.sqlparser.dialect.mysql.ast.statement;

import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlSetTransactionStatement extends MySqlStatementImpl {

  private Boolean global;

  private String isolationLevel;

  private String accessModel;

  public void accept0(MySqlASTVisitor visitor) {
    visitor.visit(this);
    visitor.endVisit(this);
  }

  public Boolean getGlobal() {
    return global;
  }

  public void setGlobal(Boolean global) {
    this.global = global;
  }

  public String getIsolationLevel() {
    return isolationLevel;
  }

  public void setIsolationLevel(String isolationLevel) {
    this.isolationLevel = isolationLevel;
  }

  public String getAccessModel() {
    return accessModel;
  }

  public void setAccessModel(String accessModel) {
    this.accessModel = accessModel;
  }

}
