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


import java.sql.ResultSet;
import java.sql.SQLException;

import studio.raptor.ddal.jdbc.adapter.AbstractPreparedStatementAdapter;
import studio.raptor.ddal.jdbc.processor.PreparedStatementProcessor;

/**
 * 支持分片的预编译语句对象.
 *
 * @author Sam
 */
public final class RaptorPreparedStatement extends AbstractPreparedStatementAdapter {

  RaptorPreparedStatement(final RaptorConnection raptorConnection, final String sql) throws SQLException {
    this(raptorConnection, sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY, ResultSet.HOLD_CURSORS_OVER_COMMIT);
  }

  private RaptorPreparedStatement(final RaptorConnection raptorConnection,
                                  final String sql, final int resultSetType, final int resultSetConcurrency, final int resultSetHoldability) {
    super(raptorConnection, resultSetType, resultSetConcurrency, resultSetHoldability);
    getRaptorConnection().getProcessContext().setOriginSql(sql);
  }

  @Override
  public ResultSet executeQuery() throws SQLException {
    ResultSet rs;
    try {
      rs = getPreparedStmtProcessor().executeQuery();
    } finally {
      clearProcessContext();
    }
    setCurrentResultSet(rs);
    return rs;
  }

  @Override
  public int executeUpdate() throws SQLException {
    try {
      return getPreparedStmtProcessor().executeUpdate();
    } finally {
      clearProcessContext();
    }
  }

  @Override
  public boolean execute() throws SQLException {
    try {
      return getPreparedStmtProcessor().execute();
    } finally {
      clearProcessContext();
    }
  }

  private void clearProcessContext() throws SQLException {
    clearParameters();
    setCurrentResultSet(null);
  }

  @Override
  public void clearBatch() throws SQLException {
    //cachedRoutePreparedStatementMap.clear();
    clearProcessContext();
  }

  @Override
  public int[] executeBatch() throws SQLException {
    try {
      return null;//new PreparedStatementExecutor(getRaptorConnection().getShardingContext().getExecutorEngine(), cachedRoutePreparedStatementMap.values()).executeBatch();
    } finally {
      clearBatch();
    }
  }

  private PreparedStatementProcessor getPreparedStmtProcessor() {
    return getRaptorConnection().getPreparedStatementProcessor();
  }

}
