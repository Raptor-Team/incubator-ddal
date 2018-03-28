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

import com.google.common.base.Preconditions;

import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Calendar;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.JdbcErrCodes;
import studio.raptor.ddal.common.jdbc.ResultSetUtil;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.jdbc.adapter.AbstractResultSetAdapter;

/**
 * JDBC 结果集，数据存储于ResultData对象，实现{@link ResultSet}接口
 *
 * @author Sam
 * @since 3.0.0
 */
public final class RaptorResultSet extends AbstractResultSetAdapter {

  public RaptorResultSet(final ResultData resultData) throws SQLException {
    super(resultData);
  }


  /**
   * Get the value of a column in the current row as a Java object
   * <p>
   * This method will return the value of the given column as a Java object.
   * The type of the Java object will be the default Java Object type
   * corresponding to the column's SQL type, following the mapping specified
   * in the JDBC specification.
   * </p>
   * <p>
   * This method may also be used to read database specific abstract data types.
   * </p>
   *
   * @param columnIndex the first column is 1, the second is 2...
   * @return a Object holding the column value
   * @throws SQLException if a database access error occurs
   */
  @Override
  public Object getObject(final int columnIndex) throws SQLException {

    checkColumnIndexInRange(columnIndex);
        /*int SQLType = getRowFieldSQLType(columnIndex);
        switch (SQLType) {
            case Types.BIT:
            case Types.BOOLEAN:
                return getBoolean(columnIndex);
            case Types.TINYINT:
            case Types.SMALLINT:
                return getInt(columnIndex);
            case Types.INTEGER:
                long val = getLong(columnIndex);
                // 大于Java的Integer.MAX_VALUE，说明是无符号int，用Long类型来表达
                if (val > Integer.MAX_VALUE) {
                    return val;
                }
                return getInt(columnIndex);
            case Types.BIGINT:
                // 20位大整数，用BigDecimal表达
                return getBigDecimal(columnIndex);
            case Types.REAL:
                return getFloat(columnIndex);
            case Types.FLOAT:
            case Types.DOUBLE:
                return getDouble(columnIndex);

            case Types.CHAR:
            case Types.VARCHAR:
                return getString(columnIndex);
            case Types.DATE:
                return getDate(columnIndex);
            case Types.TIME:
                return getTime(columnIndex);
            case Types.TIMESTAMP:
                return getTimestamp(columnIndex);
            default:
                throw new BasicException(ErrCodes.COMMON_600, SQLType);
        }*/
    return getObjectInternal(columnIndex);
  }

  /**
   * Get the value of a column in the current row as a Java object
   * <p>
   * This method will return the value of the given column as a
   * Java object. The type of the Java object will be the default
   * Java Object type corresponding to the column's SQL type,
   * following the mapping specified in the JDBC specification.
   * </p>
   * <p>
   * This method may also be used to read database specific abstract
   * data types.
   * </p>
   *
   * @param columnLabel is the SQL name of the column
   * @return a Object holding the column value
   * @throws SQLException if a database access error occurs
   */
  @Override
  public Object getObject(String columnLabel) throws SQLException {
    return getObject(findColumn(columnLabel));
  }

  /**
   * Maps the given <code>ResultSet</code> column label to its
   * <code>ResultSet</code> column index.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column index of the given column name
   * @throws SQLException if the <code>ResultSet</code> object does not contain a column labeled
   *                      <code>columnLabel</code>, a database access error occurs or this method is
   *                      called on a closed result set
   */
  @Override
  public int findColumn(String columnLabel) throws SQLException {
    Preconditions.checkState(
        !StringUtil.isEmpty(columnLabel), String.format("Column label [%s] is empty.", columnLabel));
    String colLabelUpperCase = columnLabel.toUpperCase();
    Preconditions.checkState(columnLabelIndex.containsKey(colLabelUpperCase), String.format("ResultSet doesn't contain column [%s]", columnLabel));
    return columnLabelIndex.get(colLabelUpperCase);
  }

  /**
   * Reports whether
   * the last column read had a value of SQL <code>NULL</code>.
   * Note that you must first call one of the getter methods
   * on a column to try to read its value and then call
   * the method <code>wasNull</code> to see if the value read was
   * SQL <code>NULL</code>.
   *
   * @return <code>true</code> if the last column value read was SQL <code>NULL</code> and
   * <code>false</code> otherwise
   * @throws SQLException if a database access error occurs or this method is called on a closed
   *                      result set
   */
  @Override
  public boolean wasNull() throws SQLException {
    return wasNull;
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>String</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public String getString(int columnIndex) throws SQLException {
    return (String) ResultSetUtil.convertValue(getObject(columnIndex), String.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>String</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public String getString(String columnLabel) throws SQLException {
    return getString(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row of this <code>ResultSet</code>
   * object as a <code>boolean</code> in the Java programming language. <p> <P>If the designated
   * column has a datatype of CHAR or VARCHAR and contains a "0" or has a datatype of BIT, TINYINT,
   * SMALLINT, INTEGER or BIGINT and contains  a 0, a value of <code>false</code> is returned.  If
   * the designated column has a datatype of CHAR or VARCHAR and contains a "1" or has a datatype of
   * BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains  a 1, a value of <code>true</code> is
   * returned.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>false</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public boolean getBoolean(int columnIndex) throws SQLException {
    checkColumnIndexInRange(columnIndex);
    int SQLType = getRowFieldSQLType(columnIndex);
    switch (SQLType) {
      case Types.BOOLEAN:
      case Types.BIT:
      case Types.TINYINT:
      case Types.SMALLINT:
      case Types.INTEGER:
      case Types.BIGINT:
      case Types.DECIMAL:
      case Types.NUMERIC:
      case Types.REAL:
      case Types.FLOAT:
      case Types.DOUBLE:
        long boolVal = getLong(columnIndex);
        return (boolVal == -1 || boolVal > 0);
      default:
        String stringVal = getString(columnIndex);
        return getBooleanFromString(stringVal);
    }
  }

  /**
   * Retrieves the value of the designated column in the current row of this <code>ResultSet</code>
   * object as a <code>boolean</code> in the Java programming language. <p> <P>If the designated
   * column has a datatype of CHAR or VARCHAR and contains a "0" or has a datatype of BIT, TINYINT,
   * SMALLINT, INTEGER or BIGINT and contains  a 0, a value of <code>false</code> is returned.  If
   * the designated column has a datatype of CHAR or VARCHAR and contains a "1" or has a datatype of
   * BIT, TINYINT, SMALLINT, INTEGER or BIGINT and contains  a 1, a value of <code>true</code> is
   * returned.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>false</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public boolean getBoolean(String columnLabel) throws SQLException {
    return getBoolean(findColumn(columnLabel));
  }


  private boolean getBooleanFromString(String stringVal) throws SQLException {
    if ((stringVal != null) && (stringVal.length() > 0)) {
      int c = Character.toLowerCase(stringVal.charAt(0));
      return ((c == 't') || (c == 'y') || (c == '1') || stringVal.equals("-1"));
    }
    return false;
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>byte</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public byte getByte(int columnIndex) throws SQLException {
/*
        String val = getString(columnIndex);

        // null 和 空串都直接返回0，这是跟jdbc driver有区别的地方。
        if (StringUtil.isEmpty(val)) {
            return 0;
        }
        val = val.trim();
        try {
            int decimalIndex = val.indexOf(".");
            if (decimalIndex != -1) {
                double valueAsDouble = Double.parseDouble(val);
                if (valueAsDouble < Byte.MIN_VALUE || valueAsDouble > Byte.MAX_VALUE) {
                    throw new NumberFormatException("Out of range");
                }
                return (byte) valueAsDouble;
            }
            long valueAsLong = Long.parseLong(val);
            if (valueAsLong < Byte.MIN_VALUE || valueAsLong > Byte.MAX_VALUE) {
                throw new NumberFormatException("Out of range");
            }
            return (byte) valueAsLong;

        } catch (NumberFormatException NFE) {
            throw new BasicException(ErrCodes.COMMON_603, val);
        }*/
    return (byte) ResultSetUtil.convertValue(getObject(columnIndex), byte.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>byte</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public byte getByte(String columnLabel) throws SQLException {
    return getByte(findColumn(columnLabel));
  }


  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>byte</code> array in the Java programming language.
   * The bytes represent the raw values returned by the driver.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public byte[] getBytes(String columnLabel) throws SQLException {
    return getBytes(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>byte</code> array in the Java programming language.
   * The bytes represent the raw values returned by the driver.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public byte[] getBytes(int columnIndex) throws SQLException {
    String val = getString(columnIndex);
    if (val == null) {
      return null;
    }
    return val.getBytes();
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>short</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public short getShort(final int columnIndex) throws SQLException {
        /*String value = getString(columnIndex);
        if(StringUtil.isEmpty(value) && value.matches("\\d+")) {
            return Short.valueOf(value);
        }
        return 0;*/
    return (short) ResultSetUtil.convertValue(getObject(columnIndex), short.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>short</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public short getShort(String columnLabel) throws SQLException {
    return getShort(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * an <code>int</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public int getInt(int columnIndex) throws SQLException {
        /*String value = getString(columnIndex);
        if(StringUtil.isEmpty(value) || "null".equals(value) || "NULL".equals(value)) {
            return 0;
        }
        if(value.matches("\\d+")) {
            return Integer.valueOf(value);
        }
        return 0;*/
    return (int) ResultSetUtil.convertValue(getObject(columnIndex), int.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * an <code>int</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public int getInt(String columnLabel) throws SQLException {
    return getInt(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>long</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public long getLong(int columnIndex) throws SQLException {
        /*String value = getString(columnIndex);
        if(StringUtil.isEmpty(value) || "null".equals(value) || "NULL".equals(value)) {
            return 0;
        }
        if(value.matches("\\d+")) {
            return Long.valueOf(value);
        }
        return 0;*/
    return (long) ResultSetUtil.convertValue(getObject(columnIndex), long.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>long</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public long getLong(String columnLabel) throws SQLException {
    return getLong(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>float</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public float getFloat(int columnIndex) throws SQLException {
        /*String val = getString(columnIndex);
        if(StringUtil.isEmpty(val)) {
            return 0;
        }
        return Float.valueOf(val);*/
    return (float) ResultSetUtil.convertValue(getObject(columnIndex), float.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>float</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public float getFloat(String columnLabel) throws SQLException {
    return getFloat(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>double</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public double getDouble(int columnIndex) throws SQLException {
        /*String val = getString(columnIndex);
        if(StringUtil.isEmpty(val)) {
            return 0;
        }
        return Double.valueOf(val);*/
    return (double) ResultSetUtil.convertValue(getObject(columnIndex), double.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>double</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>0</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public double getDouble(String columnLabel) throws SQLException {
    return getDouble(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.BigDecimal</code> in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @param scale       the number of digits to the right of the decimal point
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException                    if the columnIndex is not valid; if a database access
   *                                         error occurs or this method is called on a closed
   *                                         result set
   * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
   * @deprecated Use {@code getBigDecimal(int columnIndex)} or {@code getBigDecimal(String
   * columnLabel)}
   */
  @Deprecated
  @Override
  public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        /*BigDecimal result = getBigDecimal(columnIndex);
        if(null == result) {
            return null;
        }
        return result.setScale(scale, BigDecimal.ROUND_HALF_UP);*/
    BigDecimal result = (BigDecimal) ResultSetUtil.convertValue(getObject(columnIndex), BigDecimal.class);
    return result.setScale(scale, BigDecimal.ROUND_HALF_UP);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.math.BigDecimal</code> in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @param scale       the number of digits to the right of the decimal point
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException                    if the columnLabel is not valid; if a database access
   *                                         error occurs or this method is called on a closed
   *                                         result set
   * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
   * @deprecated Use {@code getBigDecimal(int columnIndex)} or {@code getBigDecimal(String
   * columnLabel)}
   */
  @Deprecated
  @Override
  public BigDecimal getBigDecimal(String columnLabel, int scale) throws SQLException {
    return getBigDecimal(findColumn(columnLabel), scale);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a
   * <code>java.math.BigDecimal</code> with full precision.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value (full precision); if the value is SQL <code>NULL</code>, the value
   * returned is <code>null</code> in the Java programming language.
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        /*String val = getString(columnIndex);
        if (StringUtil.isEmpty(val)) {
            return null;
        }
        BigDecimal result;
        try {
            result = new BigDecimal(val);
        } catch (NumberFormatException NFE) {
            throw new BasicException(ErrCodes.COMMON_605, val, columnIndex);
        }
        return result;*/
    return (BigDecimal) ResultSetUtil.convertValue(getObject(columnIndex), BigDecimal.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a
   * <code>java.math.BigDecimal</code> with full precision.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value (full precision); if the value is SQL <code>NULL</code>, the value
   * returned is <code>null</code> in the Java programming language.
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public BigDecimal getBigDecimal(String columnLabel) throws SQLException {
    return getBigDecimal(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.Date</code> object in the Java programming language.
   * <p>
   * a <code>val</code> object representing a date in the format
   * "yyyy-[m]m-[d]d". The leading zero for <code>mm</code> and
   * <code>dd</code> may also be omitted.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Date getDate(int columnIndex) throws SQLException {
        /*String val = getString(columnIndex);
        if(null == val) {
            return null;
        }
        return Date.valueOf(val);*/
    return (Date) ResultSetUtil.convertValue(getObject(columnIndex), Date.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.Date</code> object in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Date getDate(String columnLabel) throws SQLException {
    return getDate(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.sql.Date</code> object
   * in the Java programming language.
   * This method uses the given calendar to construct an appropriate millisecond
   * value for the date if the underlying database does not store
   * timezone information.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @param cal         the <code>java.util.Calendar</code> object to use in constructing the date
   * @return the column value as a <code>java.sql.Date</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Date getDate(int columnIndex, Calendar cal) throws SQLException {
    return getDate(columnIndex);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.sql.Date</code> object
   * in the Java programming language.
   * This method uses the given calendar to construct an appropriate millisecond
   * value for the date if the underlying database does not store
   * timezone information.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause. If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @param cal         the <code>java.util.Calendar</code> object to use in constructing the date
   * @return the column value as a <code>java.sql.Date</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Date getDate(String columnLabel, Calendar cal) throws SQLException {
    return getDate(findColumn(columnLabel), cal);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.Time</code> object in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Time getTime(int columnIndex) throws SQLException {
    return (Time) ResultSetUtil.convertValue(getObject(columnIndex), Time.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.Time</code> object in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Time getTime(String columnLabel) throws SQLException {
    return getTime(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.sql.Time</code> object
   * in the Java programming language.
   * This method uses the given calendar to construct an appropriate millisecond
   * value for the time if the underlying database does not store
   * timezone information.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @param cal         the <code>java.util.Calendar</code> object to use in constructing the time
   * @return the column value as a <code>java.sql.Time</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Time getTime(int columnIndex, Calendar cal) throws SQLException {
    return getTime(columnIndex);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.sql.Time</code> object
   * in the Java programming language.
   * This method uses the given calendar to construct an appropriate millisecond
   * value for the time if the underlying database does not store
   * timezone information.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @param cal         the <code>java.util.Calendar</code> object to use in constructing the time
   * @return the column value as a <code>java.sql.Time</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Time getTime(String columnLabel, Calendar cal) throws SQLException {
    return getTime(findColumn(columnLabel), cal);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.Timestamp</code> object in the Java programming language.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Timestamp getTimestamp(int columnIndex) throws SQLException {
    return (Timestamp) ResultSetUtil.convertValue(getObject(columnIndex), Timestamp.class);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as
   * a <code>java.sql.Timestamp</code> object in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause. If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value; if the value is SQL <code>NULL</code>, the value returned is
   * <code>null</code>
   * @throws SQLException if the columnLabel is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Timestamp getTimestamp(String columnLabel) throws SQLException {
    return getTimestamp(findColumn(columnLabel));
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
   * in the Java programming language.
   * This method uses the given calendar to construct an appropriate millisecond
   * value for the timestamp if the underlying database does not store
   * timezone information.
   * <p>
   * Note: this feature is not supported currently.
   *
   * @param columnIndex the first column is 1, the second is 2, ...
   * @param cal         the <code>java.util.Calendar</code> object to use in constructing the
   *                    timestamp
   * @return the column value as a <code>java.sql.Timestamp</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException if the columnIndex is not valid; if a database access error occurs or this
   *                      method is called on a closed result set
   */
  @Override
  public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
    return getTimestamp(columnIndex);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.sql.Timestamp</code> object
   * in the Java programming language.
   * This method uses the given calendar to construct an appropriate millisecond
   * value for the timestamp if the underlying database does not store
   * timezone information.
   * <p>
   * Note: this feature is not supported currently.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause. If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @param cal         the <code>java.util.Calendar</code> object to use in constructing the date
   * @return the column value as a <code>java.sql.Timestamp</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException if the columnLabel is not valid or if a database access error occurs or
   *                      this method is called on a closed result set
   */
  @Override
  public Timestamp getTimestamp(String columnLabel, Calendar cal) throws SQLException {
    return getTimestamp(findColumn(columnLabel), cal);
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.net.URL</code>
   * object in the Java programming language.
   *
   * @param columnIndex the index of the column 1 is the first, 2 is the second,...
   * @return the column value as a <code>java.net.URL</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException                    if the columnIndex is not valid; if a database access
   *                                         error occurs; this method is called on a closed result
   *                                         set or if a URL is malformed
   * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
   */
  @Override
  public URL getURL(int columnIndex) throws SQLException {
    String val = getString(columnIndex);

    if (null == val) {
      return null;
    }
    try {
      return new URL(val);
    } catch (MalformedURLException exception) {
      throw new GenericException(JdbcErrCodes.JDBC_602, val);
    }
  }

  /**
   * Retrieves the value of the designated column in the current row
   * of this <code>ResultSet</code> object as a <code>java.net.URL</code>
   * object in the Java programming language.
   *
   * @param columnLabel the label for the column specified with the SQL AS clause.  If the SQL AS
   *                    clause was not specified, then the label is the name of the column
   * @return the column value as a <code>java.net.URL</code> object; if the value is SQL
   * <code>NULL</code>, the value returned is <code>null</code> in the Java programming language
   * @throws SQLException                    if the columnLabel is not valid; if a database access
   *                                         error occurs; this method is called on a closed result
   *                                         set or if a URL is malformed
   * @throws SQLFeatureNotSupportedException if the JDBC driver does not support this method
   */
  @Override
  public URL getURL(String columnLabel) throws SQLException {
    return getURL(findColumn(columnLabel));
  }


  @SuppressWarnings("unchecked")
  public <T> T getObject(int columnIndex, Class<T> type) throws SQLException {
    if (type == null) {
      throw new RuntimeException("Type parameter can not be null");
    }
    if (type.equals(String.class)) {
      return (T) getString(columnIndex);
    } else if (type.equals(BigDecimal.class)) {
      return (T) getBigDecimal(columnIndex);
    } else if (type.equals(Boolean.class) || type.equals(Boolean.TYPE)) {
      return (T) Boolean.valueOf(getBoolean(columnIndex));
    } else if (type.equals(Integer.class) || type.equals(Integer.TYPE)) {
      return (T) Integer.valueOf(getInt(columnIndex));
    } else if (type.equals(Long.class) || type.equals(Long.TYPE)) {
      return (T) Long.valueOf(getLong(columnIndex));
    } else if (type.equals(Float.class) || type.equals(Float.TYPE)) {
      return (T) Float.valueOf(getFloat(columnIndex));
    } else if (type.equals(Double.class) || type.equals(Double.TYPE)) {
      return (T) Double.valueOf(getDouble(columnIndex));
    } else if (type.equals(byte[].class)) {
      return (T) getBytes(columnIndex);
    } else if (type.equals(java.sql.Date.class)) {
      return (T) getDate(columnIndex);
    } else if (type.equals(Time.class)) {
      return (T) getTime(columnIndex);
    } else if (type.equals(Timestamp.class)) {
      return (T) getTimestamp(columnIndex);
    } else if (type.equals(Clob.class)) {
      return (T) getClob(columnIndex);
    } else if (type.equals(Blob.class)) {
      return (T) getBlob(columnIndex);
    } else if (type.equals(Array.class)) {
      return (T) getArray(columnIndex);
    } else if (type.equals(Ref.class)) {
      return (T) getRef(columnIndex);
    } else if (type.equals(URL.class)) {
      return (T) getURL(columnIndex);
    } else {
      return type.cast(getObject(columnIndex));
    }
  }

  @Override
  public <T> T getObject(String columnLabel, Class<T> type) throws SQLException {
    return getObject(findColumn(columnLabel), type);
  }

  /**
   * 只有在SQL只路由到一个分片时，这个方法才有实现的可能。
   * 考虑将此接口设置成unsupported.
   *
   * @return Statement
   * @throws SQLException if a database access error occurs
   */
  @Override
  public Statement getStatement() throws SQLException {
    return null;
  }

  /**
   * 默认采用first-to-last
   */
  @Override
  public int getFetchDirection() throws SQLException {
    return ResultSet.FETCH_FORWARD;
  }

  /**
   * 只支持ResultSet.TYPE_FORWARD_ONLY
   *
   * @return ResultSet.TYPE_FORWARD_ONLY
   * @throws SQLException 数据库异常
   */
  @Override
  public int getType() throws SQLException {
    return ResultSet.TYPE_FORWARD_ONLY;
  }

  /**
   * 只支持ResultSet.CONCUR_READ_ONLY
   */
  @Override
  public int getConcurrency() throws SQLException {
    return ResultSet.CONCUR_READ_ONLY;
  }


  @Override
  public Clob getClob(int columnIndex) throws SQLException {
    return (Clob) getObject(columnIndex);
  }

  @Override
  public Clob getClob(String columnLabel) throws SQLException {
    return (Clob) getObject(columnLabel);
  }

  @Override
  public Blob getBlob(int columnIndex) throws SQLException {
    return (Blob) getObject(columnIndex);
  }

  @Override
  public Blob getBlob(String columnLabel) throws SQLException {
    return (Blob) getObject(columnLabel);
  }
}
