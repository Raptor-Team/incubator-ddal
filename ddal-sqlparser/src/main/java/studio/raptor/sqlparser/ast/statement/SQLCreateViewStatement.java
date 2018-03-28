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
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObjectImpl;
import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.ast.expr.SQLCharExpr;
import studio.raptor.sqlparser.ast.expr.SQLLiteralExpr;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class SQLCreateViewStatement extends SQLStatementImpl implements SQLDDLStatement {

  protected final List<Column> columns = new ArrayList<Column>();
  protected SQLName name;
  protected SQLSelect subQuery;
  protected boolean ifNotExists = false;

  protected String algorithm;
  protected SQLName definer;
  protected String sqlSecurity;
  private boolean orReplace = false;
  private Level with;

  private SQLLiteralExpr comment;

  public SQLCreateViewStatement() {

  }

  public SQLCreateViewStatement(String dbType) {
    super(dbType);
  }

  public boolean isOrReplace() {
    return orReplace;
  }

  public void setOrReplace(boolean orReplace) {
    this.orReplace = orReplace;
  }

  public SQLName getName() {
    return name;
  }

  public void setName(SQLName name) {
    this.name = name;
  }

  public Level getWith() {
    return with;
  }

  public void setWith(Level with) {
    this.with = with;
  }

  public SQLSelect getSubQuery() {
    return subQuery;
  }

  public void setSubQuery(SQLSelect subQuery) {
    this.subQuery = subQuery;
  }

  public List<Column> getColumns() {
    return columns;
  }

  public void addColumn(Column column) {
    if (column != null) {
      column.setParent(this);
    }
    this.columns.add(column);
  }

  public boolean isIfNotExists() {
    return ifNotExists;
  }

  public void setIfNotExists(boolean ifNotExists) {
    this.ifNotExists = ifNotExists;
  }

  public SQLLiteralExpr getComment() {
    return comment;
  }

  public void setComment(SQLLiteralExpr comment) {
    if (comment != null) {
      comment.setParent(this);
    }
    this.comment = comment;
  }

  public String getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(String algorithm) {
    this.algorithm = algorithm;
  }

  public SQLName getDefiner() {
    return definer;
  }

  public void setDefiner(SQLName definer) {
    if (definer != null) {
      definer.setParent(this);
    }
    this.definer = definer;
  }

  public String getSqlSecurity() {
    return sqlSecurity;
  }

  public void setSqlSecurity(String sqlSecurity) {
    this.sqlSecurity = sqlSecurity;
  }

  public void output(StringBuffer buf) {
    buf.append("CREATE VIEW ");
    this.name.output(buf);

    if (this.columns.size() > 0) {
      buf.append(" (");
      for (int i = 0, size = this.columns.size(); i < size; ++i) {
        if (i != 0) {
          buf.append(", ");
        }
        this.columns.get(i).output(buf);
      }
      buf.append(")");
    }

    buf.append(" AS ");
    this.subQuery.output(buf);

    if (this.with != null) {
      buf.append(" WITH ");
      buf.append(this.with.name());
    }
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.name);
      acceptChild(visitor, this.columns);
      acceptChild(visitor, this.comment);
      acceptChild(visitor, this.subQuery);
    }
    visitor.endVisit(this);
  }

  public static enum Level {
    CASCADED, LOCAL
  }

  public static class Column extends SQLObjectImpl {

    private SQLExpr expr;
    private SQLCharExpr comment;

    public SQLExpr getExpr() {
      return expr;
    }

    public void setExpr(SQLExpr expr) {
      if (expr != null) {
        expr.setParent(this);
      }
      this.expr = expr;
    }

    public SQLCharExpr getComment() {
      return comment;
    }

    public void setComment(SQLCharExpr comment) {
      if (comment != null) {
        comment.setParent(this);
      }
      this.comment = comment;
    }

    @Override
    protected void accept0(SQLASTVisitor visitor) {
      if (visitor.visit(this)) {
        acceptChild(visitor, expr);
        acceptChild(visitor, comment);
      }
    }
  }
}
