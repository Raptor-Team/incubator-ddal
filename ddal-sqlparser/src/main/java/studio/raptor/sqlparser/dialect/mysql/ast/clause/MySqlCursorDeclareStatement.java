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
package studio.raptor.sqlparser.dialect.mysql.ast.clause;

import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlStatementImpl;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;

/**
 */
public class MySqlCursorDeclareStatement extends MySqlStatementImpl {

  //cursor name
  private String cursorName;
  //select statement
  private SQLSelectStatement select;

  public String getCursorName() {
    return cursorName;
  }

  public void setCursorName(String cursorName) {
    this.cursorName = cursorName;
  }

  public SQLSelectStatement getSelect() {
    return select;
  }

  public void setSelect(SQLSelectStatement select) {
    this.select = select;
  }

  @Override
  public void accept0(MySqlASTVisitor visitor) {
    // TODO Auto-generated method stub
    if (visitor.visit(this)) {
      acceptChild(visitor, select);
    }
    visitor.endVisit(this);

  }

}
