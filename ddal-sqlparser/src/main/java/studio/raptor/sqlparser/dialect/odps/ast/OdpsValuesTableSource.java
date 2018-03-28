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
import studio.raptor.sqlparser.ast.expr.SQLListExpr;
import studio.raptor.sqlparser.ast.statement.SQLTableSourceImpl;
import studio.raptor.sqlparser.dialect.odps.visitor.OdpsASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

/**
 * Created by wenshao on 23/02/2017.
 */
public class OdpsValuesTableSource extends SQLTableSourceImpl implements OdpsObject {

  private List<SQLListExpr> values = new ArrayList<SQLListExpr>();
  private List<SQLName> columns = new ArrayList<SQLName>();

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    accept0((OdpsASTVisitor) visitor);
  }

  public List<SQLListExpr> getValues() {
    return values;
  }

  public List<SQLName> getColumns() {
    return columns;
  }

  @Override
  public void accept0(OdpsASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, values);
      acceptChild(visitor, columns);
    }
    visitor.endVisit(this);
  }
}
