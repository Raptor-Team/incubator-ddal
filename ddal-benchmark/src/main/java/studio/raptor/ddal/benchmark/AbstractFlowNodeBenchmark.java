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

package studio.raptor.ddal.benchmark;

import com.google.common.collect.ImmutableMap;

import java.util.Map;

import io.airlift.stats.CpuTimer;

import static io.airlift.stats.CpuTimer.CpuDuration;
import static java.util.concurrent.TimeUnit.NANOSECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * @author Sam
 * @since 3.0.0
 */
public abstract class AbstractFlowNodeBenchmark extends AbstractBenchmark {

  private static int defaultWarmUpCount = 1000000;

  public AbstractFlowNodeBenchmark(String benchmarkName,
                            int warmupIterations,
                            int measuredIterations) {
    super(benchmarkName, warmupIterations, measuredIterations);
  }

  public AbstractFlowNodeBenchmark(String benchmarkName,
                            int measuredIterations) {
    super(benchmarkName, defaultWarmUpCount, measuredIterations);
  }

  protected abstract void prepareTaskContext();

  protected abstract TaskStats executeFlowNode();

  @Override
  protected Map<String, Long> runOnce() {
    prepareTaskContext();
    CpuTimer cpuTimer = new CpuTimer();
    TaskStats taskStats = executeFlowNode();
    CpuDuration executionTime = cpuTimer.elapsedTime();

    long inputSqlCount = taskStats.getSqlCount();

    return ImmutableMap.<String, Long>builder()
            .put("input_sql_count", inputSqlCount)
            .put("sqls_per_second", (long) (inputSqlCount / executionTime.getWall().getValue(SECONDS)))
            .put("wall_nanos", executionTime.getWall().roundTo(NANOSECONDS))
            .put("cpu_nanos", executionTime.getCpu().roundTo(NANOSECONDS))
            .put("user_nanos", executionTime.getUser().roundTo(NANOSECONDS))
            .build();
  }
}
