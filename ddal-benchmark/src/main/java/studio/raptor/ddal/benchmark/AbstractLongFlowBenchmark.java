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
import java.util.concurrent.TimeUnit;

import io.airlift.units.Duration;

/**
 * @author Sam
 * @since 3.0.0
 */
public abstract class AbstractLongFlowBenchmark extends AbstractBenchmark {

  private static int defaultWarmUpCount = 100000;

  protected AbstractLongFlowBenchmark(String benchmarkName,
                            int warmupIterations,
                            int measuredIterations) {
    super(benchmarkName, warmupIterations, measuredIterations);
  }

  AbstractLongFlowBenchmark(String benchmarkName,
                            int measuredIterations) {
    super(benchmarkName, defaultWarmUpCount, measuredIterations);
  }

  protected abstract void prepareLongFlowContext();

  protected abstract void destroyLongFlowContext();

  protected abstract TaskStats executeLongFlow();

  @Override
  protected Map<String, Long> runOnce() {
    prepareLongFlowContext();
    long startNanos = System.nanoTime();
    TaskStats taskStats = executeLongFlow();
    Duration duration = Duration.nanosSince(startNanos);
    destroyLongFlowContext();
    long inputSqlCount = taskStats.getSqlCount();
    return ImmutableMap.<String, Long>builder()
            .put("input_sql_count", inputSqlCount)
            .put("sqls_per_second", (long) (inputSqlCount / duration.getValue(TimeUnit.SECONDS)))
            .put("wall_nanos", duration.roundTo(TimeUnit.NANOSECONDS))
            .put("cpu_nanos", duration.roundTo(TimeUnit.NANOSECONDS))
            .put("user_nanos", duration.roundTo(TimeUnit.NANOSECONDS))
            .build();
  }
}
