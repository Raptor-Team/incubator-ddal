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

package studio.raptor.ddal.core.engine.plan.node.impl.execute;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.ExecuteException;
import studio.raptor.ddal.common.exception.ExecuteException.Code;
import studio.raptor.ddal.common.sql.SQLHintParser.SQLHint;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.config.config.SystemProperties;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.executor.ExecutionGroup;
import studio.raptor.ddal.core.executor.ExecutionUnit;
import studio.raptor.ddal.core.executor.strategy.ReadWriteStrategy;

/**
 * 数据源选择。
 *
 * @author Sam
 * @since 3.0.0
 */
public class DataSourceSelector extends ProcessNode {

  private static Logger log = LoggerFactory.getLogger(DataSourceSelector.class);
  /**
   * 读写控制策略。若未指定，则使用返回false的默认读写控制策略。
   */
  private static ReadWriteStrategy readWriteStrategy;

  @Override
  protected void execute(ProcessContext context) {
    Map<String, ContextConnectionWrapper> connWrapper = context.getShardBackendConnWrapper();
    ExecutionGroup executionGroup = context.getCurrentExecutionGroup();

    if (null == executionGroup || null == executionGroup.getExecutionUnits()
        || executionGroup.getExecutionUnits().isEmpty()) {
      return;
    }
    for (ExecutionUnit unit : executionGroup.getExecutionUnits()) {
      ContextConnectionWrapper curShardConnWrapper = connWrapper.get(unit.getShard().getName());
      if (null == curShardConnWrapper) {
        curShardConnWrapper = new ContextConnectionWrapper();
        connWrapper.put(unit.getShard().getName(), curShardConnWrapper);
      }

      boolean hasReadWriteConn = null != curShardConnWrapper.getReadWriteConnection();
      BackendConnection curBConn;
      SQLHint sqlHint;
      // 上下文没有读写连接时，只读控制才有效。
      if (log.isDebugEnabled()) {
        logReadwriteControl(hasReadWriteConn, readWriteStrategy.isReadOnly()
            || (null != (sqlHint = context.getSqlHint()) && null != sqlHint.getReadonly()));
      }
      if (!hasReadWriteConn &&
          (readWriteStrategy.isReadOnly()
              || (null != (sqlHint = context.getSqlHint()) && null != sqlHint.getReadonly()))
          ) {

        curBConn = curShardConnWrapper.getReadonlyConnection();
        // 如果指定连接
        if(null != curBConn) {
          try {
            curBConn.close();
          } catch (SQLException e) {
            throw ExecuteException.create(Code.GET_READONLY_CONNECTION_FAILED_ERROR, e);
          }
        }

        try {
          sqlHint = context.getSqlHint();
          List<Integer> seqArray =
              null != sqlHint && null != sqlHint.getReadonly()
                  ? sqlHint.getReadonly().getSeq()
                  : null;
          curShardConnWrapper.setReadonlyConnection(
              (curBConn = BackendDataSourceManager
                  .getReadOnlyConnection(
                      unit.getShard().getDsGroup(), context.isAutoCommit(), seqArray))
          );
        } catch (SQLException e) {
          throw ExecuteException.create(Code.GET_READONLY_CONNECTION_FAILED_ERROR, e);
        }

      } else {
        if (null == (curBConn = curShardConnWrapper.getReadWriteConnection())) {
          try {
            curShardConnWrapper.setReadWriteConnection(
                (curBConn = BackendDataSourceManager
                    .getReadWriteConnection(unit.getShard().getDsGroup(), context.isAutoCommit()))
            );
          } catch (SQLException e) {
            throw ExecuteException.create(Code.GET_READWRITE_CONNECTION_FAILED_ERROR, e);
          }
        }
      }
      curShardConnWrapper.setCurrentConnection(curBConn);
    }
  }


  private void logReadwriteControl(boolean hasRWConn, boolean appControlToRead) {
    if (hasRWConn) {
      if (appControlToRead) {
        log.debug(
            "!!WARNING: Readonly control will be ignored because readwrite connection exists in current context.");
      }
    }

    if (!hasRWConn) {
      if (appControlToRead) {
        log.debug("!!INFO: Readonly control success.");
      }
    }
  }

  static {
    String strategyClazz = null;
    Class<?> strategyClass;
    try {
      if (null != SystemProperties.getInstance()
          && null != SystemProperties.getInstance().getMapper()) {
        strategyClazz = SystemProperties.getInstance().getMapper().get("strategy.readwrite");
      }
      strategyClass = StringUtil.isEmpty(strategyClazz) ? null : Class.forName(strategyClazz);

      if (null == strategyClass || !ReadWriteStrategy.class.isAssignableFrom(strategyClass)) {
        readWriteStrategy = new ReadWriteStrategy() {
          @Override
          public boolean isReadOnly() {
            return false;
          }
        };
        log.info("Default readwrite strategy will be used instead of unknown strategy {}",
            strategyClazz);
      } else {
        try {
          readWriteStrategy = (ReadWriteStrategy) strategyClass.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
          throw ExecuteException.create(Code.READ_WRITE_STRATEGY_INSTANCE_ERROR, e);
        }
      }
    } catch (ClassNotFoundException | ExceptionInInitializerError ex) {
      throw ExecuteException.create(Code.READ_WRITE_STRATEGY_CONFIG_ERROR, ex);
    }
  }
}
