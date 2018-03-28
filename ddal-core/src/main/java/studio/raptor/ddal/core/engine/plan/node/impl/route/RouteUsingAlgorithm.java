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

package studio.raptor.ddal.core.engine.plan.node.impl.route;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.RouteErrCodes;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.router.result.FullRouteResult;
import studio.raptor.ddal.core.router.result.RouteResult;
import studio.raptor.ddal.core.router.util.RouteCalculator;
import studio.raptor.ddal.core.router.util.RouteCondition;

/**
 * @author Sam
 * @since 3.0.0
 */
public class RouteUsingAlgorithm extends ProcessNode {

  private static Logger logger = LoggerFactory.getLogger(RouteUsingAlgorithm.class);

  @Override
  protected void execute(ProcessContext context) {
    List<RouteCondition> routeConditions = context.getRouteConditions();
    //目前不支持夸库join，所以只支持单表分库查询
    if (routeConditions.size() > 1) {
      logger.error("Only support one shard table in sql");
      throw new GenericException(RouteErrCodes.ROUTE_401);
    }

    RouteResult result = new FullRouteResult();
    result.setShards(context.getVirtualDb().getShards());
    result.setParameters(context.getSqlParameters());

    result.setDbShard(RouteCalculator.calculateDbShard(context.getSqlParameters(), routeConditions.get(0)));
    result.setTableShard(RouteCalculator.calculateTableShard(context.getShardTables(), context.getSqlParameters(), routeConditions.get(0)));
    context.setRouteResult(result);
  }
}
