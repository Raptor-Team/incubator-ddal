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

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlStatementImpl;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;

/**
 */
public class MySqlRepeatStatement extends MySqlStatementImpl {

  private String labelName;

  private List<SQLStatement> statements = new ArrayList<SQLStatement>();

  private SQLExpr condition;

  @Override
  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, statements);
      acceptChild(visitor, condition);
    }
    visitor.endVisit(this);
  }

  public List<SQLStatement> getStatements() {
    return statements;
  }

  public void setStatements(List<SQLStatement> statements) {
    this.statements = statements;
  }

  public String getLabelName() {
    return labelName;
  }

  public void setLabelName(String labelName) {
    this.labelName = labelName;
  }

  public SQLExpr getCondition() {
    return condition;
  }

  public void setCondition(SQLExpr condition) {
    this.condition = condition;
  }
}
