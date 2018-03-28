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

/**
 * DDAL监控的核心对象，并且不直接包含度量工具的实现细节。是否开启
 * 度量统计功能依赖于当前classpath中是否引用Metrics实现。
 *
 * @author Sam
 * @since 3.0.0
 */
public class Monitor {

  /**
   * Metrics注册器
   */
  private Object metricRegistry;

  /**
   * 进程维度SQL监控
   */
  private ProcSQLMonitor procSQLMonitor;

  /**
   * 进程维度SQL吞吐量监控
   */
  private ProcTpsMonitor procTpsMonitor;

  /**
   * JVM堆内存监控
   */
  private ProcMemMonitor procMemMonitor;

  private static final Monitor MONITOR = new Monitor();

  private Monitor() {
    try {
      metricRegistry = Class.forName("com.codahale.metrics.MetricRegistry").newInstance();
      procSQLMonitor = new ProcSQLMonitor(metricRegistry);
      procTpsMonitor = new ProcTpsMonitor(metricRegistry);
      procMemMonitor = new ProcMemMonitor();
    } catch (Throwable throwable) {
      metricRegistry = null;
      procSQLMonitor = null;
      procTpsMonitor = null;
      procMemMonitor = null;
    }
  }

//  static {
//    try {
//      metricRegistry = Class.forName("com.codahale.metrics.MetricRegistry").newInstance();
//      procSQLMonitor = new ProcSQLMonitor(metricRegistry);
//      procTpsMonitor = new ProcTpsMonitor(metricRegistry);
//
//    } catch (Throwable throwable) {
//      metricRegistry = null;
//      procSQLMonitor = null;
//      procTpsMonitor = null;
//    }
//  }

  public static Object getMetricRegistry() {
    return MONITOR.metricRegistry;
  }

  /**
   * SQL计数器加1
   */
  public static void countProcSqlTotal() {
    if (null != MONITOR.procSQLMonitor) {
      MONITOR.procSQLMonitor.countTotalCount();
    }
  }

  /**
   * 获取SQL计数器的值。
   *
   * @return SQL计数器的值
   */
  public static long getProcSqlTotal() {
    return null != MONITOR.procSQLMonitor ? MONITOR.procSQLMonitor.getSqlTotalCount() : 0L;
  }

  /**
   * 查询语句计数器加1
   */
  public static void countProcQuery() {
    if (null != MONITOR.procSQLMonitor) {
      MONITOR.procSQLMonitor.countQueryCount();
    }
  }

  /**
   * 获取查询语句计数器的值
   *
   * @return 计数器的值
   */
  public static long getProcQueryCount() {
    return null != MONITOR.procSQLMonitor ? MONITOR.procSQLMonitor.getQueryCount() : 0L;
  }

  /**
   * 修改语句计数器加1
   */
  public static void countProcDml() {
    if (null != MONITOR.procSQLMonitor) {
      MONITOR.procSQLMonitor.countDmlCount();
    }
  }

  /**
   * 获取修改语句计数器的值
   *
   * @return 修改语句计数器的值
   */
  public static long getProcDmlCount() {
    return null != MONITOR.procSQLMonitor ? MONITOR.procSQLMonitor.getDmlCount() : 0L;
  }

  public static void countProcSuccess() {
    if (null != MONITOR.procSQLMonitor) {
      MONITOR.procSQLMonitor.countSuccessCount();
    }
  }

  public static long getProcSuccessCount() {
    return null != MONITOR.procSQLMonitor ? MONITOR.procSQLMonitor.getSuccessCount() : 0L;
  }

  public static void countProcFail() {
    if (null != MONITOR.procSQLMonitor) {
      MONITOR.procSQLMonitor.countFailCount();
    }
  }

  public static long getProcFailCount() {
    return null != MONITOR.procSQLMonitor ? MONITOR.procSQLMonitor.getFailCount() : 0L;
  }

  /**
   * 全分片扫描次数加一。
   */
  public static void countScanAllShards() {
    if(null != MONITOR.procSQLMonitor) {
      MONITOR.procSQLMonitor.countScanAllShards();
    }
  }

  /**
   * 获取全分片扫描执行次数。
   *
   * @return 全分片扫描次数
   */
  public static long getScanAllShardsCount() {
    return null != MONITOR.procSQLMonitor ? MONITOR.procSQLMonitor.getScanAllShardsCount() : 0L;
  }

  //########################
  // 线程维度的TPS统计
  //########################

  public static void procTpsMark() {
    if (null != MONITOR.procTpsMonitor) {
      MONITOR.procTpsMonitor.mark();
    }
  }

  public static double[] getProcTpsRates() {
    return null != MONITOR.procTpsMonitor
        ? new double[]{
        MONITOR.procTpsMonitor.getMeanRate(),
        MONITOR.procTpsMonitor.getOneMinuteRate(),
        MONITOR.procTpsMonitor.getFiveMinutesRate(),
        MONITOR.procTpsMonitor.getFifteenMinutesRate()
    }
        : null;
  }

  //########################
  // 线程维度的Query语句 TPS统计
  //########################

  /**
   * 标记查询语句的执行事件
   */
  public static void procQueryTpsMark() {
    if (null != MONITOR.procTpsMonitor) {
      MONITOR.procTpsMonitor.markQuery();
    }
  }

  /**
   * 查询语句的tps数据。
   * 返回数据参考 {@link #getProcDmlTpsRates}
   *
   * @return 查询语句的tps统计数据
   */
  public static double[] getProcQueryTpsRates() {
    return null != MONITOR.procTpsMonitor
        ? new double[]{
        MONITOR.procTpsMonitor.getQueryMeanRate(),
        MONITOR.procTpsMonitor.getQueryOneMinuteRate(),
        MONITOR.procTpsMonitor.getQueryFiveMinutesRate(),
        MONITOR.procTpsMonitor.getQueryFifteenMinutesRate()
    }
        : null;
  }

  //########################
  // 线程维度的DML TPS统计
  //########################

  /**
   * 标记DML语句执行事件。
   */
  public static void procDmlTpsMark() {
    if (null != MONITOR.procTpsMonitor) {
      MONITOR.procTpsMonitor.markDML();
    }
  }

  /**
   * DML的Tps统计数据，四个时间段的tps通过数组的方式返回。
   * {meanRate, oneMinuteRate, fiveMinutesRate, fifteenMinutesRate}
   *
   * @return DML的Tps统计数据
   */
  public static double[] getProcDmlTpsRates() {
    return null != MONITOR.procTpsMonitor
        ? new double[]{
        MONITOR.procTpsMonitor.getDmlMeanRate(),
        MONITOR.procTpsMonitor.getDmlOneMinuteRate(),
        MONITOR.procTpsMonitor.getDmlFiveMinutesRate(),
        MONITOR.procTpsMonitor.getDmlFifteenMinutesRate()
    }
        : null;
  }

  /**
   * JVM当前堆内存大小
   *
   * @return heap size
   */
  public static long getHeapSize() {
    return MONITOR.procMemMonitor.getHeapSize();
  }

  /**
   * JVM最大堆内存
   *
   * @return 最大堆内存
   */
  public static long getHeapMaxSize() {
    return MONITOR.procMemMonitor.getHeapMaxSize();
  }
}
