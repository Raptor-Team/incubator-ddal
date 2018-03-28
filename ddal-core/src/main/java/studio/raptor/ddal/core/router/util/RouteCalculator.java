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

import com.google.common.collect.BoundType;
import com.google.common.collect.Range;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import studio.raptor.ddal.common.algorithm.MultiKeyShardAlgorithm;
import studio.raptor.ddal.common.algorithm.ShardAlgorithm;
import studio.raptor.ddal.common.algorithm.ShardValue;
import studio.raptor.ddal.common.algorithm.SingleKeyShardAlgorithm;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.RouteErrCodes;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.parser.result.ParameterGenerator;
import studio.raptor.sqlparser.stat.TableStat.Condition;

/**
 * 路由计算器
 *
 * @author Charley
 * @since 1.0
 */
public class RouteCalculator {

  /**
   * 分库路有结果计算
   * @param parameters
   * @param routeCondition
   * @return
   */
  public static Collection<String> calculateDbShard( List<Object> parameters, RouteCondition routeCondition) {
    Table table = routeCondition.getShardTable();

    //无分片条件
    if (null == table.getDatabaseRule() || routeCondition.getDbShardCondition().isEmpty()) {
      return table.getDatabaseShards();
    }

    ShardAlgorithm algorithm = table.getDatabaseRule().getAlgorithm();

    List<Condition> conditions = routeCondition.getDbShardCondition();
    //一维分片
    if (algorithm instanceof SingleKeyShardAlgorithm) {
      ShardValue<?> shardValue = createShardValue(parameters, conditions.get(0));
      return doSingleKeyShard(table.getDatabaseShards(), (SingleKeyShardAlgorithm) algorithm, shardValue);
    }
    //多维分片
    else {
      List<ShardValue<?>> shardValues = createShardValues(parameters, table.getDatabaseRule().getShardColumns(), conditions);
      return doMultiKeyShard(table.getDatabaseShards(), (MultiKeyShardAlgorithm) algorithm, shardValues);
    }
  }


  /**
   * 分表路由结果计算
   * @param parameters
   * @param routeCondition
   * @return
   */
  public static Map<String, Collection<String>> calculateTableShard(List<Table> shardTables, List<Object> parameters, RouteCondition routeCondition) {
    Map<String, Collection<String>> result = new HashMap<>();

    Table table = routeCondition.getShardTable();

    if (null == table.getTableRule()) {
      return result;
    }

    List<Condition> conditions = routeCondition.getTableShardCondition();
    if (conditions.isEmpty()) {
      result.put(table.getName(), table.getActualTables());
      return result;
    }

    ShardAlgorithm algorithm = table.getTableRule().getAlgorithm();
    //一维分片
    if (algorithm instanceof SingleKeyShardAlgorithm) {
      ShardValue<?> shardValue = createShardValue(parameters, conditions.get(0));
      Collection<String> shards = doSingleKeyShard(table.getActualTables(), (SingleKeyShardAlgorithm) algorithm, shardValue);

      result.put(table.getName(), shards);
      for(Table otherTable : shardTables){
        if(otherTable != table && otherTable.hasTableShard()){
          result.put(otherTable.getName(), otherTable.getActualTables());
        }
      }
    }
    //多维分片
    else {
      List<ShardValue<?>> shardValues = createShardValues(parameters,table.getTableRule().getShardColumns(), conditions);
      Collection<String> shards = doMultiKeyShard(table.getActualTables(), (MultiKeyShardAlgorithm) algorithm, shardValues);
      result.put(table.getName(), shards);
    }
    return result;
  }

  /**
   * 创建分片值对象数组
   */
  private static List<ShardValue<?>> createShardValues( List<Object> parameters,String[] shardColumns, List<Condition> conditions) {
    List<ShardValue<?>> shardValues = new ArrayList<>(conditions.size());
    for(String shardColumn : shardColumns){
      boolean hitCondition = false;
      for (Condition condition : conditions) {
        if(!hitCondition && shardColumn.equals(condition.getColumn().getName().toUpperCase())){
          shardValues.add(createShardValue(parameters, condition));
          hitCondition = true;
        }
      }
      if(!hitCondition){
        shardValues.add(new ShardValue<>());
      }
    }
    return shardValues;
  }

  /**
   * 将条件对象转换为分片值对象.
   *
   * @param condition 条件对象
   * @return 分片值对象
   */
  private static ShardValue<?> createShardValue(List<Object> parameters, Condition condition) {
    List<Object> conditionValues = ParameterGenerator.parameterizeValues(parameters, condition.getValues());
    switch (condition.getOperator()) {
      case "=":
      case "IN":
        if (1 == conditionValues.size()) {
          return new ShardValue(condition.getColumn().getName(),
              (Comparable<?>) conditionValues.get(0));
        }
        return new ShardValue(condition.getColumn().getName(), conditionValues);
      case "between":
        return new ShardValue(condition.getColumn().getName(),
            Range.range((Comparable<?>) conditionValues.get(0), BoundType.CLOSED, (Comparable<?>) conditionValues.get(1),
                BoundType.CLOSED));
      default:
        return new ShardValue<>();
    }
  }

  /**
   * 一维分片计算
   */
  private static Collection<String> doSingleKeyShard(List<String> shards,
      SingleKeyShardAlgorithm<?> singleKeyAlgorithm, ShardValue shardValue) {
    switch (shardValue.getType()) {
      case SINGLE:
        return Collections.singletonList(singleKeyAlgorithm.doEqual(shards, shardValue));
      case LIST:
        return singleKeyAlgorithm.doIn(shards, shardValue);
      case RANGE:
        return singleKeyAlgorithm.doBetween(shards, shardValue);
      case OTHER:
        return shards;
      default:
        throw new UnsupportedOperationException(shardValue.getType().getClass().getName());
    }
  }

  /**
   * 多维分片计算
   */
  private static Collection<String> doMultiKeyShard(List<String> shardNames,
      MultiKeyShardAlgorithm multiKeyAlgorithm, Collection<ShardValue<?>> shardValues) {
    return multiKeyAlgorithm.shard(shardNames, shardValues);
  }


  /**
   * 判断语句中是含有分片表.
   *
   * 1、如果tableNames中出现分片不一致时，不许执行。
   * 2、如果出现2个以上分表也不许执行。
   *
   * @param vDB 虚拟DB配置
   * @param tableNames 表名集合
   * @return 分片表列表
   */
  public static List<Table> getShardTable(VirtualDb vDB, Set<String> tableNames) {
    List<Table> shardTables = new ArrayList<>(tableNames.size());
    int hasActualTableCount = 0;
    boolean readTableShards = true;
    String shardString = "";
    for (String tableName : tableNames) {
      Table table = vDB.getTable(tableName);
      if (table.isGlobal()) {
        continue;
      }
      if (readTableShards) {
        shardString = table.getDatabaseShards().toString();
        readTableShards = false;
      }
      // 表的分片范围不一致时，快速报错。
      if (!shardString.equals(table.getDatabaseShards().toString())) {
        throw new GenericException(RouteErrCodes.ROUTE_431, tableNames);
      }

      // 分表最大只允许两个，超过两个报错。
      if (table.hasTableShard()) {
        if (hasActualTableCount <= 2) {
          hasActualTableCount++;
        } else {
          throw new GenericException(RouteErrCodes.ROUTE_430);
        }
      }
      shardTables.add(table);
    }
    return shardTables;
  }
}
