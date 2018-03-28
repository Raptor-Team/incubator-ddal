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
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLTableElement;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlObjectImpl;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlTableIndex extends MySqlObjectImpl implements SQLTableElement {

  private SQLName name;
  private String indexType;
  private List<SQLExpr> columns = new ArrayList<SQLExpr>();

  public MySqlTableIndex() {

  }

  public SQLName getName() {
    return name;
  }

  public void setName(SQLName name) {
    this.name = name;
  }

  public String getIndexType() {
    return indexType;
  }

  public void setIndexType(String indexType) {
    this.indexType = indexType;
  }

  public List<SQLExpr> getColumns() {
    return columns;
  }

  public void addColumn(SQLExpr column) {
    if (column != null) {
      column.setParent(this);
    }
    this.columns.add(column);
  }

  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, name);
      acceptChild(visitor, columns);
    }
    visitor.endVisit(this);
  }
}
