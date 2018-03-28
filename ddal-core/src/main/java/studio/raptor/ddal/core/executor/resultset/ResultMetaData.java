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

package studio.raptor.ddal.core.executor.resultset;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;

/**
 * 结果集Meta
 *
 * @author Sam
 * @since 3.0.0
 */
public class ResultMetaData implements ResultSetMetaData {

  private final int columnCount;
  private final boolean[] autoIncrement;
  private final boolean[] caseSensitive;
  private final boolean[] searchable;
  private final boolean[] currency;
  private final int[] nullable;
  private final boolean[] signed;
  private final int[] columnDisplaySize;
  private final String[] columnLabel;
  private final String[] columnName;
  private final String[] schemaName;
  private final int[] precision;
  private final int[] scale;
  private final String[] tableName;
  private final String[] catalogName;
  private final int[] columnType;
  private final String[] columnTypeName;
  private final boolean[] readonly;
  private final boolean[] writable;
  private final boolean[] definitelyWritable;
  private final String[] columnClassName;

  ResultMetaData(ResultSetMetaData metaData) throws SQLException {
    this.columnCount = metaData.getColumnCount();
    autoIncrement = new boolean[columnCount];
    caseSensitive = new boolean[columnCount];
    searchable = new boolean[columnCount];
    currency = new boolean[columnCount];
    nullable = new int[columnCount];
    signed = new boolean[columnCount];
    columnDisplaySize = new int[columnCount];
    columnLabel = new String[columnCount];
    columnName = new String[columnCount];
    schemaName = new String[columnCount];
    precision = new int[columnCount];
    scale = new int[columnCount];
    tableName = new String[columnCount];
    catalogName = new String[columnCount];
    columnType = new int[columnCount];
    columnTypeName = new String[columnCount];
    readonly = new boolean[columnCount];
    writable = new boolean[columnCount];
    definitelyWritable = new boolean[columnCount];
    columnClassName = new String[columnCount];

    for (int i = 0; i < columnCount; i++) {
      int iPlus = i + 1;
      autoIncrement[i] = metaData.isAutoIncrement(iPlus);
      caseSensitive[i] = metaData.isCaseSensitive(iPlus);
      searchable[i] = metaData.isSearchable(iPlus);
      currency[i] = metaData.isCurrency(iPlus);
      nullable[i] = metaData.isNullable(iPlus);
      signed[i] = metaData.isSigned(iPlus);
      columnDisplaySize[i] = metaData.getColumnDisplaySize(iPlus);
      columnLabel[i] = metaData.getColumnLabel(iPlus);
      columnName[i] = metaData.getColumnName(iPlus);
      schemaName[i] = metaData.getSchemaName(iPlus);
      precision[i] = metaData.getPrecision(iPlus);
      scale[i] = metaData.getScale(iPlus);
      tableName[i] = metaData.getTableName(iPlus);
      catalogName[i] = metaData.getCatalogName(iPlus);
      columnType[i] = metaData.getColumnType(iPlus);
      columnTypeName[i] = metaData.getColumnTypeName(iPlus);
      readonly[i] = metaData.isReadOnly(iPlus);
      writable[i] = metaData.isWritable(iPlus);
      definitelyWritable[i] = metaData.isDefinitelyWritable(iPlus);
      columnClassName[i] = metaData.getColumnClassName(iPlus);
    }
  }

  @Override
  public int getColumnCount() throws SQLException {
    return this.columnCount;
  }

  @Override
  public boolean isAutoIncrement(int column) throws SQLException {
    return this.autoIncrement[column - 1];
  }

  @Override
  public boolean isCaseSensitive(int column) throws SQLException {
    return this.caseSensitive[column - 1];
  }

  @Override
  public boolean isSearchable(int column) throws SQLException {
    return this.searchable[column - 1];
  }

  @Override
  public boolean isCurrency(int column) throws SQLException {
    return this.currency[column - 1];
  }

  @Override
  public int isNullable(int column) throws SQLException {
    return this.nullable[column - 1];
  }

  @Override
  public boolean isSigned(int column) throws SQLException {
    return this.signed[column - 1];
  }

  @Override
  public int getColumnDisplaySize(int column) throws SQLException {
    return this.columnDisplaySize[column - 1];
  }

  @Override
  public String getColumnLabel(int column) throws SQLException {
    return columnLabel[column - 1];
  }

  @Override
  public String getColumnName(int column) throws SQLException {
    return columnName[column - 1];
  }

  @Override
  public String getSchemaName(int column) throws SQLException {
    return schemaName[column - 1];
  }

  @Override
  public int getPrecision(int column) throws SQLException {
    return precision[column - 1];
  }

  @Override
  public int getScale(int column) throws SQLException {
    return scale[column - 1];
  }

  @Override
  public String getTableName(int column) throws SQLException {
    return tableName[column - 1];
  }

  @Override
  public String getCatalogName(int column) throws SQLException {
    return catalogName[column - 1];
  }

  @Override
  public int getColumnType(int column) throws SQLException {
    return columnType[column - 1];
  }

  @Override
  public String getColumnTypeName(int column) throws SQLException {
    return columnTypeName[column - 1];
  }

  @Override
  public boolean isReadOnly(int column) throws SQLException {
    return readonly[column - 1];
  }

  @Override
  public boolean isWritable(int column) throws SQLException {
    return writable[column - 1];
  }

  @Override
  public boolean isDefinitelyWritable(int column) throws SQLException {
    return definitelyWritable[column - 1];
  }

  @Override
  public String getColumnClassName(int column) throws SQLException {
    return columnClassName[column - 1];
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    return null;
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return false;
  }
}
