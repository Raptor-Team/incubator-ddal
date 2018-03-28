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

package studio.raptor.ddal.core.engine.plan.node.impl.parse;

import java.util.Set;
import studio.raptor.ddal.common.exception.ParseSqlException;
import studio.raptor.ddal.common.exception.ParseSqlException.Code;
import studio.raptor.ddal.common.sql.SQLHintParser.Shard;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOperator;
import studio.raptor.sqlparser.stat.TableStat.Column;
import studio.raptor.sqlparser.stat.TableStat.Condition;

/**
 * 处理隐藏分片键值对
 *
 * @author Sam
 * @since 3.0.0
 */
public class HandleShardKeyValueHint extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    context.getHintShardConditions().add(parseRouteHint(context));
  }


  private Condition parseRouteHint(ProcessContext context) {
    Shard shard = context.getSqlHint().getShard();
    Set<String> tableNames = context.getParseResult().getTableNames();
    //目前只支持单表Hint
    if(tableNames.isEmpty()){
      throw ParseSqlException.create(Code.UNSUPPORTED_SQL_ERROR);
    }
    String tableName = tableNames.iterator().next();
    Condition hintCondition = createHiddenCondition(tableName , shard.getColumn(), shard.getValue());
    return hintCondition;
  }

  /**
   * 填充隐藏分片键值对参数
   *
   * 目前只支持单表Hint
   */
  private Condition createHiddenCondition(String tableName, String columnName, Object value) {
      Condition condition = new Condition();
      Column column = new Column(tableName, columnName);
      condition.setColumn(column);
      condition.setOperator(SQLBinaryOperator.Equality.name);
      condition.getValues().add(value);
      return condition;
  }
}
