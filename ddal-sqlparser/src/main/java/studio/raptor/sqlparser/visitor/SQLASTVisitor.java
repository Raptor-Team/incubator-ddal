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
package studio.raptor.sqlparser.visitor;

import studio.raptor.sqlparser.ast.SQLCommentHint;
import studio.raptor.sqlparser.ast.SQLDataType;
import studio.raptor.sqlparser.ast.SQLDeclareItem;
import studio.raptor.sqlparser.ast.SQLKeep;
import studio.raptor.sqlparser.ast.SQLLimit;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.SQLOrderBy;
import studio.raptor.sqlparser.ast.SQLOver;
import studio.raptor.sqlparser.ast.SQLParameter;
import studio.raptor.sqlparser.ast.SQLPartition;
import studio.raptor.sqlparser.ast.SQLPartitionByHash;
import studio.raptor.sqlparser.ast.SQLPartitionByList;
import studio.raptor.sqlparser.ast.SQLPartitionByRange;
import studio.raptor.sqlparser.ast.SQLPartitionValue;
import studio.raptor.sqlparser.ast.SQLSubPartition;
import studio.raptor.sqlparser.ast.SQLSubPartitionByHash;
import studio.raptor.sqlparser.ast.SQLSubPartitionByList;
import studio.raptor.sqlparser.ast.expr.SQLAggregateExpr;
import studio.raptor.sqlparser.ast.expr.SQLAllColumnExpr;
import studio.raptor.sqlparser.ast.expr.SQLAllExpr;
import studio.raptor.sqlparser.ast.expr.SQLAnyExpr;
import studio.raptor.sqlparser.ast.expr.SQLArrayExpr;
import studio.raptor.sqlparser.ast.expr.SQLBetweenExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLBooleanExpr;
import studio.raptor.sqlparser.ast.expr.SQLCaseExpr;
import studio.raptor.sqlparser.ast.expr.SQLCastExpr;
import studio.raptor.sqlparser.ast.expr.SQLCharExpr;
import studio.raptor.sqlparser.ast.expr.SQLCurrentOfCursorExpr;
import studio.raptor.sqlparser.ast.expr.SQLDateExpr;
import studio.raptor.sqlparser.ast.expr.SQLDefaultExpr;
import studio.raptor.sqlparser.ast.expr.SQLExistsExpr;
import studio.raptor.sqlparser.ast.expr.SQLGroupingSetExpr;
import studio.raptor.sqlparser.ast.expr.SQLHexExpr;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLInListExpr;
import studio.raptor.sqlparser.ast.expr.SQLInSubQueryExpr;
import studio.raptor.sqlparser.ast.expr.SQLIntegerExpr;
import studio.raptor.sqlparser.ast.expr.SQLListExpr;
import studio.raptor.sqlparser.ast.expr.SQLMethodInvokeExpr;
import studio.raptor.sqlparser.ast.expr.SQLNCharExpr;
import studio.raptor.sqlparser.ast.expr.SQLNotExpr;
import studio.raptor.sqlparser.ast.expr.SQLNullExpr;
import studio.raptor.sqlparser.ast.expr.SQLNumberExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.expr.SQLQueryExpr;
import studio.raptor.sqlparser.ast.expr.SQLSequenceExpr;
import studio.raptor.sqlparser.ast.expr.SQLSomeExpr;
import studio.raptor.sqlparser.ast.expr.SQLTimestampExpr;
import studio.raptor.sqlparser.ast.expr.SQLUnaryExpr;
import studio.raptor.sqlparser.ast.expr.SQLVariantRefExpr;
import studio.raptor.sqlparser.ast.statement.SQLAlterDatabaseStatement;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddColumn;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddIndex;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAlterColumn;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAnalyzePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableCheckPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableCoalescePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableConvertCharSet;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDisableConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDisableKeys;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDisableLifecycle;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDiscardPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropColumnItem;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropForeignKey;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropIndex;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropKey;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropPrimaryKey;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableEnableConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableEnableKeys;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableEnableLifecycle;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableImportPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableOptimizePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableReOrganizePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRebuildPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRename;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRenameColumn;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRenamePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRepairPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableSetComment;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableSetLifecycle;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableTouch;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableTruncatePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterViewRenameStatement;
import studio.raptor.sqlparser.ast.statement.SQLAssignItem;
import studio.raptor.sqlparser.ast.statement.SQLBlockStatement;
import studio.raptor.sqlparser.ast.statement.SQLCallStatement;
import studio.raptor.sqlparser.ast.statement.SQLCharacterDataType;
import studio.raptor.sqlparser.ast.statement.SQLCheck;
import studio.raptor.sqlparser.ast.statement.SQLCloseStatement;
import studio.raptor.sqlparser.ast.statement.SQLColumnCheck;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLColumnPrimaryKey;
import studio.raptor.sqlparser.ast.statement.SQLColumnReference;
import studio.raptor.sqlparser.ast.statement.SQLColumnUniqueKey;
import studio.raptor.sqlparser.ast.statement.SQLCommentStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateDatabaseStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateIndexStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateProcedureStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateSequenceStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateTriggerStatement;
import studio.raptor.sqlparser.ast.statement.SQLCreateViewStatement;
import studio.raptor.sqlparser.ast.statement.SQLDeleteStatement;
import studio.raptor.sqlparser.ast.statement.SQLDescribeStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropDatabaseStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropFunctionStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropIndexStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropProcedureStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropSequenceStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropTableSpaceStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropTriggerStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropUserStatement;
import studio.raptor.sqlparser.ast.statement.SQLDropViewStatement;
import studio.raptor.sqlparser.ast.statement.SQLErrorLoggingClause;
import studio.raptor.sqlparser.ast.statement.SQLExplainStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprHint;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLFetchStatement;
import studio.raptor.sqlparser.ast.statement.SQLForeignKeyImpl;
import studio.raptor.sqlparser.ast.statement.SQLGrantStatement;
import studio.raptor.sqlparser.ast.statement.SQLIfStatement;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.ast.statement.SQLJoinTableSource;
import studio.raptor.sqlparser.ast.statement.SQLLoopStatement;
import studio.raptor.sqlparser.ast.statement.SQLMergeStatement;
import studio.raptor.sqlparser.ast.statement.SQLNotNullConstraint;
import studio.raptor.sqlparser.ast.statement.SQLNullConstraint;
import studio.raptor.sqlparser.ast.statement.SQLOpenStatement;
import studio.raptor.sqlparser.ast.statement.SQLPrimaryKeyImpl;
import studio.raptor.sqlparser.ast.statement.SQLReleaseSavePointStatement;
import studio.raptor.sqlparser.ast.statement.SQLRevokeStatement;
import studio.raptor.sqlparser.ast.statement.SQLRollbackStatement;
import studio.raptor.sqlparser.ast.statement.SQLSavePointStatement;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.ast.statement.SQLSelectGroupByClause;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.ast.statement.SQLSelectOrderByItem;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLSetStatement;
import studio.raptor.sqlparser.ast.statement.SQLShowTablesStatement;
import studio.raptor.sqlparser.ast.statement.SQLStartTransactionStatement;
import studio.raptor.sqlparser.ast.statement.SQLSubqueryTableSource;
import studio.raptor.sqlparser.ast.statement.SQLTruncateStatement;
import studio.raptor.sqlparser.ast.statement.SQLUnionQuery;
import studio.raptor.sqlparser.ast.statement.SQLUnionQueryTableSource;
import studio.raptor.sqlparser.ast.statement.SQLUnique;
import studio.raptor.sqlparser.ast.statement.SQLUpdateSetItem;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.ast.statement.SQLUseStatement;
import studio.raptor.sqlparser.ast.statement.SQLWithSubqueryClause;

public interface SQLASTVisitor {

  void endVisit(SQLAllColumnExpr x);

  void endVisit(SQLBetweenExpr x);

  void endVisit(SQLBinaryOpExpr x);

  void endVisit(SQLCaseExpr x);

  void endVisit(SQLCaseExpr.Item x);

  void endVisit(SQLCharExpr x);

  void endVisit(SQLIdentifierExpr x);

  void endVisit(SQLInListExpr x);

  void endVisit(SQLIntegerExpr x);

  void endVisit(SQLExistsExpr x);

  void endVisit(SQLNCharExpr x);

  void endVisit(SQLNotExpr x);

  void endVisit(SQLNullExpr x);

  void endVisit(SQLNumberExpr x);

  void endVisit(SQLPropertyExpr x);

  void endVisit(SQLSelectGroupByClause x);

  void endVisit(SQLSelectItem x);

  void endVisit(SQLSelectStatement selectStatement);

  void postVisit(SQLObject astNode);

  void preVisit(SQLObject astNode);

  boolean visit(SQLAllColumnExpr x);

  boolean visit(SQLBetweenExpr x);

  boolean visit(SQLBinaryOpExpr x);

  boolean visit(SQLCaseExpr x);

  boolean visit(SQLCaseExpr.Item x);

  boolean visit(SQLCastExpr x);

  boolean visit(SQLCharExpr x);

  boolean visit(SQLExistsExpr x);

  boolean visit(SQLIdentifierExpr x);

  boolean visit(SQLInListExpr x);

  boolean visit(SQLIntegerExpr x);

  boolean visit(SQLNCharExpr x);

  boolean visit(SQLNotExpr x);

  boolean visit(SQLNullExpr x);

  boolean visit(SQLNumberExpr x);

  boolean visit(SQLPropertyExpr x);

  boolean visit(SQLSelectGroupByClause x);

  boolean visit(SQLSelectItem x);

  void endVisit(SQLCastExpr x);

  boolean visit(SQLSelectStatement astNode);

  void endVisit(SQLAggregateExpr astNode);

  boolean visit(SQLAggregateExpr astNode);

  boolean visit(SQLVariantRefExpr x);

  void endVisit(SQLVariantRefExpr x);

  boolean visit(SQLQueryExpr x);

  void endVisit(SQLQueryExpr x);

  boolean visit(SQLUnaryExpr x);

  void endVisit(SQLUnaryExpr x);

  boolean visit(SQLHexExpr x);

  void endVisit(SQLHexExpr x);

  boolean visit(SQLSelect x);

  void endVisit(SQLSelect select);

  boolean visit(SQLSelectQueryBlock x);

  void endVisit(SQLSelectQueryBlock x);

  boolean visit(SQLExprTableSource x);

  void endVisit(SQLExprTableSource x);

  boolean visit(SQLOrderBy x);

  void endVisit(SQLOrderBy x);

  boolean visit(SQLSelectOrderByItem x);

  void endVisit(SQLSelectOrderByItem x);

  boolean visit(SQLDropTableStatement x);

  void endVisit(SQLDropTableStatement x);

  boolean visit(SQLCreateTableStatement x);

  void endVisit(SQLCreateTableStatement x);

  boolean visit(SQLColumnDefinition x);

  void endVisit(SQLColumnDefinition x);

  boolean visit(SQLColumnDefinition.Identity x);

  void endVisit(SQLColumnDefinition.Identity x);

  boolean visit(SQLDataType x);

  void endVisit(SQLDataType x);

  boolean visit(SQLCharacterDataType x);

  void endVisit(SQLCharacterDataType x);

  boolean visit(SQLDeleteStatement x);

  void endVisit(SQLDeleteStatement x);

  boolean visit(SQLCurrentOfCursorExpr x);

  void endVisit(SQLCurrentOfCursorExpr x);

  boolean visit(SQLInsertStatement x);

  void endVisit(SQLInsertStatement x);

  boolean visit(SQLInsertStatement.ValuesClause x);

  void endVisit(SQLInsertStatement.ValuesClause x);

  boolean visit(SQLUpdateSetItem x);

  void endVisit(SQLUpdateSetItem x);

  boolean visit(SQLUpdateStatement x);

  void endVisit(SQLUpdateStatement x);

  boolean visit(SQLCreateViewStatement x);

  void endVisit(SQLCreateViewStatement x);

  boolean visit(SQLCreateViewStatement.Column x);

  void endVisit(SQLCreateViewStatement.Column x);

  boolean visit(SQLNotNullConstraint x);

  void endVisit(SQLNotNullConstraint x);

  void endVisit(SQLMethodInvokeExpr x);

  boolean visit(SQLMethodInvokeExpr x);

  void endVisit(SQLUnionQuery x);

  boolean visit(SQLUnionQuery x);

  void endVisit(SQLSetStatement x);

  boolean visit(SQLSetStatement x);

  void endVisit(SQLAssignItem x);

  boolean visit(SQLAssignItem x);

  void endVisit(SQLCallStatement x);

  boolean visit(SQLCallStatement x);

  void endVisit(SQLJoinTableSource x);

  boolean visit(SQLJoinTableSource x);

  void endVisit(SQLSomeExpr x);

  boolean visit(SQLSomeExpr x);

  void endVisit(SQLAnyExpr x);

  boolean visit(SQLAnyExpr x);

  void endVisit(SQLAllExpr x);

  boolean visit(SQLAllExpr x);

  void endVisit(SQLInSubQueryExpr x);

  boolean visit(SQLInSubQueryExpr x);

  void endVisit(SQLListExpr x);

  boolean visit(SQLListExpr x);

  void endVisit(SQLSubqueryTableSource x);

  boolean visit(SQLSubqueryTableSource x);

  void endVisit(SQLTruncateStatement x);

  boolean visit(SQLTruncateStatement x);

  void endVisit(SQLDefaultExpr x);

  boolean visit(SQLDefaultExpr x);

  void endVisit(SQLCommentStatement x);

  boolean visit(SQLCommentStatement x);

  void endVisit(SQLUseStatement x);

  boolean visit(SQLUseStatement x);

  boolean visit(SQLAlterTableAddColumn x);

  void endVisit(SQLAlterTableAddColumn x);

  boolean visit(SQLAlterTableDropColumnItem x);

  void endVisit(SQLAlterTableDropColumnItem x);

  boolean visit(SQLAlterTableDropIndex x);

  void endVisit(SQLAlterTableDropIndex x);

  boolean visit(SQLDropIndexStatement x);

  void endVisit(SQLDropIndexStatement x);

  boolean visit(SQLDropViewStatement x);

  void endVisit(SQLDropViewStatement x);

  boolean visit(SQLSavePointStatement x);

  void endVisit(SQLSavePointStatement x);

  boolean visit(SQLRollbackStatement x);

  void endVisit(SQLRollbackStatement x);

  boolean visit(SQLReleaseSavePointStatement x);

  void endVisit(SQLReleaseSavePointStatement x);

  void endVisit(SQLCommentHint x);

  boolean visit(SQLCommentHint x);

  void endVisit(SQLCreateDatabaseStatement x);

  boolean visit(SQLCreateDatabaseStatement x);

  void endVisit(SQLOver x);

  boolean visit(SQLOver x);

  void endVisit(SQLKeep x);

  boolean visit(SQLKeep x);

  void endVisit(SQLColumnPrimaryKey x);

  boolean visit(SQLColumnPrimaryKey x);

  boolean visit(SQLColumnUniqueKey x);

  void endVisit(SQLColumnUniqueKey x);

  void endVisit(SQLWithSubqueryClause x);

  boolean visit(SQLWithSubqueryClause x);

  void endVisit(SQLWithSubqueryClause.Entry x);

  boolean visit(SQLWithSubqueryClause.Entry x);

  void endVisit(SQLAlterTableAlterColumn x);

  boolean visit(SQLAlterTableAlterColumn x);

  boolean visit(SQLCheck x);

  void endVisit(SQLCheck x);

  boolean visit(SQLAlterTableDropForeignKey x);

  void endVisit(SQLAlterTableDropForeignKey x);

  boolean visit(SQLAlterTableDropPrimaryKey x);

  void endVisit(SQLAlterTableDropPrimaryKey x);

  boolean visit(SQLAlterTableDisableKeys x);

  void endVisit(SQLAlterTableDisableKeys x);

  boolean visit(SQLAlterTableEnableKeys x);

  void endVisit(SQLAlterTableEnableKeys x);

  boolean visit(SQLAlterTableStatement x);

  void endVisit(SQLAlterTableStatement x);

  boolean visit(SQLAlterTableDisableConstraint x);

  void endVisit(SQLAlterTableDisableConstraint x);

  boolean visit(SQLAlterTableEnableConstraint x);

  void endVisit(SQLAlterTableEnableConstraint x);

  boolean visit(SQLColumnCheck x);

  void endVisit(SQLColumnCheck x);

  boolean visit(SQLExprHint x);

  void endVisit(SQLExprHint x);

  boolean visit(SQLAlterTableDropConstraint x);

  void endVisit(SQLAlterTableDropConstraint x);

  boolean visit(SQLUnique x);

  void endVisit(SQLUnique x);

  boolean visit(SQLPrimaryKeyImpl x);

  void endVisit(SQLPrimaryKeyImpl x);

  boolean visit(SQLCreateIndexStatement x);

  void endVisit(SQLCreateIndexStatement x);

  boolean visit(SQLAlterTableRenameColumn x);

  void endVisit(SQLAlterTableRenameColumn x);

  boolean visit(SQLColumnReference x);

  void endVisit(SQLColumnReference x);

  boolean visit(SQLForeignKeyImpl x);

  void endVisit(SQLForeignKeyImpl x);

  boolean visit(SQLDropSequenceStatement x);

  void endVisit(SQLDropSequenceStatement x);

  boolean visit(SQLDropTriggerStatement x);

  void endVisit(SQLDropTriggerStatement x);

  void endVisit(SQLDropUserStatement x);

  boolean visit(SQLDropUserStatement x);

  void endVisit(SQLExplainStatement x);

  boolean visit(SQLExplainStatement x);

  void endVisit(SQLGrantStatement x);

  boolean visit(SQLGrantStatement x);

  void endVisit(SQLDropDatabaseStatement x);

  boolean visit(SQLDropDatabaseStatement x);

  void endVisit(SQLAlterTableAddIndex x);

  boolean visit(SQLAlterTableAddIndex x);

  void endVisit(SQLAlterTableAddConstraint x);

  boolean visit(SQLAlterTableAddConstraint x);

  void endVisit(SQLCreateTriggerStatement x);

  boolean visit(SQLCreateTriggerStatement x);

  void endVisit(SQLDropFunctionStatement x);

  boolean visit(SQLDropFunctionStatement x);

  void endVisit(SQLDropTableSpaceStatement x);

  boolean visit(SQLDropTableSpaceStatement x);

  void endVisit(SQLDropProcedureStatement x);

  boolean visit(SQLDropProcedureStatement x);

  void endVisit(SQLBooleanExpr x);

  boolean visit(SQLBooleanExpr x);

  void endVisit(SQLUnionQueryTableSource x);

  boolean visit(SQLUnionQueryTableSource x);

  void endVisit(SQLTimestampExpr x);

  boolean visit(SQLTimestampExpr x);

  void endVisit(SQLRevokeStatement x);

  boolean visit(SQLRevokeStatement x);

  void endVisit(SQLBinaryExpr x);

  boolean visit(SQLBinaryExpr x);

  void endVisit(SQLAlterTableRename x);

  boolean visit(SQLAlterTableRename x);

  void endVisit(SQLAlterViewRenameStatement x);

  boolean visit(SQLAlterViewRenameStatement x);

  void endVisit(SQLShowTablesStatement x);

  boolean visit(SQLShowTablesStatement x);

  void endVisit(SQLAlterTableAddPartition x);

  boolean visit(SQLAlterTableAddPartition x);

  void endVisit(SQLAlterTableDropPartition x);

  boolean visit(SQLAlterTableDropPartition x);

  void endVisit(SQLAlterTableRenamePartition x);

  boolean visit(SQLAlterTableRenamePartition x);

  void endVisit(SQLAlterTableSetComment x);

  boolean visit(SQLAlterTableSetComment x);

  void endVisit(SQLAlterTableSetLifecycle x);

  boolean visit(SQLAlterTableSetLifecycle x);

  void endVisit(SQLAlterTableEnableLifecycle x);

  boolean visit(SQLAlterTableEnableLifecycle x);

  void endVisit(SQLAlterTableDisableLifecycle x);

  boolean visit(SQLAlterTableDisableLifecycle x);

  void endVisit(SQLAlterTableTouch x);

  boolean visit(SQLAlterTableTouch x);

  void endVisit(SQLArrayExpr x);

  boolean visit(SQLArrayExpr x);

  void endVisit(SQLOpenStatement x);

  boolean visit(SQLOpenStatement x);

  void endVisit(SQLFetchStatement x);

  boolean visit(SQLFetchStatement x);

  void endVisit(SQLCloseStatement x);

  boolean visit(SQLCloseStatement x);

  boolean visit(SQLGroupingSetExpr x);

  void endVisit(SQLGroupingSetExpr x);

  boolean visit(SQLIfStatement x);

  void endVisit(SQLIfStatement x);

  boolean visit(SQLIfStatement.ElseIf x);

  void endVisit(SQLIfStatement.ElseIf x);

  boolean visit(SQLIfStatement.Else x);

  void endVisit(SQLIfStatement.Else x);

  boolean visit(SQLLoopStatement x);

  void endVisit(SQLLoopStatement x);

  boolean visit(SQLParameter x);

  void endVisit(SQLParameter x);

  boolean visit(SQLCreateProcedureStatement x);

  void endVisit(SQLCreateProcedureStatement x);

  boolean visit(SQLBlockStatement x);

  void endVisit(SQLBlockStatement x);

  boolean visit(SQLAlterTableDropKey x);

  void endVisit(SQLAlterTableDropKey x);

  boolean visit(SQLDeclareItem x);

  void endVisit(SQLDeclareItem x);

  boolean visit(SQLPartitionValue x);

  void endVisit(SQLPartitionValue x);

  boolean visit(SQLPartition x);

  void endVisit(SQLPartition x);

  boolean visit(SQLPartitionByRange x);

  void endVisit(SQLPartitionByRange x);

  boolean visit(SQLPartitionByHash x);

  void endVisit(SQLPartitionByHash x);

  boolean visit(SQLPartitionByList x);

  void endVisit(SQLPartitionByList x);

  boolean visit(SQLSubPartition x);

  void endVisit(SQLSubPartition x);

  boolean visit(SQLSubPartitionByHash x);

  void endVisit(SQLSubPartitionByHash x);

  boolean visit(SQLSubPartitionByList x);

  void endVisit(SQLSubPartitionByList x);

  boolean visit(SQLAlterDatabaseStatement x);

  void endVisit(SQLAlterDatabaseStatement x);

  boolean visit(SQLAlterTableConvertCharSet x);

  void endVisit(SQLAlterTableConvertCharSet x);

  boolean visit(SQLAlterTableReOrganizePartition x);

  void endVisit(SQLAlterTableReOrganizePartition x);

  boolean visit(SQLAlterTableCoalescePartition x);

  void endVisit(SQLAlterTableCoalescePartition x);

  boolean visit(SQLAlterTableTruncatePartition x);

  void endVisit(SQLAlterTableTruncatePartition x);

  boolean visit(SQLAlterTableDiscardPartition x);

  void endVisit(SQLAlterTableDiscardPartition x);

  boolean visit(SQLAlterTableImportPartition x);

  void endVisit(SQLAlterTableImportPartition x);

  boolean visit(SQLAlterTableAnalyzePartition x);

  void endVisit(SQLAlterTableAnalyzePartition x);

  boolean visit(SQLAlterTableCheckPartition x);

  void endVisit(SQLAlterTableCheckPartition x);

  boolean visit(SQLAlterTableOptimizePartition x);

  void endVisit(SQLAlterTableOptimizePartition x);

  boolean visit(SQLAlterTableRebuildPartition x);

  void endVisit(SQLAlterTableRebuildPartition x);

  boolean visit(SQLAlterTableRepairPartition x);

  void endVisit(SQLAlterTableRepairPartition x);

  boolean visit(SQLSequenceExpr x);

  void endVisit(SQLSequenceExpr x);

  boolean visit(SQLMergeStatement x);

  void endVisit(SQLMergeStatement x);

  boolean visit(SQLMergeStatement.MergeUpdateClause x);

  void endVisit(SQLMergeStatement.MergeUpdateClause x);

  boolean visit(SQLMergeStatement.MergeInsertClause x);

  void endVisit(SQLMergeStatement.MergeInsertClause x);

  boolean visit(SQLErrorLoggingClause x);

  void endVisit(SQLErrorLoggingClause x);

  boolean visit(SQLNullConstraint x);

  void endVisit(SQLNullConstraint x);

  boolean visit(SQLCreateSequenceStatement x);

  void endVisit(SQLCreateSequenceStatement x);

  boolean visit(SQLDateExpr x);

  void endVisit(SQLDateExpr x);

  boolean visit(SQLLimit x);

  void endVisit(SQLLimit x);

  void endVisit(SQLStartTransactionStatement x);

  boolean visit(SQLStartTransactionStatement x);

  void endVisit(SQLDescribeStatement x);

  boolean visit(SQLDescribeStatement x);
}
