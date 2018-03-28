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

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLObjectImpl;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLInsertStatement extends SQLInsertInto implements SQLStatement {

  protected boolean upsert = false; // for phoenix
  private String dbType;

  public SQLInsertStatement() {

  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, tableSource);
      this.acceptChild(visitor, columns);
      this.acceptChild(visitor, valuesList);
      this.acceptChild(visitor, query);
    }

    visitor.endVisit(this);
  }

  public boolean isUpsert() {
    return upsert;
  }

  public void setUpsert(boolean upsert) {
    this.upsert = upsert;
  }

  @Override
  public String getDbType() {
    return dbType;
  }

  public void setDbType(String dbType) {
    this.dbType = dbType;
  }

  public static class ValuesClause extends SQLObjectImpl {

    private final List<SQLExpr> values;

    public ValuesClause() {
      this(new ArrayList<SQLExpr>());
    }

    public ValuesClause(List<SQLExpr> values) {
      this.values = values;
      for (int i = 0; i < values.size(); ++i) {
        values.get(i).setParent(this);
      }
    }

    public void addValue(SQLExpr value) {
      value.setParent(this);
      values.add(value);
    }

    public List<SQLExpr> getValues() {
      return values;
    }

    public void output(StringBuffer buf) {
      buf.append(" VALUES (");
      for (int i = 0, size = values.size(); i < size; ++i) {
        if (i != 0) {
          buf.append(", ");
        }
        values.get(i).output(buf);
      }
      buf.append(")");
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
      if (visitor.visit(this)) {
        this.acceptChild(visitor, values);
      }

      visitor.endVisit(this);
    }
  }
}
