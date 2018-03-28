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


import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;

import studio.raptor.ddal.jdbc.adapter.WrapperAdapter;

/**
 * Declare a static statement object that does not support actions.
 *
 * @author jackcao
 * @since 3.0
 */
public abstract class AbstractUnsupportedOperationStatement extends WrapperAdapter implements Statement {

  @Override
  public final ResultSet getGeneratedKeys() throws SQLException {
    throw new SQLFeatureNotSupportedException("getGeneratedKeys");
  }

  @Override
  public final int getFetchDirection() throws SQLException {
    throw new SQLFeatureNotSupportedException("getFetchDirection");
  }

  @Override
  public final void setFetchDirection(final int direction) throws SQLException {
    throw new SQLFeatureNotSupportedException("setFetchDirection");
  }

  @Override
  public final void addBatch(final String sql) throws SQLException {
    throw new SQLFeatureNotSupportedException("addBatch sql");
  }

  @Override
  public void clearBatch() throws SQLException {
    throw new SQLFeatureNotSupportedException("clearBatch");
  }

  @Override
  public int[] executeBatch() throws SQLException {
    throw new SQLFeatureNotSupportedException("executeBatch");
  }

  @Override
  public final void closeOnCompletion() throws SQLException {
    throw new SQLFeatureNotSupportedException("closeOnCompletion");
  }

  @Override
  public final boolean isCloseOnCompletion() throws SQLException {
    throw new SQLFeatureNotSupportedException("isCloseOnCompletion");
  }
}
