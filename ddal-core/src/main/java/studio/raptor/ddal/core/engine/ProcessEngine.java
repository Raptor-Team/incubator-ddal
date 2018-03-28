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

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.common.sql.SQLHintParser;
import studio.raptor.ddal.common.sql.SQLHintParser.SQLHint;
import studio.raptor.ddal.common.sql.SQLUtil;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.constants.FlowType;
import studio.raptor.ddal.core.engine.plan.PlanNodeChain;
import studio.raptor.ddal.core.engine.processdefinition.SqlProcessDefinition;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.monitor.Monitor;
import studio.raptor.ddal.core.parser.Parser;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.SQLStatementType;

/**
 * 处理所有SQL的执行引擎。
 * 核心方法是{@link #process(FlowType)}，目前支持DML、COMMIT和ROLLBACK。
 *
 * @author Sam
 * @since 3.0.0
 */
public final class ProcessEngine {


  private ProcessContext processContext;

  /**
   * 关系型数据库处理流程定义。
   */
  private static SqlProcessDefinition processDef = new SqlProcessDefinition();

  ProcessEngine(ProcessContext processContext) {
    this.processContext = processContext;
  }

  public void process(FlowType flowType) throws SQLException {
    switch (flowType) {
      case DML:
        try {
          Monitor.countProcSqlTotal();
          Monitor.procTpsMark();
          processDML();
          // 处理DML语句的索引表
          processIdxDML();
          Monitor.countProcSuccess();
        } catch (Exception ge) {
          Monitor.countProcFail();
          // 还后端连接
          Map<String, ContextConnectionWrapper> bkConnWrapper = getProcessContext()
              .getShardBackendConnWrapper();
          if (null != bkConnWrapper && !bkConnWrapper.isEmpty()) {
            BackendConnection _bc;
            for (Map.Entry<String, ContextConnectionWrapper> wrapEntry : bkConnWrapper.entrySet()) {
              if (null != (_bc = wrapEntry.getValue().getReadWriteConnection())) {
                _bc.close();
              }
              if (null != (_bc = wrapEntry.getValue().getReadonlyConnection())) {
                _bc.close();
              }
            }
          }
          // SQL执行异常之后，清除RaptorConnection上下文中的后端连接
          // 当前RC再次收到SQL处理请求时会再次从后端连接池获取连接。
          // 避免多次还回连接池的异常发生
          Map<String, ContextConnectionWrapper> connWrapper = getProcessContext()
              .getShardBackendConnWrapper();
          if (connWrapper.size() > 0) {
            connWrapper.clear();
          }
          throw ge;
        } finally {
          getProcessContext().clear();
        }
        break;
      case COMMIT:
        processDef.commitFlow(getProcessContext());
        break;
      case ROLLBACK:
        processDef.rollbackFlow(getProcessContext());
        break;
      default:
        break;
    }
  }

  private void doReplace(String sql, String hint) {
    SqlMetaData sqlMetaData = MemoryObjectsBasedOnSQL.getCacheSql(hint + sql);
    if (null != sqlMetaData && sqlMetaData.isReplaced) {
      processContext.setOriginSql(sqlMetaData.getReplaceSql());
    }
    parseSql(false);
  }


  /**
   * SQL解析以及缓存解析结果。解析结果可缓存的前提条件是预编译语句。因为
   * SQL语句的注释会不断地变化，所以缓存解析结果时会先删除SQL语句前部的
   * 注释。
   */
  private void parseSql(boolean doReplace) {
    String sql = processContext.getOriginSql();
    SQLHint sqlHint = SQLHintParser.parse(sql);

    if (null != sqlHint) {
      processContext.setSqlHint(sqlHint);
      processContext.setHasHint(true);
      processContext.setSqlHintStr(sqlHint.toString());
    } else {
      processContext.setSqlHint(null);
      processContext.setHasHint(false);
      processContext.setSqlHintStr("");
    }
    processContext.setTrimmedHintSql(sql.substring(SQLUtil.findStartOfStatement(sql)));

    if (doReplace) {

      if (null != processContext.getSqlParameters() && !processContext.getSqlParameters()
          .isEmpty()) {
        String sqlSpec =
            (null == sqlHint ? "" : sqlHint.toSpec()) + processContext.getTrimmedHintSql();
        if (null == MemoryObjectsBasedOnSQL.getCacheSql(sqlSpec)) {
          MemoryObjectsBasedOnSQL.cacheSql(sqlSpec, new SqlMetaData(sql));
        }
      }

      doReplace(processContext.getTrimmedHintSql(), null == sqlHint ? "" : sqlHint.toSpec());
    }

//    ParseResult parsedResult = MemoryObjectsBasedOnSQL
//        .getMemoryParseResult(processContext.getTrimmedHintSql());
//    if (null == parsedResult) {
//      parsedResult = Parser.parse(processContext.getDatabaseType(), sql);
//      MemoryObjectsBasedOnSQL.memorizeParseResult(processContext.getTrimmedHintSql(), parsedResult);
//    }
    // fixme 缓存解析结果情况下，高并发出现语法树中表丢失的问题
    ParseResult parsedResult = Parser.parse(processContext.getDatabaseType(), sql);
    processContext.setParseResult(parsedResult);

  }

  private void processDML() {
    parseSql(true);
    String sqlHint = processContext.getSqlHintStr();
    String sql = processContext.getTrimmedHintSql();
    // 对于已存在执行流程实例的SQL，直接使用流程实例执行
    //fixme 执行计划缓存结果在判断是否单分片执行结果这里有问题，先暂时关闭执行计划缓存
//    PlanNodeChain planInstance = MemoryObjectsBasedOnSQL.getCachedPlan(sqlHint + sql);
    processContext.setPlanPersistable(false);
//    if (null != planInstance) {
//      planInstance.runFromStart(processContext);
//    } else {
      // 预编译语句或者有分页和分片注释的语句，才可以缓存执行计划。
      if ((null != processContext.getSqlParameters()
          && !processContext.getSqlParameters().isEmpty())
          ||
          (processContext.hasHint()
              && (null != processContext.getSqlHint().getPage()
              || null != processContext.getSqlHint().getShard()))
          ) {
        processContext.setPlanPersistable(true);
      }
      processContext.setPlanInstance(PlanNodeChain.createInstance());
      PlanNodeChain.getInsTemplate().runFromStart(processContext);
      if (processContext.isPlanPersistable()) {
        MemoryObjectsBasedOnSQL.cachePlan(sqlHint + sql, processContext.getPlanInstance());
      }
//    }
  }

  /**
   * 处理索引表SQL。索引表SQL紧跟正常的流程之后执行，正常的执行流程生成索引表相关的SQL。
   * 索引表SQL分为查询和修改。索引查询的结果作为原查询语句的最终结果。索引表修改操作的结果
   * 不能写入上下文，所以原语句的修改结果需提前缓存，待索引语句执行结束之后还原。
   */
  private void processIdxDML() {
    List<IndexTableOps> indexTableOpsList = new FastArrayList<>();
    indexTableOpsList.addAll(processContext.getIndexTableOpsList());
    if (indexTableOpsList.isEmpty()) {
      return;
    }

    // 根据原语句的类型判断执行结果是否需要还原
    ParseResult parseResult = processContext.getParseResult();
    SQLStatementType stmtType = parseResult.getSqlType();
    boolean restoreResultData = false;
    ResultData resultData = null;
    if (stmtType == SQLStatementType.INSERT
        || stmtType == SQLStatementType.UPDATE || stmtType == SQLStatementType.DELETE) {
      restoreResultData = true;
      resultData = processContext.getMergedResult();
    }

    // 执行索引语句
    for (IndexTableOps indexTableOps : indexTableOpsList) {
      // 按需清理执行上下文数据
      processContext.clear();
      // 设置执行语句以及绑定参数
      processContext.setOriginSql(indexTableOps.getIdxSql());
      processContext.getSqlParameters().clear();
      List<Object> idxSqlParams = indexTableOps.getParameters();
      if (!idxSqlParams.isEmpty()) {
        for (Object idxSqlParam : idxSqlParams) {
          processContext.getSqlParameters().add(idxSqlParam);
        }
      }
      // 调用常规执行
      // 流程执行索引语句
      processDML();
    }

    // 还原原语句的执行结果
    if (restoreResultData) {
      processContext.setMergedResult(resultData);
    }
  }

  public ProcessContext getProcessContext() {
    return processContext;
  }
}
