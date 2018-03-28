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

package studio.raptor.ddal.core.monitor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.json.JSONArray;
import studio.raptor.ddal.common.json.JSONObject;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.connection.jdbc.HikariDataSource;
import studio.raptor.ddal.core.connection.jdbc.HikariPoolMXBean;

/**
 * @author Sam
 * @since 3.0.0
 */
public class MonitorServlet extends HttpServlet {

  private static final long serialVersionUID = 734398626820571179L;
  private static Logger log = LoggerFactory.getLogger(MonitorServlet.class);
  private ServletHandler servletHandler;
  private final double[] zeroMetricsRate = {0.0, 0.0, 0.0, 0.0};

  @Override
  public void init(ServletConfig pServletConfig) throws ServletException {
    servletHandler = new DefaultServletHandler();
    Object registry = Monitor.getMetricRegistry();
    if (null == registry) {
      log.info("Monitor can't be use, because no metric registry can be found in current context.");
    }
    super.init(pServletConfig);
  }

  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {
    sendStreamingResponse(resp, servletHandler.handleRequest(req.getRequestURI()));
  }

  private void sendStreamingResponse(HttpServletResponse pResp, String jsonStr) throws IOException {
    ChunkedWriter writer = null;
    try {
      writer = new ChunkedWriter(pResp.getOutputStream(), "UTF-8");
      writer.write(jsonStr);
    } finally {
      if (writer != null) {
        writer.flush();
        writer.close();
      }
    }
  }

  private class DefaultServletHandler extends ServletHandler {

    /**
     * 连接池指标表字段
     * 连接池名称，空闲连接数，在用连接数，总连接数
     */
    private final String[] COL_CONN_POOL_STATS =
        new String[]{"poolName", "idleConnections", "activeConnections", "totalConnections"};

    /**
     * 已执行SQL总数，更新操作数，查询次数，执行成功次数，执行失败次数，全分片扫描次数
     */
    private final String[] COL_PROC_SQL_SUMMARY =
        new String[]{"totalCount", "dmlCount", "queryCount", "successCount", "failCount", "scanAllShardsCount"};

    /**
     * 综合+查询+更新操作：平均速率，1分钟内平均速率，5分钟内平均速率，15分钟内平均速率
     */
    private final String[] COL_PROC_TPS =
        new String[]{"meanRate", "1mRate", "5mRate", "15mRate",
            "queryMeanRate", "query1mRate", "query5mRate", "query15mRate",
            "dmlMeanRate", "dml1mRate", "dml5mRate", "dml15mRate"};

    /**
     * JVM内存参数：堆内存大小，最大堆内存大小
     */
    private final String[] COL_PROC_MEM_INFO = new String[] {"heapSize", "heapMaxSize"};

    /**
     * do not delete
     *
     * @return connection pool stats
     */
    @SuppressWarnings("unused")
    protected String getConnPoolStats() {
      Map<String, DataSource> dataSourceGroup =
          BackendDataSourceManager.INSTANCE.getPoolNameIndexedDataSources();
      if (null != dataSourceGroup && dataSourceGroup.size() > 0) {
        List<Map<String, Object>> list = new ArrayList<>(dataSourceGroup.size());
        for (Map.Entry<String, DataSource> entry : dataSourceGroup.entrySet()) {
          HikariPoolMXBean mxb = ((HikariDataSource) entry.getValue()).getHikariPoolMXBean();
          list.add(
              addProcInfo(
                  newMap(
                      COL_CONN_POOL_STATS,
                      new Object[]{mxb.toString(), mxb.getIdleConnections(),
                          mxb.getActiveConnections(),
                          mxb.getTotalConnections()})
              )
          );
        }
        return JSONArray.toJSONString(list);
      }
      return null;
    }

    @Override
    protected String getProcSqlSummary() {
      return JSONObject.toJSONString(
          addProcInfo(
              newMap(
                  COL_PROC_SQL_SUMMARY,
                  new Object[]{Monitor.getProcSqlTotal(), Monitor.getProcDmlCount(),
                      Monitor.getProcQueryCount(), Monitor.getProcSuccessCount(),
                      Monitor.getProcFailCount(), Monitor.getScanAllShardsCount()}
              )
          ));
    }

    @Override
    protected String getProcTps() {
      double[] procTps = Monitor.getProcTpsRates();
      if (null == procTps) {
        procTps = zeroMetricsRate;
      }

      double[] procQueryTps = Monitor.getProcQueryTpsRates();
      if (null == procQueryTps) {
        procQueryTps = zeroMetricsRate;
      }

      double[] procDmlTps = Monitor.getProcDmlTpsRates();
      if (null == procDmlTps) {
        procDmlTps = zeroMetricsRate;
      }

      return JSONObject.toJSONString(
          addProcInfo(
              newMap(
                  COL_PROC_TPS,
                  new Object[]{
                      procTps[0], procTps[1], procTps[2], procTps[3],
                      procQueryTps[0], procQueryTps[1], procQueryTps[2], procQueryTps[3],
                      procDmlTps[0], procDmlTps[1], procDmlTps[2], procDmlTps[3]
                  }
              )
          )
      );
    }

    @Override
    protected String getProcMemInfo() {
      return JSONObject.toJSONString(
        addProcInfo(
            newMap(
                COL_PROC_MEM_INFO,
                new Object[]{Monitor.getHeapSize(), Monitor.getHeapMaxSize()}
            )
        )
      );
    }

    /**
     * 添加进程相关的指标信息
     *
     * @param metricData 度量数据
     * @return 包含进程信息的度量数据
     */
    private Map<String, Object> addProcInfo(Map<String, Object> metricData) {
      // do nothing currently
      return metricData;
    }

    private Map<String, Object> newMap(String[] keyArray, Object[] valueArray) {
      Map<String, Object> map = new HashMap<>();
      for (int i = 0, len = keyArray.length; i < len; i++) {
        map.put(keyArray[i], valueArray[i]);
      }
      return map;
    }
  }
}
