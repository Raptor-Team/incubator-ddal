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
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.config.model.shard.Shard;
import studio.raptor.ddal.config.model.shard.Shards;

/**
 * 路由结果集定义
 *
 * @author Charley
 * @since 1.0
 */
public class RouteResult {

    protected Shards shards;

    protected Collection<String> dbShard = new ArrayList<>();

    protected Map<String/* virtualTable */, Collection<String>/* actualTable */> tableShard = new HashMap<>();

    protected List<Object> parameters;// 参数列表，预编译语句有值，非预编译则为Null

    //路由节点集合
    protected final List<RouteNode> routeNodes = new LinkedList<>();

    public Shards getShards() {
        return shards;
    }

    public void setShards(Shards shards) {
        this.shards = shards;
    }

    public Collection<String> getDbShard() {
        return dbShard;
    }

    public void setDbShard(Collection<String> dbShard) {
        this.dbShard = dbShard;
    }

    public void addDbShard(String shard) {
        this.dbShard.add(shard);
    }

    public Map<String, Collection<String>> getTableShard() {
        return tableShard;
    }

    public void setTableShard(Map<String, Collection<String>> tableShard) {
        this.tableShard = tableShard;
    }

    public void addTableShard(String virtualTable, List<String> actualTables) {
        this.tableShard.put(virtualTable, actualTables);
    }

    public List<Object> getParameters() {
        return parameters;
    }

    public void setParameters(List<Object> parameters) {
        this.parameters = parameters;
    }

    /**
     * 添加预编译语句路由节点
     *
     * @param parameters    参数列表
     * @param shard      分片名称
     */
    public void addNode(List<Object> parameters, Shard shard, Map<String, String> actualTables) {
        RouteNode routeNode = new RouteNode(parameters, shard, actualTables);
        this.routeNodes.add(routeNode);
    }

    public List<RouteNode> getRouteNodes() {
        return routeNodes;
    }

    @Override
    public String toString() {
        return "RouteResult{" +
                "routeNodes=" + routeNodes +
                '}';
    }
}


