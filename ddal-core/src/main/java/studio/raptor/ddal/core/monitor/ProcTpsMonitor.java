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

import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;

/**
 * 从进程维度统计SQL的执行速率。统计数据来自DDAL接收到的所有SQL请求，不
 * 区分SQL类别和SQL执行的正确与否。
 *
 * 速率统计四个事件维度：应用完整时间段平均速率，最近一分钟内的平均速率，最近
 * 五分钟内的平均速率以及最近15分钟内的平均速率。
 *
 * @author Sam
 * @since 3.0.0
 */
public class ProcTpsMonitor {

  private final String PROC_TPS_CATEGORY = "proc.tps";
  private final String PROC_TPS_TOTAL = "total";
  private final String PROC_TPS_QUERY = "query";
  private final String PROC_TPS_DML = "dml";

  private final Meter procTpsMeter;
  private final Meter procQueryTpsMeter;
  private final Meter procDMLTpsMeter;

  public ProcTpsMonitor(Object registry) {
    this.procTpsMeter = ((MetricRegistry) registry)
        .meter(MetricRegistry.name(PROC_TPS_CATEGORY, PROC_TPS_TOTAL));
    this.procQueryTpsMeter = ((MetricRegistry) registry)
        .meter(MetricRegistry.name(PROC_TPS_CATEGORY, PROC_TPS_QUERY));
    this.procDMLTpsMeter = ((MetricRegistry) registry)
        .meter(MetricRegistry.name(PROC_TPS_CATEGORY, PROC_TPS_DML));
  }

  /**
   * 标记SQL语句执行事件。
   */
  public void mark() {
    this.procTpsMeter.mark();
  }

  /**
   * 标记查询语句的执行
   */
  public void markQuery() {
    this.procQueryTpsMeter.mark();
  }

  /**
   * 标记DML语句的执行。此处的dml表示新增、修改和删除操作。
   */
  public void markDML() {
    this.procDMLTpsMeter.mark();
  }

  /**
   * 获取进程维度所有SQL吞吐量的平均速率。
   *
   * @return 平均速录
   */
  public double getMeanRate() {
    return this.procTpsMeter.getMeanRate();
  }

  /**
   * 进程的一分钟速率。
   *
   * @return 一分钟速率
   */
  public double getOneMinuteRate() {
    return this.procTpsMeter.getOneMinuteRate();
  }

  /**
   * 进程全局五分钟速率。
   *
   * @return 五分钟速率
   */
  public double getFiveMinutesRate() {
    return this.procTpsMeter.getFiveMinuteRate();
  }

  /**
   * 15分钟速率。
   *
   * @return 15分钟速率
   */
  public double getFifteenMinutesRate() {
    return this.procTpsMeter.getFifteenMinuteRate();
  }

  /**
   * 查询SQL平均速率。
   *
   * @return 查询SQL的平均速率
   */
  public double getQueryMeanRate() {
    return this.procQueryTpsMeter.getMeanRate();
  }

  /**
   * 查询SQL一分钟内的平均速率。
   *
   * @return 最近一分钟内的平均速率
   */
  public double getQueryOneMinuteRate() {
    return this.procQueryTpsMeter.getOneMinuteRate();
  }

  /**
   * 查询SQL五分钟内的平均速率。
   *
   * @return 五分钟内的平均速率
   */
  public double getQueryFiveMinutesRate() {
    return this.procQueryTpsMeter.getFiveMinuteRate();
  }

  /**
   * 查询语句最近十五分钟内的平均速率。
   *
   * @return 最近十五分钟内的平均速率
   */
  public double getQueryFifteenMinutesRate() {
    return this.procQueryTpsMeter.getFifteenMinuteRate();
  }


  /**
   * DML平均速率。
   *
   * @return DML的平均速率
   */
  public double getDmlMeanRate() {
    return this.procDMLTpsMeter.getMeanRate();
  }

  /**
   * DML一分钟内的平均速率。
   *
   * @return 最近一分钟内的平均速率
   */
  public double getDmlOneMinuteRate() {
    return this.procDMLTpsMeter.getOneMinuteRate();
  }

  /**
   * DML五分钟内的平均速率。
   *
   * @return 五分钟内的平均速率
   */
  public double getDmlFiveMinutesRate() {
    return this.procDMLTpsMeter.getFiveMinuteRate();
  }

  /**
   * DML最近十五分钟内的平均速率。
   *
   * @return 最近十五分钟内的平均速率
   */
  public double getDmlFifteenMinutesRate() {
    return this.procDMLTpsMeter.getFifteenMinuteRate();
  }
}
