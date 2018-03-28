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

package studio.raptor.ddal.core.engine.processdefinition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Collection;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.ContextConnectionWrapper;
import studio.raptor.ddal.core.engine.ProcessContext;

/**
 * 关系型数据库流程定义。
 *
 * @author Sam
 * @since 1.0
 */
public class SqlProcessDefinition {

  private static Logger logger = LoggerFactory.getLogger(SqlProcessDefinition.class);

  /**
   * commit command process flow.
   *
   * @param context process context
   * @throws GenericException Process exception
   */
  public void commitFlow(ProcessContext context) throws GenericException {
    try {
      Collection<ContextConnectionWrapper> backendConnectionWrappers =
          context.getShardBackendConnWrapper().values();
      if (!backendConnectionWrappers.isEmpty()) {
//        String txId = context.getTransactionId();
//        if(null != txId) {
//          context.getTxLogRecorder().flush();
//        }
        BackendConnection conn;
        for (ContextConnectionWrapper backendConnectionWrapper : backendConnectionWrappers) {
          if(null != (conn = backendConnectionWrapper.getReadWriteConnection())) {
            conn.commit();
          }
        }
//        if (null != txId) {
//          context.getTxLogRecorder().recordTxCommit(txId);
//        }
      }
    } catch (SQLException e) {
      context.setTransactionId(null);
      logger.error("Commit Exception.", e);
      throw new GenericException(e);
    }
//    finally {
//      context.setTransactionId(null);
//      context.getTxLogRecorder().clear();
//    }
  }

  /**
   * rollback command process flow.
   *
   * @param context process context
   * @throws GenericException Process exception
   */
  public void rollbackFlow(ProcessContext context) throws GenericException {
    try {
      Collection<ContextConnectionWrapper> backendConnectionWrappers =
          context.getShardBackendConnWrapper().values();
      if (!backendConnectionWrappers.isEmpty()) {
        BackendConnection conn;
        for (ContextConnectionWrapper backendConnectionWrapper : backendConnectionWrappers) {
          if(null != (conn = backendConnectionWrapper.getReadWriteConnection())) {
            conn.rollback();
          }
        }
      }
    } catch (SQLException e) {
      logger.error("Commit Exception.", e);
      throw new GenericException(e);
    }
//    finally {
//      context.setTransactionId(null);
//      context.getTxLogRecorder().clear();
//    }
  }
}
