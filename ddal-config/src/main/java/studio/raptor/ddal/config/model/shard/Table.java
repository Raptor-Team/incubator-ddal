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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.config.model.rule.ShardRule;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class Table {

  private String name;

  private List<String> databaseShards;
  private ShardRule databaseRule;

  private List<String> actualTables;
  private ShardRule tableRule;
  private boolean hasTableShard = false;

  private String[] shardColumns = new String[0];

  private boolean isMulti = false;

  private boolean isGlobal = false;

  private Table parentTable;
  private boolean isSubTable = false;

  private Map<String, String> sequences;
  private boolean autoIncrement = false;

  private Map<String, Index> indexColumn;
  private boolean hasIndex = false;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<String> getDatabaseShards() {
    return databaseShards;
  }

  public void setDatabaseShards(List<String> databaseShards) {
    this.databaseShards = databaseShards;
  }

  public ShardRule getDatabaseRule() {
    return databaseRule;
  }

  public void setDatabaseRule(ShardRule databaseRule) {
    this.databaseRule = databaseRule;
  }

  public List<String> getActualTables() {
    return actualTables;
  }

  public void setActualTables(List<String> actualTables) {
    this.actualTables = actualTables;
    this.hasTableShard = true;
  }

  public ShardRule getTableRule() {
    return tableRule;
  }

  public void setTableRule(ShardRule tableRule) {
    this.tableRule = tableRule;
  }

  public String[] getShardColumns() {
    return shardColumns;
  }

  public void addShardColumns(String[] shardColumns) {
    List<String> temp = new ArrayList<>(this.shardColumns.length + shardColumns.length);
    temp.addAll(Arrays.asList(this.shardColumns));
    for (String column : shardColumns) {
      if (!temp.contains(column)) {
        temp.add(column);
      }
    }
    this.shardColumns = temp.toArray(new String[temp.size()]);
  }

  public boolean isMulti() {
    return isMulti;
  }

  public void setMulti(boolean multi) {
    isMulti = multi;
  }

  public boolean isGlobal() {
    return isGlobal;
  }

  public void setGlobal(boolean global) {
    isGlobal = global;
  }

  public Table getParentTable() {
    return parentTable;
  }

  public void setParentTable(Table parentTable) {
    this.parentTable = parentTable;
  }

  public boolean isSubTable() {
    return isSubTable;
  }

  public void setSubTable(boolean subTable) {
    isSubTable = subTable;
  }

  public boolean hasTableShard() {
    return hasTableShard;
  }

  public void setHasTableShard(boolean hasTableShard) {
    this.hasTableShard = hasTableShard;
  }

  public Map<String, String> getSequences() {
    return sequences;
  }

  public void setSequences(Map<String, String> sequences) {
    this.sequences = sequences;
  }

  public boolean isAutoIncrement() {
    return autoIncrement;
  }

  public void setAutoIncrement(boolean autoIncrement) {
    this.autoIncrement = autoIncrement;
  }

  public Map<String, Index> getIndexColumn() {
    return indexColumn;
  }

  public void setIndexColumn(Map<String, Index> indexColumn) {
    this.indexColumn = indexColumn;
  }

  public boolean hasIndex() {
    return hasIndex;
  }

  public void setHasIndex(boolean hasIndex) {
    this.hasIndex = hasIndex;
  }

  @Override
  public String toString() {
    return "Table{" +
        "name='" + name + '\'' +
        ", databaseShards=" + databaseShards +
        ", databaseRule=" + databaseRule +
        ", actualTables=" + actualTables +
        ", tableRule=" + tableRule +
        ", shardColumns=" + Arrays.toString(shardColumns) +
        ", isMulti=" + isMulti +
        ", isGlobal=" + isGlobal +
        ", parentTable=" + parentTable +
        ", isSubTable=" + isSubTable +
        '}';
  }
}
