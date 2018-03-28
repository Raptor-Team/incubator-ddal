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

package studio.raptor.ddal.core.connection.jdbc.util;

import static java.util.concurrent.TimeUnit.SECONDS;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.Locale;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Brett Wooldridge
 */
public final class UtilityElf {

  /**
   * Check whether the provided String is empty or {@code null} and return a
   * {@code null} in either case, otherwise return the trimmed String.
   *
   * @param text the String to check for emptiness
   * @return null if string is null or empty
   */
  public static String getNullIfEmpty(final String text) {
    return text == null ? null : text.trim().isEmpty() ? null : text.trim();
  }

  /**
   * Sleep and transform an InterruptedException into a RuntimeException.
   *
   * @param millis the number of milliseconds to sleep
   */
  public static void quietlySleep(final long millis) {
    try {
      Thread.sleep(millis);
    } catch (InterruptedException e) {
      // I said be quiet!
    }
  }

  /**
   * Create and instance of the specified class using the constructor matching the specified
   * arguments.
   *
   * @param <T> the class type
   * @param className the name of the class to instantiate
   * @param clazz a class to cast the result as
   * @param args arguments to a constructor
   * @return an instance of the specified class
   */
  public static <T> T createInstance(final String className, final Class<T> clazz,
      final Object... args) {
    if (className == null) {
      return null;
    }

    try {
      Class<?> loaded = UtilityElf.class.getClassLoader().loadClass(className);
      if (args.length == 0) {
        return clazz.cast(loaded.newInstance());
      }

      Class<?>[] argClasses = new Class<?>[args.length];
      for (int i = 0; i < args.length; i++) {
        argClasses[i] = args[i].getClass();
      }
      Constructor<?> constructor = loaded.getConstructor(argClasses);
      return clazz.cast(constructor.newInstance(args));
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * Create a ThreadPoolExecutor.
   *
   * @param queueSize the queue size
   * @param threadName the thread name
   * @param threadFactory an optional ThreadFactory
   * @param policy the RejectedExecutionHandler policy
   * @return a ThreadPoolExecutor
   */
  public static ThreadPoolExecutor createThreadPoolExecutor(final int queueSize,
      final String threadName, ThreadFactory threadFactory, final RejectedExecutionHandler policy) {
    if (threadFactory == null) {
      threadFactory = new DefaultThreadFactory(threadName, true);
    }

    LinkedBlockingQueue<Runnable> queue = new LinkedBlockingQueue<>(queueSize);
    ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 5, SECONDS, queue, threadFactory,
        policy);
    executor.allowCoreThreadTimeOut(true);
    return executor;
  }

  /**
   * Create a ThreadPoolExecutor.
   *
   * @param queue the BlockingQueue to use
   * @param threadName the thread name
   * @param threadFactory an optional ThreadFactory
   * @param policy the RejectedExecutionHandler policy
   * @return a ThreadPoolExecutor
   */
  public static ThreadPoolExecutor createThreadPoolExecutor(final BlockingQueue<Runnable> queue,
      final String threadName, ThreadFactory threadFactory, final RejectedExecutionHandler policy) {
    if (threadFactory == null) {
      threadFactory = new DefaultThreadFactory(threadName, true);
    }

    ThreadPoolExecutor executor = new ThreadPoolExecutor(1 /*core*/, 1 /*max*/, 5 /*keepalive*/,
        SECONDS, queue, threadFactory, policy);
    executor.allowCoreThreadTimeOut(true);
    return executor;
  }

  // ***********************************************************************
  //                       Misc. public methods
  // ***********************************************************************

  /**
   * Get the int value of a transaction isolation level by name.
   *
   * @param transactionIsolationName the name of the transaction isolation level
   * @return the int value of the isolation level or -1
   */
  public static int getTransactionIsolation(final String transactionIsolationName) {
    if (transactionIsolationName != null) {
      try {
        // use the english locale to avoid the infamous turkish locale bug
        final String upperName = transactionIsolationName.toUpperCase(Locale.ENGLISH);
        if (upperName.startsWith("TRANSACTION_")) {
          Field field = Connection.class.getField(upperName);
          return field.getInt(null);
        }
        final int level = Integer.parseInt(transactionIsolationName);
        switch (level) {
          case Connection.TRANSACTION_READ_UNCOMMITTED:
          case Connection.TRANSACTION_READ_COMMITTED:
          case Connection.TRANSACTION_REPEATABLE_READ:
          case Connection.TRANSACTION_SERIALIZABLE:
          case Connection.TRANSACTION_NONE:
            return level;
          default:
            throw new IllegalArgumentException();
        }
      } catch (Exception e) {
        throw new IllegalArgumentException(
            "Invalid transaction isolation value: " + transactionIsolationName);
      }
    }

    return -1;
  }

  /**
   * Are we running on JDK 8 or above?
   *
   * @return true if JDK 8+, false otherwise
   */
  public static boolean isJdk8Plus() {
    Matcher matcher = Pattern.compile("(?:(\\d+(?:\\.?\\d*)))")
        .matcher(System.getProperty("java.version"));
    if (!matcher.find()) {
      return false;
    }

    return Float.parseFloat(matcher.group(1)) > 1.7f;
  }

  public static final class DefaultThreadFactory implements ThreadFactory {

    private final String threadName;
    private final boolean daemon;

    public DefaultThreadFactory(String threadName, boolean daemon) {
      this.threadName = threadName;
      this.daemon = daemon;
    }

    @Override
    public Thread newThread(Runnable r) {
      Thread thread = new Thread(r, threadName);
      thread.setDaemon(daemon);
      return thread;
    }
  }
}
