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

package studio.raptor.ddal.core.engine.plan.node.impl.rewrite;

import java.util.Map;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.sequence.DefaultIdGenerator;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLVariantRefExpr;
import studio.raptor.sqlparser.ast.statement.SQLInsertInto;

/**
 * 处理自增主键
 *
 * @author Charley
 * @since  1.0
 */
public class HandleAutoIncrement extends ProcessNode {

  /**
   * 处理自增主键
   *
   * @param context
   */
  @Override
  protected void execute(ProcessContext context) {
    ParseResult parseResult = context.getParseResult();
    VirtualDb virtualDb = context.getVirtualDb();
    String tableName = parseResult.getTableNames().iterator().next();
    Table table = virtualDb.getTable(tableName);
    if(!parseResult.isHandledAutoIncrement()){
      Map<String, String> sequences = table.getSequences();
      for(Map.Entry<String, String> entry : sequences.entrySet()){
        String column = entry.getKey();
        if(!parseResult.containsColumn(tableName, column)){
          //改写解析语法树
          SQLInsertInto statement = (SQLInsertInto) parseResult.getStatement();
          statement.addColumn(new SQLIdentifierExpr(column));
          statement.getValues().addValue(new SQLVariantRefExpr("?"));

          //参数列表增加序列
          context.getSqlParameters().add(DefaultIdGenerator.getInstance().nextId(entry.getValue()));
        }
      }
      parseResult.setHandledAutoIncrement(true);
    }
  }
}
