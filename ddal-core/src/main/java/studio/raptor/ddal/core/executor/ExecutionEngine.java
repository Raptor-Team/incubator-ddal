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

package studio.raptor.ddal.core.executor;

import com.google.common.base.Strings;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.common.exception.ExecuteException;
import studio.raptor.ddal.common.exception.ExecuteException.Code;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.util.ExecutorUtil;
import studio.raptor.ddal.config.common.ConfigConstant;
import studio.raptor.ddal.config.config.SystemProperties;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.executor.task.DQLExecutionTask;
import studio.raptor.ddal.core.executor.task.ExecutionTask;
import studio.raptor.ddal.core.executor.task.IUDExecutionTask;
import studio.raptor.ddal.core.executor.task.TaskRunnableWrapper;

/**
 * 执行引擎
 *
 * @author Charley
 * @since 1.0
 */
public class ExecutionEngine {

    private static final String CORE_THREAD_SIZE_KEY = "executor.coreThreadSize";
    private static final String MAX_THREAD_SIZE_KEY = "executor.maxThreadSize";
    private static final String QUEUE_SIZE_KEY = "executor.queueSize";

    // SQL执行线程池（1~2*cpu_cores, QueueSize=100, 拒绝任务后抛异常
    private static final ThreadPoolExecutor SQL_EXECUTOR;

    static{
        String coreThreadSizeString = SystemProperties.getInstance().get(CORE_THREAD_SIZE_KEY);
        String maxThreadSizeString = SystemProperties.getInstance().get(MAX_THREAD_SIZE_KEY);
        String queueSizeString = SystemProperties.getInstance().get(QUEUE_SIZE_KEY);

        int coreThreadSize = Strings.isNullOrEmpty(coreThreadSizeString) ? ConfigConstant.DEFAULT_CPU_CORES + 1 : Integer.valueOf(coreThreadSizeString);
        int maxThreadSize = Strings.isNullOrEmpty(maxThreadSizeString) ? ConfigConstant.DEFAULT_CPU_CORES *2 + 1 : Integer.valueOf(maxThreadSizeString);
        int queueSize = Strings.isNullOrEmpty(queueSizeString)  ? 100 : Integer.valueOf(queueSizeString);

        SQL_EXECUTOR = (ThreadPoolExecutor) ExecutorUtil
            .createCustomizationThreadPool("sqlExecute", coreThreadSize, maxThreadSize, queueSize);
    }

    /**
     * 执行Select语句
     *
     * @param executionGroup
     * @return
     */
    public void executeDQL(Map<String, ContextConnectionWrapper> connectionWrappers, ExecutionGroup executionGroup) {
        try {
            ExecutionTask[] tasks = new ExecutionTask[executionGroup.getSize()];
            for (int i = 0; i < executionGroup.getSize(); i++) {
                ExecutionTask task = new DQLExecutionTask(connectionWrappers, executionGroup.getExecutionUnits().get(i));
                tasks[i] = task;
            }
            executeTask(tasks);
        } catch (Exception e) {
            throw ExecuteException.create(Code.EXECUTION_ERROR_ON_PHYSICAL_DB, e);
        }
    }

    /**
     * Insert, Update, Delete语句执行
     *
     * @param executionGroup
     * @return
     */
    public void executeIUD(Map<String, ContextConnectionWrapper> connectionsWrapper, ExecutionGroup executionGroup) {
        try {
            ExecutionTask[] tasks = new ExecutionTask[executionGroup.getSize()];
            for (int i = 0; i < executionGroup.getSize(); i++) {
                ExecutionTask task = new IUDExecutionTask(connectionsWrapper, executionGroup.getExecutionUnits().get(i));
                tasks[i] = task;
            }
            executeTask(tasks);
        } catch (Exception e) {
          throw ExecuteException.create(Code.EXECUTION_ERROR_ON_PHYSICAL_DB, e);
        }
    }

    /**
     * 执行任务
     *
     * @param tasks
     * @throws GenericException
     */
    private void executeTask(ExecutionTask[] tasks) throws Exception {
        int taskCount = tasks.length;
        if (taskCount == 1) {
            tasks[0].execute();
        } else if (taskCount > 1) {
            executeTaskConcurrently(tasks, taskCount);
        }
    }

    /**
     * 并发执行任务
     * @param tasks
     * @param count
     * @throws InterruptedException
     * @throws ExecutionException
     */
    private void executeTaskConcurrently(ExecutionTask[] tasks, int count)
        throws InterruptedException, ExecutionException {
        CompletionService completionService = new ExecutorCompletionService(SQL_EXECUTOR);
        List<Future<Boolean>> futures = new FastArrayList<>(count);
        for (ExecutionTask task : tasks) {
            TaskRunnableWrapper runnableWrapper = new TaskRunnableWrapper(task);
            futures.add(completionService.submit(runnableWrapper));
        }
        try{
            for(int i=0; i<count; i++){
                completionService.take().get();
            }
        }catch(InterruptedException | ExecutionException e){
            for(Future<Boolean> future : futures){
                future.cancel(true);
            }
            throw e;
        }
    }

    /**
     * 设置并发数
     */
    public boolean setPoolSize(int size) {
        try {
            SQL_EXECUTOR.setMaximumPoolSize(size);
            SQL_EXECUTOR.setCorePoolSize(size);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 安全关闭线程池
     */
    public void shutdown() {
        SQL_EXECUTOR.shutdown();
        try {
            if (!SQL_EXECUTOR.awaitTermination(5, TimeUnit.SECONDS)) {
                SQL_EXECUTOR.shutdownNow();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
            Thread.currentThread().interrupt();
        }
    }

}
