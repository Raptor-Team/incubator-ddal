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

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlOutputVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class MySqlInsertStatement extends SQLInsertStatement {

  private final List<SQLExpr> duplicateKeyUpdate = new ArrayList<SQLExpr>();
  private boolean lowPriority = false;
  private boolean delayed = false;
  private boolean highPriority = false;
  private boolean ignore = false;
  private boolean rollbackOnFail = false;

  public List<SQLExpr> getDuplicateKeyUpdate() {
    return duplicateKeyUpdate;
  }

  public boolean isLowPriority() {
    return lowPriority;
  }

  public void setLowPriority(boolean lowPriority) {
    this.lowPriority = lowPriority;
  }

  public boolean isDelayed() {
    return delayed;
  }

  public void setDelayed(boolean delayed) {
    this.delayed = delayed;
  }

  public boolean isHighPriority() {
    return highPriority;
  }

  public void setHighPriority(boolean highPriority) {
    this.highPriority = highPriority;
  }

  public boolean isIgnore() {
    return ignore;
  }

  public void setIgnore(boolean ignore) {
    this.ignore = ignore;
  }

  public boolean isRollbackOnFail() {
    return rollbackOnFail;
  }

  public void setRollbackOnFail(boolean rollbackOnFail) {
    this.rollbackOnFail = rollbackOnFail;
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

  public void output(StringBuffer buf) {
    new MySqlOutputVisitor(buf).visit(this);
  }

  protected void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, getTableSource());
      this.acceptChild(visitor, getColumns());
      this.acceptChild(visitor, getValuesList());
      this.acceptChild(visitor, getQuery());
      this.acceptChild(visitor, getDuplicateKeyUpdate());
    }

    visitor.endVisit(this);
  }
}