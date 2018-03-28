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

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.ExecuteException;
import studio.raptor.ddal.common.exception.ExecuteException.ExecutionErrorOnPhysicalDBException;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.jdbc.JDBCType;

/**
 * 预编译语句（RaptorPreparedStatement）测试案例。
 *
 * @author Sam Tsai
 * @since 3.0.0
 */
public class RaptorPreparedStatementTest extends AutoPrepareTestingEnv {

  @Test
  public void testExecuteQuery_Select_One() throws SQLException {
    String sql = "select 1";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      try (
          ResultSet resultSet = statement.executeQuery();
      ) {
        int oneRowOnly = 0;
        while (resultSet.next()) {
          oneRowOnly++;
          Assert.assertThat(resultSet.getString(1), Is.is("1"));
        }
        Assert.assertThat(oneRowOnly, Is.is(1));
      }
    }
  }

  @Test
  public void testExecuteUpdate() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, new Random().nextInt(100000) + 100000);
      statement.setString(2, "Customer Name");
      statement.setInt(3, 1);
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      int updateRows = statement.executeUpdate();
      Assert.assertThat(updateRows, Is.is(1));

      statement.setLong(1, new Random().nextInt(100000) + 100000);
      statement.setString(2, "Customer Name");
      statement.setInt(3, 1);
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()), Calendar.getInstance());
      updateRows = statement.executeUpdate();
      Assert.assertThat(updateRows, Is.is(1));
      connection.commit();
    }
  }

  @Test
  public void testExecuteQuery() throws SQLException {
    String sql = "select id, name, gender, create_date from customer where id = ?";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 2);
      try (
          ResultSet resultSet = statement.executeQuery();
      ) {
        while (resultSet.next()) {
          Assert.assertThat(resultSet.getLong(1), Is.is(2L));
          Assert.assertThat(resultSet.getInt(3), Is.is(1));
          Assert.assertThat(resultSet.getTimestamp(4).toString(), Is.is("2016-11-01 11:01:00.0"));
        }
      }
    }
  }

  @Test
  public void testInsertNull() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "xxxx");
      statement.setNull(3, JDBCType.INTEGER.getVendorTypeNumber());
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      statement.executeUpdate();
    } catch (Exception e) {
      if (e instanceof GenericException) {
        Assert.assertThat(((GenericException) e).getAdditionalInfo(),
            Is.is("NULL not allowed for column \"GENDER\"; SQL statement:\n" +
                "INSERT INTO schema0.customer (id, name, gender, create_date) VALUES (?, ?, ?, ?) [23502-193]"));
      }
    }

    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setNull(2, JDBCType.VARCHAR.getVendorTypeNumber(), JDBCType.VARCHAR.getName());
      statement.setInt(3, 1);
      statement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
      statement.executeUpdate();
    } catch (Exception e) {
      if (e instanceof GenericException) {
        Assert.assertThat(((GenericException) e).getAdditionalInfo(),
            Is.is("NULL not allowed for column \"NAME\"; SQL statement:\n" +
                "INSERT INTO schema0.customer (id, name, gender, create_date) VALUES (?, ?, ?, ?) [23502-193]"));
      }
    }
  }


  @Test(expected = ExecuteException.ExecutionErrorOnPhysicalDBException.class)
  public void testSetBoolean() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "xxxx");
      statement.setInt(3, 1);
      statement.setBoolean(4, false);
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecuteException.ExecutionErrorOnPhysicalDBException.class)
  public void testSetByte() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setInt(3, 1);
      statement.setByte(4, (byte) 1);
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetShort() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setInt(3, 1);
      statement.setShort(4, (short) 2);
      statement.executeUpdate();
    }
  }


  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetFloat() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setInt(3, 1);
      statement.setFloat(4, 2f);
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetDouble() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setInt(3, 1);
      statement.setDouble(4, 2d);
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetBigDecimal() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setInt(3, 1);
      statement.setBigDecimal(4, new BigDecimal(201701229111L));
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetDate() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setDate(3, new Date(1485051585368L));
      statement.setDate(4, new Date(System.currentTimeMillis()));
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetDateCalender() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setDate(3, new Date(1485051585368L), Calendar.getInstance());
      statement.setDate(4, new Date(System.currentTimeMillis()));
      statement.executeUpdate();
    }
  }


  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetTime() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setTime(3, new Time(1485051585368L));
      statement.setDate(4, new Date(System.currentTimeMillis()));
      statement.executeUpdate();
    }
  }

  @Test(expected = ExecutionErrorOnPhysicalDBException.class)
  public void testSetTimeCalendar() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (
        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setLong(1, 137634);
      statement.setString(2, "hello world");
      statement.setTime(3, new Time(1485051585368L), Calendar.getInstance());
      statement.setDate(4, new Date(System.currentTimeMillis()));
      statement.executeUpdate();
    }
  }
}