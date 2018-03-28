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

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
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
public class JdbcInsertSampler extends AbstractJavaSamplerClient {

  private static final String INSERT_SQL = "Insert into TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (?,'Test','M ',30,'15889001158')";
  private static volatile AtomicLong ID = new AtomicLong(
      Long.parseLong(System.getProperty("ddal.startup.id")));

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    BackendConnection conn = null;
    try {
      conn = BackendDataSourceManager.getBackendConnection("group_1", false, false);
      ResultData result = conn.executePreparedUpdate(INSERT_SQL,
          Collections.singletonList((Object) ID.getAndIncrement()));
      conn.commit();
      if (result.getAffectedRows() == 1) {
        sampleResult.setSuccessful(true);
      } else {
        sampleResult.setSuccessful(false);
      }
    } catch (Exception e) {
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