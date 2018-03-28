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

package studio.raptor.ddal.core.connection;

import java.sql.SQLException;
import java.util.List;

import studio.raptor.ddal.core.executor.resultset.ResultData;

/**
 * Logical backend connection, and it has two implementations
 *
 * @author Sam
 * @since 3.0.0
 */
public interface BackendConnection {

  void setAutoCommit(boolean autoCommit) throws SQLException;

  /**
   * 连接归还给连接池，以便被应用再次使用。
   *
   * BTW：此连接并不是真正被关闭。
   */
  void close() throws SQLException;

  /**
   * The method commit() makes all changes made since the previous
   * commit/rollback permanent and releases any database locks currently held
   * by the Connection. This method should only be used when auto-commit has
   * been disabled.
   * <p>
   * <b>Note:</b> MySQL does not support transactions, so this method is a no-op.
   * </p>
   *
   * @throws SQLException if a database access error occurs
   */
  void commit() throws SQLException;

  /**
   * Execute a SQL statement that returns a single ResultSet
   *
   * @param sql typically a static SQL SELECT statement
   * @return a ResulSet that contains the data produced by the query
   * @throws SQLException if a database access error occurs
   */
  ResultData executeQuery(String sql) throws SQLException;

  /**
   * Execute a SQL INSERT, UPDATE or DELETE statement. In addition SQL statements
   * that return nothing such as SQL DDL statements can be executed.
   *
   * @param sql a SQL statement
   * @return either a row count, or 0 for SQL commands
   * @throws SQLException if a database access error occurs
   */
  ResultData executeUpdate(String sql) throws SQLException;

  /**
   * Check this connection is read only or not.
   *
   * @return connection is read only or not.
   * @throws SQLException if a database access error occurs
   */
  boolean isReadOnly() throws SQLException;

  /**
   * Set backend connection is read only
   *
   * @param readOnly read only flag
   * @throws SQLException if a database access error occurs
   */
  void setReadOnly(boolean readOnly) throws SQLException;

  /**
   * A Prepared SQL query is executed and its ResultSet is returned
   *
   * @param sql        a SQL prepared statement
   * @param parameters parameters of prepared statment
   * @return a ResultSet that contains the data produced by the query - never null
   * @throws SQLException if a database access error occurs
   */
  ResultData executePreparedQuery(String sql, List<Object> parameters) throws SQLException;

  /**
   * Execute a SQL INSERT, UPDATE or DELETE statement. In addition, SQL
   * statements that return nothing such as SQL DDL statements can be
   * executed.
   *
   * @param sql        a SQL prepared statement
   * @param parameters parameters of prepared statment
   * @return either the row count for INSERT, UPDATE or DELETE; or 0 for SQL statements that return
   * nothing.
   * @throws SQLException if a database access error occurs
   */
  ResultData executePreparedUpdate(String sql, List<Object> parameters) throws SQLException;

  /**
   * The method rollback() drops all changes made since the previous
   * commit/rollback and releases any database locks currently held by the
   * Connection.
   *
   * @throws SQLException if a database access error occurs
   */
  void rollback() throws SQLException;

  // JDBC-4.1
  // set catalog/schema, this is a no-op
  void setSchema(String schema) throws SQLException;

}
