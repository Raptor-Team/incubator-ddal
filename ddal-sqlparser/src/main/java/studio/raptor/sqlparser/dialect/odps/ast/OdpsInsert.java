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
import studio.raptor.sqlparser.ast.SQLObjectImpl;
import studio.raptor.sqlparser.ast.statement.SQLAssignItem;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OdpsInsert extends SQLObjectImpl {

  protected SQLExprTableSource tableSource;
  private boolean overwrite = false;
  private List<SQLAssignItem> partitions = new ArrayList<SQLAssignItem>();
  private SQLSelect query;

  public boolean isOverwrite() {
    return overwrite;
  }

  public void setOverwrite(boolean overwrite) {
    this.overwrite = overwrite;
  }

  public List<SQLAssignItem> getPartitions() {
    return partitions;
  }

  public void setPartitions(List<SQLAssignItem> partitions) {
    this.partitions = partitions;
  }

  public void addPartition(SQLAssignItem partition) {
    if (partition != null) {
      partition.setParent(this);
    }
    this.partitions.add(partition);
  }

  public SQLExprTableSource getTableSource() {
    return tableSource;
  }

  public void setTableSource(SQLName tableName) {
    this.setTableSource(new SQLExprTableSource(tableName));
  }

  public void setTableSource(SQLExprTableSource tableSource) {
    if (tableSource != null) {
      tableSource.setParent(this);
    }
    this.tableSource = tableSource;
  }

  public SQLSelect getQuery() {
    return query;
  }

  public void setQuery(SQLSelect query) {
    if (query != null) {
      query.setParent(this);
    }
    this.query = query;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  protected void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, tableSource);
      acceptChild(visitor, partitions);
      acceptChild(visitor, query);
    }
    visitor.endVisit(this);
  }

}
