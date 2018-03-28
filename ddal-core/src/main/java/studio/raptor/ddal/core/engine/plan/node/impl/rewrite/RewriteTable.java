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

package studio.raptor.ddal.core.engine.plan.node.impl.rewrite;

import java.util.List;
import java.util.Map;
import studio.raptor.ddal.core.constants.EngineConstants;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.router.result.RouteNode;

/**
 * 表名重写
 *
 * @author Sam
 * @since 3.0.0
 */
public class RewriteTable extends ProcessNode {

  /**
   * 分表在分片间无差异，所以使用第一个节点的表映射关系进行
   * 表名的改写。
   *
   * @param context 执行上下文
   */
  @Override
  protected void execute(ProcessContext context) {

    List<RouteNode> routeNodes = context.getRouteResult().getRouteNodes();
    for (RouteNode routeNode : routeNodes) {
      if (null == routeNode.getActualTables() || routeNode.getActualTables().isEmpty()) {
        continue;
      }
      Map<String, String> actualTables = routeNode.getActualTables();
      for (Map.Entry<String, String> entry : actualTables.entrySet()) {
        context.getSqlBuilder().rewriteTable(entry.getKey());
        routeNode.getRewriteAttributes().put(
            String.format(
                EngineConstants.REWRITE_PH_TBL_TEMPLATE,
                EngineConstants.REWRITE_PH_TBL_PREFIX, entry.getKey(), EngineConstants.REWRITE_PH_TBL_SUFFIX),
            entry.getValue()
        );
      }
    }
  }
}
