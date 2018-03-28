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

import com.codahale.metrics.Counter;
import com.codahale.metrics.MetricRegistry;

/**
 * 基于SQL语句纬度的监控
 *
 * @author Sam
 * @since 3.0.0
 */

class ProcSQLMonitor {

  private final String PROC_SQL_COUNT_CATEGORY = "proc.sql";
  private final String TOTAL_COUNT = "totalCount";
  private final String QUERY_COUNT = "queryCount";
  private final String DML_COUNT = "dmlCount";
  private final String SUCCESS_COUNT = "successCount";
  private final String FAIL_COUNT = "failCount";
  private final String SCAN_ALL_SHARDS_COUNT = "scanAllShardsCount";

  private final Counter totalCounter;
  private final Counter queryCounter;
  private final Counter dmlCounter;
  private final Counter successCounter;
  private final Counter failCounter;
  private final Counter scanAllShardsCounter;

  ProcSQLMonitor(Object registry) {
    totalCounter = ((MetricRegistry) registry)
        .counter(MetricRegistry.name(PROC_SQL_COUNT_CATEGORY, TOTAL_COUNT));
    queryCounter = ((MetricRegistry) registry)
        .counter(MetricRegistry.name(PROC_SQL_COUNT_CATEGORY, QUERY_COUNT));
    dmlCounter = ((MetricRegistry) registry)
        .counter(MetricRegistry.name(PROC_SQL_COUNT_CATEGORY, DML_COUNT));
    successCounter = ((MetricRegistry) registry)
        .counter(MetricRegistry.name(PROC_SQL_COUNT_CATEGORY, SUCCESS_COUNT));
    failCounter = ((MetricRegistry) registry)
        .counter(MetricRegistry.name(PROC_SQL_COUNT_CATEGORY, FAIL_COUNT));
    scanAllShardsCounter = ((MetricRegistry) registry)
        .counter(MetricRegistry.name(PROC_SQL_COUNT_CATEGORY, SCAN_ALL_SHARDS_COUNT));
  }

  void countTotalCount() {
    totalCounter.inc();
  }

  long getSqlTotalCount() {
    return totalCounter.getCount();
  }

  void countQueryCount() {
    queryCounter.inc();
  }

  long getQueryCount() {
    return queryCounter.getCount();
  }

  void countDmlCount() {
    dmlCounter.inc();
  }

  long getDmlCount() {
    return dmlCounter.getCount();
  }

  void countSuccessCount() {
    successCounter.inc();
  }

  long getSuccessCount() {
    return successCounter.getCount();
  }

  /**
   * SQL执行失败次数加1
   */
  void countFailCount() {
    failCounter.inc();
  }

  /**
   * 查询SQL执行失败总次数。
   *
   * @return SQL执行失败次数
   */
  long getFailCount() {
    return failCounter.getCount();
  }

  /**
   * 全分片扫描计数器加1
   */
  void countScanAllShards() {
    scanAllShardsCounter.inc();
  }

  /**
   * 获取全分片扫描计数器的当前值。
   *
   * @return 全分片扫描次数。
   */
  long getScanAllShardsCount() {
    return scanAllShardsCounter.getCount();
  }
}
