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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import studio.raptor.sqlparser.ast.SQLCommentHint;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.SQLPartitionBy;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlObjectImpl;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

public class MySqlCreateTableStatement extends SQLCreateTableStatement implements MySqlStatement {

  private Map<String, SQLObject> tableOptions = new LinkedHashMap<String, SQLObject>();

  private SQLPartitionBy partitioning;

  private List<SQLCommentHint> hints = new ArrayList<SQLCommentHint>();

  private List<SQLCommentHint> optionHints = new ArrayList<SQLCommentHint>();

  private SQLExprTableSource like;

  private SQLName tableGroup;

  public MySqlCreateTableStatement() {
    super(JdbcConstants.MYSQL);
  }

  public SQLExprTableSource getLike() {
    return like;
  }

  public void setLike(SQLExprTableSource like) {
    if (like != null) {
      like.setParent(this);
    }
    this.like = like;
  }

  public void setLike(SQLName like) {
    this.setLike(new SQLExprTableSource(like));
  }

  public List<SQLCommentHint> getHints() {
    return hints;
  }

  public void setHints(List<SQLCommentHint> hints) {
    this.hints = hints;
  }

  public SQLPartitionBy getPartitioning() {
    return partitioning;
  }

  public void setPartitioning(SQLPartitionBy partitioning) {
    this.partitioning = partitioning;
  }

  public Map<String, SQLObject> getTableOptions() {
    return tableOptions;
  }

  public void setTableOptions(Map<String, SQLObject> tableOptions) {
    this.tableOptions = tableOptions;
  }

  @Deprecated
  public SQLSelect getQuery() {
    return select;
  }

  @Deprecated
  public void setQuery(SQLSelect query) {
    this.select = query;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof MySqlASTVisitor) {
      accept0((MySqlASTVisitor) visitor);
    } else {
      throw new IllegalArgumentException(
          "not support visitor type : " + visitor.getClass().getName());
    }
  }

  public void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      this.acceptChild(visitor, getHints());
      this.acceptChild(visitor, getTableSource());
      this.acceptChild(visitor, getTableElementList());
      this.acceptChild(visitor, getLike());
      this.acceptChild(visitor, getSelect());
    }
    visitor.endVisit(this);
  }

  public List<SQLCommentHint> getOptionHints() {
    return optionHints;
  }

  public void setOptionHints(List<SQLCommentHint> optionHints) {
    this.optionHints = optionHints;
  }

  public SQLName getTableGroup() {
    return tableGroup;
  }

  public void setTableGroup(SQLName tableGroup) {
    this.tableGroup = tableGroup;
  }

  public static class TableSpaceOption extends MySqlObjectImpl {

    private SQLName name;
    private SQLExpr storage;

    public SQLName getName() {
      return name;
    }

    public void setName(SQLName name) {
      this.name = name;
    }

    public SQLExpr getStorage() {
      return storage;
    }

    public void setStorage(SQLExpr storage) {
      this.storage = storage;
    }

    @Override
    public void accept0(MySqlASTVisitor visitor) {
      if (visitor.visit(this)) {
        acceptChild(visitor, getName());
        acceptChild(visitor, getStorage());
      }
      visitor.endVisit(this);
    }

  }
}
