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

package studio.raptor.ddal.core.connection.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.executor.resultset.ResultData;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JdbcBackendConnection implements BackendConnection {

  private Connection connection;

  public JdbcBackendConnection(Connection connection) {
    this.connection = connection;
  }

  @Override
  public void setAutoCommit(boolean autoCommit) throws SQLException {
    this.connection.setAutoCommit(autoCommit);
  }

  @Override
  public void close() throws SQLException {
    this.connection.close();
  }

  @Override
  public void commit() throws SQLException {
    this.connection.commit();
  }

  @Override
  public ResultData executeQuery(String sql) throws SQLException {
    try (
        Statement statement = this.connection.createStatement();
        ResultSet rs = statement.executeQuery(sql)
    ) {
      return new ResultData(rs);
    }
  }

  @Override
  public ResultData executeUpdate(String sql) throws SQLException {
    try (
        Statement statement = this.connection.createStatement()
    ) {
      return new ResultData(statement.executeUpdate(sql));
    }
  }

  @Override
  public boolean isReadOnly() throws SQLException {
    return this.connection.isReadOnly();
  }

  @Override
  public void setReadOnly(boolean readOnly) throws SQLException {
    this.connection.setReadOnly(readOnly);
  }

  @Override
  public ResultData executePreparedQuery(String sql, List<Object> parameters) throws SQLException {
    try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
      this.setParameters(preparedStatement, parameters);
      try (ResultSet rs = preparedStatement.executeQuery()) {
        return new ResultData(rs);
      }
    }
  }

  @Override
  public ResultData executePreparedUpdate(String sql, List<Object> parameters) throws SQLException {
    try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
      setParameters(preparedStatement, parameters);
      return new ResultData(preparedStatement.executeUpdate());
    }
  }

  @Override
  public void rollback() throws SQLException {
    this.connection.rollback();
  }

  @Override
  public void setSchema(String schema) throws SQLException {
    this.connection.setSchema(schema);
  }

  /**
   * 预编译语句参数设置
   *
   * @param preparedStatement 预编译语句
   * @param parameters        参数集合
   * @throws SQLException Set parameters error.
   */
  private void setParameters(final PreparedStatement preparedStatement, final List<Object> parameters) throws SQLException {
    int i = 1;
    for (Object each : parameters) {
      preparedStatement.setObject(i++, each);
    }
  }
}
