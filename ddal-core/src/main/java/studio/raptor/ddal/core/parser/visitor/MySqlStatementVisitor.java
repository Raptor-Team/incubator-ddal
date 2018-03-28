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


import java.util.Map;
import studio.raptor.ddal.core.parser.result.Operate;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.sqlparser.ast.SQLDeclareItem;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLStartTransactionStatement;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;
import studio.raptor.sqlparser.ast.statement.SQLUnionQuery;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlForceIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlIgnoreIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlKey;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlPrimaryKey;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlUnique;
import studio.raptor.sqlparser.dialect.mysql.ast.MySqlUseIndexHint;
import studio.raptor.sqlparser.dialect.mysql.ast.MysqlForeignKey;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlCaseStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlCaseStatement.MySqlWhenStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlCursorDeclareStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlDeclareConditionStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlDeclareHandlerStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlDeclareStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlIterateStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlLeaveStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlRepeatStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlSelectIntoStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.clause.MySqlWhileStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlCharExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlExtractExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlIntervalExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlMatchAgainstExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlOrderingExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlOutFileExpr;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlUserName;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.CobarShowStatus;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableAlterColumn;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableChangeColumn;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableCharacter;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableDiscardTablespace;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableImportTablespace;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableModifyColumn;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterTableOption;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAlterUserStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlAnalyzeStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlBinlogStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlCommitStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlCreateTableStatement.TableSpaceOption;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlCreateUserStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlCreateUserStatement.UserSpecification;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlDeleteStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlExecuteStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlExplainStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlHelpStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlHintStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlInsertStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlKillStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlLoadDataInFileStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlLoadXmlStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlLockTableStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlOptimizeStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlPartitionByKey;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlPrepareStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlRenameTableStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlReplaceStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlResetStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlRollbackStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSelectQueryBlock;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSetCharSetStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSetNamesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSetPasswordStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSetTransactionStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowAuthorsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowBinLogEventsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowBinaryLogsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCharacterSetStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCollationStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowColumnsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowContributorsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateDatabaseStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateEventStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateFunctionStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateProcedureStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateTableStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateTriggerStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowCreateViewStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowDatabasesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowEngineStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowEnginesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowErrorsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowEventsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowFunctionCodeStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowFunctionStatusStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowGrantsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowIndexesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowKeysStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowMasterLogsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowMasterStatusStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowOpenTablesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowPluginsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowPrivilegesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowProcedureCodeStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowProcedureStatusStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowProcessListStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowProfileStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowProfilesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowRelayLogEventsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowSlaveHostsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowSlaveStatusStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowStatusStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowTableStatusStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowTriggersStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowVariantsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlShowWarningsStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSubPartitionByKey;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlSubPartitionByList;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlTableIndex;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlUnionQuery;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlUnlockTablesStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlUpdateStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlUpdateTableSource;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MysqlDeallocatePrepareStatement;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitor;
import studio.raptor.sqlparser.stat.TableStat.Mode;
import studio.raptor.sqlparser.util.JdbcUtils;

public class MySqlStatementVisitor extends StatementVisitor implements MySqlASTVisitor {

  public MySqlStatementVisitor(ParseResult parseResult){
    super(parseResult);
  }

  public boolean visit(SQLSelectStatement x) {
    parseResult.setOperate(Operate.DML);
    parseResult.setAliasMap();
    parseResult.getAliasMap().put("DUAL", null);

    return true;
  }

  @Override
  public String getDbType() {
    return JdbcUtils.MYSQL;
  }

  // DUAL
  public boolean visit(MySqlDeleteStatement x) {
    parseResult.setOperate(Operate.DML);
    parseResult.setAliasMap();

    setMode(x, Mode.Delete);

    accept(x.getFrom());
    accept(x.getUsing());
    x.getTableSource().accept(this);

    if (x.getTableSource() instanceof SQLExprTableSource) {
      SQLName tableName = (SQLName) ((SQLExprTableSource) x.getTableSource()).getExpr();
      String ident = tableName.toString();
      setCurrentTable(x, ident);

      addTable(ident, (SQLExprTableSource) x.getTableSource());
    }

    accept(x.getWhere());

    accept(x.getOrderBy());
    accept(x.getLimit());

    return false;
  }

  public void endVisit(MySqlDeleteStatement x) {
    parseResult.setAliasMap(null);
  }

  @Override
  public void endVisit(MySqlInsertStatement x) {
    setModeOrigin(x);
  }

  @Override
  public boolean visit(MySqlInsertStatement x) {
    parseResult.setOperate(Operate.DML);
    setMode(x, Mode.Insert);

    parseResult.setAliasMap();

    SQLName tableName = x.getTableName();

    String ident = null;
    if (tableName instanceof SQLIdentifierExpr) {
      ident = ((SQLIdentifierExpr) tableName).getName();
    } else if (tableName instanceof SQLPropertyExpr) {
      SQLPropertyExpr propertyExpr = (SQLPropertyExpr) tableName;
      if (propertyExpr.getOwner() instanceof SQLIdentifierExpr) {
        ident = propertyExpr.toString();
      }
    }

    if (ident != null) {
      setCurrentTable(x, ident);

      addTable(ident, x.getTableSource());

      Map<String, String> aliasMap = parseResult.getAliasMap();
      putAliasMap(aliasMap, x.getAlias(), ident);
      putAliasMap(aliasMap, ident, ident);
    }

    accept(x.getColumns());
    accept(x.getValuesList());
    accept(x.getQuery());
    accept(x.getDuplicateKeyUpdate());

    this.addInsertPairs(x);

    return false;
  }

  @Override
  public boolean visit(MySqlTableIndex x) {

    return false;
  }

  @Override
  public void endVisit(MySqlTableIndex x) {

  }

  @Override
  public boolean visit(MySqlKey x) {
    for (SQLObject item : x.getColumns()) {
      item.accept(this);
    }
    return false;
  }

  @Override
  public void endVisit(MySqlKey x) {

  }

  @Override
  public boolean visit(MySqlPrimaryKey x) {
    for (SQLObject item : x.getColumns()) {
      item.accept(this);
    }
    return false;
  }

  @Override
  public void endVisit(MySqlPrimaryKey x) {

  }

  @Override
  public void endVisit(MySqlIntervalExpr x) {

  }

  @Override
  public boolean visit(MySqlIntervalExpr x) {

    return true;
  }

  @Override
  public void endVisit(MySqlExtractExpr x) {

  }

  @Override
  public boolean visit(MySqlExtractExpr x) {

    return true;
  }

  @Override
  public void endVisit(MySqlMatchAgainstExpr x) {

  }

  @Override
  public boolean visit(MySqlMatchAgainstExpr x) {

    return true;
  }

  @Override
  public void endVisit(MySqlPrepareStatement x) {

  }

  @Override
  public boolean visit(MySqlPrepareStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlExecuteStatement x) {

  }

  @Override
  public boolean visit(MySqlExecuteStatement x) {

    return true;
  }

  @Override
  public void endVisit(MysqlDeallocatePrepareStatement x) {

  }

  @Override
  public boolean visit(MysqlDeallocatePrepareStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlLoadDataInFileStatement x) {

  }

  @Override
  public boolean visit(MySqlLoadDataInFileStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlLoadXmlStatement x) {

  }

  @Override
  public boolean visit(MySqlLoadXmlStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlReplaceStatement x) {

  }

  @Override
  public boolean visit(MySqlReplaceStatement x) {
    setMode(x, Mode.Replace);

    parseResult.setAliasMap();

    SQLName tableName = x.getTableName();

    String ident = null;
    if (tableName instanceof SQLIdentifierExpr) {
      ident = ((SQLIdentifierExpr) tableName).getName();
    } else if (tableName instanceof SQLPropertyExpr) {
      SQLPropertyExpr propertyExpr = (SQLPropertyExpr) tableName;
      if (propertyExpr.getOwner() instanceof SQLIdentifierExpr) {
        ident = propertyExpr.toString();
      }
    }

    if (ident != null) {
      setCurrentTable(x, ident);

      addTable(ident, x.getTableSource());

      putAliasMap(parseResult.getAliasMap(), ident, ident);
    }

    accept(x.getColumns());
    accept(x.getValuesList());
    accept(x.getQuery());

    return false;
  }

  @Override
  public void endVisit(SQLStartTransactionStatement x) {

  }

  @Override
  public boolean visit(SQLStartTransactionStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlCommitStatement x) {

  }

  @Override
  public boolean visit(MySqlCommitStatement x) {
    parseResult.setOperate(Operate.DCL);
    return true;
  }

  @Override
  public void endVisit(MySqlRollbackStatement x) {

  }

  @Override
  public boolean visit(MySqlRollbackStatement x) {
    parseResult.setOperate(Operate.DCL);
    return true;
  }

  @Override
  public void endVisit(MySqlShowColumnsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowColumnsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowDatabasesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowDatabasesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowWarningsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowWarningsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowStatusStatement x) {
    return true;
  }

  @Override
  public void endVisit(CobarShowStatus x) {

  }

  @Override
  public boolean visit(CobarShowStatus x) {
    return true;
  }

  @Override
  public void endVisit(MySqlKillStatement x) {

  }

  @Override
  public boolean visit(MySqlKillStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlBinlogStatement x) {

  }

  @Override
  public boolean visit(MySqlBinlogStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlResetStatement x) {

  }

  @Override
  public boolean visit(MySqlResetStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlCreateUserStatement x) {

  }

  @Override
  public boolean visit(MySqlCreateUserStatement x) {
    parseResult.setOperate(Operate.DDL);
    return false;
  }

  @Override
  public void endVisit(UserSpecification x) {

  }

  @Override
  public boolean visit(UserSpecification x) {
    return true;
  }

  @Override
  public void endVisit(MySqlPartitionByKey x) {

  }

  @Override
  public boolean visit(MySqlPartitionByKey x) {
    accept(x.getColumns());
    return false;
  }

  @Override
  public boolean visit(MySqlSelectQueryBlock x) {
    return this.visit((SQLSelectQueryBlock) x);
  }

  @Override
  public void endVisit(MySqlSelectQueryBlock x) {

  }

  @Override
  public boolean visit(MySqlOutFileExpr x) {
    return false;
  }

  @Override
  public void endVisit(MySqlOutFileExpr x) {

  }

  @Override
  public boolean visit(MySqlExplainStatement x) {
    if (x.getTableName() != null) {
      String table = x.getTableName().toString();
      addTable(table);
      if (x.getColumnName() != null) {
        addColumn(table, x.getColumnName().toString());
      }
    }

    if (x.getStatement() != null) {
      accept(x.getStatement());
    }

    return false;
  }

  @Override
  public void endVisit(MySqlExplainStatement x) {

  }

  @Override
  public boolean visit(MySqlUpdateStatement x) {
    parseResult.setOperate(Operate.DML);
    visit((SQLUpdateStatement) x);
    for (SQLExpr item : x.getReturning()) {
      item.accept(this);
    }

    return false;
  }

  @Override
  public void endVisit(MySqlUpdateStatement x) {

  }

  @Override
  public boolean visit(MySqlSetTransactionStatement x) {
    parseResult.setOperate(Operate.DCL);
    return false;
  }

  @Override
  public void endVisit(MySqlSetTransactionStatement x) {

  }

  @Override
  public boolean visit(MySqlSetNamesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlSetNamesStatement x) {

  }

  @Override
  public boolean visit(MySqlSetCharSetStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlSetCharSetStatement x) {

  }

  @Override
  public boolean visit(MySqlShowAuthorsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowAuthorsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowBinaryLogsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowBinaryLogsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowMasterLogsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowMasterLogsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCollationStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCollationStatement x) {

  }

  @Override
  public boolean visit(MySqlShowBinLogEventsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowBinLogEventsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCharacterSetStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCharacterSetStatement x) {

  }

  @Override
  public boolean visit(MySqlShowContributorsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowContributorsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateDatabaseStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateDatabaseStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateEventStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateEventStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateFunctionStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateFunctionStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateProcedureStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateProcedureStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateTableStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateTableStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateTriggerStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateTriggerStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateViewStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowCreateViewStatement x) {

  }

  @Override
  public boolean visit(MySqlShowEngineStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowEngineStatement x) {

  }

  @Override
  public boolean visit(MySqlShowEnginesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowEnginesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowErrorsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowErrorsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowEventsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowEventsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowFunctionCodeStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowFunctionCodeStatement x) {

  }

  @Override
  public boolean visit(MySqlShowFunctionStatusStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowFunctionStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowGrantsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowGrantsStatement x) {

  }

  @Override
  public boolean visit(MySqlUserName x) {
    return false;
  }

  @Override
  public void endVisit(MySqlUserName x) {

  }

  @Override
  public boolean visit(MySqlShowIndexesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowIndexesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowKeysStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowKeysStatement x) {

  }

  @Override
  public boolean visit(MySqlShowMasterStatusStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowMasterStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowOpenTablesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowOpenTablesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowPluginsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowPluginsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowPrivilegesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowPrivilegesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProcedureCodeStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowProcedureCodeStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProcedureStatusStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowProcedureStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProcessListStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowProcessListStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProfileStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowProfileStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProfilesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowProfilesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowRelayLogEventsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowRelayLogEventsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowSlaveHostsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowSlaveHostsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowSlaveStatusStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowSlaveStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowTableStatusStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowTableStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowTriggersStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowTriggersStatement x) {

  }

  @Override
  public boolean visit(MySqlShowVariantsStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlShowVariantsStatement x) {

  }

  @Override
  public boolean visit(MySqlRenameTableStatement.Item x) {
    return false;
  }

  @Override
  public void endVisit(MySqlRenameTableStatement.Item x) {

  }

  @Override
  public boolean visit(MySqlRenameTableStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlRenameTableStatement x) {

  }

  @Override
  public boolean visit(MySqlUnionQuery x) {
    return visit((SQLUnionQuery) x);
  }

  @Override
  public void endVisit(MySqlUnionQuery x) {

  }

  @Override
  public boolean visit(MySqlUseIndexHint x) {
    return false;
  }

  @Override
  public void endVisit(MySqlUseIndexHint x) {

  }

  @Override
  public boolean visit(MySqlIgnoreIndexHint x) {
    return false;
  }

  @Override
  public void endVisit(MySqlIgnoreIndexHint x) {

  }

  @Override
  public boolean visit(MySqlLockTableStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlLockTableStatement x) {

  }

  @Override
  public boolean visit(MySqlUnlockTablesStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlUnlockTablesStatement x) {

  }

  @Override
  public boolean visit(MySqlForceIndexHint x) {
    return false;
  }

  @Override
  public void endVisit(MySqlForceIndexHint x) {

  }

  @Override
  public boolean visit(MySqlAlterTableChangeColumn x) {
    parseResult.setOperate(Operate.DDL);
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String table = stmt.getName().toString();

    String columnName = x.getColumnName().toString();
    addColumn(table, columnName);
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableChangeColumn x) {

  }

  @Override
  public boolean visit(MySqlAlterTableModifyColumn x) {
    parseResult.setOperate(Operate.DDL);
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String table = stmt.getName().toString();

    String columnName = x.getNewColumnDefinition().getName().toString();
    addColumn(table, columnName);
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableModifyColumn x) {

  }

  @Override
  public boolean visit(MySqlAlterTableCharacter x) {
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableCharacter x) {

  }

  @Override
  public boolean visit(MySqlAlterTableOption x) {
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableOption x) {

  }

  @Override
  public boolean visit(MySqlCreateTableStatement x) {
    parseResult.setOperate(Operate.DDL);
    boolean val = super.visit((SQLCreateTableStatement) x);

    for (SQLObject option : x.getTableOptions().values()) {
      if (option instanceof SQLTableSource) {
        option.accept(this);
      }
    }

    return val;
  }

  @Override
  public void endVisit(MySqlCreateTableStatement x) {

  }

  @Override
  public boolean visit(MySqlHelpStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlHelpStatement x) {

  }

  @Override
  public boolean visit(MySqlCharExpr x) {
    return false;
  }

  @Override
  public void endVisit(MySqlCharExpr x) {

  }

  @Override
  public boolean visit(MySqlUnique x) {
    return false;
  }

  @Override
  public void endVisit(MySqlUnique x) {

  }

  @Override
  public boolean visit(MysqlForeignKey x) {
    return super.visit(x);
  }

  @Override
  public void endVisit(MysqlForeignKey x) {

  }

  @Override
  public boolean visit(MySqlAlterTableDiscardTablespace x) {
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableDiscardTablespace x) {

  }

  @Override
  public boolean visit(MySqlAlterTableImportTablespace x) {
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableImportTablespace x) {

  }

  @Override
  public boolean visit(TableSpaceOption x) {
    return false;
  }

  @Override
  public void endVisit(TableSpaceOption x) {
  }

  @Override
  public boolean visit(MySqlAnalyzeStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAnalyzeStatement x) {

  }

  @Override
  public boolean visit(MySqlAlterUserStatement x) {
    parseResult.setOperate(Operate.DDL);
    return true;
  }

  @Override
  public void endVisit(MySqlAlterUserStatement x) {

  }

  @Override
  public boolean visit(MySqlOptimizeStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlOptimizeStatement x) {

  }

  @Override
  public boolean visit(MySqlSetPasswordStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlSetPasswordStatement x) {

  }

  @Override
  public boolean visit(MySqlHintStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlHintStatement x) {

  }

  @Override
  public boolean visit(MySqlOrderingExpr x) {
    return true;
  }

  @Override
  public void endVisit(MySqlOrderingExpr x) {

  }

  @Override
  public boolean visit(MySqlAlterTableAlterColumn x) {
    return false;
  }

  @Override
  public void endVisit(MySqlAlterTableAlterColumn x) {

  }

  /**
   * support procedure
   */
  @Override
  public boolean visit(MySqlWhileStatement x) {
    accept(x.getStatements());
    return false;
  }

  @Override
  public void endVisit(MySqlWhileStatement x) {

  }

  @Override
  public boolean visit(MySqlCaseStatement x) {
    accept(x.getWhenList());
    return false;
  }

  @Override
  public void endVisit(MySqlCaseStatement x) {

  }

  @Override
  public boolean visit(MySqlDeclareStatement x) {
    for (SQLDeclareItem item : x.getVarList()) {
      item.setParent(x);

      SQLName var = (SQLName) item.getName();
      parseResult.getVariants().put(var.toString(), var);
    }

    return false;
  }

  @Override
  public void endVisit(MySqlDeclareStatement x) {

  }

  @Override
  public boolean visit(MySqlSelectIntoStatement x) {
    parseResult.setOperate(Operate.DML);
    return false;
  }

  @Override
  public void endVisit(MySqlSelectIntoStatement x) {

  }

  @Override
  public boolean visit(MySqlWhenStatement x) {
    accept(x.getStatements());
    return false;
  }

  @Override
  public void endVisit(MySqlWhenStatement x) {

  }

  @Override
  public boolean visit(MySqlLeaveStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlLeaveStatement x) {

  }

  @Override
  public boolean visit(MySqlIterateStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlIterateStatement x) {

  }

  @Override
  public boolean visit(MySqlRepeatStatement x) {
    accept(x.getStatements());
    return false;
  }

  @Override
  public void endVisit(MySqlRepeatStatement x) {

  }

  @Override
  public boolean visit(MySqlCursorDeclareStatement x) {
    accept(x.getSelect());
    return false;
  }

  @Override
  public void endVisit(MySqlCursorDeclareStatement x) {

  }

  @Override
  public boolean visit(MySqlUpdateTableSource x) {
    if (x.getUpdate() != null) {
      return this.visit(x.getUpdate());
    }
    return false;
  }

  @Override
  public void endVisit(MySqlUpdateTableSource x) {

  }

  @Override
  public boolean visit(MySqlSubPartitionByKey x) {
    return false;
  }

  @Override
  public void endVisit(MySqlSubPartitionByKey x) {

  }

  @Override
  public boolean visit(MySqlSubPartitionByList x) {
    return false;
  }

  @Override
  public void endVisit(MySqlSubPartitionByList x) {

  }

  @Override
  public boolean visit(MySqlDeclareHandlerStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlDeclareHandlerStatement x) {

  }

  @Override
  public boolean visit(MySqlDeclareConditionStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlDeclareConditionStatement x) {

  }
}
