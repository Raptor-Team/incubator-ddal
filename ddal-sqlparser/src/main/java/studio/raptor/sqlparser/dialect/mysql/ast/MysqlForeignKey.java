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
package studio.raptor.sqlparser.dialect.mysql.ast;

import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.statement.SQLForeignKeyImpl;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

/**
 */
public class MysqlForeignKey extends SQLForeignKeyImpl {

  protected Option onUpdate;
  protected Option onDelete;
  private SQLName indexName;
  private boolean hasConstraint;
  private Match referenceMatch;

  public SQLName getIndexName() {
    return indexName;
  }

  public void setIndexName(SQLName indexName) {
    this.indexName = indexName;
  }

  public boolean isHasConstraint() {
    return hasConstraint;
  }

  public void setHasConstraint(boolean hasConstraint) {
    this.hasConstraint = hasConstraint;
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof MySqlASTVisitor) {
      accept0((MySqlASTVisitor) visitor);
    }
  }

  protected void accept0(MySqlASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, this.getName());
      acceptChild(visitor, this.getReferencedTableName());
      acceptChild(visitor, this.getReferencingColumns());
      acceptChild(visitor, this.getReferencedColumns());

      acceptChild(visitor, indexName);
    }
    visitor.endVisit(this);
  }

  public Match getReferenceMatch() {
    return referenceMatch;
  }

  public void setReferenceMatch(Match referenceMatch) {
    this.referenceMatch = referenceMatch;
  }

  public Option getOnUpdate() {
    return onUpdate;
  }

  public void setOnUpdate(Option onUpdate) {
    this.onUpdate = onUpdate;
  }

  public Option getOnDelete() {
    return onDelete;
  }

  public void setOnDelete(Option onDelete) {
    this.onDelete = onDelete;
  }

  public static enum Option {

    RESTRICT("RESTRICT"), CASCADE("CASCADE"), SET_NULL("SET NULL"), NO_ACTION("NO ACTION");

    public final String name;
    public final String name_lcase;

    Option(String name) {
      this.name = name;
      this.name_lcase = name.toLowerCase();
    }

    public String getText() {
      return name;
    }

  }

  public static enum Match {
    FULL("FULL"), PARTIAL("PARTIAL"), SIMPLE("SIMPLE");

    public final String name;
    public final String name_lcase;

    Match(String name) {
      this.name = name;
      this.name_lcase = name.toLowerCase();
    }
  }

  public static enum On {
    DELETE("DELETE"), //
    UPDATE("UPDATE");

    public final String name;
    public final String name_lcase;

    On(String name) {
      this.name = name;
      this.name_lcase = name.toLowerCase();
    }
  }
}
