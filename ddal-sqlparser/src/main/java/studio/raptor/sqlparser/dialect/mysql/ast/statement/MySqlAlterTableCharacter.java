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

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableItem;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlObjectImpl;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;

public class MySqlAlterTableCharacter extends MySqlObjectImpl implements SQLAlterTableItem {

  private SQLExpr characterSet;
  private SQLExpr collate;

  @Override
  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, characterSet);
      acceptChild(visitor, collate);
    }
    visitor.endVisit(this);
  }

  public SQLExpr getCharacterSet() {
    return characterSet;
  }

  public void setCharacterSet(SQLExpr characterSet) {
    this.characterSet = characterSet;
  }

  public SQLExpr getCollate() {
    return collate;
  }

  public void setCollate(SQLExpr collate) {
    this.collate = collate;
  }

}
