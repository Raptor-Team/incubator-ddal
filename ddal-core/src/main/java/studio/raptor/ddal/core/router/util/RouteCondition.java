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

package studio.raptor.ddal.core.router.util;

import java.util.List;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.sqlparser.stat.TableStat.Condition;

/**
 * Table与Column集合
 *
 * @author Charley
 * @since 1.0
 */
public class RouteCondition {

  private Table shardTable;

  private List<Condition> dbShardCondition = new FastArrayList<>();

  private List<Condition> tableShardCondition = new FastArrayList<>();

  public Table getShardTable() {
    return shardTable;
  }

  public void setShardTable(Table shardTable) {
    this.shardTable = shardTable;
  }

  public List<Condition> getDbShardCondition() {
    return dbShardCondition;
  }

  public void addDbShardCondition(Condition condition) {
    this.dbShardCondition.add(condition);
  }

  public List<Condition> getTableShardCondition() {
    return tableShardCondition;
  }

  public void addTableShardCondition(Condition condition) {
    this.tableShardCondition.add(condition);
  }

  public boolean conditionIsEmpty() {
    return dbShardCondition.isEmpty() && tableShardCondition.isEmpty();
  }
}
