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

package studio.raptor.ddal.benchmark.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.weakref.jmx.internal.guava.base.Strings;
import studio.raptor.ddal.benchmark.AbstractLongFlowBenchmark;
import studio.raptor.ddal.benchmark.TaskStats;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DualBenchmark extends AbstractLongFlowBenchmark {

  private DataSource dataSource;
  TaskStats taskStats = new TaskStats(1);

  public DualBenchmark() {
    super("Oracle dual", 0, 10);
  }

  @Override
  protected void prepareLongFlowContext() {
    dataSource = new RaptorDataSource("school", "oracle");
  }

  @Override
  protected void destroyLongFlowContext() {

  }

  @Override
  protected TaskStats executeLongFlow() {
    String sql = "select * from dual";
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {
      ResultSetMetaData rsmd = resultSet.getMetaData();
      System.out.println("Query [" + sql + "]");
      int columnsNumber = rsmd.getColumnCount();
      int rowCount = 0;
      while (resultSet.next()) {
        for (int i = 1; i <= columnsNumber; i++) {
          String columnValue = resultSet.getString(i);
          System.out.print(rsmd.getColumnName(i) + ':' + Strings.padEnd(columnValue, 12, ' '));
        }
        System.out.println("");
        rowCount++;
      }
      System.out.println("Result total count:" + rowCount);
    } catch (SQLException e) {
      throw new RuntimeException("");
    }
    return taskStats;
  }

  public static void main(String[] args) {
    new DualBenchmark().runBenchmark();
  }
}
