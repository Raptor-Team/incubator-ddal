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

package studio.raptor.ddal.jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.constants.FlowType;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.ProcessEngine;
import studio.raptor.ddal.core.engine.ProcessEngineFactory;
import studio.raptor.ddal.jdbc.adapter.AbstractConnectionAdapter;
import studio.raptor.ddal.jdbc.processor.PreparedStatementProcessor;
import studio.raptor.ddal.jdbc.processor.StatementProcessor;

/**
 * Supports sharded database connections.
 *
 * @author jackcao
 * @since 3.0
 */
public final class RaptorConnection extends AbstractConnectionAdapter {

  private final ProcessEngine processEngine;
  private final ProcessContext processContext;
  private final StatementProcessor statementProcessor;
  private final PreparedStatementProcessor preparedStatementProcessor;

  RaptorConnection(DatabaseType databaseType, String virtualDbName) {
    this.processContext = new ProcessContext(databaseType, virtualDbName);
    this.processEngine = ProcessEngineFactory.create(this.processContext);
    statementProcessor = new StatementProcessor(this.processEngine);
    preparedStatementProcessor = new PreparedStatementProcessor(this.processEngine);
  }

  @Override
  public PreparedStatement prepareStatement(final String sql) throws SQLException {
    return new RaptorPreparedStatement(this, sql);
  }

  /**
   * 结果集类型和并发类型目前支持默认值。
   *
   * @param sql sql script
   * @param resultSetType ResultSet.TYPE_FORWARD_ONLY
   * @param resultSetConcurrency ResultSet.CONCUR_READ_ONLY
   * @return RaptorPreparedStatement
   * @throws SQLException Sql exception
   */
  @Override
  public PreparedStatement prepareStatement(final String sql, final int resultSetType, final int resultSetConcurrency) throws SQLException {
    return new RaptorPreparedStatement(this, sql);
  }

  @Override
  public Statement createStatement() throws SQLException {
    return new RaptorStatement(this);
  }

  @Override
  public Collection<BackendConnection> getConnections() {
    ProcessContext context = this.processEngine.getProcessContext();
    Map<String, ContextConnectionWrapper> connectionsWrapper = context
        .getShardBackendConnWrapper();
    if (null != connectionsWrapper && !connectionsWrapper.isEmpty()) {
      List<BackendConnection> connections = new ArrayList<>();
      BackendConnection bConn;
      for (ContextConnectionWrapper connWrapper : connectionsWrapper.values()) {
        if(null != (bConn = connWrapper.getReadWriteConnection())) {
          connections.add(bConn);
        }
        if(null != (bConn = connWrapper.getReadonlyConnection())) {
          connections.add(bConn);
        }
      }
      return connections;
    }
    return Collections.emptySet();
  }

  @Override
  public final void setAutoCommit(final boolean autoCommit) throws SQLException {
    this.autoCommit = autoCommit;
    // pass auto commit flag to processing context.
    this.processContext.setAutoCommit(autoCommit);
    // 如果当前执行上下文已有物理连接，将自动提交的标记直接
    // 传递给物理连接。
    for (BackendConnection each : getConnections()) {
      each.setAutoCommit(autoCommit);
    }
  }

  @Override
  public void commit() throws SQLException {
    processEngine.process(FlowType.COMMIT);
  }

  @Override
  public void rollback() throws SQLException {
    processEngine.process(FlowType.ROLLBACK);
  }

  ProcessEngine getProcessEngine() {
    return processEngine;
  }

  public ProcessContext getProcessContext() {
    return processContext;
  }

  StatementProcessor getStatementProcessor() {
    return statementProcessor;
  }

  PreparedStatementProcessor getPreparedStatementProcessor() {
    return preparedStatementProcessor;
  }
}
