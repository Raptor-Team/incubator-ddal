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

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.JdbcErrCodes;
import studio.raptor.ddal.core.executor.resultset.ColumnDefinition;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;
import studio.raptor.ddal.jdbc.unsupported.AbstractUnsupportedOperationResultSet;

/**
 * Proxy result set adapter.
 *
 * @author jackcao
 * @since 3.0
 */
public abstract class AbstractResultSetAdapter extends AbstractUnsupportedOperationResultSet {

  /**
   * 结果集列名索引，key是columnLabel， value是columnIndex
   */
  protected final Map<String, Integer> columnLabelIndex;
  private static Map<String, String> colLabelUCaseCache;
  /**
   * 内部结果集对象
   */
  private final ResultData resultData;
  /**
   * 结果集头信息
   */
  private final List<ColumnDefinition> resultDataHead;
  /**
   * 最近一次从ResultSet中获取的值是否为null
   */
  protected boolean wasNull;
  /**
   * 游标在第一条数据之前的标志。
   */
  private boolean beforeFirst = true;
  /**
   * 数据访问游标
   */
  private int cursor = 0;
  private boolean closed;
  /**
   * RaptorResultSet对象创建好之后，后端物理连接的ResultSet其实
   * 已经关闭了，此参数已无太大价值。
   */
  private int fetchSize;
  /**
   * 当前行数据
   */
  private RowData currentRowData;

  private ResultSetMetaData resultSetMetaData;

  /**
   * RaptorResultSet构造器。设置当前ResultSet需要处理的数据对象，创建列名索引避免
   * 在获取结果集数据时重复遍历列集合。
   *
   * 处理ResultData的ColumnLabel时使用缓存技术，避免频繁做upperCase，减小耗时。
   *
   * @param resultData 内部数据对象
   * @throws SQLException exception when build {@link studio.raptor.ddal.jdbc.RaptorResultSet}
   */
  public AbstractResultSetAdapter(final ResultData resultData) throws SQLException {
    this.resultData = resultData;
    this.resultDataHead = this.resultData.getHead();
    resultSetMetaData = resultData.getMetaData();

    if (null == colLabelUCaseCache) {
      synchronized (this) {
        if (null == colLabelUCaseCache) {
          colLabelUCaseCache = new ConcurrentHashMap<>();
        }
      }
    }
    if (null != this.resultDataHead && !this.resultDataHead.isEmpty()) {
      columnLabelIndex = new HashMap<>(this.resultDataHead.size());
      int columnIndex = 1;
      for (ColumnDefinition columnDefinition : this.resultDataHead) {
        String colDef = colLabelUCaseCache.get(columnDefinition.getName());
        if (null == colDef) {
          colDef = columnDefinition.getName().toUpperCase();
          colLabelUCaseCache.put(columnDefinition.getName(), colDef);
        }
        columnLabelIndex.put(colDef, columnIndex++);
      }
    } else {
      columnLabelIndex = new HashMap<>();
    }
  }

  @Override
  public boolean next() throws SQLException {
    if (beforeFirst) {
      cursor = 0;
      beforeFirst = false;
    }
    Optional<RowData> row = nextRow();
    if (null != row && row.isPresent()) {
      currentRowData = row.get();
      return true;
    }
    return false;
  }

  /**
   * Set next row data to currentRowData, and plus cursor by 1.
   *
   * @return next row data
   */
  private Optional<RowData> nextRow() {
    if (null == resultData || resultData.getRowCount() < 1) {
      return Optional.absent();
    }
    if (cursor <= resultData.getRowCount() - 1) {
      return Optional.of(resultData.getRows().get(cursor++));
    }
    return Optional.absent();
  }

  /**
   * 获取当前字段的SQL类型
   *
   * @param columnIndex 列索引，从1开始
   * @return SQL类型
   */
  protected int getRowFieldSQLType(int columnIndex) {
    return resultDataHead.get(columnIndex - 1).getType();
  }

  /**
   * Check column index is in range or not, if not throw RuntimeException of
   * {@link JdbcErrCodes#JDBC_604}
   *
   * @param columnIndex column index
   */
  protected void checkColumnIndexInRange(int columnIndex) {
    if (!inRange(columnIndex)) {
      throw new GenericException(JdbcErrCodes.JDBC_604, columnIndex);
    }
  }

  private boolean inRange(int columnIndex) {
    return columnIndex > 0 && columnIndex <= currentRowData.getCellCount();
  }

  protected Object getObjectInternal(int columnIndex) throws SQLException {
    Preconditions.checkState(!isClosed(), "ResultSet is closed");
    Preconditions.checkState(!beforeFirst, "Before start of ResultSet");
    Preconditions.checkState(inRange(columnIndex),
        String.format("Column Index %d out of range", columnIndex));
    Object obj = currentRowData.getCell(columnIndex);
    this.wasNull = null == obj;
    return obj;
  }

  @Override
  public final void close() throws SQLException {
    closed = true;
  }

  @Override
  public final boolean isClosed() throws SQLException {
    return closed;
  }

  @Override
  public final void setFetchDirection(final int direction) throws SQLException {

  }

  @Override
  public int getFetchSize() throws SQLException {
    return fetchSize;
  }

  @Override
  public final void setFetchSize(final int rows) throws SQLException {
    fetchSize = rows;
  }

  @Override
  public ResultSetMetaData getMetaData() throws SQLException {
    return this.resultSetMetaData;
  }
}
