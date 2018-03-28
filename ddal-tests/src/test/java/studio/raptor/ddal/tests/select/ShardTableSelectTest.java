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

package studio.raptor.ddal.tests.select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.GenericException;
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
public class ShardTableSelectTest extends AutoPrepareTestingEnv {

  /**
   * 单分片Select
   */
  @Test
  public void testSelectWithSingleShard() {
    String sql = "select * from teacher where tno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 2012112604);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(2012112604, resultSet.getInt("tno"));
        Assert.assertEquals("王萍", resultSet.getString(2));
        Assert.assertEquals("女", resultSet.getString(3));
        Assert.assertEquals("18052028579", resultSet.getString(5));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 多分片Select
   */
  @Test
  public void testSelectWithMultiShard() {
    String sql = "select * from teacher where sex = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, "女");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        int index = 0;
        while (resultSet.next()) {
          index++;
        }
        Assert.assertEquals(9, index);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 与全局表Join Select
   */
  @Test
  public void testSelectJoinGlobalTable() {
    String sql = "select course.* from teacher, course where teacher.tno = course.tno and teacher.tno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 2012112607);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        int index = 0;
        while (resultSet.next()) {
          index++;
        }
        Assert.assertEquals(3, index);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 与分片表Join Select，只有一个分片条件
   */
  @Test
  public void testSelectJoinShardTableWithOneCondition() {
    String sql = "select sc.* from student st, scores sc where sc.sno = st.sno and st.sno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 200901020104L);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        int index = 0;
        while (resultSet.next()) {
          index++;
        }
        Assert.assertEquals(13, index);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 分表全分片查询.
   * 签到表两个分表的数据总数是10
   */
  @Test
  public void testSelectShardTables_noCondition() {
    String sql = "select sign_id, sno, position, sign_time from sign_records";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()
    ) {
      int count = 0;
      while (resultSet.next()) {
        count++;
      }
      Assert.assertEquals(10, count);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 分表全分片与配置表Join查询.
   * 签到表两个分表的数据总数是10
   */
  @Test
  public void testSelectShardTablesJoin_noCondition() {
    String sql = "select a.sign_id, a.sno, a.position, b.position_name, a.sign_time from sign_records a, sign_position_spec b where a.position = b.position_id;";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()
    ) {
      int count = 0;
      while (resultSet.next()) {
        count++;
      }
      Assert.assertEquals(10, count);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 不支持的操作，两张分片表Join
   */
  @Test(expected = GenericException.class)
  public void testSelectWithUnsupportSQL() {
    String sql = "select t.*, s.* from teacher t, student s where t.tno = ? and s.sno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 2012112601);
      preparedStatement.setLong(2, 200901020104L);
      preparedStatement.executeQuery();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
