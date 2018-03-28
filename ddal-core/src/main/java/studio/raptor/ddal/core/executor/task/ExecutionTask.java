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

package studio.raptor.ddal.core.executor.task;

import java.net.SocketException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.retry.RetryLoop;
import studio.raptor.ddal.common.retry.RetryNTimes;
import studio.raptor.ddal.config.model.shard.Shard;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.executor.ExecutionUnit;
import studio.raptor.ddal.core.executor.resultset.ResultData;

/**
 * 抽象执行任务
 *
 * @author Charley
 * @since 1.0
 */
public abstract class ExecutionTask {

    private static final Logger log = LoggerFactory.getLogger(ExecutionTask.class);

    private final RetryLoop retryLoop = new RetryLoop(new RetryNTimes(3, 10));

    protected final String sql;
    final boolean isPrepared;
    protected final List<Object> parameters;
    private final Map<String, ContextConnectionWrapper> connectionsWrapper;
    final ExecutionUnit executionUnit;

    ExecutionTask(Map<String, ContextConnectionWrapper> connectionsWrapper, ExecutionUnit executionUnit) {
        this.sql = executionUnit.getFinalSql();
        this.isPrepared = executionUnit.isPrepared();
        this.parameters = executionUnit.getParameters();
        this.connectionsWrapper = connectionsWrapper;
        this.executionUnit = executionUnit;
    }

    public void execute() throws Exception {
        BackendConnection connection = fetchConnection(this.executionUnit.getShard());
        this.executionUnit.setConnection(connection);
        ResultData resultData = executeWithRetry();
        this.executionUnit.setResultData(resultData);
    }

    /**
     * 获取后端连接，若Session中持有则用持有的连接，若无则去连接池中拿连接
     * @param shard 分片
     */
    private BackendConnection fetchConnection(Shard shard) throws SQLException {
        return connectionsWrapper.get(shard.getName()).getCurrentConnection();
    }

    /**
     * 带重试的执行，默认重试次数为3
     */
    private ResultData executeWithRetry() throws Exception {
        ResultData resultData = null;
        while (retryLoop.shouldContinue()) {
            try {
                resultData = doExecute();
                retryLoop.markComplete();
            } catch (Exception e) {
                log.error("Execute task error, sql is: {}", sql);
                if (e.getCause() instanceof SocketException) {
                    // 异常连接交给连接池处理
                    //this.executionUnit.getConnection().reallyClose();
                    throw e;
                } else {
                    retryLoop.takeException(e);
                }
            }
        }
        return resultData;
    }

    /**
     * 抽象执行方法
     */
    protected abstract ResultData doExecute() throws SQLException;

    /**
     * 预编译语句参数设置
     *
     * @param preparedStatement 预编译语句
     * @param parameters 参数集合
     * @throws SQLException Set parameters error.
     */
    protected void setParameters(final PreparedStatement preparedStatement,
        final List<Object> parameters) throws SQLException {
        int i = 1;
        for (Object each : parameters) {
            preparedStatement.setObject(i++, each);
        }
    }
}
