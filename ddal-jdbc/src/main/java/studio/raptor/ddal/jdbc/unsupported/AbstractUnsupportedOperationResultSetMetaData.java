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

package studio.raptor.ddal.jdbc.unsupported;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;

/**
 * @author Sam
 * @since 3.0.0
 */
public abstract class AbstractUnsupportedOperationResultSetMetaData implements ResultSetMetaData {


  @Override
  public int getPrecision(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("getPrecision");
  }

  @Override
  public int getScale(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("getScale");
  }

  @Override
  public String getCatalogName(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("getCatalogName");
  }

  @Override
  public boolean isReadOnly(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("isReadOnly");
  }

  @Override
  public boolean isWritable(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("isWritable");
  }

  @Override
  public boolean isDefinitelyWritable(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("isDefinitelyWritable");
  }

  @Override
  public String getColumnClassName(int column) throws SQLException {
    throw new SQLFeatureNotSupportedException("getColumnClassName");
  }

  @Override
  public <T> T unwrap(Class<T> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException("unwrap");
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    throw new SQLFeatureNotSupportedException("isWrapperFor");
  }

}
