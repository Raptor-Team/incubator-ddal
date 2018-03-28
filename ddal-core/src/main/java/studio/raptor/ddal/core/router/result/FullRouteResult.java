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

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.config.model.shard.Shard;

/**
 * 全量路由结果集定义
 *
 * @author Charley
 * @since 1.0
 */
public class FullRouteResult extends RouteResult {

  @Override
  public List<RouteNode> getRouteNodes() {
    if (!this.routeNodes.isEmpty()) {
      return this.routeNodes;
    }

    List<Shard> resolvedShard = getResolvedShard();
    List<Map<String, String>> actualTables = cartesianTable();

    if (actualTables.isEmpty()) {
      for (Shard shard : resolvedShard) {
        RouteNode routeNode = new RouteNode();
        routeNode.setParameters(this.parameters);
        routeNode.setShard(shard);
        this.routeNodes.add(routeNode);
      }
    } else {
      for (Shard shard : resolvedShard) {
        for (Map<String, String> actualTable : actualTables) {
          RouteNode routeNode = new RouteNode();
          routeNode.setParameters(this.parameters);
          routeNode.setShard(shard);
          routeNode.setActualTables(actualTable);
          this.routeNodes.add(routeNode);
        }
      }
    }

    return this.routeNodes;
  }

  private List<Shard> getResolvedShard() {
    List<Shard> shardList = new FastArrayList<>();
    for (String shardName : this.dbShard) {
      shardList.add(this.shards.get(shardName));
    }
    return shardList;
  }

  private List<Map<String, String>> cartesianTable() {
    List<Map<String, String>> actualTables = new FastArrayList<>();

    Entry<String, Collection<String>> left = null;
    Entry<String, Collection<String>> right = null;
    int index = 0;
    for (Entry<String, Collection<String>> entry : this.tableShard.entrySet()) {
      if (index == 0) {
        left = entry;
      }
      if (index == 1) {
        right = entry;
      }
      index++;
    }

    if (null != left && right == null) {
      for (String leftActualTable : left.getValue()) {
        Map<String, String> tableMap = new HashMap<>();
        tableMap.put(left.getKey(), leftActualTable);
        actualTables.add(tableMap);
      }
    }

    if (null != left && null != right) {
      for (String leftActualTable : left.getValue()) {
        for (String rightActualTable : right.getValue()) {
          Map<String, String> tableMap = new HashMap<>();
          tableMap.put(left.getKey(), leftActualTable);
          tableMap.put(right.getKey(), rightActualTable);
          actualTables.add(tableMap);
        }
      }
    }

    return actualTables;
  }
}


