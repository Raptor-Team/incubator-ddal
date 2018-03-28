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

import java.util.ArrayList;
import java.util.List;
import studio.raptor.ddal.common.collections.FastArrayList;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.router.util.RouteCalculator;
import studio.raptor.ddal.core.router.util.RouteCondition;
import studio.raptor.sqlparser.stat.TableStat.Column;
import studio.raptor.sqlparser.stat.TableStat.Condition;

/**
 * 获取SQL中的分片表
 *
 * @author Sam
 * @since 3.0.0
 */
public class GetRouteCondition extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    List<Table> shardTables = RouteCalculator.getShardTable(context.getVirtualDb(),
        context.getParseResult().getTableNames());
    context.setShardTables(shardTables);
    List<RouteCondition> routeConditions = takeTableAndCondition(context, shardTables);
    context.setRouteConditions(routeConditions);
  }

  /**
   * 填装Table条件对象
   *
   * @param context 执行上下文
   * @return 表及分片条件集合
   */
  private List<RouteCondition> takeTableAndCondition(ProcessContext context,
      List<Table> shardTables) {
    List<RouteCondition> routeConditions = new ArrayList<>();

    //先从解析结果中获取分片条件
    List<Condition> shardConditions = new FastArrayList<>();
    List<Condition> parsedConditions = context.getParseResult().getConditions();
    if(!parsedConditions.isEmpty()){
      for (Table shardTable : shardTables) {
        String[] shardColumns = shardTable.getShardColumns();
        for (String shardColumn : shardColumns) {
          Condition condition = findCondition(parsedConditions, shardTable.getName(), shardColumn);
          if (null != condition && !condition.getValues().isEmpty()) {
            shardConditions.add(condition);
          } else {
            //从注解中获取条件
            condition = context.findHintShardCondition(shardColumn);
            if (null != condition) {
              shardConditions.add(condition);
            }
          }
        }
      }
    }else if(!context.getHintShardConditions().isEmpty()){
      shardConditions.addAll(context.getHintShardConditions());
    }
    //若SQL中不存在分片条件则从注解中获取
//    if(shardConditions.isEmpty() && !context.getHintShardConditions().isEmpty()){
//      shardConditions.addAll(context.getHintShardConditions());
//    }

    if (shardConditions.isEmpty()) {
      return routeConditions;
    }

    for (Table shardTable : shardTables) {
      RouteCondition routeCondition = new RouteCondition();

      //DB Shard Condition
      if (null != shardTable.getDatabaseRule()) {
        for (String column : shardTable.getDatabaseRule().getShardColumns()) {
          Condition condition = findCondition(shardConditions, shardTable.getName(), column);
          if (null != condition && !condition.getValues().isEmpty()) {
            routeCondition.addDbShardCondition(condition);
          }
        }
      }

      //Table Shard Condition
      if (null != shardTable.getTableRule()) {
        for (String column : shardTable.getTableRule().getShardColumns()) {
          Condition condition = findCondition(shardConditions, shardTable.getName(), column);
          if (null != condition && !condition.getValues().isEmpty()) {
            routeCondition.addTableShardCondition(condition);
          }
        }
      }

      if (!routeCondition.conditionIsEmpty()) {
        routeCondition.setShardTable(shardTable);
        routeConditions.add(routeCondition);
      }
    }

    return routeConditions;
  }

  /**
   * TODO 不要删除！原方法，Hint优先级大于条件，此策略应为最合理的
   * 填装Table条件对象
   *
   * @param context 执行上下文
   * @return 表及分片条件集合
   */
//  private List<RouteCondition> takeTableAndCondition(ProcessContext context,
//      List<Table> shardTables) {
//    List<Condition> conditions;
//
//    if (!context.getHintShardConditions().isEmpty()) {
//      conditions = context.getHintShardConditions();
//    } else {
//      conditions = context.getParseResult().getConditions();
//    }
//
//    List<RouteCondition> routeConditions = new ArrayList<>();
//    for (Table shardTable : shardTables) {
//      RouteCondition routeCondition = new RouteCondition();
//
//      //DB Shard Condition
//      if (null != shardTable.getDatabaseRule()) {
//        for (String column : shardTable.getDatabaseRule().getShardColumns()) {
//          Condition condition = findCondition(conditions, shardTable.getName(), column);
//          if (null != condition && !condition.getValues().isEmpty()) {
//            routeCondition.addDbShardCondition(condition);
//          }
//        }
//      }
//
//      //Table Shard Condition
//      if (null != shardTable.getTableRule()) {
//        for (String column : shardTable.getTableRule().getShardColumns()) {
//          Condition condition = findCondition(conditions, shardTable.getName(), column);
//          if (null != condition && !condition.getValues().isEmpty()) {
//            routeCondition.addTableShardCondition(condition);
//          }
//        }
//      }
//
//      if (!routeCondition.conditionIsEmpty()) {
//        routeCondition.setShardTable(shardTable);
//        routeConditions.add(routeCondition);
//      }
//    }
//
//    return routeConditions;
//  }

  //TODO 考虑换数据结构
  public Condition findCondition(List<Condition> conditions, String tableName, String columnName) {
    Condition resultCondition = null;
    if (!conditions.isEmpty()) {
      for (Condition condition : conditions) {
        Column column = condition.getColumn();
        // 解决SQL中带了属主导致找不到路由规则的问题
        String _table = column.getTable();
        int lastDotIndex;
        if ((lastDotIndex = _table.lastIndexOf('.')) != -1) {
          _table = _table.substring(lastDotIndex + 1);
        }
        if (_table.equalsIgnoreCase(tableName) && column.getName().equalsIgnoreCase(columnName)) {
          resultCondition = condition;
          break;
        }
      }
    }
    return resultCondition;
  }
}
