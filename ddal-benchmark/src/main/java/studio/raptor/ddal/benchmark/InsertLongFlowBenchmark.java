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

package studio.raptor.ddal.benchmark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class InsertLongFlowBenchmark extends AbstractLongFlowBenchmark {

  private static Logger logger = LoggerFactory.getLogger(InsertLongFlowBenchmark.class);

  private DataSource dataSource;
  private Connection connection;

  private InsertLongFlowBenchmark() {
    super("Insert teacher table", 1, 1);
  }

  public static void main(String[] args) {
    InsertLongFlowBenchmark insertLongFlowBenchmark = new InsertLongFlowBenchmark();
    insertLongFlowBenchmark.runBenchmark();
  }

  @Override
  protected void prepareLongFlowContext() {

  }

  @Override
  protected void destroyLongFlowContext() {

  }

  @Override
  protected TaskStats executeLongFlow() {
    Connection jdbcConn = null;
    Map<Integer, Map<Integer, Object>> result = new HashMap<>();
    try {
      Class.forName(oracle.jdbc.driver.OracleDriver.class.getName());
      jdbcConn = DriverManager.getConnection("jdbc:oracle:thin:@192.168.199.24:1521:orcla", "ddal_test", "ddal_test_123");
      jdbcConn.setAutoCommit(true);
      try(Statement statement = jdbcConn.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from course")) {
        int i = 0;
        while (resultSet.next()) {
          result.put(i, new HashMap<Integer, Object>());
          for (int j = 1; j < 4; j++) {
            result.get(i).put(j, resultSet.getObject(j));
          }
          i++;
        }
      }
    } catch (SQLException | ClassNotFoundException ignore) {
      logger.error("", ignore);
    } finally {
      if(null != jdbcConn) {
        try{
          jdbcConn.close();
        } catch (SQLException e) {
          logger.error("", e);
        }
      }
    }


    try {
      String sql = "insert into course(cno, cname, tno) values(?, ?, ?)";
      if (null == dataSource) {
        dataSource = new RaptorDataSource("school", "oracle");
      }
      connection = dataSource.getConnection();

      try(PreparedStatement preparedStatement = connection.prepareStatement(sql)){
        for(int i=0;i<10;i++) {
          for(int j=0;j<3;j++) {
            preparedStatement.setObject(j+1, result.get(i).get(j+1));
          }
          preparedStatement.execute();
        }
      }
      connection.commit();
    } catch (Exception e) {
      logger.error(String.format("Execute long flow [%s] error.", getBenchmarkName()), e);
      try {
        connection.rollback();
      } catch (SQLException ignore) {
        logger.error("Insert failed", ignore);
      }
      throw new RuntimeException(e);
    } finally {
      try {
        if(null != connection && !connection.isClosed()) {
          connection.close();
        }
      } catch (SQLException ignore) {}
    }
    return new TaskStats(1);
  }
}
