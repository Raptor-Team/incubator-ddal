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

package studio.raptor.ddal.demo.jmeter.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DDALHybridDmlSampler extends AbstractJavaSamplerClient {

  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());
  private static final String INSERT_SQL = "Insert into TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (?,?,'M ',30,'15889001158')";
  private static final String SELECT_SQL = "select tno, tname, sex, age, tphone from TEACHER where TNO = ?";
  private static final String UPDATE_SQL = "update teacher set tname = ? where tno = ?";
  private static final String DELETE_SQL = "delete from teacher where tno = ?";

  private static final DataSource dataSource = new RaptorDataSource("school", "oracle");

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    try {
      Long id = ID.getAndIncrement();
      String tname = RandomStringUtils.randomAlphabetic(18);

      // insert
      try (
          Connection conn = dataSource.getConnection();
          PreparedStatement statement = conn.prepareStatement(INSERT_SQL);
      ) {
        statement.setLong(1, id);
        statement.setString(2, tname);
        int affectedRows = statement.executeUpdate();
        if (1 != affectedRows) {
          throw new Exception("Insert data error.");
        }
        conn.commit();
      }

      // select
      try (
          Connection conn = dataSource.getConnection();
          PreparedStatement statement = conn.prepareStatement(SELECT_SQL);
      ) {
        statement.setLong(1, id);
        ResultSet resultSet = statement.executeQuery();
        if (null == resultSet) {
          throw new Exception("Insert data error.");
        }
      }

      // update
      try (
          Connection conn = dataSource.getConnection();
          PreparedStatement statement = conn.prepareStatement(UPDATE_SQL);
      ) {
        statement.setString(1, RandomStringUtils.randomAlphabetic(15));
        statement.setLong(2, id);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
          throw new Exception("Update data error.");
        }
        conn.commit();
      }

      // delete
      try (
          Connection conn = dataSource.getConnection();
          PreparedStatement statement = conn.prepareStatement(DELETE_SQL);
      ) {
        statement.setLong(1, id);
        int affectedRows = statement.executeUpdate();
        if (affectedRows != 1) {
          throw new Exception("Delete data error.");
        }
        conn.commit();
      }
      sampleResult.setSuccessful(true);
    } catch (Exception e) {
      e.printStackTrace();
      sampleResult.setSuccessful(false);
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }
}
