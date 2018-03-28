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

package studio.raptor.ddal.core.engine.plan.node.impl.execute;

import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;

/**
 * @author Sam
 * @since 3.0.0
 */
public class HandleTransaction extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    // fixme 暂时不要删除
//    if (null == context.getTransactionId()) {
//      // 开启事务时将事务ID保存在上下文中，并记录到事务日志中。
//      String txId = context.getTxIdGenerator().newTransactionId();
//      context.setTransactionId(txId);
//      context.getTxLogRecorder().recordVersion();
//      context.getTxLogRecorder().record("" + context.getDatabaseType().ordinal());
//      context.getTxLogRecorder().record(context.getVirtualDbName());
//      context.getTxLogRecorder().record(txId);
//    }
//    for(ExecutionUnit eu : context.getCurrentExecutionGroup().getExecutionUnits()) {
//      context.getTxLogRecorder().record(eu.getFinalSql(true));
//    }
  }
}
