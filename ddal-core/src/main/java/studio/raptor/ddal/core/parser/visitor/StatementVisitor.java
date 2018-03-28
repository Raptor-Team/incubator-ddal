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

import com.google.common.base.Optional;
import com.google.common.base.Strings;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.core.parser.result.Operate;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn.AggregationType;
import studio.raptor.ddal.core.parser.result.merger.GroupByColumn;
import studio.raptor.ddal.core.parser.result.merger.Limit;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn.OrderByType;
import studio.raptor.sqlparser.ast.SQLDeclareItem;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLLimit;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.SQLOrderBy;
import studio.raptor.sqlparser.ast.SQLOrderingSpecification;
import studio.raptor.sqlparser.ast.SQLParameter;
import studio.raptor.sqlparser.ast.SQLPartition;
import studio.raptor.sqlparser.ast.SQLPartitionByHash;
import studio.raptor.sqlparser.ast.SQLPartitionByList;
import studio.raptor.sqlparser.ast.SQLPartitionByRange;
import studio.raptor.sqlparser.ast.SQLPartitionValue;
import studio.raptor.sqlparser.ast.SQLSetQuantifier;
import studio.raptor.sqlparser.ast.SQLSubPartition;
import studio.raptor.sqlparser.ast.SQLSubPartitionByHash;
import studio.raptor.sqlparser.ast.expr.SQLAggregateExpr;
import studio.raptor.sqlparser.ast.expr.SQLAllColumnExpr;
import studio.raptor.sqlparser.ast.expr.SQLArrayExpr;
import studio.raptor.sqlparser.ast.expr.SQLBetweenExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOperator;
import studio.raptor.sqlparser.ast.expr.SQLCastExpr;
import studio.raptor.sqlparser.ast.expr.SQLCurrentOfCursorExpr;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLInListExpr;
import studio.raptor.sqlparser.ast.expr.SQLInSubQueryExpr;
import studio.raptor.sqlparser.ast.expr.SQLIntegerExpr;
import studio.raptor.sqlparser.ast.expr.SQLMethodInvokeExpr;
import studio.raptor.sqlparser.ast.expr.SQLPropertyExpr;
import studio.raptor.sqlparser.ast.expr.SQLSequenceExpr;
import studio.raptor.sqlparser.ast.statement.SQLAlterDatabaseStatement;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddColumn;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAddIndex;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableAnalyzePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableCheckPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableCoalescePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableConvertCharSet;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDisableConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDiscardPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropForeignKey;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropIndex;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropKey;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableDropPrimaryKey;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableEnableConstraint;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableImportPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableItem;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableOptimizePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableReOrganizePartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRebuildPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRename;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableRepairPartition;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableStatement;
import studio.raptor.sqlparser.ast.statement.SQLAlterTableTruncatePartition;
import studio.raptor.sqlparser.ast.statement.SQLBlockStatement;
import studio.raptor.sqlparser.ast.statement.SQLCallStatement;
import studio.raptor.sqlparser.ast.statement.SQLCheck;
import studio.raptor.sqlparser.ast.statement.SQLCloseStatement;
import studio.raptor.sqlparser.ast.statement.SQLColumnConstraint;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLCommentStatement;
import studio.raptor.sqlparser.ast.statement.SQLConstraint;
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
import studio.raptor.sqlparser.ast.statement.SQLExplainStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLFetchStatement;
import studio.raptor.sqlparser.ast.statement.SQLForeignKeyImpl;
import studio.raptor.sqlparser.ast.statement.SQLGrantStatement;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.ast.statement.SQLJoinTableSource;
import studio.raptor.sqlparser.ast.statement.SQLMergeStatement;
import studio.raptor.sqlparser.ast.statement.SQLObjectType;
import studio.raptor.sqlparser.ast.statement.SQLOpenStatement;
import studio.raptor.sqlparser.ast.statement.SQLPrimaryKey;
import studio.raptor.sqlparser.ast.statement.SQLRevokeStatement;
import studio.raptor.sqlparser.ast.statement.SQLRollbackStatement;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.ast.statement.SQLSelectGroupByClause;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.ast.statement.SQLSelectOrderByItem;
import studio.raptor.sqlparser.ast.statement.SQLSelectQuery;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLSetStatement;
import studio.raptor.sqlparser.ast.statement.SQLShowTablesStatement;
import studio.raptor.sqlparser.ast.statement.SQLSubqueryTableSource;
import studio.raptor.sqlparser.ast.statement.SQLTableElement;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;
import studio.raptor.sqlparser.ast.statement.SQLTruncateStatement;
import studio.raptor.sqlparser.ast.statement.SQLUnique;
import studio.raptor.sqlparser.ast.statement.SQLUniqueConstraint;
import studio.raptor.sqlparser.ast.statement.SQLUpdateSetItem;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.ast.statement.SQLUseStatement;
import studio.raptor.sqlparser.ast.statement.SQLWithSubqueryClause;
import studio.raptor.sqlparser.dialect.mysql.ast.expr.MySqlExpr;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlASTVisitorAdapter;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlOutputVisitor;
import studio.raptor.sqlparser.dialect.odps.ast.OdpsValuesTableSource;
import studio.raptor.sqlparser.dialect.oracle.ast.expr.OracleExpr;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleOutputVisitor;
import studio.raptor.sqlparser.dialect.postgresql.visitor.PGASTVisitorAdapter;
import studio.raptor.sqlparser.stat.TableStat.Column;
import studio.raptor.sqlparser.stat.TableStat.Condition;
import studio.raptor.sqlparser.stat.TableStat.Mode;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.util.JdbcUtils;
import studio.raptor.sqlparser.util.StringUtils;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;
import studio.raptor.sqlparser.visitor.SQLASTVisitorAdapter;

public class StatementVisitor extends SQLASTVisitorAdapter {

  protected final static String ATTR_TABLE = "_table_";
  protected final static String ATTR_COLUMN = "_column_";

  protected String currentTable;
  private Mode mode;

  protected final ParseResult parseResult;

  protected StatementVisitor(ParseResult parseResult){
    this.parseResult = parseResult;
  }

  public void addTable(String ident) {
    this.addTable(ident, null, null);
  }

  public void addTable(String ident, SQLExprTableSource tableSource) {
    this.addTable(ident, null, tableSource);
  }

  public Column addColumn(String tableName, String columnName) {
    Column column = parseResult.getColumn(tableName, columnName);
    if (column == null && columnName != null) {
      column = new Column(tableName, columnName);
      parseResult.getColumns().put(column, column);
    }
    return column;
  }

  public void addTable(String tableName, String alias, SQLExprTableSource tableSource) {
    if (parseResult.getVariants().containsKey(tableName)) {
      return;
    }

    if (!parseResult.getTableNames().contains(tableName)) {
      parseResult.addTableSource(tableSource);
      putAliasMap(parseResult.getAliasMap(), alias, tableName);
    }

  }

  String handleName(String ident) {
    int len = ident.length();
    if (ident.charAt(0) == '[' && ident.charAt(len - 1) == ']') {
      ident = ident.substring(1, len - 1);
    } else {
      boolean flag0 = false;
      boolean flag1 = false;
      boolean flag2 = false;
      boolean flag3 = false;
      for (int i = 0; i < len; ++i) {
        final char ch = ident.charAt(i);
        if (ch == '\"') {
          flag0 = true;
        } else if (ch == '`') {
          flag1 = true;
        } else if (ch == ' ') {
          flag2 = true;
        } else if (ch == '\'') {
          flag3 = true;
        }
      }
      if (flag0) {
        ident = ident.replaceAll("\"", "");
      }

      if (flag1) {
        ident = ident.replaceAll("`", "");
      }

      if (flag2) {
        ident = ident.replaceAll(" ", "");
      }

      if (flag3) {
        ident = ident.replaceAll("'", "");
      }
    }
    ident = aliasWrap(ident);

    return ident;
  }

  public void setCurrentTable(String table) {
    this.currentTable = table;
  }

  public void setCurrentTable(SQLObject x) {
    x.putAttribute("_old_local_", this.currentTable);
  }

  public void restoreCurrentTable(SQLObject x) {
    String table = (String) x.getAttribute("_old_local_");
    this.currentTable = table;
  }

  public void setCurrentTable(SQLObject x, String table) {
    x.putAttribute("_old_local_", this.currentTable);
    this.currentTable = table;
  }

  public String getCurrentTable() {
    return currentTable;
  }

  protected Mode getMode() {
    return mode;
  }

  protected void setModeOrigin(SQLObject x) {
    Mode originalMode = (Mode) x.getAttribute("_original_use_mode");
    mode = originalMode;
  }

  protected Mode setMode(SQLObject x, Mode mode) {
    Mode oldMode = this.mode;
    x.putAttribute("_original_use_mode", oldMode);
    this.mode = mode;
    return oldMode;
  }

  private boolean visitOrderBy(SQLIdentifierExpr x) {
    if (containsSubQuery(currentTable)) {
      return false;
    }

    String identName = x.getName();
    if (parseResult.getAliasMap() != null && parseResult.getAliasMap().containsKey(identName.toLowerCase()) && parseResult
        .getAliasMap().get(identName) == null) {
      return false;
    }

    if (currentTable != null) {
      orderByAddColumn(currentTable, identName, x);
    } else {
      orderByAddColumn("UNKOWN", identName, x);
    }
    return false;
  }

  private boolean visitOrderBy(SQLPropertyExpr x) {
    if (x.getOwner() instanceof SQLIdentifierExpr) {
      String owner = ((SQLIdentifierExpr) x.getOwner()).getName();

      if (containsSubQuery(owner)) {
        return false;
      }

      owner = aliasWrap(owner);

      if (owner != null) {
        String columnName = x.getName();
        Column column = new Column(owner, columnName);
        OrderByType orderByType = getOrderVyType(x.getParent(), column);
        Optional<String> alias = Optional.fromNullable(parseResult.getAliasMap().get(x.toString().toLowerCase()));
        OrderByColumn orderByColumn = new OrderByColumn(Optional.of(owner), columnName.toUpperCase(), alias, orderByType);
        parseResult.getOrderByColumns().add(orderByColumn);
      }
    }

    return false;
  }

  private void orderByAddColumn(String table, String columnName, SQLObject expr) {
    Column column = new Column(table, columnName);
    OrderByType orderByType = getOrderVyType(expr.getParent(), column);
    /** TODO 临时解决 Upper */
    OrderByColumn orderByColumn = new OrderByColumn(columnName.toUpperCase(), orderByType);
    parseResult.getOrderByColumns().add(orderByColumn);
  }

  private OrderByType getOrderVyType(SQLObject expr, Column column){
    OrderByType orderByType = OrderByType.ASC;
    if (expr instanceof SQLSelectOrderByItem) {
      SQLOrderingSpecification type = ((SQLSelectOrderByItem) expr).getType();
      column.getAttributes().put("orderBy.type", type);
      if(null != type && type.name.equals(SQLOrderingSpecification.DESC.name)){
        orderByType = OrderByType.DESC;
      }
    }
    return orderByType;
  }

  protected class OrderByStatVisitor extends SQLASTVisitorAdapter {

    private final SQLOrderBy orderBy;

    public OrderByStatVisitor(SQLOrderBy orderBy) {
      this.orderBy = orderBy;
      for (SQLSelectOrderByItem item : orderBy.getItems()) {
        item.getExpr().setParent(item);
      }
    }

    public SQLOrderBy getOrderBy() {
      return orderBy;
    }

    public boolean visit(SQLIdentifierExpr x) {
      return visitOrderBy(x);
    }

    public boolean visit(SQLPropertyExpr x) {
      return visitOrderBy(x);
    }
  }

  protected class MySqlOrderByStatVisitor extends MySqlASTVisitorAdapter {

    private final SQLOrderBy orderBy;

    public MySqlOrderByStatVisitor(SQLOrderBy orderBy) {
      this.orderBy = orderBy;
      for (SQLSelectOrderByItem item : orderBy.getItems()) {
        item.getExpr().setParent(item);
      }
    }

    public SQLOrderBy getOrderBy() {
      return orderBy;
    }

    public boolean visit(SQLIdentifierExpr x) {
      return visitOrderBy(x);
    }

    public boolean visit(SQLPropertyExpr x) {
      return visitOrderBy(x);
    }
  }

  protected class PGOrderByStatVisitor extends PGASTVisitorAdapter {

    private final SQLOrderBy orderBy;

    public PGOrderByStatVisitor(SQLOrderBy orderBy) {
      this.orderBy = orderBy;
      for (SQLSelectOrderByItem item : orderBy.getItems()) {
        item.getExpr().setParent(item);
      }
    }

    public SQLOrderBy getOrderBy() {
      return orderBy;
    }

    public boolean visit(SQLIdentifierExpr x) {
      return visitOrderBy(x);
    }

    public boolean visit(SQLPropertyExpr x) {
      return visitOrderBy(x);
    }
  }

  protected class OracleOrderByStatVisitor extends PGASTVisitorAdapter {

    private final SQLOrderBy orderBy;

    public OracleOrderByStatVisitor(SQLOrderBy orderBy) {
      this.orderBy = orderBy;
      for (SQLSelectOrderByItem item : orderBy.getItems()) {
        item.getExpr().setParent(item);
      }
    }

    public SQLOrderBy getOrderBy() {
      return orderBy;
    }

    public boolean visit(SQLIdentifierExpr x) {
      return visitOrderBy(x);
    }

    public boolean visit(SQLPropertyExpr x) {
      return visitOrderBy(x);
    }
  }

  public boolean visit(SQLOrderBy x) {
    final SQLASTVisitor orderByVisitor = createOrderByVisitor(x);

    SQLSelectQueryBlock query = null;
    if (x.getParent() instanceof SQLSelectQueryBlock) {
      query = (SQLSelectQueryBlock) x.getParent();
    }
    if (query != null) {
      int index = 0;
      List<SQLSelectItem> selectList = ((SQLSelectQueryBlock) x.getParent()).getSelectList();
      for (SQLSelectOrderByItem item : x.getItems()) {
        SQLExpr expr = item.getExpr();
        if (expr instanceof SQLIdentifierExpr){
          if (parseResult.isAllColumn()){
            continue;
          }
          String columnName = ((SQLIdentifierExpr) expr).getName();
          if (!parseResult.getAliasMap().containsValue(columnName)){

          }
        }
        if (expr instanceof SQLPropertyExpr){
          if (parseResult.isAllColumn()){
            continue;
          }
          String owner = ((SQLPropertyExpr) expr).getOwner().toString();
          String table = parseResult.getAliasMap().get(owner);
          String column = ((SQLPropertyExpr) expr).getName();
          if (!parseResult.getColumns().containsKey(new Column(table, column))){
            SQLSelectItem col =new SQLSelectItem();
            col.setAlias("orderBy"+index);
            col.setParent(x.getParent());

            SQLPropertyExpr colExpr = new SQLPropertyExpr();
            colExpr.setOwner(((SQLPropertyExpr) expr).getOwner());
            colExpr.setName(column);
            col.setExpr(colExpr);
            selectList.add(col);
          }
        }

        if (expr instanceof SQLIntegerExpr) {
          int intValue = ((SQLIntegerExpr) expr).getNumber().intValue() - 1;
          if (intValue < query.getSelectList().size()) {
            SQLSelectItem selectItem = query.getSelectList().get(intValue);
            selectItem.getExpr().accept(orderByVisitor);
          }
        } else if (expr instanceof MySqlExpr || expr instanceof OracleExpr) {
          continue;
        }
        index++;
      }
    }
    x.accept(orderByVisitor);
    return true;
  }

  public boolean visit(SQLBetweenExpr x) {
    Column column = getColumn(x);
    if (column == null) {
      return true;
    }

    Condition condition = null;
    for (Condition item : parseResult.getConditions()) {
      if (item.getColumn().equals(column) && item.getOperator().equals("between")) {
        condition = item;
        break;
      }
    }

    if (condition == null) {
      condition = new Condition();
      condition.setColumn(column);
      condition.setOperator("between");
      parseResult.getConditions().add(condition);
    }

    condition.getValues().add(x.beginExpr);
    condition.getValues().add(x.endExpr);

    return true;
  }

  protected SQLASTVisitor createOrderByVisitor(SQLOrderBy x) {
    final SQLASTVisitor orderByVisitor;
    if (JdbcConstants.MYSQL.equals(getDbType())) {
      orderByVisitor = new MySqlOrderByStatVisitor(x);
    } else if (JdbcConstants.POSTGRESQL.equals(getDbType())) {
      orderByVisitor = new PGOrderByStatVisitor(x);
    } else if (JdbcConstants.ORACLE.equals(getDbType())) {
      orderByVisitor = new OracleOrderByStatVisitor(x);
    } else {
      orderByVisitor = new OrderByStatVisitor(x);
    }
    return orderByVisitor;
  }

  public boolean visit(SQLBinaryOpExpr x) {
    x.getLeft().setParent(x);
    x.getRight().setParent(x);

    switch (x.getOperator()) {
      case Equality:
      case NotEqual:
      case GreaterThan:
      case GreaterThanOrEqual:
      case LessThan:
      case LessThanOrGreater:
      case LessThanOrEqual:
      case LessThanOrEqualOrGreaterThan:
      case Like:
      case NotLike:
      case Is:
      case IsNot:
        handleCondition(x.getLeft(), x.getOperator().name, x.getRight());
        handleCondition(x.getRight(), x.getOperator().name, x.getLeft());
        break;
      default:
        break;
    }
    return true;
  }

  protected void handleCondition(SQLExpr expr, String operator, List<SQLExpr> values) {
    handleCondition(expr, operator, values.toArray(new SQLExpr[values.size()]));
  }

  protected void handleCondition(SQLExpr expr, String operator, SQLExpr... valueExprs) {
    if (expr instanceof SQLCastExpr) {
      expr = ((SQLCastExpr) expr).getExpr();
    }

    Column column = getColumn(expr);
    if (column == null) {
      return;
    }

    Condition condition = null;
    for (Condition item : parseResult.getConditions()) {
      if (item.getColumn().equals(column) && item.getOperator().equals(operator)) {
        condition = item;
        break;
      }
    }

    if (condition == null) {
      condition = new Condition();
      condition.setColumn(column);
      condition.setOperator(operator);
      parseResult.getConditions().add(condition);
    }

    for (SQLExpr item : valueExprs) {
      Column valueColumn = getColumn(item);
      if (valueColumn != null) {
        continue;
      }
      condition.getValues().add(item);
    }
  }

  public String getDbType() {
    return null;
  }

  protected Column getColumn(SQLExpr expr) {
    if (parseResult.getAliasMap() == null) {
      return null;
    }

    if (expr instanceof SQLMethodInvokeExpr) {
      SQLMethodInvokeExpr methodInvokeExp = (SQLMethodInvokeExpr) expr;
      if (methodInvokeExp.getParameters().size() == 1) {
        SQLExpr firstExpr = methodInvokeExp.getParameters().get(0);
        return getColumn(firstExpr);
      }
    }

    if (expr instanceof SQLCastExpr) {
      expr = ((SQLCastExpr) expr).getExpr();
    }

    if (expr instanceof SQLPropertyExpr) {
      SQLExpr owner = ((SQLPropertyExpr) expr).getOwner();
      String column = ((SQLPropertyExpr) expr).getName();

      if (owner instanceof SQLIdentifierExpr) {
        String tableName = ((SQLIdentifierExpr) owner).getName();
        String table = tableName;
        String tableNameLower = tableName.toLowerCase();

        if (parseResult.getAliasMap().containsKey(tableNameLower)) {
          table = parseResult.getAliasMap().get(tableNameLower);
        }

        if (containsSubQuery(tableNameLower)) {
          table = null;
        }

        if (parseResult.getVariants().containsKey(table)) {
          return null;
        }

        if (table != null) {
          return new Column(table, column);
        }

        return handleSubQueryColumn(tableName, column);
      }

      return null;
    }

    if (expr instanceof SQLIdentifierExpr) {
      Column attrColumn = (Column) expr.getAttribute(ATTR_COLUMN);
      if (attrColumn != null) {
        return attrColumn;
      }

      String column = ((SQLIdentifierExpr) expr).getName();
      String table = getCurrentTable();
      if (table != null && parseResult.getAliasMap().containsKey(table)) {
        table = parseResult.getAliasMap().get(table);
        if (table == null) {
          return null;
        }
      }

      if (table != null) {
        return new Column(table, column);
      }

      if (parseResult.getVariants().containsKey(column)) {
        return null;
      }

      return new Column("UNKNOWN", column);
    }

    if (expr instanceof SQLBetweenExpr) {
      SQLBetweenExpr betweenExpr = (SQLBetweenExpr) expr;

      String column = "";
      String table = getCurrentTable();
      if (betweenExpr.getTestExpr() instanceof SQLPropertyExpr) {
        SQLPropertyExpr propertyExpr = (SQLPropertyExpr) betweenExpr.getTestExpr();
        column = propertyExpr.getName();
        table = parseResult.getAliasMap().get(propertyExpr.getOwner().toString());
      } else if (betweenExpr.getTestExpr() instanceof SQLIdentifierExpr) {
        column = ((SQLIdentifierExpr) betweenExpr.getTestExpr()).getName();
      }

      if (parseResult.getAliasMap().containsKey(table)) {
        table = parseResult.getAliasMap().get(table);
      }

      if (parseResult.getVariants().containsKey(table)) {
        return null;
      }

      if (table != null) {
        return new Column(table, column);
      }

      return handleSubQueryColumn(table, column);
    }

    return null;
  }

  @Override
  public boolean visit(SQLTruncateStatement x) {
    setMode(x, Mode.Delete);

    parseResult.setAliasMap();

    String originalTable = getCurrentTable();

    for (SQLExprTableSource tableSource : x.getTableSources()) {
      SQLName name = (SQLName) tableSource.getExpr();

      String ident = name.toString();
      setCurrentTable(ident);
      x.putAttribute("_old_local_", originalTable);

      addTable(ident, tableSource);

      putAliasMap(parseResult.getAliasMap(), ident, ident);
    }

    return false;
  }

  @Override
  public boolean visit(SQLDropViewStatement x) {
    setMode(x, Mode.Drop);
    return true;
  }

  @Override
  public boolean visit(SQLDropTableStatement x) {
    setMode(x, Mode.Insert);

    parseResult.setAliasMap();

    String originalTable = getCurrentTable();

    for (SQLExprTableSource tableSource : x.getTableSources()) {
      SQLName name = (SQLName) tableSource.getExpr();
      String ident = name.toString();
      setCurrentTable(ident);
      x.putAttribute("_old_local_", originalTable);

      addTable(ident, tableSource);

      putAliasMap(parseResult.getAliasMap(), ident, ident);
    }

    return false;
  }

  @Override
  public boolean visit(SQLInsertStatement x) {
    parseResult.setOperate(Operate.DML);
    setMode(x, Mode.Insert);

    parseResult.setAliasMap();

    String originalTable = getCurrentTable();

    if (x.getTableName() instanceof SQLName) {
      String ident = x.getTableName().toString();
      setCurrentTable(ident);
      x.putAttribute("_old_local_", originalTable);

      addTable(ident, x.getTableSource());

      Map<String, String> aliasMap = parseResult.getAliasMap();
      putAliasMap(aliasMap, x.getAlias(), ident);
      putAliasMap(aliasMap, ident, ident);
    }

    accept(x.getColumns());
    accept(x.getQuery());

    addInsertPairs(x);

    return false;
  }

  protected void addInsertPairs(SQLInsertStatement x) {
    if (x == null) {
      return;
    }
    for (int i = 0; i < x.getColumns().size(); i++) {
      handleCondition(x.getColumns().get(i), SQLBinaryOperator.Equality.name, x.getValues().getValues().get(i));
    }
  }

  protected static void putAliasMap(Map<String, String> aliasMap, String name, String value) {
    if (aliasMap == null || name == null) {
      return;
    }
    aliasMap.put(name.toLowerCase(), value);
  }

  protected void accept(SQLObject x) {
    if (x != null) {
      x.accept(this);
    }
  }

  protected void accept(List<? extends SQLObject> nodes) {
    for (int i = 0, size = nodes.size(); i < size; ++i) {
      accept(nodes.get(i));
    }
  }

  @Override
  public boolean visit(SQLSelectQueryBlock x) {
    if (x.getFrom() == null) {
      return false;
    }

    setMode(x, Mode.Select);

    if (x.getFrom() instanceof SQLSubqueryTableSource) {
      x.getFrom().accept(this);
      return false;
    }

    if (x.getInto() != null && x.getInto().getExpr() instanceof SQLName) {
      SQLName into = (SQLName) x.getInto().getExpr();
      String ident = into.toString();
      addTable(ident, x.getInto());
    }

    String originalTable = getCurrentTable();

    if (x.getFrom() instanceof SQLExprTableSource) {
      SQLExprTableSource tableSource = (SQLExprTableSource) x.getFrom();
      if (tableSource.getExpr() instanceof SQLName) {
        String ident = tableSource.getExpr().toString();

        setCurrentTable(x, ident);
        x.putAttribute(ATTR_TABLE, ident);
        if (x.getParent() instanceof SQLSelect) {
          x.getParent().putAttribute(ATTR_TABLE, ident);
        }
        x.putAttribute("_old_local_", originalTable);
      }
    }

    if (x.getFrom() != null) {
      x.getFrom().accept(this); // 提前执行，获得aliasMap
      String table = (String) x.getFrom().getAttribute(ATTR_TABLE);
      if (table != null) {
        x.putAttribute(ATTR_TABLE, table);
      }
    }

    if (x.getWhere() != null) {
      x.getWhere().setParent(x);
    }

    if(!x.getSelectList().isEmpty()){
      handleSelectList(x);
    }

    //Distinct 语法支持
    if (SQLSetQuantifier.DISTINCT == x.getDistionOption()){
      parseResult.setDistinct(true);
    }

    return true;
  }

  public void endVisit(SQLSelectQueryBlock x) {
    String originalTable = (String) x.getAttribute("_old_local_");
    x.putAttribute("table", getCurrentTable());
    setCurrentTable(originalTable);

    setModeOrigin(x);
  }

  public boolean visit(SQLJoinTableSource x) {
    x.getLeft().accept(this);
    x.getRight().accept(this);
    SQLExpr condition = x.getCondition();
    if (condition != null) {
      condition.accept(this);
    }
    return false;
  }

  public boolean visit(SQLPropertyExpr x) {
    if (x.getOwner() instanceof SQLIdentifierExpr) {
      String owner = ((SQLIdentifierExpr) x.getOwner()).getName();

      if (containsSubQuery(owner)) {
        return false;
      }

      owner = aliasWrap(owner);

      if (owner != null) {
        Column column = addColumn(owner, x.getName());
        x.putAttribute(ATTR_COLUMN, column);
        if (column != null) {
          if (isParentGroupBy(x)) {
            /** TODO 临时解决 Upper */
            GroupByColumn groupByColumn = new GroupByColumn(Optional.of(owner), x.getName().toUpperCase(), OrderByType.ASC);
            parseResult.getGroupByColumns().add(groupByColumn);
          }
          setColumn(x, column);
        }
      }
    }
    return false;
  }

  protected String aliasWrap(String name) {
    Map<String, String> aliasMap = parseResult.getAliasMap();

    if (aliasMap != null) {
      if (aliasMap.containsKey(name)) {
        return aliasMap.get(name);
      }

      String name_lcase = name.toLowerCase();
      if (name_lcase != name && aliasMap.containsKey(name_lcase)) {
        return aliasMap.get(name_lcase);
      }

//            for (Map.Entry<String, String> entry : aliasMap.entrySet()) {
//                if (entry.getKey() == null) {
//                    continue;
//                }
//                if (entry.getKey().equalsIgnoreCase(name)) {
//                    return entry.getValue();
//                }
//            }
    }

    return name;
  }

  protected Column handleSubQueryColumn(String owner, String alias) {
    SQLObject query = getSubQuery(owner);

    if (query == null) {
      return null;
    }

    return handleSubQueryColumn(alias, query);
  }

  protected Column handleSubQueryColumn(String alias, SQLObject query) {
    if (query instanceof SQLSelect) {
      query = ((SQLSelect) query).getQuery();
    }

    SQLSelectQueryBlock queryBlock = null;
    List<SQLSelectItem> selectList = null;
    if (query instanceof SQLSelectQueryBlock) {
      queryBlock = (SQLSelectQueryBlock) query;

      if (queryBlock.getGroupBy() != null) {
        return null;
      }

      selectList = queryBlock.getSelectList();
    }

    if (selectList != null) {
      boolean allColumn = false;
      String allColumnOwner = null;
      for (SQLSelectItem item : selectList) {
        if (!item.getClass().equals(SQLSelectItem.class)) {
          continue;
        }

        String itemAlias = item.getAlias();
        SQLExpr itemExpr = item.getExpr();

        String ident = itemAlias;
        if (itemAlias == null) {
          if (itemExpr instanceof SQLIdentifierExpr) {
            ident = itemAlias = itemExpr.toString();
          } else if (itemExpr instanceof SQLPropertyExpr) {
            itemAlias = ((SQLPropertyExpr) itemExpr).getName();
            ident = itemExpr.toString();
          }
        }

        if (alias.equalsIgnoreCase(itemAlias)) {
          Column column = (Column) itemExpr.getAttribute(ATTR_COLUMN);
          if (column != null) {
            return column;
          } else {
            SQLTableSource from = queryBlock.getFrom();
            if (from instanceof SQLSubqueryTableSource) {
              SQLSelect select = ((SQLSubqueryTableSource) from).getSelect();
              Column subQueryColumn = handleSubQueryColumn(ident, select);
              return subQueryColumn;
            }
          }
        }

        if (itemExpr instanceof SQLAllColumnExpr) {
          allColumn = true;
        } else if (itemExpr instanceof SQLPropertyExpr) {
          SQLPropertyExpr propertyExpr = (SQLPropertyExpr) itemExpr;
          if (propertyExpr.getName().equals("*")) {
            SQLExpr owner = propertyExpr.getOwner();
            if (owner instanceof SQLIdentifierExpr) {
              allColumnOwner = ((SQLIdentifierExpr) owner).getName();
              allColumn = true;
            }
          }
        }
      }

      if (allColumn) {

        SQLTableSource from = queryBlock.getFrom();
        String tableName = getTable(from, allColumnOwner);
        if (tableName != null) {
          return new Column(tableName, alias);
        }
      }
    }

    return null;
  }

  private String getTable(SQLTableSource from, String tableAlias) {
    if (from instanceof SQLExprTableSource) {
      boolean aliasEq = StringUtils.equals(from.getAlias(), tableAlias);
      SQLExpr tableSourceExpr = ((SQLExprTableSource) from).getExpr();
      if (tableSourceExpr instanceof SQLName) {
        String tableName = ((SQLName) tableSourceExpr).toString();
        if (tableAlias == null || aliasEq) {
          return tableName;
        }
      }
    } else if (from instanceof SQLJoinTableSource) {
      SQLJoinTableSource joinTableSource = (SQLJoinTableSource) from;
      String leftMatchTableName = getTable(joinTableSource.getLeft(), tableAlias);
      if (leftMatchTableName != null) {
        return leftMatchTableName;
      }

      return getTable(joinTableSource.getRight(), tableAlias);
    }

    return null;
  }

  public boolean visit(SQLIdentifierExpr x) {
    String currentTable = getCurrentTable();

    if (containsSubQuery(currentTable)) {
      return false;
    }

    String ident = x.toString();

    if (parseResult.getVariants().containsKey(ident)) {
      return false;
    }

    Column column = null;
    if (currentTable != null) {
      column = addColumn(currentTable, ident);

      if (column != null && isParentGroupBy(x)) {
        Optional alias = parseResult.getAliasMap().containsKey(ident) ? Optional.of(parseResult.getAliasMap().get(ident)) : Optional.absent();
        /** TODO 临时解决 Upper */
        GroupByColumn groupByColumn = new GroupByColumn(Optional.of(currentTable), ident.toUpperCase(), alias, OrderByType.ASC);
        parseResult.getGroupByColumns().add(groupByColumn);
      }
      x.putAttribute(ATTR_COLUMN, column);
    } else {
      boolean skip = false;
      for (SQLObject parent = x.getParent(); parent != null; parent = parent.getParent()) {
        if (parent instanceof SQLSelectQueryBlock) {
          SQLTableSource from = ((SQLSelectQueryBlock) parent).getFrom();

          if (from instanceof OdpsValuesTableSource) {
            skip = true;
            break;
          }
        } else if (parent instanceof SQLSelectQuery) {
          break;
        }
      }
      if (!skip) {
        column = handleUnkownColumn(ident);
      }
      if (column != null) {
        x.putAttribute(ATTR_COLUMN, column);
      }
    }
    if (column != null) {
      SQLObject parent = x.getParent();
      if (parent instanceof SQLPrimaryKey) {
        column.setPrimaryKey(true);
      } else if (parent instanceof SQLUnique) {
        column.setUnique(true);
      }

      setColumn(x, column);
    }
    return false;
  }

  private boolean isParentSelectItem(SQLObject parent) {
    if (parent == null) {
      return false;
    }

    if (parent instanceof SQLSelectItem) {
      return true;
    }

    if (parent instanceof SQLSelectQueryBlock) {
      return false;
    }

    return isParentSelectItem(parent.getParent());
  }

  private boolean isParentGroupBy(SQLObject parent) {
    if (parent == null) {
      return false;
    }

    if (parent instanceof SQLSelectGroupByClause) {
      return true;
    }

    return isParentGroupBy(parent.getParent());
  }

  private void setColumn(SQLExpr x, Column column) {
    SQLObject current = x;
    for (; ; ) {
      SQLObject parent = current.getParent();

      if (parent == null) {
        break;
      }

      if (parent instanceof SQLSelectQueryBlock) {
        SQLSelectQueryBlock query = (SQLSelectQueryBlock) parent;
        if (query.getWhere() == current) {
          column.setWhere(true);
        }
        break;
      }

      if (parent instanceof SQLSelectGroupByClause) {
        SQLSelectGroupByClause groupBy = (SQLSelectGroupByClause) parent;
        if (current == groupBy.getHaving()) {
          column.setHaving(true);
        } else if (groupBy.getItems().contains(current)) {
          column.setGroupBy(true);
        }
        break;
      }

      if (isParentSelectItem(parent)) {
        column.setSelec(true);
        break;
      }

      if (parent instanceof SQLJoinTableSource) {
        SQLJoinTableSource join = (SQLJoinTableSource) parent;
        if (join.getCondition() == current) {
          column.setJoin(true);
        }
        break;
      }

      current = parent;
    }
  }

  protected Column handleUnkownColumn(String columnName) {
    return addColumn("UNKNOWN", columnName);
  }

  public boolean visit(SQLAllColumnExpr x) {
    String currentTable = getCurrentTable();

    if (containsSubQuery(currentTable)) {
      return false;
    }

    if (x.getParent() instanceof SQLAggregateExpr) {
      SQLAggregateExpr aggregateExpr = (SQLAggregateExpr) x.getParent();
      if ("count".equalsIgnoreCase(aggregateExpr.getMethodName())) {
        return false;
      }
    }

    if (currentTable != null) {
      Column column = addColumn(currentTable, "*");
      if (isParentSelectItem(x.getParent())) {
        column.setSelec(true);
      }
    }
    return false;
  }

  public boolean visit(SQLSelectStatement x) {
    parseResult.setOperate(Operate.DML);
    parseResult.setAliasMap();
    return true;
  }

  public void endVisit(SQLSelectStatement x) {
  }

  @Override
  public boolean visit(SQLWithSubqueryClause.Entry x) {
    String alias = x.getName().toString();
    Map<String, String> aliasMap = parseResult.getAliasMap();
    SQLWithSubqueryClause with = (SQLWithSubqueryClause) x.getParent();

    if (Boolean.TRUE == with.getRecursive()) {

      if (aliasMap != null && alias != null) {
        putAliasMap(aliasMap, alias, null);
        addSubQuery(alias, x.getSubQuery().getQuery());
      }

      x.getSubQuery().accept(this);
    } else {
      x.getSubQuery().accept(this);

      if (aliasMap != null && alias != null) {
        putAliasMap(aliasMap, alias, null);
        addSubQuery(alias, x.getSubQuery().getQuery());
      }
    }

    return false;
  }

  public boolean visit(SQLSubqueryTableSource x) {
    x.getSelect().accept(this);

    SQLSelectQuery query = x.getSelect().getQuery();

    Map<String, String> aliasMap = parseResult.getAliasMap();
    if (aliasMap != null && x.getAlias() != null) {
      putAliasMap(aliasMap, x.getAlias(), null);
      addSubQuery(x.getAlias(), query);
    }
    return false;
  }

  protected void addSubQuery(String alias, SQLObject query) {
    String alias_lcase = alias.toLowerCase();
    parseResult.getSubQueryMap().put(alias_lcase, query);
  }

  protected SQLObject getSubQuery(String alias) {
    String alias_lcase = alias.toLowerCase();
    return parseResult.getSubQueryMap().get(alias_lcase);
  }

  protected boolean containsSubQuery(String alias) {
    if (alias == null) {
      return false;
    }

    String alias_lcase = alias.toLowerCase();
    return parseResult.getSubQueryMap().containsKey(alias_lcase);
  }

  protected boolean isSimpleExprTableSource(SQLExprTableSource x) {
    return x.getExpr() instanceof SQLName;
  }

  public boolean visit(SQLExprTableSource x) {
    if (isSimpleExprTableSource(x)) {
      String ident = x.getExpr().toString();

      if (parseResult.getVariants().containsKey(ident)) {
        return false;
      }

      if (containsSubQuery(ident)) {
        return false;
      }

      Map<String, String> aliasMap = parseResult.getAliasMap();

      addTable(ident, x);

      if (aliasMap != null) {
        String alias = x.getAlias();
        if (alias != null && !aliasMap.containsKey(alias)) {
          putAliasMap(aliasMap, alias, ident);
        }
        if (!aliasMap.containsKey(ident)) {
          putAliasMap(aliasMap, ident, ident);
        }
      }
    } else {
      accept(x.getExpr());
    }

    return false;
  }

  public boolean visit(SQLSelectItem x) {
    x.getExpr().accept(this);

    String alias = x.getAlias();

    Map<String, String> aliasMap = parseResult.getAliasMap();
    if (alias != null && (!alias.isEmpty()) && aliasMap != null) {
      if (x.getExpr() instanceof SQLName) {
        putAliasMap(aliasMap, alias, x.getExpr().toString());
      } else {
        putAliasMap(aliasMap, alias, null);
      }
    }

    return false;
  }

  public void endVisit(SQLSelect x) {
    restoreCurrentTable(x);
  }

  public boolean visit(SQLSelect x) {
    setCurrentTable(x);

    if (x.getOrderBy() != null) {
      x.getOrderBy().setParent(x);
    }

    accept(x.getWithSubQuery());
    accept(x.getQuery());

    String originalTable = getCurrentTable();

    setCurrentTable((String) x.getQuery().getAttribute("table"));
    x.putAttribute("_old_local_", originalTable);

    accept(x.getOrderBy());

    setCurrentTable(originalTable);

    return false;
  }

  public boolean visit(SQLAggregateExpr x) {
    parseResult.getAggregateFunctions().add(x);

    accept(x.getArguments());
    accept(x.getWithinGroup());
    accept(x.getOver());
    return false;
  }

  public boolean visit(SQLUpdateStatement x) {
    parseResult.setOperate(Operate.DML);
    parseResult.setAliasMap();

    setMode(x, Mode.Update);

    SQLName identName = x.getTableName();
    if (identName != null) {
      String ident = identName.toString();
      setCurrentTable(ident);

      if (x.getTableSource() instanceof SQLExprTableSource) {
        SQLExprTableSource exprTableSource = (SQLExprTableSource) x.getTableSource();
        addTable(ident, exprTableSource);
      }

      Map<String, String> aliasMap = parseResult.getAliasMap();
      putAliasMap(aliasMap, ident, ident);
    } else {
      x.getTableSource().accept(this);
    }

    accept(x.getFrom());

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

  public boolean visit(SQLDeleteStatement x) {
    parseResult.setOperate(Operate.DML);
    parseResult.setAliasMap();

    setMode(x, Mode.Delete);

    String tableName = x.getTableName().toString();
    setCurrentTable(tableName);

    if (x.getAlias() != null) {
      putAliasMap(parseResult.getAliasMap(), x.getAlias(), tableName);
    }

    if (x.getTableSource() instanceof SQLSubqueryTableSource) {
      SQLSelectQuery selectQuery = ((SQLSubqueryTableSource) x.getTableSource()).getSelect()
          .getQuery();
      if (selectQuery instanceof SQLSelectQueryBlock) {
        SQLSelectQueryBlock subQueryBlock = ((SQLSelectQueryBlock) selectQuery);
        subQueryBlock.getWhere().accept(this);
      }
    }

    if (x.getTableSource() instanceof SQLExprTableSource) {
      SQLExprTableSource exprTableSource = (SQLExprTableSource) x.getTableSource();
      addTable(tableName, exprTableSource);
    }

    accept(x.getWhere());

    return false;
  }

  public boolean visit(SQLInListExpr x) {
    if (x.isNot()) {
      handleCondition(x.getExpr(), "NOT IN", x.getTargetList());
    } else {
      handleCondition(x.getExpr(), "IN", x.getTargetList());
    }

    return true;
  }

  @Override
  public boolean visit(SQLInSubQueryExpr x) {
    if (x.isNot()) {
      handleCondition(x.getExpr(), "NOT IN");
    } else {
      handleCondition(x.getExpr(), "IN");
    }
    return true;
  }

  public void endVisit(SQLDeleteStatement x) {

  }

  public void endVisit(SQLUpdateStatement x) {
  }

  public boolean visit(SQLCreateTableStatement x) {
    parseResult.setOperate(Operate.DDL);
    for (SQLTableElement e : x.getTableElementList()) {
      e.setParent(x);
    }

    String tableName = x.getName().toString();

    addTable(tableName, x.getTableSource());
    setCurrentTable(x, tableName);

    accept(x.getTableElementList());

    restoreCurrentTable(x);

    if (x.getInherits() != null) {
      x.getInherits().accept(this);
    }

    if (x.getSelect() != null) {
      x.getSelect().accept(this);
    }

    return false;
  }

  public boolean visit(SQLColumnDefinition x) {
    String tableName = null;
    {
      SQLObject parent = x.getParent();
      if (parent instanceof SQLCreateTableStatement) {
        tableName = ((SQLCreateTableStatement) parent).getName().toString();
      }
    }

    if (tableName == null) {
      return true;
    }

    String columnName = x.getName().toString();
    Column column = addColumn(tableName, columnName);
    if (x.getDataType() != null) {
      column.setDataType(x.getDataType().getName());
    }

    for (SQLColumnConstraint item : x.getConstraints()) {
      if (item instanceof SQLPrimaryKey) {
        column.setPrimaryKey(true);
      } else if (item instanceof SQLUnique) {
        column.setUnique(true);
      }
    }

    return false;
  }

  @Override
  public boolean visit(SQLCallStatement x) {
    parseResult.setOperate(Operate.DML);
    return false;
  }

  @Override
  public void endVisit(SQLCommentStatement x) {

  }

  @Override
  public boolean visit(SQLCommentStatement x) {
    return false;
  }

  public boolean visit(SQLCurrentOfCursorExpr x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableAddColumn x) {
    parseResult.setOperate(Operate.DDL);
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String table = stmt.getName().toString();

    for (SQLColumnDefinition column : x.getColumns()) {
      String columnName = column.getName().toString();
      addColumn(table, columnName);
    }
    return false;
  }

  @Override
  public void endVisit(SQLAlterTableAddColumn x) {

  }

  @Override
  public boolean visit(SQLRollbackStatement x) {
    parseResult.setOperate(Operate.DCL);
    return false;
  }

  public boolean visit(SQLCreateViewStatement x) {
    parseResult.setOperate(Operate.DDL);
    x.getSubQuery().accept(this);
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDropForeignKey x) {
    return false;
  }

  @Override
  public boolean visit(SQLUseStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDisableConstraint x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableEnableConstraint x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableStatement x) {
    parseResult.setOperate(Operate.DDL);
    String tableName = x.getName().toString();
    addTable(tableName, x.getTableSource());

    setCurrentTable(x, tableName);

    for (SQLAlterTableItem item : x.getItems()) {
      item.setParent(x);
      item.accept(this);
    }

    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDropConstraint x) {
    return false;
  }

  @Override
  public boolean visit(SQLDropIndexStatement x) {
    setMode(x, Mode.DropIndex);
    SQLExprTableSource table = x.getTableName();
    if (table != null) {
      SQLName name = (SQLName) table.getExpr();

      String ident = name.toString();
      setCurrentTable(ident);

      addTable(ident, table);

      putAliasMap(parseResult.getAliasMap(), ident, ident);
    }
    return false;
  }

  @Override
  public boolean visit(SQLCreateIndexStatement x) {
    parseResult.setOperate(Operate.DDL);
    setMode(x, Mode.CreateIndex);

    SQLName name = (SQLName) ((SQLExprTableSource) x.getTable()).getExpr();

    String table = name.toString();
    setCurrentTable(table);

    addTable(table, (SQLExprTableSource) x.getTable());

    putAliasMap(parseResult.getAliasMap(), table, table);

    for (SQLSelectOrderByItem item : x.getItems()) {
      SQLExpr expr = item.getExpr();
      if (expr instanceof SQLIdentifierExpr) {
        SQLIdentifierExpr identExpr = (SQLIdentifierExpr) expr;
        String columnName = identExpr.getName();
        addColumn(table, columnName);
      }
    }

    return false;
  }

  @Override
  public boolean visit(SQLForeignKeyImpl x) {

    for (SQLName column : x.getReferencingColumns()) {
      column.accept(this);
    }

    String table = x.getReferencedTableName().getSimpleName();
    setCurrentTable(table);

    addTable(table);
    for (SQLName column : x.getReferencedColumns()) {
      String columnName = column.getSimpleName();
      addColumn(table, columnName);
    }

    return false;
  }

  @Override
  public boolean visit(SQLDropSequenceStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLDropTriggerStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLDropUserStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLGrantStatement x) {
    if (x.getOn() != null && (x.getObjectType() == null
        || x.getObjectType() == SQLObjectType.TABLE)) {
      x.getOn().accept(this);
    }
    return false;
  }

  @Override
  public boolean visit(SQLRevokeStatement x) {
    if (x.getOn() != null) {
      x.getOn().accept(this);
    }
    return false;
  }

  @Override
  public boolean visit(SQLDropDatabaseStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableAddIndex x) {
    parseResult.setOperate(Operate.DDL);
    for (SQLSelectOrderByItem item : x.getItems()) {
      item.accept(this);
    }

    String table = ((SQLAlterTableStatement) x.getParent()).getName().toString();
    addTable(table, ((SQLAlterTableStatement) x.getParent()).getTableSource());
    return false;
  }

  public boolean visit(SQLCheck x) {
    x.getExpr().accept(this);
    return false;
  }

  public boolean visit(SQLCreateTriggerStatement x) {
    return false;
  }

  public boolean visit(SQLDropFunctionStatement x) {
    return false;
  }

  public boolean visit(SQLDropTableSpaceStatement x) {
    return false;
  }

  public boolean visit(SQLDropProcedureStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableRename x) {
    return false;
  }

  @Override
  public boolean visit(SQLArrayExpr x) {
    accept(x.getValues());

    SQLExpr exp = x.getExpr();
    if (exp instanceof SQLIdentifierExpr) {
      if (((SQLIdentifierExpr) exp).getName().equals("ARRAY")) {
        return false;
      }
    }
    exp.accept(this);
    return false;
  }

  @Override
  public boolean visit(SQLOpenStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLFetchStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLCloseStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLCreateProcedureStatement x) {
    parseResult.setOperate(Operate.DDL);
    String name = x.getName().toString();
    parseResult.getVariants().put(name, x);
    accept(x.getBlock());
    return false;
  }

  @Override
  public boolean visit(SQLBlockStatement x) {
    for (SQLParameter param : x.getParameters()) {
      param.setParent(x);

      SQLExpr name = param.getName();
      parseResult.getVariants().put(name.toString(), name);
    }
    return true;
  }

  @Override
  public boolean visit(SQLShowTablesStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLDeclareItem x) {
    return false;
  }

  @Override
  public boolean visit(SQLPartitionByHash x) {
    return false;
  }

  @Override
  public boolean visit(SQLPartitionByRange x) {
    return false;
  }

  @Override
  public boolean visit(SQLPartitionByList x) {
    return false;
  }

  @Override
  public boolean visit(SQLPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLSubPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLSubPartitionByHash x) {
    return false;
  }

  @Override
  public boolean visit(SQLPartitionValue x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterDatabaseStatement x) {
    return true;
  }

  @Override
  public boolean visit(SQLAlterTableConvertCharSet x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDropPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableReOrganizePartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableCoalescePartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableTruncatePartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDiscardPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableImportPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableAnalyzePartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableCheckPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableOptimizePartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableRebuildPartition x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableRepairPartition x) {
    return false;
  }

  public boolean visit(SQLSequenceExpr x) {
    return false;
  }

  @Override
  public boolean visit(SQLMergeStatement x) {
    parseResult.setAliasMap();

    String originalTable = getCurrentTable();

    setMode(x.getUsing(), Mode.Select);
    x.getUsing().accept(this);

    setMode(x, Mode.Merge);

    String ident = x.getInto().toString();
    setCurrentTable(x, ident);
    x.putAttribute("_old_local_", originalTable);

    addTable(ident, (SQLExprTableSource) x.getInto());

    Map<String, String> aliasMap = parseResult.getAliasMap();
    if (aliasMap != null) {
      if (x.getAlias() != null) {
        putAliasMap(aliasMap, x.getAlias(), ident);
      }
      putAliasMap(aliasMap, ident, ident);
    }

    x.getOn().accept(this);

    if (x.getUpdateClause() != null) {
      x.getUpdateClause().accept(this);
    }

    if (x.getInsertClause() != null) {
      x.getInsertClause().accept(this);
    }

    return false;
  }

  @Override
  public boolean visit(SQLSetStatement x) {
    return false;
  }

  public boolean visit(SQLCreateSequenceStatement x) {
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableAddConstraint x) {
    SQLConstraint constraint = x.getConstraint();
    if (constraint instanceof SQLUniqueConstraint) {
      SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
      String tableName = stmt.getName().toString();

      addTable(tableName, stmt.getTableSource());
    }
    return true;
  }

  @Override
  public boolean visit(SQLAlterTableDropIndex x) {
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String tableName = stmt.getName().toString();

    addTable(tableName, stmt.getTableSource());
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDropPrimaryKey x) {
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String tableName = stmt.getName().toString();

    addTable(tableName, stmt.getTableSource());
    return false;
  }

  @Override
  public boolean visit(SQLAlterTableDropKey x) {
    SQLAlterTableStatement stmt = (SQLAlterTableStatement) x.getParent();
    String tableName = stmt.getName().toString();

    addTable(tableName, stmt.getTableSource());
    return false;
  }

  @Override
  public boolean visit(SQLDescribeStatement x) {
    String tableName = x.getObject().toString();

    addTable(tableName);

    SQLName column = x.getColumn();
    if (column != null) {
      String columnName = column.toString();
      this.addColumn(tableName, columnName);
    }
    return false;
  }

  public boolean visit(SQLExplainStatement x) {
    if (x.getStatement() != null) {
      accept(x.getStatement());
    }

    return false;
  }

  @Override
  public boolean visit(SQLLimit x) {
//    int offset = ((SQLIntegerExpr) x.parseOffset()).getNumber().intValue();
//    int count = ((SQLIntegerExpr) x.parseRowCount()).getNumber().intValue();
    //fixme 这个地方的limit会不会覆盖注释的hint？
    parseResult.setLimit(new Limit(x.getOffset(), x.getRowCount()));
    return super.visit(x);
  }

  /**
   * 重写AVG
   * AVG语法在多分片执行时需要转换成sum和count，再由结果集做处理，否则结果不准确
   *
   * @param x
   */
  private void handleSelectList(SQLSelectQueryBlock x){
    List<SQLSelectItem> selectList = x.getSelectList();

    int size = selectList.size();
    for(int i =0; i<size; i++){
      parseResult.increaseItemIndex();
      SQLSelectItem selectItem = selectList.get(i);
      if  (selectItem.getExpr() instanceof SQLAllColumnExpr){
        parseResult.setAllColumn(true);
      }
      if (selectItem.getExpr() instanceof SQLAggregateExpr) {
        handleAggregate(selectList, i, selectItem);
      }
    }
  }

  private void handleAggregate(List<SQLSelectItem> selectList, int index, SQLSelectItem selectItem){
    SQLAggregateExpr expr = (SQLAggregateExpr) selectItem.getExpr();
    String method = expr.getMethodName();
    if(method.equalsIgnoreCase(AggregationType.AVG.name())){
      String colName = selectItem.getAlias();
      if(Strings.isNullOrEmpty(colName)){
        colName = AggregationType.AVG.name() + index;
        selectItem.setAlias(colName);
        /** TODO 临时解决 */
      }else{
        colName.toUpperCase();
      }
      /** TODO 临时解决 */

      SQLSelectItem count = new SQLSelectItem();
      String countColName = colName + "COUNT";
      count.setAlias(countColName);
      SQLAggregateExpr countExp = new SQLAggregateExpr("COUNT");
      this.copyProperties(expr, countExp);
      countExp.getArguments().addAll(expr.getArguments());
      countExp.setMethodName("COUNT");
      count.setExpr(countExp);
      selectList.add(count);

      SQLSelectItem sum =new SQLSelectItem();
      String sumColName = colName + "SUM";
      sum.setAlias(sumColName);
      SQLAggregateExpr sumExp =new SQLAggregateExpr("SUM");
      this.copyProperties(expr,sumExp);
      sumExp.getArguments().addAll(expr.getArguments());
      sumExp.setMethodName("SUM");
      sum.setExpr(sumExp);
      selectList.add(sum);

      AggregationColumn avgColumn = new AggregationColumn(colName.toLowerCase(), AggregationType.AVG, Optional.of(colName), Optional.<String>absent());
      AggregationColumn sumColumn = new AggregationColumn(sumColName.toLowerCase(), AggregationType.SUM, Optional.of(sumColName), Optional.<String>absent());
      AggregationColumn countColumn = new AggregationColumn(countColName.toLowerCase(), AggregationType.COUNT, Optional.of(countColName), Optional.<String>absent());
      avgColumn.getDerivedColumns().add(countColumn);
      avgColumn.getDerivedColumns().add(sumColumn);
      parseResult.getAggregationColumns().add(avgColumn);
    }else{
      StringBuilder sb = new StringBuilder();
      if(JdbcUtils.MYSQL.equals(parseResult.getDbType())){
        expr.accept(new MySqlOutputVisitor(sb));
      }else if (JdbcUtils.ORACLE.equals(parseResult.getDbType())){
        expr.accept(new OracleOutputVisitor(sb));
      }

      String expression = sb.toString();
      AggregationType aggregationType = AggregationType.valueOf(expr.getMethodName().toUpperCase());
      Optional alias =  Optional.fromNullable(selectItem.getAlias());
      Optional option = null == expr.getOption()? Optional.absent() : Optional.of(expr.getOption().toString());
      AggregationColumn aggColumn = new AggregationColumn(expression, aggregationType, alias, option, index+1);
      parseResult.getAggregationColumns().add(aggColumn);
    }
  }

  public static void copyProperties(Object fromObj, Object toObj) {
    Class<? extends Object> fromClass = fromObj.getClass();
    Class<? extends Object> toClass = toObj.getClass();

    try {
      BeanInfo fromBean = Introspector.getBeanInfo(fromClass);
      BeanInfo toBean = Introspector.getBeanInfo(toClass);

      PropertyDescriptor[] toPd = toBean.getPropertyDescriptors();
      List<PropertyDescriptor> fromPd = Arrays.asList(fromBean
          .getPropertyDescriptors());

      for (PropertyDescriptor propertyDescriptor : toPd) {
        propertyDescriptor.getDisplayName();
        PropertyDescriptor pd = fromPd.get(fromPd
            .indexOf(propertyDescriptor));
        if (pd.getDisplayName().equals(
            propertyDescriptor.getDisplayName())
            && !pd.getDisplayName().equals("class")
            && propertyDescriptor.getWriteMethod() != null) {
          propertyDescriptor.getWriteMethod().invoke(toObj, pd.getReadMethod().invoke(fromObj, null));
        }

      }
    } catch (IntrospectionException e) {
      throw  new RuntimeException(e);
    } catch (IllegalArgumentException e) {
      throw  new RuntimeException(e);
    } catch (IllegalAccessException e) {
      throw  new RuntimeException(e);
    } catch (InvocationTargetException e) {
      throw  new RuntimeException(e);
    }
  }
}
