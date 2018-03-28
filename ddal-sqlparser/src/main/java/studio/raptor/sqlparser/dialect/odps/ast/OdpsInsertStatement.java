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
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OdpsInsertStatement extends SQLStatementImpl implements SQLStatement {

  private SQLTableSource from;

  private List<OdpsInsert> items = new ArrayList<OdpsInsert>();

  public OdpsInsertStatement() {
    super(JdbcConstants.ODPS);
  }

  public SQLTableSource getFrom() {
    return from;
  }

  public void setFrom(SQLTableSource from) {
    this.from = from;
  }

  public List<OdpsInsert> getItems() {
    return items;
  }

  public void addItem(OdpsInsert item) {
    if (item != null) {
      item.setParent(this);
    }
    this.items.add(item);
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  protected void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, from);
      acceptChild(visitor, items);
    }
    visitor.endVisit(this);
  }
}
