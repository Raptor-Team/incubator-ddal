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

package studio.raptor.ddal.config.model.shard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;
import studio.raptor.ddal.common.exception.ConfigException;
import studio.raptor.ddal.common.exception.ConfigException.Code;
import studio.raptor.ddal.common.util.StringUtil;

public class VirtualDb {

  private String name;
  private boolean rmOwner;
  private int sqlMaxLimit;
  private String shardGroup;
  private Shards shards;
  private Tables tables;
  private List<Wildcard> wildcards = new ArrayList<>();
  private Sequences sequences;

  /**
   * 缓存应用SQL中的表名与其大小写的映射关系，避免每次都做大小写转换
   */
  private static transient Map<String, String> ucase0 = new ConcurrentHashMap<>();

  /**
   * 匹配通配符的表，生成表配置，并存放在内存
   */
  private static Map<String, Table> memoryGlobalTables = new ConcurrentHashMap<>();

  public VirtualDb() {
  }

  public Table getTable(String tableName) {
    String ucaseTableName = ucase0.get(tableName);
    if (StringUtil.isEmpty(ucaseTableName)) {
      ucaseTableName = tableName.toUpperCase();
      ucase0.put(tableName, ucaseTableName);
    }
    Table table = tables.get(ucaseTableName);
    if (null == table) {
      //从内存配置中获取
      table = memoryGlobalTables.get(ucaseTableName);
      if(null == table){
        //是否匹配通配符
        Wildcard wildcard = marchWildcard(ucaseTableName);
        if(null != wildcard){
          table = createMemoryTable(ucaseTableName, wildcard);
        }else{
          throw ConfigException.create(Code.CONFIG_NOT_FOUND_ERROR, "Table name: "+ucaseTableName);
        }
      }
    }
    return table;
  }

  private Wildcard marchWildcard(String tableName){
    Wildcard result = null;
    for(Wildcard wildcard : wildcards){
      if(null != wildcard.getPattern() && Pattern.matches(wildcard.getPattern(), tableName)){
        result = wildcard;
        break;
      }
    }
    return result;
  }

  /**
   * 创建通配符
   * @param name
   * @param wildcard
   */
  private Table createMemoryTable(String name, Wildcard wildcard){
    Table table = new Table();
    table.setName(name);
    table.setGlobal(wildcard.isGlobal());
    table.setDatabaseShards(wildcard.getDatabaseShards());
    memoryGlobalTables.put(name, table);
    return table;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public boolean isRmOwner() {
    return rmOwner;
  }

  public void setRmOwner(boolean rmOwner) {
    this.rmOwner = rmOwner;
  }

  public int getSqlMaxLimit() {
    return sqlMaxLimit;
  }

  public void setSqlMaxLimit(int sqlMaxLimit) {
    this.sqlMaxLimit = sqlMaxLimit;
  }

  public VirtualDb(String name, boolean rmOwner, int sqlMaxLimit, Shards shards, Tables tables,
      Sequences sequences) {
    super();
    this.name = name;
    this.rmOwner = rmOwner;
    this.sqlMaxLimit = sqlMaxLimit;
    this.shards = shards;
    this.tables = tables;
    this.sequences = sequences;
  }

  public String getShardGroup() {
    return shardGroup;
  }

  public void setShardGroup(String shardGroup) {
    this.shardGroup = shardGroup;
  }

  public Shards getShards() {
    return shards;
  }

  public void setShards(Shards shards) {
    this.shards = shards;
  }

  public Tables getTables() {
    return tables;
  }

  public void setTables(Tables tables) {
    this.tables = tables;
  }

  public List<Wildcard> getWildcards() {
    return wildcards;
  }

  public void setWildcards(List<Wildcard> wildcards) {
    this.wildcards = wildcards;
  }

  public Sequences getSequences() {
    return sequences;
  }

  public void setSequences(Sequences sequences) {
    this.sequences = sequences;
  }

  @Override
  public String toString() {
    return "VirtualDb [name=" + name + ", rmOwner=" + rmOwner + ", sqlMaxLimit=" + sqlMaxLimit
        + ", tables="
        + tables + ", sequences=" + sequences + "]";
  }

}
