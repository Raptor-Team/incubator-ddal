/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.ddal.core.parser.visitor;


import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.core.parser.result.Operate;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLMethodInvokeExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLCheck;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLCreateIndexStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLDeleteStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLForeignKeyImpl;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.ast.statement.SQLMergeStatement.MergeInsertClause;
import studio.raptor.sqlparser.ast.statement.SQLMergeStatement.MergeUpdateClause;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;
import studio.raptor.sqlparser.ast.statement.SQLUnique;
import studio.raptor.sqlparser.ast.statement.SQLUpdateSetItem;
import studio.raptor.sqlparser.dialect.oracle.ast.OracleDataTypeIntervalDay;
import studio.raptor.sqlparser.dialect.oracle.ast.OracleDataTypeIntervalYear;
import studio.raptor.sqlparser.dialect.oracle.ast.OracleDataTypeTimestamp;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.CycleClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfFlashbackQueryClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.FlashbackQueryClause.AsOfSnapshotClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.FlashbackQueryClause.VersionsFlashbackQueryClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.CellAssignment;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.CellAssignmentItem;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.MainModelClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.ModelColumn;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.ModelColumnClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.ModelRulesClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.QueryPartitionClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.ModelClause.ReturnRowsClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.OracleLobStorageClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.OracleReturningClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.OracleStorageClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.OracleWithSubqueryEntry;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.PartitionExtensionClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.SampleClause;
import studio.raptor.sqlparser.dialect.oracle.ast.clause.SearchClause;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleAnalytic;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleAnalyticWindowing;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleArgumentExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleBinaryDoubleExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleBinaryFloatExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleCursorExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleDatetimeExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleDbLinkExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleIntervalExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleIsSetExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleOuterExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleRangeExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleSizeExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleSysdateExpr;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterIndexStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterProcedureStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterSessionStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterSynonymStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTableDropPartition;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTableModify;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTableMoveTablespace;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTableSplitPartition;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTableTruncatePartition;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTablespaceAddDataFile;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTablespaceStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterTriggerStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleAlterViewStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleCheck;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleCommitStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleCreateDatabaseDbLinkStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleCreateIndexStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleCreateTableStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleDeleteStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleDropDbLinkStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleExceptionStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleExitStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleExplainStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleExprStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleFileSpecification;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleForStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleForeignKey;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleGotoStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleInsertStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleLabelStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleLockTableStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleMultiInsertStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClause;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleMultiInsertStatement.ConditionalInsertClauseItem;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleMultiInsertStatement.InsertIntoClause;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OraclePLSQLCommitStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OraclePrimaryKey;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSavePointStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelect;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectForUpdate;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectHierachicalQueryClause;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectJoin;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectPivot;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectPivot.Item;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectQueryBlock;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectRestriction.CheckOption;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectRestriction.ReadOnly;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectSubqueryTableSource;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectTableReference;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectUnPivot;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSetTransactionStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleUnique;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleUpdateStatement;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleUsingIndexClause;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;
import studio.raptor.sqlparser.stat.TableStat.Column;
import studio.raptor.sqlparser.stat.TableStat.Mode;
import studio.raptor.sqlparser.util.JdbcUtils;

public class OracleStatementVisitor extends StatementVisitor implements OracleASTVisitor {

  public OracleStatementVisitor(ParseResult parseResult) {
    super(parseResult);
    parseResult.getVariants().put("DUAL", null);
    parseResult.getVariants().put("NOTFOUND", null);
    parseResult.getVariants().put("TRUE", null);
    parseResult.getVariants().put("FALSE", null);
  }

  @Override
  public String getDbType() {
    return JdbcUtils.ORACLE;
  }

  protected Column getColumn(SQLExpr expr) {
    if (expr instanceof OracleOuterExpr) {
      expr = ((OracleOuterExpr) expr).getExpr();
    }

    return super.getColumn(expr);
  }

  public boolean visit(OracleSelectTableReference x) {
    SQLExpr expr = x.getExpr();
    if (expr instanceof SQLMethodInvokeExpr) {
      SQLMethodInvokeExpr methodInvoke = (SQLMethodInvokeExpr) expr;
      if ("TABLE".equalsIgnoreCase(methodInvoke.getMethodName())
          && methodInvoke.getParameters().size() == 1) {
        expr = methodInvoke.getParameters().get(0);
      }
    }
    Map<String, String> aliasMap = parseResult.getAliasMap();
    if (expr instanceof SQLName) {
      String ident;
      if (expr instanceof SQLPropertyExpr) {
        String owner = ((SQLPropertyExpr) expr).getOwner().toString();
        String name = ((SQLPropertyExpr) expr).getName();

        if (aliasMap.containsKey(owner)) {
          owner = aliasMap.get(owner);
        }
        ident = owner + "." + name;
      } else {
        ident = expr.toString();
      }
      if (containsSubQuery(ident)) {
        return false;
      }
      if ("DUAL".equalsIgnoreCase(ident)) {
        //TODO 处理Dual
        parseResult.setTableNames(Sets.newHashSet(ident.toUpperCase()));
        return false;
      }
      x.putAttribute(ATTR_TABLE, ident);
      addTable(ident, x);
      putAliasMap(aliasMap, x.getAlias(), ident);
      putAliasMap(aliasMap, ident, ident);
      return false;
    }
    accept(x.getExpr());
    return false;
  }

  public void endVisit(OracleSelect x) {
    endVisit((SQLSelect) x);
  }

  public boolean visit(OracleSelect x) {
    return visit((SQLSelect) x);
  }

  public void endVisit(SQLSelect x) {
    if (x.getQuery() != null) {
      String table = (String) x.getQuery().getAttribute(ATTR_TABLE);
      if (table != null) {
        x.putAttribute(ATTR_TABLE, table);
      }
    }
    restoreCurrentTable(x);
  }

  public boolean visit(OracleUpdateStatement x) {
    parseResult.setOperate(Operate.DML);
    parseResult.setAliasMap();
    setMode(x, Mode.Update);
    SQLTableSource tableSource = x.getTableSource();
    SQLExpr tableExpr = null;
    if (tableSource instanceof SQLExprTableSource) {
      tableExpr = ((SQLExprTableSource) tableSource).getExpr();
    }
    if (tableExpr instanceof SQLName) {
      String ident = tableExpr.toString();
      setCurrentTable(ident);
      addTable(ident, (SQLExprTableSource) tableSource);
      Map<String, String> aliasMap = parseResult.getAliasMap();
      aliasMap.put(ident, ident);
      aliasMap.put(tableSource.getAlias(), ident);
    } else {
      tableSource.accept(this);
    }
    accept(x.getItems());
    accept(x.getWhere());

    List<SQLUpdateSetItem> updateSetItem = x.getItems();
    if (updateSetItem != null && updateSetItem.size() > 0) {
      int length = updateSetItem.size();
      for (int i = 0; i < length; i++) {
        SQLUpdateSetItem item = updateSetItem.get(i);
        String column = handleName(item.getColumn().toString().toUpperCase());
        parseResult.getUpdateItems().add(column);
      }
    }
    return false;
  }

  public void endVisit(OracleUpdateStatement x) {
  }

  public boolean visit(OracleDeleteStatement x) {
    parseResult.setOperate(Operate.DML);
    return visit((SQLDeleteStatement) x);
  }

  public void endVisit(OracleDeleteStatement x) {
  }

  public boolean visit(OracleSelectQueryBlock x) {
    if (x.getWhere() != null) {
      x.getWhere().setParent(x);
    }

    if (x.getInto() instanceof SQLName) {
      String tableName = x.getInto().toString();
      addTable(tableName);
    }

    visit((SQLSelectQueryBlock) x);

    return true;
  }

  public void endVisit(OracleSelectQueryBlock x) {
    endVisit((SQLSelectQueryBlock) x);
  }

  public boolean visit(SQLPropertyExpr x) {
    if ("ROWNUM".equalsIgnoreCase(x.getName())) {
      return false;
    }

    return super.visit(x);
  }

  public boolean visit(SQLIdentifierExpr x) {
    if ("ROWNUM".equalsIgnoreCase(x.getName())) {
      return false;
    }

    if ("SYSDATE".equalsIgnoreCase(x.getName())) {
      return false;
    }

    if ("+".equalsIgnoreCase(x.getName())) {
      return false;
    }

    if ("LEVEL".equals(x.getName())) {
      return false;
    }

    return super.visit(x);
  }

  @Override
  public void endVisit(OraclePLSQLCommitStatement astNode) {

  }

  @Override
  public void endVisit(OracleAnalytic x) {

  }

  @Override
  public void endVisit(OracleAnalyticWindowing x) {

  }

  @Override
  public void endVisit(OracleDbLinkExpr x) {

  }

  @Override
  public void endVisit(OracleIntervalExpr x) {

  }

  @Override
  public void endVisit(OracleOuterExpr x) {

  }

  @Override
  public void endVisit(OracleSelectForUpdate x) {

  }

  @Override
  public void endVisit(OracleSelectHierachicalQueryClause x) {

  }

  @Override
  public void endVisit(OracleSelectJoin x) {

  }

  @Override
  public void endVisit(OracleSelectPivot x) {

  }

  @Override
  public void endVisit(Item x) {

  }

  @Override
  public void endVisit(CheckOption x) {

  }

  @Override
  public void endVisit(ReadOnly x) {

  }

  @Override
  public void endVisit(OracleSelectSubqueryTableSource x) {

  }

  @Override
  public void endVisit(OracleSelectUnPivot x) {

  }

  @Override
  public boolean visit(OraclePLSQLCommitStatement astNode) {
    parseResult.setOperate(Operate.DCL);
    return true;
  }

  @Override
  public boolean visit(OracleAnalytic x) {

    return true;
  }

  @Override
  public boolean visit(OracleAnalyticWindowing x) {

    return true;
  }

  @Override
  public boolean visit(OracleDbLinkExpr x) {

    return true;
  }

  @Override
  public boolean visit(OracleIntervalExpr x) {

    return true;
  }

  @Override
  public boolean visit(OracleOuterExpr x) {

    return true;
  }

  @Override
  public boolean visit(OracleSelectForUpdate x) {

    return true;
  }

  @Override
  public boolean visit(OracleSelectHierachicalQueryClause x) {
    return true;
  }

  @Override
  public boolean visit(OracleSelectJoin x) {
    return true;
  }

  @Override
  public boolean visit(OracleSelectPivot x) {

    return true;
  }

  @Override
  public boolean visit(Item x) {

    return true;
  }

  @Override
  public boolean visit(CheckOption x) {

    return true;
  }

  @Override
  public boolean visit(ReadOnly x) {
    parseResult.setOperate(Operate.DDL);
    return true;
  }

  @Override
  public boolean visit(OracleSelectSubqueryTableSource x) {
    accept(x.getSelect());
    accept(x.getPivot());
    accept(x.getFlashback());

    String table = (String) x.getSelect().getAttribute(ATTR_TABLE);
    if (x.getAlias() != null) {
      if (table != null) {
        parseResult.getAliasMap().put(x.getAlias(), table);
      }
      addSubQuery(x.getAlias(), x.getSelect());
      this.setCurrentTable(x.getAlias());
    }

    if (table != null) {
      x.putAttribute(ATTR_TABLE, table);
    }
    return false;
  }

  @Override
  public boolean visit(OracleSelectUnPivot x) {

    return true;
  }

  @Override
  public boolean visit(SampleClause x) {
    return true;
  }

  @Override
  public void endVisit(SampleClause x) {

  }

  @Override
  public void endVisit(OracleSelectTableReference x) {

  }

  @Override
  public boolean visit(PartitionExtensionClause x) {

    return true;
  }

  @Override
  public void endVisit(PartitionExtensionClause x) {

  }

  @Override
  public boolean visit(VersionsFlashbackQueryClause x) {

    return true;
  }

  @Override
  public void endVisit(VersionsFlashbackQueryClause x) {

  }

  @Override
  public boolean visit(AsOfFlashbackQueryClause x) {

    return true;
  }

  @Override
  public void endVisit(AsOfFlashbackQueryClause x) {

  }

  @Override
  public boolean visit(OracleWithSubqueryEntry x) {
    Map<String, String> aliasMap = parseResult.getAliasMap();
    if (aliasMap != null) {
      String alias = null;
      if (x.getName() != null) {
        alias = x.getName().toString();
      }

      if (alias != null) {
        putAliasMap(aliasMap, alias, null);
        addSubQuery(alias, x.getSubQuery());
      }
    }
    x.getSubQuery().accept(this);
    return false;
  }

  @Override
  public void endVisit(OracleWithSubqueryEntry x) {

  }

  @Override
  public boolean visit(SearchClause x) {

    return true;
  }

  @Override
  public void endVisit(SearchClause x) {

  }

  @Override
  public boolean visit(CycleClause x) {

    return true;
  }

  @Override
  public void endVisit(CycleClause x) {

  }

  @Override
  public boolean visit(OracleBinaryFloatExpr x) {

    return true;
  }

  @Override
  public void endVisit(OracleBinaryFloatExpr x) {

  }

  @Override
  public boolean visit(OracleBinaryDoubleExpr x) {

    return true;
  }

  @Override
  public void endVisit(OracleBinaryDoubleExpr x) {

  }

  @Override
  public boolean visit(OracleCursorExpr x) {

    return true;
  }

  @Override
  public void endVisit(OracleCursorExpr x) {

  }

  @Override
  public boolean visit(OracleIsSetExpr x) {

    return true;
  }

  @Override
  public void endVisit(OracleIsSetExpr x) {

  }

  @Override
  public boolean visit(ReturnRowsClause x) {

    return true;
  }

  @Override
  public void endVisit(ReturnRowsClause x) {

  }

  @Override
  public boolean visit(MainModelClause x) {

    return true;
  }

  @Override
  public void endVisit(MainModelClause x) {

  }

  @Override
  public boolean visit(ModelColumnClause x) {

    return true;
  }

  @Override
  public void endVisit(ModelColumnClause x) {

  }

  @Override
  public boolean visit(QueryPartitionClause x) {

    return true;
  }

  @Override
  public void endVisit(QueryPartitionClause x) {

  }

  @Override
  public boolean visit(ModelColumn x) {

    return true;
  }

  @Override
  public void endVisit(ModelColumn x) {

  }

  @Override
  public boolean visit(ModelRulesClause x) {

    return true;
  }

  @Override
  public void endVisit(ModelRulesClause x) {

  }

  @Override
  public boolean visit(CellAssignmentItem x) {

    return true;
  }

  @Override
  public void endVisit(CellAssignmentItem x) {

  }

  @Override
  public boolean visit(CellAssignment x) {

    return true;
  }

  @Override
  public void endVisit(CellAssignment x) {

  }

  @Override
  public boolean visit(ModelClause x) {
    return true;
  }

  @Override
  public void endVisit(ModelClause x) {

  }

  @Override
  public boolean visit(MergeUpdateClause x) {
    return true;
  }

  @Override
  public void endVisit(MergeUpdateClause x) {

  }

  @Override
  public boolean visit(MergeInsertClause x) {
    return true;
  }

  @Override
  public void endVisit(MergeInsertClause x) {

  }

  @Override
  public boolean visit(OracleReturningClause x) {
    return true;
  }

  @Override
  public void endVisit(OracleReturningClause x) {

  }

  @Override
  public boolean visit(OracleInsertStatement x) {
    return visit((SQLInsertStatement) x);
  }

  @Override
  public void endVisit(OracleInsertStatement x) {
    endVisit((SQLInsertStatement) x);
  }

  @Override
  public boolean visit(InsertIntoClause x) {

    if (x.getTableName() instanceof SQLName) {
      String ident = ((SQLName) x.getTableName()).toString();
      setCurrentTable(x, ident);

      addTable(ident);

      Map<String, String> aliasMap = parseResult.getAliasMap();
      if (aliasMap != null) {
        if (x.getAlias() != null) {
          putAliasMap(aliasMap, x.getAlias(), ident);
        }
        putAliasMap(aliasMap, ident, ident);
      }
    }

    accept(x.getColumns());
    accept(x.getQuery());
    accept(x.getReturning());
    accept(x.getErrorLogging());

    return false;
  }

  @Override
  public void endVisit(InsertIntoClause x) {

  }

  @Override
  public boolean visit(OracleMultiInsertStatement x) {
    x.putAttribute("_original_use_mode", getMode());
    setMode(x, Mode.Insert);

    parseResult.setAliasMap();

    accept(x.getSubQuery());

    for (OracleMultiInsertStatement.Entry entry : x.getEntries()) {
      entry.setParent(x);
    }

    accept(x.getEntries());

    return false;
  }

  @Override
  public void endVisit(OracleMultiInsertStatement x) {

  }

  @Override
  public boolean visit(ConditionalInsertClause x) {
    for (ConditionalInsertClauseItem item : x.getItems()) {
      item.setParent(x);
    }
    if (x.getElseItem() != null) {
      x.getElseItem().setParent(x);
    }
    return true;
  }

  @Override
  public void endVisit(ConditionalInsertClause x) {

  }

  @Override
  public boolean visit(ConditionalInsertClauseItem x) {
    SQLObject parent = x.getParent();
    if (parent instanceof ConditionalInsertClause) {
      parent = parent.getParent();
    }
    if (parent instanceof OracleMultiInsertStatement) {
      SQLSelect subQuery = ((OracleMultiInsertStatement) parent).getSubQuery();
      if (subQuery != null) {
        String table = (String) subQuery.getAttribute("_table_");
        setCurrentTable(x, table);
      }
    }
    x.getWhen().accept(this);
    x.getThen().accept(this);
    restoreCurrentTable(x);
    return false;
  }

  @Override
  public void endVisit(ConditionalInsertClauseItem x) {

  }

  @Override
  public boolean visit(OracleAlterSessionStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterSessionStatement x) {

  }

  @Override
  public boolean visit(OracleExprStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleExprStatement x) {

  }

  @Override
  public boolean visit(OracleLockTableStatement x) {
    String tableName = x.getTable().toString();
    addTable(tableName);
    return false;
  }

  @Override
  public void endVisit(OracleLockTableStatement x) {

  }

  @Override
  public boolean visit(OracleDatetimeExpr x) {
    return true;
  }

  @Override
  public void endVisit(OracleDatetimeExpr x) {

  }

  @Override
  public boolean visit(OracleSysdateExpr x) {
    return false;
  }

  @Override
  public void endVisit(OracleSysdateExpr x) {

  }

  @Override
  public void endVisit(OracleExceptionStatement.Item x) {

  }

  @Override
  public boolean visit(OracleExceptionStatement.Item x) {
    return true;
  }

  @Override
  public boolean visit(OracleExceptionStatement x) {
    return true;
  }

  @Override
  public void endVisit(OracleExceptionStatement x) {

  }

  @Override
  public boolean visit(OracleArgumentExpr x) {
    return true;
  }

  @Override
  public void endVisit(OracleArgumentExpr x) {

  }

  @Override
  public boolean visit(OracleSetTransactionStatement x) {
    parseResult.setOperate(Operate.DCL);
    return false;
  }

  @Override
  public void endVisit(OracleSetTransactionStatement x) {

  }

  @Override
  public boolean visit(OracleExplainStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleExplainStatement x) {

  }

  @Override
  public boolean visit(OracleAlterProcedureStatement x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(OracleAlterProcedureStatement x) {

  }

  @Override
  public boolean visit(OracleAlterTableDropPartition x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableDropPartition x) {

  }

  @Override
  public boolean visit(OracleAlterTableTruncatePartition x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableTruncatePartition x) {

  }

  @Override
  public void endVisit(SQLAlterTableStatement x) {
    parseResult.setOperate(Operate.DDL);
    restoreCurrentTable(x);
  }

  @Override
  public boolean visit(OracleAlterTableSplitPartition.TableSpaceItem x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableSplitPartition.TableSpaceItem x) {

  }

  @Override
  public boolean visit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableSplitPartition.UpdateIndexesClause x) {

  }

  @Override
  public boolean visit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableSplitPartition.NestedTablePartitionSpec x) {

  }

  @Override
  public boolean visit(OracleAlterTableSplitPartition x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableSplitPartition x) {

  }

  @Override
  public boolean visit(OracleAlterTableModify x) {
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String table = stmt.getName().toString();

    for (SQLColumnDefinition column : x.getColumns()) {
      String columnName = column.getName().toString();
      addColumn(table, columnName);

    }

    return false;
  }

  @Override
  public void endVisit(OracleAlterTableModify x) {

  }

  @Override
  public boolean visit(OracleCreateIndexStatement x) {
    parseResult.setOperate(Operate.DDL);
    return visit((SQLCreateIndexStatement) x);
  }

  @Override
  public void endVisit(OracleCreateIndexStatement x) {
    restoreCurrentTable(x);
  }

  @Override
  public boolean visit(OracleAlterIndexStatement x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(OracleAlterIndexStatement x) {

  }

  @Override
  public boolean visit(OracleForStatement x) {
    x.getRange().setParent(x);

    SQLName index = x.getIndex();
    parseResult.getVariants().put(index.toString(), x);

    x.getRange().accept(this);
    accept(x.getStatements());

    return false;
  }

  @Override
  public void endVisit(OracleForStatement x) {

  }

  @Override
  public boolean visit(OracleAlterIndexStatement.Rebuild x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(OracleAlterIndexStatement.Rebuild x) {

  }

  @Override
  public boolean visit(OracleRangeExpr x) {
    return true;
  }

  @Override
  public void endVisit(OracleRangeExpr x) {

  }

  @Override
  public boolean visit(OraclePrimaryKey x) {
    accept(x.getColumns());

    return false;
  }

  @Override
  public void endVisit(OraclePrimaryKey x) {

  }

  @Override
  public boolean visit(OracleCreateTableStatement x) {
    parseResult.setOperate(Operate.DDL);
    this.visit((SQLCreateTableStatement) x);

    if (x.getSelect() != null) {
      x.getSelect().accept(this);
    }

    return false;
  }

  @Override
  public void endVisit(OracleCreateTableStatement x) {
    this.endVisit((SQLCreateTableStatement) x);
  }

  @Override
  public boolean visit(OracleStorageClause x) {
    return false;
  }

  @Override
  public void endVisit(OracleStorageClause x) {

  }

  @Override
  public boolean visit(OracleGotoStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleGotoStatement x) {

  }

  @Override
  public boolean visit(OracleLabelStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleLabelStatement x) {

  }

  @Override
  public boolean visit(OracleCommitStatement x) {
    parseResult.setOperate(Operate.DCL);
    return true;
  }

  @Override
  public void endVisit(OracleCommitStatement x) {

  }

  @Override
  public boolean visit(OracleAlterTriggerStatement x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(OracleAlterTriggerStatement x) {

  }

  @Override
  public boolean visit(OracleAlterSynonymStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterSynonymStatement x) {

  }

  @Override
  public boolean visit(AsOfSnapshotClause x) {
    return false;
  }

  @Override
  public void endVisit(AsOfSnapshotClause x) {

  }

  @Override
  public boolean visit(OracleAlterViewStatement x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(OracleAlterViewStatement x) {

  }

  @Override
  public boolean visit(OracleAlterTableMoveTablespace x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTableMoveTablespace x) {

  }

  @Override
  public boolean visit(OracleSizeExpr x) {
    return false;
  }

  @Override
  public void endVisit(OracleSizeExpr x) {

  }

  @Override
  public boolean visit(OracleFileSpecification x) {
    return false;
  }

  @Override
  public void endVisit(OracleFileSpecification x) {

  }

  @Override
  public boolean visit(OracleAlterTablespaceAddDataFile x) {
    return false;
  }

  @Override
  public void endVisit(OracleAlterTablespaceAddDataFile x) {

  }

  @Override
  public boolean visit(OracleAlterTablespaceStatement x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(OracleAlterTablespaceStatement x) {

  }

  @Override
  public boolean visit(OracleExitStatement x) {
    return true;
  }

  @Override
  public void endVisit(OracleExitStatement x) {

  }

  @Override
  public boolean visit(OracleSavePointStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleSavePointStatement x) {

  }

  @Override
  public boolean visit(OracleCreateDatabaseDbLinkStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleCreateDatabaseDbLinkStatement x) {

  }

  @Override
  public boolean visit(OracleDropDbLinkStatement x) {
    return false;
  }

  @Override
  public void endVisit(OracleDropDbLinkStatement x) {

  }

  @Override
  public boolean visit(OracleDataTypeTimestamp x) {
    return false;
  }

  @Override
  public void endVisit(OracleDataTypeTimestamp x) {

  }

  @Override
  public boolean visit(OracleDataTypeIntervalYear x) {
    return false;
  }

  @Override
  public void endVisit(OracleDataTypeIntervalYear x) {

  }

  @Override
  public boolean visit(OracleDataTypeIntervalDay x) {
    return false;
  }

  @Override
  public void endVisit(OracleDataTypeIntervalDay x) {

  }

  @Override
  public boolean visit(OracleUsingIndexClause x) {
    return false;
  }

  @Override
  public void endVisit(OracleUsingIndexClause x) {

  }

  @Override
  public boolean visit(OracleLobStorageClause x) {
    return false;
  }

  @Override
  public void endVisit(OracleLobStorageClause x) {

  }

  @Override
  public boolean visit(OracleUnique x) {
    return visit((SQLUnique) x);
  }

  @Override
  public void endVisit(OracleUnique x) {

  }

  @Override
  public boolean visit(OracleForeignKey x) {
    return visit((SQLForeignKeyImpl) x);
  }

  @Override
  public void endVisit(OracleForeignKey x) {

  }

  @Override
  public boolean visit(OracleCheck x) {
    return visit((SQLCheck) x);
  }

  @Override
  public void endVisit(OracleCheck x) {

  }
}
