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

import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.Collection;

import studio.raptor.ddal.jdbc.unsupported.AbstractUnsupportedOperationStatement;

/**
 * Static statement object adaptation class.
 *
 * @author jack
 * @since 3.0
 */
public abstract class AbstractStatementAdapter extends AbstractUnsupportedOperationStatement {

  private final Class<? extends Statement> recordTargetClass;

  private boolean closed;
  private int fetchSize;

  public AbstractStatementAdapter(Class<? extends Statement> recordTargetClass) {
    this.recordTargetClass = recordTargetClass;
  }

  @Override
  public final void close() throws SQLException {
    for (Statement each : getRoutedStatements()) {
      each.close();
    }
    closed = true;
    getRoutedStatements().clear();
  }

  @Override
  public final boolean isClosed() throws SQLException {
    return closed;
  }

  /**
   * Statement is not poolable default.
   */
  @Override
  public final boolean isPoolable() throws SQLException {
    return false;
  }

  @Override
  public final void setPoolable(final boolean poolable) throws SQLException {
    // do nothing
  }

  @Override
  public final int getFetchSize() throws SQLException {
    return fetchSize;
  }

  /**
   * 物理的FetchSize不通过这个接口设置。物理连接的fetchSize通过连接池
   * 参数设置。
   *
   * @param rows fetchSize
   * @throws SQLException sql exception
   */
  @Override
  public final void setFetchSize(final int rows) throws SQLException {
    this.fetchSize = rows;
  }

  @Override
  public final void setEscapeProcessing(final boolean enable) throws SQLException {
    if (getRoutedStatements().isEmpty()) {
      recordMethodInvocation(recordTargetClass, "setEscapeProcessing", new Class[]{boolean.class}, new Object[]{enable});
      return;
    }
    for (Statement each : getRoutedStatements()) {
      each.setEscapeProcessing(enable);
    }
  }

  @Override
  public final void cancel() throws SQLException {
    for (Statement each : getRoutedStatements()) {
      each.cancel();
    }
  }

  @Override
  public SQLWarning getWarnings() throws SQLException {
    return null;
  }

  @Override
  public void clearWarnings() throws SQLException {
  }

  @Override
  public final void setCursorName(final String name) throws SQLException {
    if (getRoutedStatements().isEmpty()) {
      recordMethodInvocation(recordTargetClass, "setCursorName", new Class[]{String.class}, new Object[]{name});
      return;
    }
    for (Statement each : getRoutedStatements()) {
      each.setCursorName(name);
    }
  }

  /*
   * 只有存储过程会出现多结果集, 因此不支持.
   */
  @Override
  public final boolean getMoreResults() throws SQLException {
    return false;
  }

  @Override
  public final boolean getMoreResults(final int current) throws SQLException {
    return false;
  }

  @Override
  public final int getMaxFieldSize() throws SQLException {
    return getRoutedStatements().isEmpty() ? 0 : getRoutedStatements().iterator().next().getMaxFieldSize();
  }

  @Override
  public final void setMaxFieldSize(final int max) throws SQLException {
    if (getRoutedStatements().isEmpty()) {
      recordMethodInvocation(recordTargetClass, "setMaxFieldSize", new Class[]{int.class}, new Object[]{max});
      return;
    }
    for (Statement each : getRoutedStatements()) {
      each.setMaxFieldSize(max);
    }
  }

  @Override
  public final int getMaxRows() throws SQLException {
    return getRoutedStatements().isEmpty() ? -1 : getRoutedStatements().iterator().next().getMaxRows();
  }

  @Override
  public final void setMaxRows(final int max) throws SQLException {
    if (getRoutedStatements().isEmpty()) {
      recordMethodInvocation(recordTargetClass, "setMaxRows", new Class[]{int.class}, new Object[]{max});
      return;
    }
    for (Statement each : getRoutedStatements()) {
      each.setMaxRows(max);
    }
  }

  @Override
  public final int getQueryTimeout() throws SQLException {
    return getRoutedStatements().isEmpty() ? 0 : getRoutedStatements().iterator().next().getQueryTimeout();
  }

  @Override
  public final void setQueryTimeout(final int seconds) throws SQLException {
    if (getRoutedStatements().isEmpty()) {
      recordMethodInvocation(recordTargetClass, "setQueryTimeout", new Class[]{int.class}, new Object[]{seconds});
      return;
    }
    for (Statement each : getRoutedStatements()) {
      each.setQueryTimeout(seconds);
    }
  }

  /**
   * 获取路由的静态语句对象集合.
   *
   * @return 路由的静态语句对象集合
   * @throws SQLException SQL执行异常
   */
  protected abstract Collection<? extends Statement> getRoutedStatements() throws SQLException;
}
