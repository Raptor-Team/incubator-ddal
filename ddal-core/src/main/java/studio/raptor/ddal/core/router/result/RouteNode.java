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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.config.model.shard.Shard;

/**
 * 路由结果节点
 *
 * @author Charley
 * @since 1.0
 */
public class RouteNode {

  /**
   * SQL语句重写占位符属性
   */
  private Map<String, String> rewriteAttributes = new HashMap<>();
  private String finalSql;// 最终执行SQL
  private List<Object> parameters;// 参数列表，预编译语句有值，非预编译则为Null
  private Shard shard;// 最终执行分片名
  private Map<String, String> actualTables = new HashMap<>();//实际表Map

  public RouteNode() {
  }

  public RouteNode(List<Object> parameters, Shard shard, Map<String, String> actualTables) {
    this.parameters = parameters;
    this.shard = shard;
    this.actualTables = actualTables;
  }

  public void setFinalSql(String finalSql) {
    this.finalSql = finalSql;
  }

  public String getFinalSql() {
    return finalSql;
  }

  public List<Object> getParameters() {
    return parameters;
  }

  public Shard getShard() {
    return shard;
  }

  public void setParameters(List<Object> parameters) {
    this.parameters = parameters;
  }

  public void setShard(Shard shard) {
    this.shard = shard;
  }

  public Map<String, String> getActualTables() {
    return actualTables;
  }

  public void setActualTables(Map<String, String> actualTables) {
    this.actualTables = actualTables;
  }

  public void addActualTable(String virtualTable, String actualTable) {
    actualTables.put(virtualTable, actualTable);
  }

  public Map<String, String> getRewriteAttributes() {
    return rewriteAttributes;
  }

  @Override
  public String toString() {
    return "RouteNode{" +
        "rewriteAttributes=" + rewriteAttributes +
        ", finalSql='" + finalSql + '\'' +
        ", parameters=" + parameters +
        ", shard=" + shard +
        ", actualTables=" + actualTables +
        '}';
  }
}
