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

import java.util.List;
import studio.raptor.sqlparser.ast.SQLCommentHint;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlLockTableStatement extends MySqlStatementImpl {

  private SQLExprTableSource tableSource;

  private LockType lockType;

  private List<SQLCommentHint> hints;

  public SQLExprTableSource getTableSource() {
    return tableSource;
  }

  public void setTableSource(SQLName name) {
    setTableSource(new SQLExprTableSource(name));
  }

  public void setTableSource(SQLExprTableSource tableSource) {
    if (tableSource != null) {
      tableSource.setParent(this);
    }
    this.tableSource = tableSource;
  }

  public LockType getLockType() {
    return lockType;
  }

  public void setLockType(LockType lockType) {
    this.lockType = lockType;
  }

  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, tableSource);
    }
    visitor.endVisit(this);
  }

  public List<SQLCommentHint> getHints() {
    return hints;
  }

  public void setHints(List<SQLCommentHint> hints) {
    this.hints = hints;
  }

  public static enum LockType {
    READ("READ"), READ_LOCAL("READ LOCAL"), WRITE("WRITE"), LOW_PRIORITY_WRITE(
        "LOW_PRIORITY WRITE");

    public final String name;

    LockType(String name) {
      this.name = name;
    }
  }
}
