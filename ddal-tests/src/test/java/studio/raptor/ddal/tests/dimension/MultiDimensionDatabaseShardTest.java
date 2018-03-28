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

package studio.raptor.ddal.tests.dimension;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 单分片表Select测试
 * <p>
 * <p>
 * 场景1：单分片Select
 * 场景2：多分片Select
 * 场景3：单库Join
 * </p>
 *
 * @author Charley
 * @since 1.0
 */
public class MultiDimensionDatabaseShardTest extends AutoPrepareTestingEnv {

  @Test
  public void testWithTwoShardValue() throws SQLException {
    String sql = "select * from multi_shard_table where primary_key = ? and second_key = ?";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 100);
      preparedStatement.setLong(2, 1001);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(100, resultSet.getInt(1));
        Assert.assertEquals(1001, resultSet.getInt(2));
        Assert.assertEquals("南京艺术学院", resultSet.getString(3));
        Assert.assertFalse(resultSet.next());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithPrimaryShardValue() throws SQLException {
    String sql = "select * from multi_shard_table where primary_key = ?";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 200);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        int index = 0;
        while(resultSet.next()){
          index++;
        }
        Assert.assertEquals(2, index);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithSecondShardValue() throws SQLException {
    String sql = "select * from multi_shard_table where second_key = ?";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 2000);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        int index = 0;
        while(resultSet.next()){
          index++;
        }
        Assert.assertEquals(2, index);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithNoShardValue() throws SQLException {
    String sql = "select * from multi_shard_table";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()) {
        int index = 0;
        while(resultSet.next()){
          index++;
        }
        Assert.assertEquals(8, index);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
