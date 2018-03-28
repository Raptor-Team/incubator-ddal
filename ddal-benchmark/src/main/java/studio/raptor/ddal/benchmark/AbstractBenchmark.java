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

/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.raptor.ddal.benchmark;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.Nullable;

import io.airlift.units.Duration;
import studio.raptor.ddal.benchmark.utils.GCUtils;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static studio.raptor.ddal.benchmark.utils.FormatUtils.formatCount;

public abstract class AbstractBenchmark {
  private final String benchmarkName;
  private final int warmupIterations;
  private final int measuredIterations;

  AbstractBenchmark(String benchmarkName, int warmupIterations, int measuredIterations) {
    requireNonNull(benchmarkName, "benchmarkName is null");
    checkArgument(warmupIterations >= 0, "warmupIterations must not be negative");
    checkArgument(measuredIterations >= 0, "measuredIterations must not be negative");

    this.benchmarkName = benchmarkName;
    this.warmupIterations = warmupIterations;
    this.measuredIterations = measuredIterations;
  }

  String getBenchmarkName() {
    return benchmarkName;
  }

  /**
   * Initialize any state necessary to run benchmark. This is run once at start up.
   */
  private void setUp() {
    // Default: no-op
  }

  /**
   * Runs the benchmark and returns the result metrics
   */
  protected abstract Map<String, Long> runOnce();

  /**
   * Clean up any state from the benchmark. This is run once after all the iterations are complete.
   */
  private void tearDown() {
    // Default: no-op
  }

  public void runBenchmark() {
    runBenchmark(null);
  }

  void runBenchmark(@Nullable BenchmarkResultHook benchmarkResultHook) {
    AverageBenchmarkResults averageBenchmarkResults = new AverageBenchmarkResults();
    setUp();
    try {
      for (int i = 0; i < warmupIterations; i++) {
        runOnce();
      }
      long startYGC = GCUtils.getYoungGC();
      long startYGCTime = GCUtils.getYoungGCTime();
      long startFGC = GCUtils.getFullGC();
      long startMillis = System.currentTimeMillis();

      for (int i = 0; i < measuredIterations; i++) {
        Map<String, Long> results = runOnce();
        if (benchmarkResultHook != null) {
          benchmarkResultHook.addResults(results);
        }
        averageBenchmarkResults.addResults(results);
      }
      long ygc = GCUtils.getYoungGC() - startYGC;
      long ygct = GCUtils.getYoungGCTime() - startYGCTime;
      long fgc = GCUtils.getFullGC() - startFGC;

      long millis = System.currentTimeMillis() - startMillis;
      System.out.println("Time costs:\t" + millis + ", ygc " + ygc + ", ygct " + ygct + ", fgc " + fgc);

    } catch (Exception t) {
      throw new RuntimeException("Exception in " + getBenchmarkName(), t);
    } finally {
      tearDown();
    }
    if (benchmarkResultHook != null) {
      benchmarkResultHook.finished();
    }

    Map<String, Double> resultsAvg = averageBenchmarkResults.getAverageResultsValues();
    Duration cpuNanos = new Duration(resultsAvg.get("cpu_nanos"), NANOSECONDS);

    long inputRows = resultsAvg.get("input_sql_count").longValue();
    System.out.printf("Benchmark:%s, CPU：%s, Count：%s, Rate：%s %n",
            getBenchmarkName(),
            cpuNanos.toString(TimeUnit.MICROSECONDS),
            formatCount(inputRows),
            resultsAvg.get("sqls_per_second")
    );
  }

  List<Map<String, Object>> orm(ResultSet resultSet) throws SQLException {
    List<Map<String, Object>> result = new ArrayList<>();
    while (resultSet.next()) {
      ResultSetMetaData metaData = resultSet.getMetaData();
      int columnCount = metaData.getColumnCount();
      Map<String, Object> rowData = new HashMap<>();
      for (int i = 1; i <= columnCount; i++) {
        rowData.put(metaData.getColumnLabel(i), resultSet.getObject(i));
      }
      result.add(rowData);
    }
    return result;
  }
}
