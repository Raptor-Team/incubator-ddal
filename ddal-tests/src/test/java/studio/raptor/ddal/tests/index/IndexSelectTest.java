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

package studio.raptor.ddal.tests.index;

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
public class IndexSelectTest extends AutoPrepareTestingEnv {

  /**
   * 带分片条件的索引查询
   */
  @Test
  public void testIndexWithShardCondition() {
    String sql = "select * from scores where sno = ? and cno= ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 200901020101L);
      preparedStatement.setLong(2, 112618);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("SCORES", resultSet.getMetaData().getTableName(1));
        Assert.assertEquals(100, resultSet.getLong(1));
        Assert.assertEquals(200901020101L, resultSet.getLong(2));
        Assert.assertEquals(112618, resultSet.getLong(3));
        Assert.assertEquals("2011-2012第二学期", resultSet.getString(4));
        Assert.assertEquals("98", resultSet.getString(5));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 不带分片条件的索引查询
   */
  @Test
  public void testIndexTable() {
    String sql = "select * from _scores_cno where cno= ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 112618);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("_SCORES_CNO", resultSet.getMetaData().getTableName(1));
        Assert.assertEquals(100, resultSet.getLong(1));
        Assert.assertEquals(200901020101L, resultSet.getLong(2));
        Assert.assertEquals(112618, resultSet.getLong(3));
        Assert.assertEquals("2011-2012第二学期", resultSet.getString(4));
        Assert.assertEquals("98", resultSet.getString(5));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 不带分片条件的索引查询
   */
  @Test
  public void testIndexWithoutShardCondition() {
    String sql = "select * from scores where cno= ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 112618);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("_SCORES_CNO", resultSet.getMetaData().getTableName(1));
        Assert.assertEquals(100, resultSet.getLong(1));
        Assert.assertEquals(200901020101L, resultSet.getLong(2));
        Assert.assertEquals(112618, resultSet.getLong(3));
        Assert.assertEquals("2011-2012第二学期", resultSet.getString(4));
        Assert.assertEquals("98", resultSet.getString(5));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
