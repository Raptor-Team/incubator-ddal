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
public class IndexIUDTest extends AutoPrepareTestingEnv {

  /**
   * 索引更新
   */
  @Test
  public void testIndexUpdate() {
    String updateSql = "update scores set grade='89' where scorceno= ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(updateSql)) {
      preparedStatement.setLong(1, 110);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String selectSql = "select grade from _scores_cno where cno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
      preparedStatement.setLong(1, 112605);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("_SCORES_CNO", resultSet.getMetaData().getTableName(1));
        Assert.assertEquals("89", resultSet.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * 索引插入
   */
  @Test
  public void testIndexInsert() {
    String insertSql = "insert into ddal_test_1.scores(scorceno,sno,cno,term,grade) values(1100,200901020111,9112618,'test','98');";
    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String selectSql = "select term from _scores_cno where cno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
      preparedStatement.setLong(1, 9112618);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("_SCORES_CNO", resultSet.getMetaData().getTableName(1));
        Assert.assertEquals("test", resultSet.getString(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 索引删除
   */
  @Test
  public void testIndexDelete() {
    String insertSql = "insert into ddal_test_1.scores(scorceno,sno,cno,term,grade) values(1100,200901020111,8112618,'test1','98')";
    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String deleteSql = "delete from scores where cno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(deleteSql)) {
      preparedStatement.setLong(1, 8112618);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String selectSql = "select term from _scores_cno where cno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql)) {
      preparedStatement.setLong(1, 8112618);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertEquals("_SCORES_CNO", resultSet.getMetaData().getTableName(1));
        Assert.assertFalse(resultSet.next());
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
