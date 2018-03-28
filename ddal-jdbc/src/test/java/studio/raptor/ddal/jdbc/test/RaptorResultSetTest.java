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
import java.sql.Statement;
import java.sql.Timestamp;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;

/**
 * RaptorResultSet 单元测试
 *
 * @author Sam
 * @since 3.0.0
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RaptorResultSetTest extends AutoPrepareTestingEnv {

  @Test
  public void testGetString() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement.executeQuery("select * from crm222.customer a limit 0, 10");
      ) {
        // use first row
        if (resultSet.next()) {
          Assert.assertThat(resultSet.getString(1), Is.is("2"));
        }
      }
    }
  }

  @Test
  public void testGetTimestamp() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement.executeQuery("select * from crm222.customer limit 0, 10");
      ) {
        // 只用第一条数据
        if (resultSet.next()) {
          Timestamp expectedTimestamp = Timestamp.valueOf("2016-11-01 11:01:00");
          Assert.assertThat(resultSet.getTimestamp(4), Is.is(expectedTimestamp));
          Assert.assertThat(resultSet.getTimestamp("create_date"), Is.is(expectedTimestamp));
        }
      }
    }
  }

  @Test
  public void testGetObject() throws SQLException {

    try (

        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement.executeQuery("select * from crm222.customer limit 0, 10");
      ) {
        // 只用第一条数据
        if (resultSet.next()) {
          Object indexObj = resultSet.getObject(4);
          Object labelObj = resultSet.getObject("create_date");
          Assert.assertTrue(null != indexObj && indexObj instanceof Timestamp);
          Assert.assertTrue(null != labelObj && labelObj instanceof Timestamp);
        }
      }
    }
  }

  @Test
  public void testUpdateCustomer() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      int affectedRows = statement
          .executeUpdate("update crm222.customer set gender = '0' where id = 1");
      Assert.assertThat(affectedRows, Is.is(1));
    }
  }

  @Test
  public void testInsert() throws SQLException {

    try (

        Statement statement = connection.createStatement()
    ) {
      int affectedRows = statement.executeUpdate(
          "insert into customer (id, name, gender, create_date) values (11, '萧景琰', 1, '2016-11-01 11:01:00')");
      Assert.assertThat(affectedRows, Is.is(1));
    }
  }

  @Test
  public void testZDelete() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      int affectedRows = statement.executeUpdate("delete from customer where id = 2");
      Assert.assertThat(affectedRows, Is.is(1));
    }
  }
}
