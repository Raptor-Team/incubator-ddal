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
package studio.raptor.sqlparser.dialect.sqlserver.ast;

import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.dialect.sqlserver.visitor.SQLServerASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLServerSelectQueryBlock extends SQLSelectQueryBlock {

  private SQLServerTop top;

  public SQLServerTop getTop() {
    return top;
  }

  public void setTop(SQLServerTop top) {
    if (top != null) {
      top.setParent(this);
    }
    this.top = top;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((SQLServerASTVisitor) visitor);
  }

  protected void accept0(SQLServerASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.top);
      acceptChild(visitor, this.selectList);
      acceptChild(visitor, this.from);
      acceptChild(visitor, this.where);
      acceptChild(visitor, this.groupBy);
    }
    visitor.endVisit(this);
  }
}
