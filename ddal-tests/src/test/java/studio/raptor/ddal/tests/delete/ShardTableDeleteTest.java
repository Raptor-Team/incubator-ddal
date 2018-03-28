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

package studio.raptor.ddal.tests.delete;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 功能描述
 *
 * @author Charley
 * @since 3.0.0
 */
public class ShardTableDeleteTest extends AutoPrepareTestingEnv {

  /**
   * 单分片Delete
   */
  @Test
  public void testDeleteWithSingleShard() {
    String sql = "delete from crm.teacher where tno = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 2012112604);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 多分片Delete，有分片字段
   */
  @Test
  public void testDeleteMultiShardWithShardKV() {
    String sql = "delete from teacher where tno in (?, ?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(2, 2012112606);
      preparedStatement.setLong(1, 2012112605);
      preparedStatement.setLong(3, 2012112607);
      preparedStatement.setLong(4, 2012112608);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(4, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * 多分片Delete，无分片字段
   */
  @Test
  public void testDeleteMultiShardWithoutShardKV() {
    String sql = "delete from student where sex = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, "女");
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(17, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
