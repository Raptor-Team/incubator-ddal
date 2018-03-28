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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/**
 * Raptor statement test case
 *
 * @author jack
 * @since 1.0
 */
public class RaptorStatementTest extends AutoPrepareTestingEnv {

  @Test
  public void testExecuteQuery() throws SQLException {
    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from customer where id = 1")
    ) {
      Assert.assertNotNull(resultSet);
    }
  }

  @Test
  public void testPreparedExecuteQuery() throws SQLException {

    try (
        PreparedStatement statement = connection
            .prepareStatement("select * from customer a where a.id = ?");
    ) {
      setParameters(statement, Collections.<Object>singletonList(1));
      try (
          ResultSet resultSet = statement.executeQuery();
      ) {
        Assert.assertNotNull(resultSet);
      }
    }
  }

  /**
   * 预编译语句参数设置
   *
   * @param preparedStatement 预编译语句
   * @param parameters 参数集合
   * @throws SQLException Set parameters error.
   */
  private void setParameters(final PreparedStatement preparedStatement,
      final List<Object> parameters) throws SQLException {
    int i = 1;
    for (Object each : parameters) {
      preparedStatement.setObject(i++, each);
    }
  }

}