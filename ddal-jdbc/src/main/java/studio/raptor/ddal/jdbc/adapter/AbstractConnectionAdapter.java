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

package studio.raptor.ddal.jdbc.adapter;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.util.Collection;

import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.jdbc.unsupported.AbstractUnsupportedOperationConnection;

/**
 * 数据库连接适配类.
 *
 * @author Jack, Sam
 * @since 3.0
 */
public abstract class AbstractConnectionAdapter extends AbstractUnsupportedOperationConnection {

  protected boolean autoCommit = true;
  private boolean readOnly = true;
  private boolean closed;

  protected abstract Collection<BackendConnection> getConnections();

  @Override
  public final boolean getAutoCommit() throws SQLException {
    return autoCommit;
  }

  @Override
  public void close() throws SQLException {
    if (!closed) {
      for (BackendConnection each : getConnections()) {
        each.close();
      }
    }
    closed = true;
  }

  @Override
  public final boolean isClosed() throws SQLException {
    return closed;
  }

  @Override
  public final boolean isReadOnly() throws SQLException {
    return readOnly;
  }

  @Override
  public final void setReadOnly(final boolean readOnly) throws SQLException {
    this.readOnly = readOnly;
    if (getConnections().isEmpty()) {
      recordMethodInvocation(Connection.class, "setReadOnly", new Class[]{boolean.class}, new Object[]{readOnly});
    }
  }

  @Override
  public final int getTransactionIsolation() throws SQLException {
    return TRANSACTION_READ_UNCOMMITTED;
  }

  // -------以下代码与MySQL实现保持一致.-------
  @Override
  public SQLWarning getWarnings() throws SQLException {
    return null;
  }

  @Override
  public void clearWarnings() throws SQLException {
  }

  @Override
  public final int getHoldability() throws SQLException {
    return ResultSet.CLOSE_CURSORS_AT_COMMIT;
  }

  @Override
  public final void setHoldability(final int holdability) throws SQLException {
  }
}
