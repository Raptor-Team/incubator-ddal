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

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.statement.SQLRollbackStatement;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class MySqlRollbackStatement extends SQLRollbackStatement implements MySqlStatement {

  private Boolean chain;
  private Boolean release;

  private SQLExpr force;

  public MySqlRollbackStatement() {
    super(JdbcConstants.MYSQL);
  }

  public Boolean getChain() {
    return chain;
  }

  public void setChain(Boolean chain) {
    this.chain = chain;
  }

  public Boolean getRelease() {
    return release;
  }

  public void setRelease(Boolean release) {
    this.release = release;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof MySqlASTVisitor) {
      accept0((MySqlASTVisitor) visitor);
    } else {
      throw new IllegalArgumentException(
          "not support visitor type : " + visitor.getClass().getName());
    }
  }

  @Override
  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, getTo());
      acceptChild(visitor, getForce());
    }

    visitor.endVisit(this);
  }

  public SQLExpr getForce() {
    return force;
  }

  public void setForce(SQLExpr force) {
    this.force = force;
  }

}
