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

package studio.raptor.ddal.benchmark.update;

import java.sql.Connection;
import java.sql.PreparedStatement;
import javax.sql.DataSource;
import studio.raptor.ddal.benchmark.AbstractLongFlowBenchmark;
import studio.raptor.ddal.benchmark.TaskStats;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class UpdateSqlBenchmark extends AbstractLongFlowBenchmark {

  private DataSource dataSource;
  private TaskStats taskStats = new TaskStats(1);


  public UpdateSqlBenchmark(String benchmarkName, int warmupIterations,
      int measuredIterations) {
    super(benchmarkName, warmupIterations, measuredIterations);
  }

  public static void main(String[] args) {
    new UpdateSqlBenchmark("Update Sql", 0, 1).runBenchmark();
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
//    String prepareSql = "update teacher set tphone = '99999' where tno = 1000";
//    String prepareSql = "update teacher set tphone = '88888'";
    String prepareSql = "update teacher set tno=9999 where tno = 1004";
    try(
        Connection conn = dataSource.getConnection();
        ) {
      conn.setAutoCommit(false);
      try(PreparedStatement preparedStatement = conn.prepareStatement(prepareSql)) {
//      preparedStatement.setLong(2, 1000);
//      preparedStatement.setString(1, "99999");
        preparedStatement.execute();
        conn.commit();
      }
    } catch (Exception e) {
      throw new RuntimeException("Update data error!", e);
    }
    return taskStats;
  }
}
