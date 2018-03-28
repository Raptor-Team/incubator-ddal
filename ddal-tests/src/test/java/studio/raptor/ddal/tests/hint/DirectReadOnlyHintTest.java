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
import java.util.Arrays;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 只读注解单元测试。 执行带ReadOnly注解的SQL之前不存在任何
 * 读写的事务。
 *
 * @author Sam
 * @since 3.1.0
 */
public class DirectReadOnlyHintTest extends AutoPrepareTestingEnv {

  // 读库随机负载策略
  String readonlySql = "/*!hint readonly */select tno, tname, sex, age, tphone from teacher where tno = 2012112601";

  // 指定seq=2的读库执行
  String readonlySeq2Sql = "/*!hint readonly(2) */select tno, tname, sex, age, tphone from teacher where tno = 2012112601";

  // 指定seq=1的读库执行
  String readonlySeq1Sql = "/*!hint readonly(1) */select tno, tname, sex, age, tphone from teacher where tno = 2012112601";

  /**
   * 随机查询三个读库，查询到的号码只可能是110， 120， 130三种情况。
   *
   * @throws SQLException sql exception
   */
  @Test
  public void testReadonlyHint() throws SQLException {
    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(readonlySql)
    ) {
      int count = 0;
      String tel = "";
      while (resultSet.next()) {
        count++;
        tel = resultSet.getString(5);
        System.out.println(String.format("查询SQL：%s\n手机号码：%s", readonlySql, tel));
      }
      Assert.assertEquals(1, count);
      Assert.assertTrue(Arrays.asList("110", "120", "130").contains(tel));
    }
  }

  /**
   * 查询序号为1的读库，查询到的号码只能是110。
   *
   * @throws SQLException sql exception
   */
  @Test
  public void testSeq1ReadonlyHint() throws SQLException {
    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(readonlySeq1Sql)
    ) {
      int count = 0;
      String tel = "";
      while (resultSet.next()) {
        count++;
        tel = resultSet.getString(5);
        System.out.println(String.format("查询SQL：%s\n手机号码：%s", readonlySeq1Sql, tel));
      }
      Assert.assertEquals(1, count);
      Assert.assertEquals("110", tel);
    }
  }

  /**
   * 查询序号为2的读库，查询到的号码只能是120。
   *
   * @throws SQLException sql exception
   */
  @Test
  public void testSeq2ReadonlyHint() throws SQLException {
    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(readonlySeq2Sql)
    ) {
      int count = 0;
      String tel = "";
      while (resultSet.next()) {
        count++;
        tel = resultSet.getString(5);
        System.out.println(String.format("查询SQL：%s\n手机号码：%s", readonlySeq2Sql, tel));
      }
      Assert.assertEquals(1, count);
      Assert.assertEquals("120", tel);
    }
  }
}
