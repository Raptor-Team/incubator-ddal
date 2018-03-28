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

package studio.raptor.ddal.jdbc.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import org.junit.Test;

/**
 * @author Sam
 * @since 3.0.0
 */
public class RaptorStatementUnsupportedTest extends AutoPrepareTestingEnv {

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getGeneratedKeys() throws SQLException {
    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.getGeneratedKeys();
    ) {
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getFetchDirection() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      statement.getFetchDirection();
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_clearBatch() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      statement.clearBatch();
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_executeBatch() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      statement.executeBatch();
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_closeOnCompletion() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      statement.closeOnCompletion();
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_isCloseOnCompletion() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      statement.isCloseOnCompletion();
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setFetchDirection() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      statement.setFetchDirection(1);
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_addBatch() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      statement.addBatch("select * from table");
    }
  }
}
