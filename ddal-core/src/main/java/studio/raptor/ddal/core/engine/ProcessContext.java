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

package studio.raptor.ddal.core.engine;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.common.sql.SQLHintParser.SQLHint;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.engine.plan.PlanNodeChain;
import studio.raptor.ddal.core.executor.ExecutionGroup;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.merger.ResultDataMergeContext;
import studio.raptor.ddal.core.parser.builder.SqlBuilder;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.SQLStatementType;
import studio.raptor.ddal.core.router.result.RouteResult;
import studio.raptor.ddal.core.router.util.RouteCondition;
import studio.raptor.sqlparser.stat.TableStat.Condition;
//import studio.raptor.ddal.core.transaction.DefaultTransactionIdGenerator;
//import studio.raptor.ddal.core.transaction.TransactionIdGenerator;
//import studio.raptor.ddal.core.transaction.TransactionLogRecorder;

/**
 * Execute context for raptor ddal core. This context is designed to hold several
 * runtime objects listed below.
 * <ul>
 * <li>Physical database connection mapped to each virtual shard.</li>
 * <li>Original sql scripts received from app client.</li>
 * <li>Shard result of origin sql which is produced by RouteEngine.</li>
 * </ul>
 *
 * @author Sam
 * @since 1.0
 */
public class ProcessContext implements Closeable {

  /**
   * 虚拟数据库
   */
  private VirtualDb virtualDb;

  private String originSql;
  private SQLHint sqlHint;
  private boolean hasHint;
  private String sqlHintStr;
  private String trimmedHintSql; // 不包含注释的SQL
  private boolean autoCommit; // jdbc事务是否自动提交
  private boolean isPlanPersistable = false; // 是否要持久化执行计划
  private PlanNodeChain planInstance = null; // 执行计划
  private DatabaseType databaseType;
  private boolean isPreparedStatement;
  private List<Object> sqlParameters;

  private List<Table> shardTables = new ArrayList<>();
  private List<RouteCondition> routeConditions = new ArrayList<>();
  private ExecutionGroup currentExecutionGroup;
  private ResultDataMergeContext resultDataMergeContext;
  private SqlBuilder sqlBuilder;

  private ParseResult parseResult;
  private List<Condition> hintShardConditions = new ArrayList<>();
  private RouteResult routeResult;
  private List<ResultData> resultDataList;
  private ResultData mergedResult;

  /**
   * 索引表操作集合
   */
  private List<IndexTableOps> indexTableOpsList = new ArrayList<>();

  private String transactionId = null; //事务ID
  // fixme 不要删除
//  private TransactionIdGenerator txIdGenerator = new DefaultTransactionIdGenerator();
//  private TransactionLogRecorder txLogRecorder = new TransactionLogRecorder();

  /**
   * 执行过程中使用的所有后端物理连接。{shardName:BackendConnection}
   *
   * Concurrency issue fixed
   * 多个SQL执行单元共享此变量，所以存在多个执行单元并发执行时出现
   * 后端连接丢失从而造成连接池无法正常回收连接的问题。
   */
  private Map<String, ContextConnectionWrapper> shardBackendConnectionsWrapper = new ConcurrentHashMap<>();

  /**
   * a constant indicating whether auto-generated keys should be made available
   * for retrieval using the method <code>getGeneratedKeys</code>; one of the
   * following constants: <code>Statement.RETURN_GENERATED_KEYS</code> or
   * <code>Statement.NO_GENERATED_KEYS</code>
   */
  private boolean retrieveGeneratedKeys = false;

  public ProcessContext(DatabaseType databaseType, String virtualDbName) {
    this.databaseType = databaseType;
    this.virtualDb = ShardConfig.getInstance().getVirtualDb(virtualDbName);
    this.sqlParameters = new FastArrayList<>();
    this.indexTableOpsList = new FastArrayList<>();
  }

  public VirtualDb getVirtualDb() {
    return virtualDb;
  }

  /**
   * 获取当前执行SQL的类型。
   *
   * @return 被执行SQL的类型。
   */
  public SQLStatementType getSqlStatementType() {
    return this.parseResult.getSqlType();
  }

  public List<Table> getShardTables() {
    return shardTables;
  }

  public void setShardTables(List<Table> shardTables) {
    this.shardTables = shardTables;
  }

  public List<RouteCondition> getRouteConditions() {
    return routeConditions;
  }

  public void setRouteConditions(
      List<RouteCondition> routeConditions) {
    this.routeConditions = routeConditions;
  }

  public ExecutionGroup getCurrentExecutionGroup() {
    return currentExecutionGroup;
  }

  public void setCurrentExecutionGroup(
      ExecutionGroup currentExecutionGroup) {
    this.currentExecutionGroup = currentExecutionGroup;
  }

  public ResultDataMergeContext getResultDataMergeContext() {
    return resultDataMergeContext;
  }

  public void setResultDataMergeContext(
      ResultDataMergeContext resultDataMergeContext) {
    this.resultDataMergeContext = resultDataMergeContext;
  }

  /**
   * 设置当前执行上下文的事务提交方式。
   *
   * FALSE：表示当前上下文可执行多条语句直至连接使用者触发commit操作才会提交事务。
   * TRUE：每条sql都在单独的事务中执行。
   *
   * @param autoCommit true or false
   */
  public void setAutoCommit(boolean autoCommit) {
    this.autoCommit = autoCommit;
  }

  public boolean isAutoCommit() {
    return autoCommit;
  }

  public ParseResult getParseResult() {
    return parseResult;
  }

  public void setParseResult(ParseResult parseResult) {
    this.parseResult = parseResult;
  }

  public List<Condition> getHintShardConditions() {
    return hintShardConditions;
  }

  public Condition findHintShardCondition(String column){
    for (Condition condition : hintShardConditions){
      if(condition.getColumn().getName().equalsIgnoreCase(column)){
        return condition;
      }
    }
    return null;
  }

  public void setHintShardConditions(
      List<Condition> hintShardConditions) {
    this.hintShardConditions = hintShardConditions;
  }

  public String getOriginSql() {
    return originSql;
  }

  public void setOriginSql(String originSql) {
    this.originSql = originSql;
  }

  public SQLHint getSqlHint() {
    return sqlHint;
  }

  public void setSqlHint(SQLHint sqlHint) {
    this.sqlHint = sqlHint;
  }

  public String getSqlHintStr() {
    return sqlHintStr;
  }

  public boolean hasHint() {
    return hasHint;
  }

  public void setHasHint(boolean hasHint) {
    this.hasHint = hasHint;
  }

  public void setSqlHintStr(String sqlHintStr) {
    this.sqlHintStr = sqlHintStr;
  }

  public String getTrimmedHintSql() {
    return trimmedHintSql;
  }

  public void setTrimmedHintSql(String trimmedHintSql) {
    this.trimmedHintSql = trimmedHintSql;
  }

  public List<Object> getSqlParameters() {
    return sqlParameters;
  }

  public DatabaseType getDatabaseType() {
    return databaseType;
  }

  public ResultData getMergedResult() {
    return mergedResult;
  }

  public void setMergedResult(ResultData mergedResult) {
    this.mergedResult = mergedResult;
  }

  public Map<String, ContextConnectionWrapper> getShardBackendConnWrapper() {
    return shardBackendConnectionsWrapper;
  }

//  public boolean hasReadonlyHint() {
//    return hasReadonlyHint;
//  }
//
//  public void setHasReadonlyHint(boolean hasReadonlyHint) {
//    this.hasReadonlyHint = hasReadonlyHint;
//  }

  public RouteResult getRouteResult() {
    return routeResult;
  }

  public void setRouteResult(RouteResult routeResult) {
    this.routeResult = routeResult;
  }

  public List<ResultData> getResultDataList() {
    return resultDataList;
  }

  public void setResultDataList(List<ResultData> resultDataList) {
    this.resultDataList = resultDataList;
  }

  public boolean isRetrieveGeneratedKeys() {
    return retrieveGeneratedKeys;
  }

  public void setRetrieveGeneratedKeys(boolean retrieveGeneratedKeys) {
    this.retrieveGeneratedKeys = retrieveGeneratedKeys;
  }

  public boolean isPreparedStatement() {
    return isPreparedStatement;
  }

  public void setIsPreparedStatement(boolean preparedStatement) {
    isPreparedStatement = preparedStatement;
  }
// fixme 不要删除
//  public TransactionLogRecorder getTxLogRecorder() {
//    return txLogRecorder;
//  }

  public String getTransactionId() {
    return transactionId;
  }

  public void setTransactionId(String transactionId) {
    this.transactionId = transactionId;
  }
// fixme 不要删除
//  public TransactionIdGenerator getTxIdGenerator() {
//    return txIdGenerator;
//  }

  public boolean isPlanPersistable() {
    return isPlanPersistable;
  }

  public void setPlanPersistable(boolean planPersistable) {
    this.isPlanPersistable = planPersistable;
  }

  public PlanNodeChain getPlanInstance() {
    return planInstance;
  }

  public void setPlanInstance(PlanNodeChain planInstance) {
    this.planInstance = planInstance;
  }

  public SqlBuilder getSqlBuilder() {
    return sqlBuilder;
  }

  public void setSqlBuilder(SqlBuilder sqlBuilder) {
    this.sqlBuilder = sqlBuilder;
  }


  /**
   * 执行上下文的索引表操作。
   * @return 索引表操作
   */
  public List<IndexTableOps> getIndexTableOpsList() {
    return indexTableOpsList;
  }

  @Override
  public void close() throws IOException {

  }

  public void clear() {

    // 清理SQL注释解析结果
    sqlHint = null;
    sqlHintStr = null;
    // 清理执行组
    currentExecutionGroup = null;

    // clear index table ops
    try {
      indexTableOpsList.clear();
    } catch (Exception ignore) {
      //no-ops
    }


    try {
      sqlParameters.clear();
    } catch (Exception ignore) {
      // no-ops
    }

    try {
      hintShardConditions.clear();
    } catch (Exception ignore) {
      // no-ops
    }

    try {
      routeConditions.clear();
    } catch (Exception ignore) {
      // no-ops
    }

    try {
      shardTables.clear();
    } catch (Exception ignore) {
      // no-ops
    }

    try {
      resultDataList.clear();
    } catch (Exception ignore) {
      // no-ops
    }
  }
}
