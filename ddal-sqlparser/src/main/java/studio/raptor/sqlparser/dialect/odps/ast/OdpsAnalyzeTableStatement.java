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
package studio.raptor.sqlparser.dialect.odps.ast;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLAssignItem;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;

public class OdpsAnalyzeTableStatement extends OdpsStatementImpl {

  private SQLExprTableSource table;
  private List<SQLAssignItem> partition = new ArrayList<SQLAssignItem>();

  @Override
  protected void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, table);
      this.acceptChild(visitor, partition);
    }
    visitor.endVisit(this);
  }

  public SQLExprTableSource getTable() {
    return table;
  }

  public void setTable(SQLName table) {
    this.setTable(new SQLExprTableSource(table));
  }

  public void setTable(SQLExprTableSource table) {
    if (table != null) {
      table.setParent(table);
    }
    this.table = table;
  }

  public List<SQLAssignItem> getPartition() {
    return partition;
  }

}
