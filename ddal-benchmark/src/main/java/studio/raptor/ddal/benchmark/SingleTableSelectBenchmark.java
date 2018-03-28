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

import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.ResultSet;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SingleTableSelectBenchmark extends AbstractLongFlowBenchmark {
  private static Logger logger = LoggerFactory.getLogger(SingleTableSelectBenchmark.class);
  private DataSource dataSource;
  private Connection connection;


  private SingleTableSelectBenchmark() {
    super("SingleTableSelect", 1, 1);
    try {
      dataSource = new RaptorDataSource("school", "mysql");
    } catch (Exception e) {
      logger.error("Create datasource failed", e);
    }

  }

  public static void main(String[] args) {
    new SingleTableSelectBenchmark().runBenchmark();
  }

  @Override
  protected void prepareLongFlowContext() {
  }

  @Override
  protected void destroyLongFlowContext() {
    try {
      connection.close();
    } catch (Exception e) {
      throw new RuntimeException("Destroy context failed.");
    }
  }

  @Override
  protected TaskStats executeLongFlow() {
    try {
      String sql = "select tno, tname, sex, age, tphone from teacher order by tno";
      if (null == dataSource) {
        dataSource = new RaptorDataSource("school", "oracle");
      }
      connection = dataSource.getConnection();
      connection.setAutoCommit(true);
      try(Statement statement = connection.createStatement();
          ResultSet resultSet = statement.executeQuery(sql)){
        String insertTemplate = "Insert into DDAL_TEST_%s.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (%s,'%s','%s',%s,'%s');";
        int i=0;
        while(resultSet.next()) {
          System.out.println(String.format(insertTemplate, i++, resultSet.getLong(1), resultSet.getString(2),resultSet.getString(3),resultSet.getLong(4),resultSet.getString(5)));
        }
      }
    } catch (Exception e) {
      logger.error(String.format("Execute long flow [%s] error.", getBenchmarkName()), e);
      throw new RuntimeException(e);
    } finally {
      try {
        if(null != connection && !connection.isClosed()) {
          connection.close();
        }
      } catch (SQLException ignore) {
      }
    }
    return new TaskStats(1);
  }

}
