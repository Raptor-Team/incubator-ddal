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
package studio.raptor.sqlparser.dialect.mysql.visitor;

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
import studio.raptor.sqlparser.visitor.SQLASTVisitorAdapter;

public class MySqlASTVisitorAdapter extends SQLASTVisitorAdapter implements MySqlASTVisitor {

  @Override
  public boolean visit(MySqlTableIndex x) {
    return true;
  }

  @Override
  public void endVisit(MySqlTableIndex x) {

  }

  @Override
  public boolean visit(MySqlKey x) {
    return true;
  }

  @Override
  public void endVisit(MySqlKey x) {

  }

  @Override
  public boolean visit(MySqlPrimaryKey x) {

    return true;
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
  public void endVisit(MySqlDeleteStatement x) {

  }

  @Override
  public boolean visit(MySqlDeleteStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlInsertStatement x) {

  }

  @Override
  public boolean visit(MySqlInsertStatement x) {

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

    return true;
  }

  @Override
  public void endVisit(MySqlCommitStatement x) {

  }

  @Override
  public boolean visit(MySqlCommitStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlRollbackStatement x) {

  }

  @Override
  public boolean visit(MySqlRollbackStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlShowColumnsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowColumnsStatement x) {

    return true;
  }

  @Override
  public void endVisit(MySqlShowDatabasesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowDatabasesStatement x) {

    return true;
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
    return true;
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
    return true;
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
    return true;
  }

  @Override
  public boolean visit(MySqlSelectQueryBlock x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSelectQueryBlock x) {

  }

  @Override
  public boolean visit(MySqlOutFileExpr x) {
    return true;
  }

  @Override
  public void endVisit(MySqlOutFileExpr x) {

  }

  @Override
  public boolean visit(MySqlExplainStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlExplainStatement x) {

  }

  @Override
  public boolean visit(MySqlUpdateStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUpdateStatement x) {

  }

  @Override
  public boolean visit(MySqlSetTransactionStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSetTransactionStatement x) {

  }

  @Override
  public boolean visit(MySqlSetNamesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSetNamesStatement x) {

  }

  @Override
  public boolean visit(MySqlSetCharSetStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSetCharSetStatement x) {

  }

  @Override
  public boolean visit(MySqlShowAuthorsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowAuthorsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowBinaryLogsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowBinaryLogsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowMasterLogsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowMasterLogsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCollationStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCollationStatement x) {

  }

  @Override
  public boolean visit(MySqlShowBinLogEventsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowBinLogEventsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCharacterSetStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCharacterSetStatement x) {

  }

  @Override
  public boolean visit(MySqlShowContributorsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowContributorsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateDatabaseStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateDatabaseStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateEventStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateEventStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateFunctionStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateFunctionStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateProcedureStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateProcedureStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateTableStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateTableStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateTriggerStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateTriggerStatement x) {

  }

  @Override
  public boolean visit(MySqlShowCreateViewStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowCreateViewStatement x) {

  }

  @Override
  public boolean visit(MySqlShowEngineStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowEngineStatement x) {

  }

  @Override
  public boolean visit(MySqlShowEnginesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowEnginesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowErrorsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowErrorsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowEventsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowEventsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowFunctionCodeStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowFunctionCodeStatement x) {

  }

  @Override
  public boolean visit(MySqlShowFunctionStatusStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowFunctionStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowGrantsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowGrantsStatement x) {
  }

  @Override
  public boolean visit(MySqlUserName x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUserName x) {

  }

  @Override
  public boolean visit(MySqlShowIndexesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowIndexesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowKeysStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowKeysStatement x) {

  }

  @Override
  public boolean visit(MySqlShowMasterStatusStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowMasterStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowOpenTablesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowOpenTablesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowPluginsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowPluginsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowPrivilegesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowPrivilegesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProcedureCodeStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowProcedureCodeStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProcedureStatusStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowProcedureStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProcessListStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowProcessListStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProfileStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowProfileStatement x) {

  }

  @Override
  public boolean visit(MySqlShowProfilesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowProfilesStatement x) {

  }

  @Override
  public boolean visit(MySqlShowRelayLogEventsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowRelayLogEventsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowSlaveHostsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowSlaveHostsStatement x) {

  }

  @Override
  public boolean visit(MySqlShowSlaveStatusStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowSlaveStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowTableStatusStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowTableStatusStatement x) {

  }

  @Override
  public boolean visit(MySqlShowTriggersStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowTriggersStatement x) {

  }

  @Override
  public boolean visit(MySqlShowVariantsStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlShowVariantsStatement x) {

  }

  @Override
  public boolean visit(MySqlRenameTableStatement.Item x) {
    return true;
  }

  @Override
  public void endVisit(MySqlRenameTableStatement.Item x) {

  }

  @Override
  public boolean visit(MySqlRenameTableStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlRenameTableStatement x) {

  }

  @Override
  public boolean visit(MySqlUnionQuery x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUnionQuery x) {

  }

  @Override
  public boolean visit(MySqlUseIndexHint x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUseIndexHint x) {

  }

  @Override
  public boolean visit(MySqlIgnoreIndexHint x) {
    return true;
  }

  @Override
  public void endVisit(MySqlIgnoreIndexHint x) {

  }

  @Override
  public boolean visit(MySqlLockTableStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlLockTableStatement x) {

  }

  @Override
  public boolean visit(MySqlUnlockTablesStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUnlockTablesStatement x) {

  }

  @Override
  public boolean visit(MySqlForceIndexHint x) {
    return true;
  }

  @Override
  public void endVisit(MySqlForceIndexHint x) {

  }

  @Override
  public boolean visit(MySqlAlterTableChangeColumn x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableChangeColumn x) {

  }

  @Override
  public boolean visit(MySqlAlterTableCharacter x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableCharacter x) {

  }

  @Override
  public boolean visit(MySqlAlterTableOption x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableOption x) {

  }

  @Override
  public boolean visit(MySqlCreateTableStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlCreateTableStatement x) {

  }

  @Override
  public boolean visit(MySqlHelpStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlHelpStatement x) {

  }

  @Override
  public boolean visit(MySqlCharExpr x) {
    return true;
  }

  @Override
  public void endVisit(MySqlCharExpr x) {

  }

  @Override
  public boolean visit(MySqlUnique x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUnique x) {

  }

  @Override
  public boolean visit(MysqlForeignKey x) {
    return true;
  }

  @Override
  public void endVisit(MysqlForeignKey x) {

  }

  @Override
  public boolean visit(MySqlAlterTableModifyColumn x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableModifyColumn x) {

  }

  @Override
  public boolean visit(MySqlAlterTableDiscardTablespace x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableDiscardTablespace x) {

  }

  @Override
  public boolean visit(MySqlAlterTableImportTablespace x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableImportTablespace x) {

  }

  @Override
  public boolean visit(TableSpaceOption x) {
    return true;
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
    return true;
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
  public boolean visit(MySqlWhileStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlWhileStatement x) {

  }

  @Override
  public boolean visit(MySqlCaseStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlCaseStatement x) {

  }

  @Override
  public boolean visit(MySqlDeclareStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlDeclareStatement x) {

  }

  @Override
  public boolean visit(MySqlSelectIntoStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSelectIntoStatement x) {

  }

  @Override
  public boolean visit(MySqlWhenStatement x) {
    return true;
  }

  @Override
  public void endVisit(MySqlWhenStatement x) {

  }
  // add:end

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
    return false;
  }

  @Override
  public void endVisit(MySqlRepeatStatement x) {

  }

  @Override
  public boolean visit(MySqlCursorDeclareStatement x) {
    return false;
  }

  @Override
  public void endVisit(MySqlCursorDeclareStatement x) {

  }

  @Override
  public boolean visit(MySqlUpdateTableSource x) {
    return true;
  }

  @Override
  public void endVisit(MySqlUpdateTableSource x) {

  }

  @Override
  public boolean visit(MySqlAlterTableAlterColumn x) {
    return true;
  }

  @Override
  public void endVisit(MySqlAlterTableAlterColumn x) {

  }

  @Override
  public boolean visit(MySqlSubPartitionByKey x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSubPartitionByKey x) {

  }

  @Override
  public boolean visit(MySqlSubPartitionByList x) {
    return true;
  }

  @Override
  public void endVisit(MySqlSubPartitionByList x) {
  }

  @Override
  public boolean visit(MySqlDeclareHandlerStatement x) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void endVisit(MySqlDeclareHandlerStatement x) {
    // TODO Auto-generated method stub

  }

  @Override
  public boolean visit(MySqlDeclareConditionStatement x) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void endVisit(MySqlDeclareConditionStatement x) {
    // TODO Auto-generated method stub

  }

} //
