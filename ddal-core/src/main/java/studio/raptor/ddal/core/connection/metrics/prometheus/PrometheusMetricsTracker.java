/*
 * Copyright (C) 2013, 2014 Brett Wooldridge
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package studio.raptor.ddal.core.connection.metrics.prometheus;

import io.prometheus.client.Counter;
import io.prometheus.client.Summary;
import studio.raptor.ddal.core.connection.metrics.MetricsTracker;

class PrometheusMetricsTracker extends MetricsTracker {

  private final Counter.Child connectionTimeoutCounter;
  private final Summary.Child elapsedAcquiredSummary;
  private final Summary.Child elapsedBorrowedSummary;

  PrometheusMetricsTracker(String poolName) {
    super();

    Counter counter = Counter.build()
        .name("hikaricp_connection_timeout_count")
        .labelNames("pool")
        .help("Connection timeout count")
        .register();

    this.connectionTimeoutCounter = counter.labels(poolName);

    Summary elapsedAcquiredSummary = Summary.build()
        .name("hikaricp_connection_acquired_nanos")
        .labelNames("pool")
        .help("Connection acquired time")
        .register();
    this.elapsedAcquiredSummary = elapsedAcquiredSummary.labels(poolName);

    Summary elapsedBorrowedSummary = Summary.build()
        .name("hikaricp_connection_usage_millis")
        .labelNames("pool")
        .help("Connection usage")
        .register();
    this.elapsedBorrowedSummary = elapsedBorrowedSummary.labels(poolName);
  }

  @Override
  public void recordConnectionAcquiredNanos(long elapsedAcquiredNanos) {
    elapsedAcquiredSummary.observe(elapsedAcquiredNanos);
  }

  @Override
  public void recordConnectionUsageMillis(long elapsedBorrowedMillis) {
    elapsedBorrowedSummary.observe(elapsedBorrowedMillis);
  }

  @Override
  public void recordConnectionTimeout() {
    connectionTimeoutCounter.inc();
  }
}
