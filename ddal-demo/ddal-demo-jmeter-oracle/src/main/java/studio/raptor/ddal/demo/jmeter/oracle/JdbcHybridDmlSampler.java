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

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.executor.resultset.ResultData;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JdbcHybridDmlSampler extends AbstractJavaSamplerClient {

  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());
  private static final String INSERT_SQL = "Insert into ddal_test_0.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (?,?,'M ',30,'15889001158')";
  private static final String SELECT_SQL = "select tno, tname, sex, age, tphone from ddal_test_0.TEACHER where TNO = ?";
  private static final String UPDATE_SQL = "update ddal_test_0.teacher set tname = ? where tno = ?";
  private static final String DELETE_SQL = "delete from ddal_test_0.teacher where tno = ?";

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    BackendConnection conn = null;
    try {
      conn = BackendDataSourceManager.getBackendConnection("group_1", false, false);
      Long id = ID.getAndIncrement();
      String tname = RandomStringUtils.randomAlphabetic(18);

      // insert
      ResultData resultData =
          conn.executePreparedUpdate(INSERT_SQL, Arrays.asList((Object) id, tname));
      if (resultData.getAffectedRows() != 1) {
        throw new Exception("Insert data error.");
      }
      conn.commit();

      // select
      resultData = conn.executePreparedQuery(SELECT_SQL, Collections.singletonList((Object) id));
      if (resultData.isEmpty()) {
        throw new Exception("Select data error.");
      }

      // update
      resultData = conn.executePreparedUpdate(UPDATE_SQL,
          Arrays.asList(RandomStringUtils.randomAlphabetic(15), (Object) id));
      if (resultData.getAffectedRows() != 1) {
        throw new Exception("Update data error");
      }
      conn.commit();

      // delete
      resultData = conn.executePreparedUpdate(DELETE_SQL, Collections.singletonList((Object) id));
      if (null == resultData || resultData.getAffectedRows() != 1) {
        throw new Exception("Delete data error");
      }
      conn.commit();
      sampleResult.setSuccessful(true);
    } catch (Exception e) {
      try {
        if (null != conn) {
          conn.rollback();
        }
      } catch (SQLException ignore) {
      }
      e.printStackTrace();
      sampleResult.setSuccessful(false);
    } finally {
      if (null != conn) {
        try {
          conn.close();
        } catch (Exception ignore) {
        }
      }
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }
}
