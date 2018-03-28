package studio.raptor.ddal.core.parser.result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.ParserErrCodes;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;
import studio.raptor.ddal.core.parser.result.merger.GroupByColumn;
import studio.raptor.ddal.core.parser.result.merger.Limit;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.expr.SQLAggregateExpr;
import studio.raptor.sqlparser.ast.statement.SQLCallStatement;
import studio.raptor.sqlparser.ast.statement.SQLDDLStatement;
import studio.raptor.sqlparser.ast.statement.SQLDeleteStatement;
import studio.raptor.sqlparser.ast.statement.SQLExplainStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;
import studio.raptor.sqlparser.ast.statement.SQLGrantStatement;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.ast.statement.SQLRevokeStatement;
import studio.raptor.sqlparser.ast.statement.SQLRollbackStatement;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLSetStatement;
import studio.raptor.sqlparser.ast.statement.SQLShowTablesStatement;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;
import studio.raptor.sqlparser.ast.statement.SQLUseStatement;
import studio.raptor.sqlparser.dialect.mysql.ast.statement.MySqlCommitStatement;
import studio.raptor.sqlparser.stat.TableStat.Column;
import studio.raptor.sqlparser.stat.TableStat.Condition;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ParseResult {
  private final DatabaseType dbType;
  private Operate operate = Operate.UNSUPPORTED;//DML/DDL/DCL/DAL
  private final SQLStatementType sqlType;
  private final SQLStatement statement;
  private final Map<String, SQLExprTableSource> tableSources = new LinkedHashMap<>();
  private Set<String> tableNames = tableSources.keySet();
  private boolean isAllColumn = false;
  private final Map<Column, Column> columns = new LinkedHashMap<>();
  private final List<String> updateItems = new ArrayList<>();
  private final List<Condition> conditions = new ArrayList<>();
  private boolean distinct = false;
  private final List<OrderByColumn> orderByColumns = new ArrayList<>();
  private final List<GroupByColumn> groupByColumns = new ArrayList<>();
  private final List<SQLAggregateExpr> aggregateFunctions = new ArrayList<>();
  private final List<AggregationColumn> aggregationColumns = new ArrayList<>();
  private final Map<String, SQLObject> subQueryMap = new LinkedHashMap<>();
  private final Map<String, SQLObject> variants = new LinkedHashMap<>();
  private Map<String, String> aliasMap = new LinkedHashMap<>();
  private static final String PATTERN_DOT = Pattern.quote(".");
  private SequenceWrapper sequenceWrapper;
  private boolean handledAutoIncrement = false;

  private Limit limit;
  private Boolean hasLimit = false;


  private int itemIndex;

  public ParseResult(DatabaseType dbType){
    this.dbType = dbType;
    this.statement = null;
    this.sqlType = SQLStatementType.SEQUENCE;
    this.operate = Operate.DAL;
  }

  public ParseResult(DatabaseType dbType, SQLStatement statement){
    this.dbType = dbType;
    this.statement = statement;
    this.sqlType = suitSqlType();
  }

  public Operate getOperate() {
    return operate;
  }

  public void setOperate(Operate operate) {
    this.operate = operate;
  }

  public SQLStatementType getSqlType() {
    return sqlType;
  }

  public SQLStatement getStatement() {
    return statement;
  }

  public void setTableNames(Set<String> tableNames){
    this.tableNames = tableNames;
  }

  public Set<String> getTableNames() {
    return tableNames;
  }

  public Map<String, SQLExprTableSource> getTableSources() {
    return tableSources;
  }

  public void addTableSource(SQLExprTableSource tableSource){
    this.tableSources.put(
        ((SQLName) tableSource.getExpr()).getSimpleName(),
        tableSource);
  }

  public boolean isAllColumn() {
    return isAllColumn;
  }

  public void setAllColumn(boolean allColumn) {
    isAllColumn = allColumn;
  }

  public Map<Column, Column> getColumns() {
    return columns;
  }

  public boolean containsColumn(String tableName, String columnName) {
    return columns.containsKey(new Column(tableName, columnName));
  }

  public Column getColumn(String tableName, String columnName) {
    if (aliasMap != null && aliasMap.containsKey(columnName) && aliasMap.get(columnName) == null) {
      return null;
    }
    Column column = new Column(tableName, columnName);
    return this.columns.get(column);
  }

  public DatabaseType getDbType() {
    return dbType;
  }

  public List<String> getUpdateItems() {
    return updateItems;
  }

  public List<Condition> getConditions() {
    return conditions;
  }

  public boolean hasDistinct() {
    return distinct;
  }

  public void setDistinct(boolean distinct) {
    this.distinct = distinct;
  }

  public List<OrderByColumn> getOrderByColumns() {
    return orderByColumns;
  }

  public List<GroupByColumn> getGroupByColumns() {
    return groupByColumns;
  }

  public List<SQLAggregateExpr> getAggregateFunctions() {
    return aggregateFunctions;
  }

  public List<AggregationColumn> getAggregationColumns() {
    return aggregationColumns;
  }
  public boolean hasGroupByOrAggregation(){
    return !getGroupByColumns().isEmpty() || !aggregationColumns.isEmpty();
  }

  public Map<String, SQLObject> getSubQueryMap() {
    return subQueryMap;
  }

  public Map<String, SQLObject> getVariants() {
    return variants;
  }

  public Map<String, String> getAliasMap() {
    return aliasMap;
  }

  public void setAliasMap(Map<String, String> aliasMap) {
    this.aliasMap = aliasMap;
  }

  public void setAliasMap() {
    this.setAliasMap(new HashMap<String, String>());
  }

  public void clearAliasMap() {
    this.aliasMap = null;
  }

  public Limit getLimit() {
    return limit;
  }

  public void setLimit(Limit limit) {
    this.limit = limit;
    this.hasLimit = true;
  }

  public Boolean hasLimit() {
    return hasLimit;
  }

  public int getItemIndex() {
    return itemIndex;
  }

  /**
   * 增加查询投射项数量.
   */
  public void increaseItemIndex() {
    itemIndex++;
  }

  public SequenceWrapper getSequenceWrapper() {
    return sequenceWrapper;
  }

  public void setSequenceWrapper(SequenceWrapper sequenceWrapper) {
    this.sequenceWrapper = sequenceWrapper;
  }

  public boolean isHandledAutoIncrement() {
    return handledAutoIncrement;
  }

  public void setHandledAutoIncrement(boolean handledAutoIncrement) {
    this.handledAutoIncrement = handledAutoIncrement;
  }

  /**
   * 适配SQL类型
   * @return
   */
  private SQLStatementType suitSqlType() {
    if (statement instanceof SQLSelectStatement) {
      return SQLStatementType.SELECT;
    }
    if (statement instanceof SQLInsertStatement) {
      return SQLStatementType.INSERT;
    }
    if (statement instanceof SQLUpdateStatement) {
      return SQLStatementType.UPDATE;
    }
    if (statement instanceof SQLDeleteStatement) {
      return SQLStatementType.DELETE;
    }
    if (statement instanceof SQLCallStatement) {
      return SQLStatementType.CALL;
    }
    if (statement instanceof MySqlCommitStatement) {
      return SQLStatementType.COMMIT;
    }
    if (statement instanceof SQLRollbackStatement) {
      return SQLStatementType.ROLLBACK;
    }
    if (statement instanceof SQLExplainStatement) {
      return SQLStatementType.EXPLAIN;
    }
    if (statement instanceof SQLGrantStatement) {
      return SQLStatementType.GRANT;
    }
    if (statement instanceof SQLRevokeStatement) {
      return SQLStatementType.REVOKE;
    }
    if (statement instanceof SQLUseStatement) {
      return SQLStatementType.USE;
    }
    if (statement instanceof SQLShowTablesStatement) {
      return SQLStatementType.SHOW;
    }
    if (statement instanceof SQLSetStatement) {
      return SQLStatementType.SET;
    }
    if (statement instanceof SQLDDLStatement) {
      return SQLStatementType.DDL;
    }
    throw new GenericException(ParserErrCodes.PARSE_301, statement.toString());
  }
}
