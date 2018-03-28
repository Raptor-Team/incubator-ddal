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

import studio.raptor.ddal.core.connection.metrics.MetricsTracker;
import studio.raptor.ddal.core.connection.metrics.MetricsTrackerFactory;
import studio.raptor.ddal.core.connection.metrics.PoolStats;

/**
 * <pre>{@code
 * HikariConfig config = new HikariConfig();
 * config.setMetricsTrackerFactory(new PrometheusMetricsTrackerFactory());
 * }</pre>
 */
public class PrometheusMetricsTrackerFactory implements MetricsTrackerFactory {

  @Override
  public MetricsTracker create(String poolName, PoolStats poolStats) {
    new HikariCPCollector(poolName, poolStats).register();
    return new PrometheusMetricsTracker(poolName);
  }
}
