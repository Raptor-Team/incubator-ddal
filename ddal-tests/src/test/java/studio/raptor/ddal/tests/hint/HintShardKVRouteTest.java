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

package studio.raptor.ddal.tests.hint;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 隐藏分片键值对路由测试
 *
 * @author Charley
 * @since 1.0
 */
public class HintShardKVRouteTest extends AutoPrepareTestingEnv {

  /**
   * 通过Hint线索路由至shard_0。
   * tno(2012112616) -> shard_0
   */
  @Test
  public void hintRouteShard0() {
    String sql = "/*!hint shard(tno=2012112616)*/select * from teacher";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      int index = 0;
      while (resultSet.next()) {
        index++;
      }
      Assert.assertEquals(4, index);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    sql = "select * from teacher";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      int index = 0;
      while (resultSet.next()) {
        index++;
      }
      Assert.assertEquals(16, index);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    sql = "/*!hint shard(tno=2012112615)*/select * from teacher";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      int index = 0;
      while (resultSet.next()) {
        index++;
      }
      Assert.assertEquals(3, index);
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }

  /**
   * 通过Hint线索路由至shard_0，然后带有where条件的语句在
   * 路由记过的分片中执行。
   * hint上的tno并不是查询条件。
   *
   * tno(2012112616) -> shard_0
   */
  @Test
  public void hintRouteShard0_AndWhere() {
    String sql = "/*!hint shard(tno=2012112616)*/select * from teacher where tphone = '18052028279'";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
    ) {
      int index = 0;
      while (resultSet.next()) {
        index++;
      }
      Assert.assertEquals(1, index);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 通过Hint线索路由至shard_1。
   * tno(2012112616) -> shard_1
   */
  @Test
  public void testHint() {
    String sql = "/*!hint shard(tno=2012112613)*/select * from teacher";
    try (
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();
    ) {

      int index = 0;
      while (resultSet.next()) {
        index++;
      }
      Assert.assertEquals(5, index);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
