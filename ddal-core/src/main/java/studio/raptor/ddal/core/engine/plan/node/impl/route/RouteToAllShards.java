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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.monitor.Monitor;
import studio.raptor.ddal.core.parser.result.SQLStatementType;
import studio.raptor.ddal.core.router.result.FullRouteResult;
import studio.raptor.ddal.core.router.result.RouteResult;

/**
 * 路由至全部分片节点。
 *
 * @author Sam
 * @since 3.0.0
 */
public class RouteToAllShards extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    VirtualDb vdb = context.getVirtualDb();

    List<String> dbShards = vdb.getShards().allShardNames();
    Map<String, Collection<String>> tableShards = new HashMap<>();
    //若表不为空，则取表中的shards，否则取VDB上的shards
    if (!context.getParseResult().getTableNames().isEmpty()) {
      boolean hasSetShards = false;
      for (String tableName : context.getParseResult().getTableNames()) {
        Table table = vdb.getTable(tableName);

        // 忽略全局表
        if (table.isGlobal() && context.getSqlStatementType() == SQLStatementType.SELECT) {
          continue;
        }

        // 若存在表则以表上的分片为准
        // 前置判断是否含有分片表环节已处理表所在分片不一致的场景，即抛出异常
        if (!hasSetShards) {
          dbShards = table.getDatabaseShards();
          hasSetShards = true;
        }

        // 若存在分表则组装分表信息
        if(table.hasTableShard()){
          tableShards.put(table.getName(), table.getActualTables());
        }
      }
    }

    RouteResult rr = new FullRouteResult();
    rr.setShards(vdb.getShards());
    rr.setParameters(context.getSqlParameters());
    rr.setDbShard(dbShards);
    rr.setTableShard(tableShards);
    context.setRouteResult(rr);

    // 「监控」全分片扫描次数加1
    Monitor.countScanAllShards();
  }
}
