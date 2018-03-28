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

package studio.raptor.ddal.core.connection.jdbc.pool;

import static java.util.Collections.unmodifiableCollection;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static java.util.concurrent.TimeUnit.SECONDS;
import static studio.raptor.ddal.core.connection.jdbc.util.ConcurrentBag.IConcurrentBagEntry.STATE_IN_USE;
import static studio.raptor.ddal.core.connection.jdbc.util.ConcurrentBag.IConcurrentBagEntry.STATE_NOT_IN_USE;
import static studio.raptor.ddal.core.connection.jdbc.util.UtilityElf.createThreadPoolExecutor;
import static studio.raptor.ddal.core.connection.jdbc.util.UtilityElf.quietlySleep;

import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.health.HealthCheckRegistry;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLTransientConnectionException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.core.connection.jdbc.HikariConfig;
import studio.raptor.ddal.core.connection.jdbc.HikariPoolMXBean;
import studio.raptor.ddal.core.connection.metrics.MetricsTrackerFactory;
import studio.raptor.ddal.core.connection.metrics.PoolStats;
import studio.raptor.ddal.core.connection.metrics.dropwizard.CodahaleHealthChecker;
import studio.raptor.ddal.core.connection.metrics.dropwizard.CodahaleMetricsTrackerFactory;
import studio.raptor.ddal.core.connection.jdbc.util.ClockSource;
import studio.raptor.ddal.core.connection.jdbc.util.ConcurrentBag;
import studio.raptor.ddal.core.connection.jdbc.util.ConcurrentBag.IBagStateListener;
import studio.raptor.ddal.core.connection.jdbc.util.SuspendResumeLock;
import studio.raptor.ddal.core.connection.jdbc.util.UtilityElf.DefaultThreadFactory;

/**
 * This is the primary connection pool class that provides the basic
 * pooling behavior for HikariCP.
 *
 * @author Brett Wooldridge
 */
public class HikariPool extends PoolBase implements HikariPoolMXBean, IBagStateListener {

  private static final ClockSource clockSource = ClockSource.INSTANCE;
  private static final int POOL_NORMAL = 0;
  private static final int POOL_SUSPENDED = 1;
  private static final int POOL_SHUTDOWN = 2;
  private final Logger LOGGER = LoggerFactory.getLogger(HikariPool.class);
  private final long ALIVE_BYPASS_WINDOW_MS = Long
      .getLong("studio.raptor.ddal.core.connection.aliveBypassWindowMs",
          MILLISECONDS.toMillis(500));
  private final long HOUSEKEEPING_PERIOD_MS = Long
      .getLong("studio.raptor.ddal.core.connection.housekeeping.periodMs", SECONDS.toMillis(30));
  private final PoolEntryCreator POOL_ENTRY_CREATOR = new PoolEntryCreator(null);
  private final Collection<Runnable> addConnectionQueue;
  private final ThreadPoolExecutor addConnectionExecutor;
  private final ThreadPoolExecutor closeConnectionExecutor;
  private final ConcurrentBag<PoolEntry> connectionBag;
  private final ProxyLeakTask leakTask;
  private final SuspendResumeLock suspendResumeLock;
  private volatile int poolState;
  private ScheduledExecutorService houseKeepingExecutorService;
  private MetricsTrackerDelegate metricsTracker;

  /**
   * Construct a HikariPool with the specified configuration.
   *
   * @param config a HikariConfig instance
   */
  public HikariPool(final HikariConfig config) {
    super(config);

    this.connectionBag = new ConcurrentBag<>(this);
    this.suspendResumeLock =
        config.isAllowPoolSuspension() ? new SuspendResumeLock() : SuspendResumeLock.FAUX_LOCK;

    initializeHouseKeepingExecutorService();

    checkFailFast();

    if (config.getMetricsTrackerFactory() != null) {
      setMetricsTrackerFactory(config.getMetricsTrackerFactory());
    } else {
      setMetricRegistry(config.getMetricRegistry());
    }

    setHealthCheckRegistry(config.getHealthCheckRegistry());

    registerMBeans(this);

    ThreadFactory threadFactory = config.getThreadFactory();

    LinkedBlockingQueue<Runnable> addConnectionQueue = new LinkedBlockingQueue<>(
        config.getMaximumPoolSize() + 1);
    this.addConnectionQueue = unmodifiableCollection(addConnectionQueue);
    this.addConnectionExecutor = createThreadPoolExecutor(addConnectionQueue,
        poolName + " connection adder", threadFactory, new ThreadPoolExecutor.DiscardPolicy());
    this.closeConnectionExecutor = createThreadPoolExecutor(config.getMaximumPoolSize(),
        poolName + " connection closer", threadFactory, new ThreadPoolExecutor.CallerRunsPolicy());

    this.leakTask = new ProxyLeakTask(config.getLeakDetectionThreshold(),
        houseKeepingExecutorService);

    this.houseKeepingExecutorService
        .scheduleWithFixedDelay(new HouseKeeper(), 100L, HOUSEKEEPING_PERIOD_MS, MILLISECONDS);
  }

  /**
   * Get a connection from the pool, or timeout after connectionTimeout milliseconds.
   *
   * @return a java.sql.Connection instance
   * @throws SQLException thrown if a timeout occurs trying to obtain a connection
   */
  public final Connection getConnection() throws SQLException {
    return getConnection(connectionTimeout);
  }

  /**
   * Get a connection from the pool, or timeout after the specified number of milliseconds.
   *
   * @param hardTimeout the maximum time to wait for a connection from the pool
   * @return a java.sql.Connection instance
   * @throws SQLException thrown if a timeout occurs trying to obtain a connection
   */
  public final Connection getConnection(final long hardTimeout) throws SQLException {
    suspendResumeLock.acquire();
    final long startTime = clockSource.currentTime();

    try {
      long timeout = hardTimeout;
      do {
        final PoolEntry poolEntry = connectionBag.borrow(timeout, MILLISECONDS);
        if (poolEntry == null) {
          break; // We timed out... break and throw exception
        }

        final long now = clockSource.currentTime();
        if (poolEntry.isMarkedEvicted() || (
            clockSource.elapsedMillis(poolEntry.lastAccessed, now) > ALIVE_BYPASS_WINDOW_MS
                && !isConnectionAlive(poolEntry.connection))) {
          closeConnection(poolEntry,
              "(connection is evicted or dead)"); // Throw away the dead connection (passed max age or failed alive test)
          timeout = hardTimeout - clockSource.elapsedMillis(startTime);
        } else {
          metricsTracker.recordBorrowStats(poolEntry, startTime);
          return poolEntry.createProxyConnection(leakTask.schedule(poolEntry), now);
        }
      } while (timeout > 0L);
    } catch (InterruptedException e) {
      throw new SQLException(poolName + " - Interrupted during connection acquisition", e);
    } finally {
      suspendResumeLock.release();
    }

    throw createTimeoutException(startTime);
  }

  /**
   * Shutdown the pool, closing all idle connections and aborting or closing
   * active connections.
   *
   * @throws InterruptedException thrown if the thread is interrupted during shutdown
   */
  public final synchronized void shutdown() throws InterruptedException {
    try {
      poolState = POOL_SHUTDOWN;

      if (addConnectionExecutor == null) {
        return;
      }

      LOGGER.info("{} - Close initiated...", poolName);
      logPoolState("Before closing ");

      softEvictConnections();

      addConnectionExecutor.shutdown();
      addConnectionExecutor.awaitTermination(5L, SECONDS);

      destroyHouseKeepingExecutorService();

      connectionBag.close();

      final ExecutorService assassinExecutor = createThreadPoolExecutor(config.getMaximumPoolSize(),
          poolName + " connection assassinator",
          config.getThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
      try {
        final long start = clockSource.currentTime();
        do {
          abortActiveConnections(assassinExecutor);
          softEvictConnections();
        } while (getTotalConnections() > 0 && clockSource.elapsedMillis(start) < SECONDS
            .toMillis(5));
      } finally {
        assassinExecutor.shutdown();
        assassinExecutor.awaitTermination(5L, SECONDS);
      }

      shutdownNetworkTimeoutExecutor();
      closeConnectionExecutor.shutdown();
      closeConnectionExecutor.awaitTermination(5L, SECONDS);
    } finally {
      logPoolState("After closing ");
      unregisterMBeans();
      metricsTracker.close();
      LOGGER.info("{} - Closed.", poolName);
    }
  }

  /**
   * Evict a connection from the pool.
   *
   * @param connection the connection to evict
   */
  public final void evictConnection(Connection connection) {
    ProxyConnection proxyConnection = (ProxyConnection) connection;
    proxyConnection.cancelLeakTask();

    try {
      softEvictConnection(proxyConnection.getPoolEntry(), "(connection evicted by user)",
          !connection.isClosed() /* owner */);
    } catch (SQLException e) {
      // unreachable in HikariCP, but we're still forced to catch it
    }
  }

  public void setMetricRegistry(Object metricRegistry) {
    if (metricRegistry != null) {
      setMetricsTrackerFactory(new CodahaleMetricsTrackerFactory((MetricRegistry) metricRegistry));
    } else {
      setMetricsTrackerFactory(null);
    }
  }

  public void setMetricsTrackerFactory(MetricsTrackerFactory metricsTrackerFactory) {
    if (metricsTrackerFactory != null) {
      this.metricsTracker = new MetricsTrackerDelegate(
          metricsTrackerFactory.create(config.getPoolName(), getPoolStats()));
    } else {
      this.metricsTracker = new NopMetricsTrackerDelegate();
    }
  }

  public void setHealthCheckRegistry(Object healthCheckRegistry) {
    if (healthCheckRegistry != null) {
      CodahaleHealthChecker
          .registerHealthChecks(this, config, (HealthCheckRegistry) healthCheckRegistry);
    }
  }

  // ***********************************************************************
  //                        IBagStateListener callback
  // ***********************************************************************

  /**
   * {@inheritDoc}
   */
  @Override
  public Future<Boolean> addBagItem(final int waiting) {
    final boolean shouldAdd = waiting - addConnectionQueue.size() >= 0; // Yes, >= is intentional.
    if (shouldAdd) {
      return addConnectionExecutor.submit(POOL_ENTRY_CREATOR);
    }

    return CompletableFuture.completedFuture(Boolean.TRUE);
  }

  // ***********************************************************************
  //                        HikariPoolMBean methods
  // ***********************************************************************

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getActiveConnections() {
    return connectionBag.getCount(STATE_IN_USE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getIdleConnections() {
    return connectionBag.getCount(STATE_NOT_IN_USE);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getTotalConnections() {
    return connectionBag.size();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final int getThreadsAwaitingConnection() {
    return connectionBag.getWaitingThreadCount();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void softEvictConnections() {
    for (PoolEntry poolEntry : connectionBag.values()) {
      softEvictConnection(poolEntry, "(connection evicted)", false /* not owner */);
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final synchronized void suspendPool() {
    if (suspendResumeLock == SuspendResumeLock.FAUX_LOCK) {
      throw new IllegalStateException(poolName + " - is not suspendable");
    } else if (poolState != POOL_SUSPENDED) {
      suspendResumeLock.suspend();
      poolState = POOL_SUSPENDED;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final synchronized void resumePool() {
    if (poolState == POOL_SUSPENDED) {
      poolState = POOL_NORMAL;
      fillPool();
      suspendResumeLock.resume();
    }
  }

  // ***********************************************************************
  //                           Package methods
  // ***********************************************************************

  /**
   * Log the current pool state at debug level.
   *
   * @param prefix an optional prefix to prepend the log message
   */
  final void logPoolState(String... prefix) {
    if (LOGGER.isDebugEnabled()) {
      LOGGER.debug("{} - {}stats (total={}, active={}, idle={}, waiting={})",
          poolName, (prefix.length > 0 ? prefix[0] : ""),
          getTotalConnections(), getActiveConnections(), getIdleConnections(),
          getThreadsAwaitingConnection());
    }
  }

  /**
   * Recycle PoolEntry (add back to the pool)
   *
   * @param poolEntry the PoolEntry to recycle
   */
  @Override
  final void recycle(final PoolEntry poolEntry) {
    metricsTracker.recordConnectionUsage(poolEntry);

    connectionBag.requite(poolEntry);
  }

  /**
   * Permanently close the real (underlying) connection (eat any exception).
   *
   * @param poolEntry poolEntry having the connection to close
   * @param closureReason reason to close
   */
  final void closeConnection(final PoolEntry poolEntry, final String closureReason) {
    if (connectionBag.remove(poolEntry)) {
      final Connection connection = poolEntry.close();
      closeConnectionExecutor.execute(new Runnable() {
        @Override
        public void run() {
          quietlyCloseConnection(connection, closureReason);
        }
      });
    }
  }

  // ***********************************************************************
  //                           Private methods
  // ***********************************************************************

  /**
   * Creating new poolEntry.
   */
  private PoolEntry createPoolEntry() {
    try {
      final PoolEntry poolEntry = newPoolEntry();

      final long maxLifetime = config.getMaxLifetime();
      if (maxLifetime > 0) {
        // variance up to 2.5% of the maxlifetime
        final long variance = maxLifetime > 10_000 ? ThreadLocalRandom
            .current().nextLong(maxLifetime / 40) : 0;
        final long lifetime = maxLifetime - variance;
        poolEntry.setFutureEol(houseKeepingExecutorService.schedule(new Runnable() {
          @Override
          public void run() {
            softEvictConnection(poolEntry, "(connection has passed maxLifetime)", false /* not owner */);
          }
        }, lifetime, MILLISECONDS));
      }

      LOGGER.debug("{} - Added connection {}", poolName, poolEntry.connection);
      return poolEntry;
    } catch (Exception e) {
      if (poolState == POOL_NORMAL) {
        LOGGER.debug("{} - Cannot acquire connection from data source", poolName, e);
      }
      return null;
    }
  }

  /**
   * Fill pool up from current idle connections (as they are perceived at the point of execution) to
   * minimumIdle connections.
   */
  private void fillPool() {
    final int connectionsToAdd = Math
        .min(config.getMaximumPoolSize() - getTotalConnections(),
            config.getMinimumIdle() - getIdleConnections())
        - addConnectionQueue.size();
    for (int i = 0; i < connectionsToAdd; i++) {
      addConnectionExecutor.submit(
          (i < connectionsToAdd - 1) ? POOL_ENTRY_CREATOR : new PoolEntryCreator("After adding "));
    }
  }

  /**
   * Attempt to abort or close active connections.
   */
  private void abortActiveConnections(final ExecutorService assassinExecutor) {
    for (PoolEntry poolEntry : connectionBag.values(STATE_IN_USE)) {
      Connection connection = poolEntry.close();
      try {
        connection.abort(assassinExecutor);
      } catch (Throwable e) {
        quietlyCloseConnection(connection, "(connection aborted during shutdown)");
      } finally {
        connectionBag.remove(poolEntry);
      }
    }
  }

  /**
   * If initializationFailFast is configured, check that we have DB connectivity.
   *
   * @throws PoolInitializationException if fails to create or validate connection
   */
  private void checkFailFast() {
    final long initializationTimeout = config.getInitializationFailTimeout();
    if (initializationTimeout < 0) {
      return;
    }

    final long startTime = clockSource.currentTime();
    do {
      final PoolEntry poolEntry = createPoolEntry();
      if (poolEntry != null) {
        if (config.getMinimumIdle() > 0) {
          connectionBag.add(poolEntry);
          LOGGER.debug("{} - Added connection {}", poolName, poolEntry.connection);
        } else {
          quietlyCloseConnection(poolEntry.close(),
              "(initialization check complete and minimumIdle is zero)");
        }

        return;
      }

      if (getLastConnectionFailure() instanceof ConnectionSetupException) {
        throwPoolInitializationException(getLastConnectionFailure().getCause());
      }

      quietlySleep(1000L);
    } while (clockSource.elapsedMillis(startTime) < initializationTimeout);

    if (initializationTimeout > 0) {
      throwPoolInitializationException(getLastConnectionFailure());
    }
  }

  private void throwPoolInitializationException(Throwable t) {
    LOGGER.error("{} - Exception during pool initialization.", poolName, t);
    destroyHouseKeepingExecutorService();
    throw new PoolInitializationException(t);
  }

  private void softEvictConnection(final PoolEntry poolEntry, final String reason,
      final boolean owner) {
    poolEntry.markEvicted();
    if (owner || connectionBag.reserve(poolEntry)) {
      closeConnection(poolEntry, reason);
    }
  }

  private void initializeHouseKeepingExecutorService() {
    if (config.getScheduledExecutor() == null) {
      final ThreadFactory threadFactory =
          config.getThreadFactory() != null ? config.getThreadFactory()
              : new DefaultThreadFactory(poolName + " housekeeper", true);
      final ScheduledThreadPoolExecutor executor = new ScheduledThreadPoolExecutor(1, threadFactory,
          new ThreadPoolExecutor.DiscardPolicy());
      executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
      executor.setRemoveOnCancelPolicy(true);
      this.houseKeepingExecutorService = executor;
    } else {
      this.houseKeepingExecutorService = config.getScheduledExecutor();
    }
  }

  private void destroyHouseKeepingExecutorService() {
    if (config.getScheduledExecutor() == null) {
      houseKeepingExecutorService.shutdownNow();
    }
  }

  private PoolStats getPoolStats() {
    return new PoolStats(SECONDS.toMillis(1)) {
      @Override
      protected void update() {
        this.pendingThreads = HikariPool.this.getThreadsAwaitingConnection();
        this.idleConnections = HikariPool.this.getIdleConnections();
        this.totalConnections = HikariPool.this.getTotalConnections();
        this.activeConnections = HikariPool.this.getActiveConnections();
      }
    };
  }

  private SQLException createTimeoutException(long startTime) {
    logPoolState("Timeout failure ");
    metricsTracker.recordConnectionTimeout();

    String sqlState = null;
    final Throwable originalException = getLastConnectionFailure();
    if (originalException instanceof SQLException) {
      sqlState = ((SQLException) originalException).getSQLState();
    }
    final SQLException connectionException = new SQLTransientConnectionException(
        poolName + " - Connection is not available, request timed out after " + clockSource
            .elapsedMillis(startTime) + "ms.", sqlState, originalException);
    if (originalException instanceof SQLException) {
      connectionException.setNextException((SQLException) originalException);
    }

    return connectionException;
  }

  // ***********************************************************************
  //                      Non-anonymous Inner-classes
  // ***********************************************************************

  public static class PoolInitializationException extends RuntimeException {

    private static final long serialVersionUID = 929872118275916520L;

    /**
     * Construct an exception, possibly wrapping the provided Throwable as the cause.
     *
     * @param t the Throwable to wrap
     */
    public PoolInitializationException(Throwable t) {
      super("Failed to initialize pool: " + t.getMessage(), t);
    }
  }

  // Mimic the Java 8 version of java.util.concurrent.CompletableFuture
  public static class CompletableFuture<T> implements Future<T> {

    private T result;

    public static <U> Future<U> completedFuture(U value) {
      CompletableFuture<U> f = new CompletableFuture<U>();
      f.result = value;
      return f;
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
      return false;
    }

    @Override
    public boolean isCancelled() {
      return false;
    }

    @Override
    public boolean isDone() {
      return true;
    }

    @Override
    public T get() throws InterruptedException, ExecutionException {
      return result;
    }

    @Override
    public T get(long timeout, TimeUnit unit)
        throws InterruptedException, ExecutionException, TimeoutException {
      return result;
    }
  }

  /**
   * Creating and adding poolEntries (connections) to the pool.
   */
  private final class PoolEntryCreator implements Callable<Boolean> {

    private final String afterPrefix;

    PoolEntryCreator(String afterPrefix) {
      this.afterPrefix = afterPrefix;
    }

    @Override
    public Boolean call() throws Exception {
      long sleepBackoff = 250L;
      while (poolState == POOL_NORMAL && shouldCreateAnotherConnection()) {
        final PoolEntry poolEntry = createPoolEntry();
        if (poolEntry != null) {
          connectionBag.add(poolEntry);
          LOGGER.debug("{} - Added connection {}", poolName, poolEntry.connection);
          if (afterPrefix != null) {
            logPoolState(afterPrefix);
          }
          return Boolean.TRUE;
        }

        // failed to get connection from db, sleep and retry
        quietlySleep(sleepBackoff);
        sleepBackoff = Math
            .min(SECONDS.toMillis(10), Math.min(connectionTimeout, (long) (sleepBackoff * 1.5)));
      }
      // Pool is suspended or shutdown or at max size
      return Boolean.FALSE;
    }

    private boolean shouldCreateAnotherConnection() {
      // only create connections if we need another idle connection or have threads still waiting
      // for a new connection, otherwise bail
      return getTotalConnections() < config.getMaximumPoolSize() &&
          (connectionBag.getWaitingThreadCount() > 0 || getIdleConnections() < config
              .getMinimumIdle());
    }
  }

  /**
   * The house keeping task to retire and maintain minimum idle connections.
   */
  private class HouseKeeper implements Runnable {

    private volatile long previous = clockSource
        .plusMillis(clockSource.currentTime(), -HOUSEKEEPING_PERIOD_MS);

    @Override
    public void run() {
      try {
        // refresh timeouts in case they changed via MBean
        connectionTimeout = config.getConnectionTimeout();
        validationTimeout = config.getValidationTimeout();
        leakTask.updateLeakDetectionThreshold(config.getLeakDetectionThreshold());

        final long idleTimeout = config.getIdleTimeout();
        final long now = clockSource.currentTime();

        // Detect retrograde time, allowing +128ms as per NTP spec.
        if (clockSource.plusMillis(now, 128) < clockSource
            .plusMillis(previous, HOUSEKEEPING_PERIOD_MS)) {
          LOGGER.warn(
              "{} - Retrograde clock change detected (housekeeper delta={}), soft-evicting connections from pool.",
              poolName, clockSource.elapsedDisplayString(previous, now));
          previous = now;
          softEvictConnections();
          fillPool();
          return;
        } else if (now > clockSource.plusMillis(previous, (3 * HOUSEKEEPING_PERIOD_MS) / 2)) {
          // No point evicting for forward clock motion, this merely accelerates connection retirement anyway
          LOGGER.warn("{} - Thread starvation or clock leap detected (housekeeper delta={}).",
              poolName, clockSource.elapsedDisplayString(previous, now));
        }

        previous = now;

        String afterPrefix = "Pool ";
        if (idleTimeout > 0L) {
          final List<PoolEntry> idleList = connectionBag.values(STATE_NOT_IN_USE);
          int removable = idleList.size() - config.getMinimumIdle();
          if (removable > 0) {
            logPoolState("Before cleanup ");
            afterPrefix = "After cleanup  ";

            // Sort pool entries on lastAccessed
            Collections.sort(idleList, PoolEntry.LASTACCESS_COMPARABLE);
            for (PoolEntry poolEntry : idleList) {
              if (clockSource.elapsedMillis(poolEntry.lastAccessed, now) > idleTimeout
                  && connectionBag.reserve(poolEntry)) {
                closeConnection(poolEntry, "(connection has passed idleTimeout)");
                if (--removable == 0) {
                  break; // keep min idle cons
                }
              }
            }
          }
        }

        logPoolState(afterPrefix);

        fillPool(); // Try to maintain minimum connections
      } catch (Exception e) {
        LOGGER.error("Unexpected exception in housekeeping task", e);
      }
    }
  }
}
