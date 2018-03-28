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

package studio.raptor.ddal.common.util;

import com.google.common.base.Preconditions;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorUtil {

    public static Map<String, ExecutorService> executors = new ConcurrentHashMap<String, ExecutorService>();

    public static final ExecutorService createSingleThreadPool(String name) {
        Preconditions.checkArgument(!executors.containsKey(name), "%s executor exists", name);
        ExecutorService executorService = Executors.newSingleThreadExecutor(new NameableThreadFactory(name, true));
        executors.put(name, executorService);
        return executorService;
    }

    public static final ExecutorService createFixedThreadPool(String name, int size) {
        return createFixedThreadPool(name, size, true);
    }

    public static final ExecutorService createFixedThreadPool(String name, int size, boolean isDaemon) {
        Preconditions.checkArgument(!executors.containsKey(name), "%s executor exists", name);
        ExecutorService executorService = Executors.newFixedThreadPool(size, new NameableThreadFactory(name, isDaemon));
        executors.put(name, executorService);
        return executorService;
    }

    public static final ExecutorService createCachedThreadPool(String name) {
        return createCachedThreadPool(name, true);
    }

    public static final ExecutorService createCachedThreadPool(String name, boolean isDaemon) {
        Preconditions.checkArgument(!executors.containsKey(name), "%s executor exists", name);
        ExecutorService executorService = Executors.newCachedThreadPool(new NameableThreadFactory(name, isDaemon));
        executors.put(name, executorService);
        return executorService;
    }

    public static final ExecutorService createCachedThreadPool(String name, int coreSize, int maxSize) {
        Preconditions.checkArgument(!executors.containsKey(name), "%s executor exists", name);
        ExecutorService executorService = new ThreadPoolExecutor(coreSize,
                maxSize,
                30L,
                TimeUnit.SECONDS,
                new SynchronousQueue<Runnable>(),// 线程数可伸缩
                new NameableThreadFactory(name, true),
                new ThreadPoolExecutor.CallerRunsPolicy());
        executors.put(name, executorService);
        return executorService;
    }

    public static final ExecutorService createCustomizationThreadPool(String name, int coreSize, int maxSize, int queueSize) {
        Preconditions.checkArgument(!executors.containsKey(name), "%s executor exists", name);
        ExecutorService executorService = new ThreadPoolExecutor(coreSize,
            maxSize,
            30L,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue<Runnable>(queueSize),// 线程数可伸缩
            new NameableThreadFactory(name, true),
            new ThreadPoolExecutor.AbortPolicy());
        executors.put(name, executorService);
        return executorService;
    }

    private static class NameableThreadFactory implements ThreadFactory {

        private final ThreadGroup group;
        private final String namePrefix;
        private final AtomicInteger threadId;
        private final boolean isDaemon;

        public NameableThreadFactory(String name, boolean isDaemon) {
            SecurityManager s = System.getSecurityManager();
            this.group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
            this.namePrefix = name;
            this.threadId = new AtomicInteger(0);
            this.isDaemon = isDaemon;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r, namePrefix + threadId.getAndIncrement());
            t.setDaemon(isDaemon);
            if (t.getPriority() != Thread.NORM_PRIORITY) {
                t.setPriority(Thread.NORM_PRIORITY);
            }
            return t;
        }
    }

}
