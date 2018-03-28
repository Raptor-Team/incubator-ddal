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
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DdalInsertSampler extends AbstractJavaSamplerClient {

  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());
  private static final String INSERT_SQL = "Insert into TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (?,'Test','M ',30,'15889001158')";
  private static volatile DataSource dataSource = new RaptorDataSource("school", "oracle");

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(INSERT_SQL);
    ) {
      statement.setLong(1, ID.getAndIncrement());
      int result = statement.executeUpdate();
      if (result == 1) {
        sampleResult.setSuccessful(true);
      } else {
        sampleResult.setSuccessful(false);
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      sampleResult.setSuccessful(false);
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }
}