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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;


/**
 * 定时任务调度
 *
 * @author Bruce
 */
public class Scheduler implements Closeable {

    final ScheduledThreadPoolExecutor executor;//可调度的线程池执行器
    final String baseThreadName;//线程名称前缀
    final AtomicLong threadId = new AtomicLong(0);//线程序号
    final private Logger logger = LoggerFactory.getLogger(Scheduler.class);

    /**
     * 创建调度器
     *
     * @param numThreads     线程数量
     * @param baseThreadName 线程名称
     * @param isDaemon       是否守护线程（不推荐设置为true）
     */
    public Scheduler(int numThreads, final String baseThreadName, final boolean isDaemon) {
        this.baseThreadName = baseThreadName;
        executor = new ScheduledThreadPoolExecutor(numThreads, new ThreadFactory() {
            public Thread newThread(Runnable r) {
                Thread t = new Thread(r, baseThreadName + threadId.getAndIncrement());
                t.setDaemon(isDaemon);
                return t;
            }
        });
        //设置有关在此执行程序已 shutdown 的情况下是否继续执行现有定期任务的策略。
        //在这种情况下，仅在执行 shutdownNow 时，或者在执行程序已关闭、将策略设置为 false 后才终止这些任务。此值默认为 false。
        executor.setContinueExistingPeriodicTasksAfterShutdownPolicy(false);
        //设置有关在此执行程序已 shutdown 的情况下是否继续执行现有延迟任务的策略。
        //在这种情况下，仅在执行 shutdownNow 时，或者在执行程序已关闭、将策略设置为 false 后才终止这些任务。此值默认为 true。
        executor.setExecuteExistingDelayedTasksAfterShutdownPolicy(false);
    }

    /**
     * 以固定周期执行
     */
    public ScheduledFuture<?> scheduleWithRate(Runnable command, long delayMs, long periodMs) {
        return executor.scheduleAtFixedRate(command, delayMs, periodMs, TimeUnit.MILLISECONDS);
    }

    /**
     * 强制关闭
     */
    public void shutdownNow() {
        List<Runnable> droppedTasks = executor.shutdownNow();
        logger.info("强制关闭调度器 " + baseThreadName + "," + droppedTasks.size() + "个任务被抛弃");
    }

    /**
     * 正常关闭
     */
    public void shutdown() {
        executor.shutdown();
        logger.info("正常关闭调度器 " + baseThreadName);
    }

    /**
     * 优雅关闭
     */
    public void shutdownGraceful() {
        try {
            shutdown();
            if (!executor.awaitTermination(30, TimeUnit.SECONDS)) {
                shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        shutdownGraceful();
    }
}
