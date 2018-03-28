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

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 测试读写连接存在时，执行带只读注解的场景。
 * 预期的结果是，带有只读注释的SQL仍然使用读写连接执行。
 *
 * @author Sam
 * @since 3.1.0
 */
public class ReadOnlyHintWithinPreviousTxTest extends AutoPrepareTestingEnv {

  // 正常SQL，即使用读写连接的SQL
  String sql = "select tno, tname, sex, age, tphone from teacher where tno = 2012112601";

  // 指定seq=2的读库执行
  String readonlySeq2Sql = "/*!hint readonly(2) */select tno, tname, sex, age, tphone from teacher where tno = 2012112601";


  /**
   * 测试读写连接存在时，执行带只读注解的场景。
   * 预期的结果是，带有只读注释的SQL仍然使用读写连接执行。
   * 所以最终结果，教师的手机号码都是读写库的18052028779
   *
   * @throws SQLException sql exception
   */
  @Test
  public void testReadonlyHintInPreviousTx() throws SQLException {

    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {
      int count = 0;
      String tel = "";
      while (resultSet.next()) {
        count++;
        tel = resultSet.getString(5);
      }
      Assert.assertEquals(1, count);
      Assert.assertEquals("18052028779", tel);
    }

    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(readonlySeq2Sql)
    ) {
      int count = 0;
      String tel = "";
      while (resultSet.next()) {
        count++;
        tel = resultSet.getString(5);
      }
      Assert.assertEquals(1, count);
      Assert.assertEquals("18052028779", tel);
    }
  }
}
