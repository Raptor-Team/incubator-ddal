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

import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class OdpsShowStatisticStmt extends SQLStatementImpl implements SQLStatement {

  private SQLExprTableSource tableSource;

  public OdpsShowStatisticStmt() {
    super(JdbcConstants.ODPS);
  }

  public SQLExprTableSource getTableSource() {
    return tableSource;
  }

  public void setTableSource(SQLExprTableSource tableSource) {
    this.tableSource = tableSource;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  protected void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, tableSource);
    }
    visitor.endVisit(this);
  }
}
