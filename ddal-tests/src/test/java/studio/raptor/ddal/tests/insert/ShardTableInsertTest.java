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

package studio.raptor.ddal.tests.insert;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 分片表Insert测试
 *
 * @author Charley
 * @since 1.0
 */
public class ShardTableInsertTest extends AutoPrepareTestingEnv {

  /**
   * 单分片Insert
   */
  @Test
  public void testInsertWithSingleShard() {
    String sql = "insert into teacher (tno, tname, sex, tphone) values (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, 2017112616);
      preparedStatement.setString(2, "王城");
      preparedStatement.setString(3, "男");
      preparedStatement.setString(4, "123124214123");
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }


  /**
   * 不支持的语句
   */
  @Test(expected = GenericException.class)
  public void testInsertWithUnsupportSQL() {
    String sql = "insert into teacher (tname, sex, tphone) values (?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, "王城");
      preparedStatement.setString(2, "男");
      preparedStatement.setString(3, "123124214123");
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
