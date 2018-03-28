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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.Executor;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * RaptorConnection不支持的方法测试
 *
 * @author Sam Tsai
 * @since 3.0.0
 */
public class RaptorConnectionUnsupportedTest extends AutoPrepareTestingEnv {

  //=====================================================================//
  // UNSUPPORTED METHODS TEST CASES
  //=====================================================================//

  private static final String U_SQL = "select * from table";

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setTransactionIsolation() throws SQLException {
    connection.setTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getMetaData() throws SQLException {
    connection.getMetaData();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_prepareStatement1() throws SQLException {
    connection.prepareStatement(U_SQL, Statement.RETURN_GENERATED_KEYS);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_prepareStatement2() throws SQLException {
    connection.prepareStatement(U_SQL, new int[]{1, 2});
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_prepareStatement3() throws SQLException {
    connection.prepareStatement(U_SQL, new String[]{"column1", "column2"});
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_prepareCall1() throws SQLException {
    connection.prepareCall(U_SQL);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_prepareCall2() throws SQLException {
    connection.prepareCall(U_SQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_prepareCall3() throws SQLException {
    connection.prepareCall(U_SQL, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE,
        ResultSet.HOLD_CURSORS_OVER_COMMIT);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_nativeSQL() throws SQLException {
    connection.nativeSQL(U_SQL);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_releaseSavepoint() throws SQLException {
    connection.releaseSavepoint(new Savepoint() {
      @Override
      public int getSavepointId() throws SQLException {
        return 0;
      }

      @Override
      public String getSavepointName() throws SQLException {
        return null;
      }
    });
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_setSavepoint1() throws SQLException {
    connection.setSavepoint();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_setSavepoint2() throws SQLException {
    connection.setSavepoint("SAVE_POINT");
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_rollback() throws SQLException {
    connection.rollback(new Savepoint() {
      @Override
      public int getSavepointId() throws SQLException {
        return 0;
      }

      @Override
      public String getSavepointName() throws SQLException {
        return null;
      }
    });
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_abort() throws SQLException {
    connection.abort(new Executor() {
      @Override
      public void execute(Runnable command) {

      }
    });
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getCatalog() throws SQLException {
    connection.getCatalog();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_setCatalog() throws SQLException {
    connection.setCatalog("CATALOG");
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getSchema() throws SQLException {
    connection.getSchema();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_setSchema() throws SQLException {
    connection.setSchema("SCHEMA");
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getTypeMap() throws SQLException {
    connection.getTypeMap();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_setTypeMap() throws SQLException {
    connection.setTypeMap(new HashMap<String, Class<?>>());
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getNetworkTimeout() throws SQLException {
    connection.getNetworkTimeout();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_setNetworkTimeout() throws SQLException {
    connection.setNetworkTimeout(new Executor() {
      @Override
      public void execute(Runnable command) {

      }
    }, 1);
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_createClob() throws SQLException {
    connection.createClob();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_createBlob() throws SQLException {
    connection.createBlob();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_createNClob() throws SQLException {
    connection.createNClob();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_createSQLXML() throws SQLException {
    connection.createSQLXML();
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_createArrayOf() throws SQLException {
    connection.createArrayOf("INTEGER", new Object[]{});
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_createStruct() throws SQLException {

    connection.createStruct("INTEGER", new Object[]{});

  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_isValid() throws SQLException {

    connection.isValid(1);

  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getClientInfo1() throws SQLException {

    connection.getClientInfo();

  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void testUnsupportedConnectionMethod_getClientInfo2() throws SQLException {

    connection.getClientInfo("");

  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedConnectionMethod_setClientInfo1() throws SQLException {

    connection.setClientInfo("", "");

  }

  @Test(expected = UnsupportedOperationException.class)
  public void testUnsupportedConnectionMethod_setClientInfo2() throws SQLException {

    connection.setClientInfo(new Properties());

  }

  /**
   * ResultSet.TYPE_FORWARD_ONLY,
   * ResultSet.CONCUR_READ_ONLY
   */
  @Test(expected = UnsupportedOperationException.class)
  public void testExecuteQueryStatementArgs() throws SQLException {
    try (
        Statement statement = connection
            .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY);
        ResultSet resultSet = statement.executeQuery("select * from customer where id = 1")
    ) {
      Assert.assertNotNull(resultSet);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testExecuteQueryStatementArgs2() throws SQLException {
    try (
        Statement statement = connection
            .createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
        ResultSet resultSet = statement.executeQuery("select * from customer where id = 1")
    ) {
      Assert.assertNotNull(resultSet);
    }
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testPrepareStatementArgs2() throws SQLException {
    String sql = "select id, name, gender, create_date from customer where id = ?";
    try (
        PreparedStatement statement = connection
            .prepareStatement(sql, ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE,
                ResultSet.HOLD_CURSORS_OVER_COMMIT);
    ) {
      statement.setLong(1, 2);
      try (
          ResultSet resultSet = statement.executeQuery();
      ) {
        while (resultSet.next()) {
          Assert.assertThat(resultSet.getLong(1), Is.is(2L));
          Assert.assertThat(resultSet.getString(2), Is.is("萧景琰"));
          Assert.assertThat(resultSet.getInt(3), Is.is(1));
          Assert.assertThat(resultSet.getTimestamp(4).toString(), Is.is("2016-11-01 11:01:00.0"));
        }
      }
    }
  }

}