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

import java.util.List;
import studio.raptor.ddal.config.model.shard.Shard;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.executor.ExecutionGroup;
import studio.raptor.ddal.core.executor.ExecutionUnit;
import studio.raptor.ddal.core.router.result.RouteNode;

/**
 * 组装执行组
 *
 * @author Sam
 * @since 3.0.0
 */
public class AssembleExecutionGroup extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    List<RouteNode> routeNodeList = context.getRouteResult().getRouteNodes();
    //组装ExecutionGroup
    ExecutionGroup executionGroup = new ExecutionGroup();
    executionGroup.setOriginalSql(context.getOriginSql());
    for (RouteNode routeNode : routeNodeList) {
      //获取连接
      Shard shard = routeNode.getShard();
      //组装执行单元
      ExecutionUnit executionUnit = new ExecutionUnit();
      executionUnit.setShard(shard);
      executionUnit.setFinalSql(routeNode.getFinalSql());
      executionUnit.setPrepared(context.isPreparedStatement());
      executionUnit.setParameters(routeNode.getParameters());
      executionGroup.addExecutionUnit(executionUnit);
    }
    context.setCurrentExecutionGroup(executionGroup);
  }
}
