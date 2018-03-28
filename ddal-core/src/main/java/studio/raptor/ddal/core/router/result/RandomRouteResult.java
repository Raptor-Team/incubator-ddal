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

package studio.raptor.ddal.core.router.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;
import java.util.concurrent.ThreadLocalRandom;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.RouteErrCodes;
import studio.raptor.ddal.config.model.shard.Shard;

/**
 * 随机路由结果集定义
 *
 * @author Charley
 * @since 1.0
 */
public class RandomRouteResult extends RouteResult {

  @Override
  public List<RouteNode> getRouteNodes() {
    if (!this.routeNodes.isEmpty()) {
      return this.routeNodes;
    }

    RouteNode routeNode = new RouteNode();
    routeNode.setParameters(this.parameters);

    int dbIndex = ThreadLocalRandom.current().nextInt(this.dbShard.size());
    Shard dbShard = this.shards.get(new ArrayList<>(this.dbShard).get(dbIndex));
    if (null == dbShard) {
      throw new GenericException(RouteErrCodes.ROUTE_411);
    }
    routeNode.setShard(dbShard);

    if (!this.tableShard.isEmpty()) {
      for (Entry<String, Collection<String>> entry : this.tableShard.entrySet()) {
        int tableIndex = ThreadLocalRandom.current().nextInt(entry.getValue().size());
        routeNode.addActualTable(entry.getKey(), new ArrayList<>(entry.getValue()).get(tableIndex));
      }
    }
    this.routeNodes.add(routeNode);

    return this.routeNodes;
  }
}


