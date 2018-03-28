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

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import javax.sql.DataSource;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import studio.raptor.ddal.jdbc.RaptorDataSource;
import studio.raptor.ddal.jdbc.test.helper.H2DatabaseHelper;

/**
 * Raptor datasource test.
 *
 * @author Sam
 * @since 3.0
 */
public class RaptorDataSourceTest {

  @BeforeClass
  public static void setup() throws SQLException, IOException {
    H2DatabaseHelper h2DatabaseHelper = new H2DatabaseHelper("~", "db1", 2);
    h2DatabaseHelper.prepareH2Database();
  }

  @Test
  public void testGetMySQLConnection() throws SQLException, IOException {

    DataSource dataSource = new RaptorDataSource("crmdb", "mysql");
    dataSource.setLogWriter(new PrintWriter(System.out));
    dataSource.getLogWriter().write("");

    // do nothing for parent log
    dataSource.getParentLogger();

    try (Connection connection = dataSource.getConnection()) {
      Assert.assertNotNull(connection);

      Assert.assertNull(connection.getWarnings());
      connection.clearWarnings();
      connection.setHoldability(ResultSet.CLOSE_CURSORS_AT_COMMIT);
      Assert.assertThat(connection.getHoldability(), Is.is(ResultSet.CLOSE_CURSORS_AT_COMMIT));
      Assert.assertThat(connection.getTransactionIsolation(),
          Is.is(Connection.TRANSACTION_READ_UNCOMMITTED));

      Assert.assertThat(connection.isReadOnly(), Is.is(true));
      connection.setReadOnly(false);
      Assert.assertThat(connection.isReadOnly(), Is.is(false));

      Assert.assertThat(connection.isClosed(), Is.is(false));
      connection.close();
      Assert.assertThat(connection.isClosed(), Is.is(true));
    }
  }

  @Test
  public void testGetOracleConnection() throws SQLException, IOException {
    DataSource dataSource = new RaptorDataSource("crmdb", "oracle");
    try (Connection connection = dataSource.getConnection()) {
      Assert.assertNotNull(connection);
    }
    try (Connection connection = dataSource.getConnection("username", "password")) {
      Assert.assertNotNull(connection);
    }
  }

  //=====================================================================//
  // UNSUPPORTED METHODS TEST CASES
  //=====================================================================//

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedMethods_getLoginTimeout() throws SQLException, IOException {
    DataSource dataSource = new RaptorDataSource("crmdb", "oracle");
    dataSource.getLoginTimeout();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedMethods_setLoginTimeout() throws SQLException, IOException {
    DataSource dataSource = new RaptorDataSource("crmdb", "oracle");
    dataSource.setLoginTimeout(1);
  }
}
