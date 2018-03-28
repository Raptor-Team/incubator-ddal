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
package studio.raptor.sqlparser.ast.statement;

import java.util.List;
import studio.raptor.sqlparser.ast.SQLCommentHint;
import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLSelectStatement extends SQLStatementImpl {

  protected SQLSelect select;

  private List<SQLCommentHint> headHints;

  public SQLSelectStatement() {

  }

  public SQLSelectStatement(String dbType) {
    super(dbType);
  }

  public SQLSelectStatement(SQLSelect select) {
    this.setSelect(select);
  }

  public SQLSelectStatement(SQLSelect select, String dbType) {
    this(dbType);
    this.setSelect(select);
  }

  public SQLSelect getSelect() {
    return this.select;
  }

  public void setSelect(SQLSelect select) {
    if (select != null) {
      select.setParent(this);
    }
    this.select = select;
  }

  public void output(StringBuffer buf) {
    this.select.output(buf);
  }

  protected void accept0(SQLASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.select);
    }
    visitor.endVisit(this);
  }

  public List<SQLCommentHint> getHeadHintsDirect() {
    return headHints;
  }

  public void setHeadHints(List<SQLCommentHint> headHints) {
    this.headHints = headHints;
  }
}
