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
package studio.raptor.sqlparser.wall.spi;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.SQLUtils;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLInListExpr;
import studio.raptor.sqlparser.ast.expr.SQLMethodInvokeExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLCallStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateTriggerStatement;
import studio.raptor.sqlparser.ast.statement.SQLDeleteStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.ast.statement.SQLSelectGroupByClause;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLSetStatement;
import studio.raptor.sqlparser.ast.statement.SQLUnionQuery;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.dialect.postgresql.ast.stmt.PGSelectQueryBlock;
import studio.raptor.sqlparser.dialect.postgresql.visitor.PGASTVisitorAdapter;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.wall.Violation;
import studio.raptor.sqlparser.wall.WallConfig;
import studio.raptor.sqlparser.wall.WallProvider;
import studio.raptor.sqlparser.wall.WallVisitor;
import studio.raptor.sqlparser.wall.violation.ErrorCode;
import studio.raptor.sqlparser.wall.violation.IllegalSQLObjectViolation;

public class PGWallVisitor extends PGASTVisitorAdapter implements WallVisitor {

  private final WallConfig config;
  private final WallProvider provider;
  private final List<Violation> violations = new ArrayList<Violation>();
  private boolean sqlModified = false;
  private boolean sqlEndOfComment = false;

  public PGWallVisitor(WallProvider provider) {
    this.config = provider.getConfig();
    this.provider = provider;
  }

  @Override
  public String getDbType() {
    return JdbcConstants.POSTGRESQL;
  }

  @Override
  public boolean isSqlModified() {
    return sqlModified;
  }

  @Override
  public void setSqlModified(boolean sqlModified) {
    this.sqlModified = sqlModified;
  }

  @Override
  public WallProvider getProvider() {
    return provider;
  }

  @Override
  public WallConfig getConfig() {
    return config;
  }

  @Override
  public void addViolation(Violation violation) {
    this.violations.add(violation);
  }

  @Override
  public List<Violation> getViolations() {
    return violations;
  }

  public boolean visit(SQLIdentifierExpr x) {
    String name = x.getName();
    name = WallVisitorUtils.form(name);
    if (config.isVariantCheck() && config.getDenyVariants().contains(name)) {
      getViolations()
          .add(new IllegalSQLObjectViolation(ErrorCode.VARIANT_DENY, "variable not allow : " + name,
              toSQL(x)));
    }
    return true;
  }

  public boolean visit(SQLPropertyExpr x) {
    WallVisitorUtils.check(this, x);
    return true;
  }

  public boolean visit(SQLInListExpr x) {
    WallVisitorUtils.check(this, x);
    return true;
  }

  public boolean visit(SQLBinaryOpExpr x) {
    return WallVisitorUtils.check(this, x);
  }

  @Override
  public boolean visit(SQLMethodInvokeExpr x) {
    WallVisitorUtils.checkFunction(this, x);

    return true;
  }

  public boolean visit(SQLExprTableSource x) {
    WallVisitorUtils.check(this, x);

    return !(x.getExpr() instanceof SQLName);
  }

  public boolean visit(SQLSelectGroupByClause x) {
    WallVisitorUtils.checkHaving(this, x.getHaving());
    return true;
  }

  @Override
  public boolean visit(SQLSelectQueryBlock x) {
    WallVisitorUtils.checkSelelct(this, x);

    return true;
  }

  @Override
  public boolean visit(PGSelectQueryBlock x) {
    WallVisitorUtils.checkSelelct(this, x);

    return true;
  }

  @Override
  public boolean visit(SQLUnionQuery x) {
    WallVisitorUtils.checkUnion(this, x);

    return true;
  }

  @Override
  public String toSQL(SQLObject obj) {
    return SQLUtils.toPGString(obj);
  }

  @Override
  public boolean isDenyTable(String name) {
    if (!config.isTableCheck()) {
      return false;
    }

    name = WallVisitorUtils.form(name);
    if (name.startsWith("v$") || name.startsWith("v_$")) {
      return true;
    }
    return !this.provider.checkDenyTable(name);
  }

  public void preVisit(SQLObject x) {
    WallVisitorUtils.preVisitCheck(this, x);
  }

  @Override
  public boolean visit(SQLSelectStatement x) {
    if (!config.isSelelctAllow()) {
      this.getViolations()
          .add(new IllegalSQLObjectViolation(ErrorCode.SELECT_NOT_ALLOW, "selelct not allow",
              this.toSQL(x)));
      return false;
    }

    WallVisitorUtils.initWallTopStatementContext();

    return true;
  }

  @Override
  public void endVisit(SQLSelectStatement x) {
    WallVisitorUtils.clearWallTopStatementContext();
  }

  @Override
  public boolean visit(SQLInsertStatement x) {
    WallVisitorUtils.initWallTopStatementContext();
    WallVisitorUtils.checkInsert(this, x);

    return true;
  }

  @Override
  public void endVisit(SQLInsertStatement x) {
    WallVisitorUtils.clearWallTopStatementContext();
  }

  @Override
  public boolean visit(SQLDeleteStatement x) {
    WallVisitorUtils.checkDelete(this, x);
    return true;
  }

  @Override
  public void endVisit(SQLDeleteStatement x) {
    WallVisitorUtils.clearWallTopStatementContext();
  }

  @Override
  public boolean visit(SQLUpdateStatement x) {
    WallVisitorUtils.initWallTopStatementContext();
    WallVisitorUtils.checkUpdate(this, x);

    return true;
  }

  @Override
  public void endVisit(SQLUpdateStatement x) {
    WallVisitorUtils.clearWallTopStatementContext();
  }

  @Override
  public boolean visit(SQLSelectItem x) {
    WallVisitorUtils.check(this, x);
    return true;
  }

  @Override
  public boolean visit(SQLCreateTableStatement x) {
    WallVisitorUtils.check(this, x);
    return true;
  }

  @Override
  public boolean visit(SQLAlterTableStatement x) {
    WallVisitorUtils.check(this, x);
    return true;
  }

  @Override
  public boolean visit(SQLDropTableStatement x) {
    WallVisitorUtils.check(this, x);
    return true;
  }

  @Override
  public boolean visit(SQLSetStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLCallStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLCreateTriggerStatement x) {
    return false;
  }

  @Override
  public boolean isSqlEndOfComment() {
    return this.sqlEndOfComment;
  }

  @Override
  public void setSqlEndOfComment(boolean sqlEndOfComment) {
    this.sqlEndOfComment = sqlEndOfComment;
  }
}
