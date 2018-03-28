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
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;
import studio.raptor.ddal.core.router.util.RouteCalculator;

/**
 * 判断SQL中是否含有分片表
 *
 * @author Sam
 * @since 3.0.0
 */
public class HasShardTableInSQL extends ForkingNode {

  @Override
  protected int judge(ProcessContext context) {
    List<Table> shardTables = RouteCalculator.getShardTable(context.getVirtualDb(),
        context.getParseResult().getTableNames());
    context.setShardTables(shardTables);
    return !shardTables.isEmpty() ? 0 : 1;
  }
}
