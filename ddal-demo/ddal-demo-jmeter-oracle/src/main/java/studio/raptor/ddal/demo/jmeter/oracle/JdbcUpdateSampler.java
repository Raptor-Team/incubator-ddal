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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.config.common.FileLoader;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.executor.resultset.ResultData;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JdbcUpdateSampler extends AbstractJavaSamplerClient {

  private static Logger logger = LoggerFactory.getLogger(JdbcUpdateSampler.class);
  private static volatile List<Long> ids;
  private static volatile int ids_size = 0;

  private void loadPrimaryKeysIntoMemory() throws IOException {
    logger.info("Start to Load update primary keys.");
    ids = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(
        FileLoader.class.getClassLoader().getResourceAsStream("update_singledb.id")));
    String id;
    while (null != (id = reader.readLine())) {
      if (!StringUtil.isEmpty(id) && id.matches("\\d+")) {
        ids.add(Long.parseLong(id));
      }
    }
    reader.close();
    if (logger.isInfoEnabled()) {
      logger.info("{} id loaded.", ids.size());
    }
  }

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    BackendConnection conn = null;
    try {
      // load update id if needed.
      if (null == ids) {
        synchronized (this) {
          if (null == ids) {
            loadPrimaryKeysIntoMemory();
            ids_size = ids.size();
          }
        }
      }
      Long tno = ids.get(RandomUtils.nextInt(0, ids_size));
      String tname = RandomStringUtils.randomAlphabetic(18);
      List<Object> param = new ArrayList<>();
      param.add(tname);
      param.add(tno);
      conn = BackendDataSourceManager.getBackendConnection("group_1", false, false);
      ResultData resultData = conn.executePreparedUpdate(JMeterConstants.UPDATE_SQL, param);
      sampleResult.setSuccessful(true);
      if (!(resultData.getAffectedRows() == 1)) {
        logger.error("tno {} not hit.", tno);
      }
      conn.commit();
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
