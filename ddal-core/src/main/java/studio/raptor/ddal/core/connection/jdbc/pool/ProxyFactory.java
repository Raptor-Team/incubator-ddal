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

package studio.raptor.ddal.core.connection.jdbc.pool;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import studio.raptor.ddal.core.connection.jdbc.util.FastList;

/**
 * A factory class that produces proxies around instances of the standard
 * JDBC interfaces.
 *
 * @author Brett Wooldridge
 */
public final class ProxyFactory {

  private ProxyFactory() {
    // unconstructable
  }

  /**
   * Create a proxy for the specified {@link Connection} instance.
   *
   * @return a proxy that wraps the specified {@link Connection}
   */
  static ProxyConnection getProxyConnection(final PoolEntry poolEntry, final Connection connection,
      final FastList<Statement> openStatements, final ProxyLeakTask leakTask, final long now,
      final boolean isReadOnly, final boolean isAutoCommit) {
    // Body is replaced (injected) by JavassistProxyFactory
//    throw new IllegalStateException(
//        "You need to run the CLI build and you need target/classes in your classpath to run.");
    return new JdbcProxyConnection(poolEntry, connection, openStatements, leakTask, now, isReadOnly, isAutoCommit);
  }

  static Statement getProxyStatement(final ProxyConnection connection, final Statement statement) {
    // Body is replaced (injected) by JavassistProxyFactory
//    throw new IllegalStateException(
//        "You need to run the CLI build and you need target/classes in your classpath to run.");
    return new JdbcProxyStatement(connection, statement);
  }

  static CallableStatement getProxyCallableStatement(final ProxyConnection connection,
      final CallableStatement statement) {
    // Body is replaced (injected) by JavassistProxyFactory
//    throw new IllegalStateException(
//        "You need to run the CLI build and you need target/classes in your classpath to run.");
    return new JdbcProxyCallableStatement(connection, statement);
  }

  static PreparedStatement getProxyPreparedStatement(final ProxyConnection connection,
      final PreparedStatement statement) {
    // Body is replaced (injected) by JavassistProxyFactory
//    throw new IllegalStateException(
//        "You need to run the CLI build and you need target/classes in your classpath to run.");
    return new JdbcProxyPreparedStatement(connection, statement);
  }

  static ResultSet getProxyResultSet(final ProxyConnection connection,
      final ProxyStatement statement, final ResultSet resultSet) {
    // Body is replaced (injected) by JavassistProxyFactory
//    throw new IllegalStateException(
//        "You need to run the CLI build and you need target/classes in your classpath to run.");
    return new JdbcProxyResultSet(connection, statement, resultSet);
  }
}
