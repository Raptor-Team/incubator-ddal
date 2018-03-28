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

import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.SQLStatementType;

/**
 * 判断是否有自增主键。
 *
 * @author Charley
 * @since 3.0.0
 */
public class HasAutoIncrement extends ForkingNode {

  @Override
  protected int judge(ProcessContext context) {
    return judge0(context.getVirtualDb(), context.getParseResult()) ? 0 : 1;
  }

  /**
   * 判断是否需要处理自增主键
   *
   * @param vdb
   * @param parseResult
   * @return
   */
  private boolean judge0(VirtualDb vdb, ParseResult parseResult){
    //是否为Insert，是继续，否则返回false
    if(!SQLStatementType.INSERT.equals(parseResult.getSqlType())){
      return false;
    }

    String tableName = parseResult.getTableNames().iterator().next();
    Table table = vdb.getTable(tableName);
    //主键是否自增，是继续，否则返回false
    if(!table.isAutoIncrement()){
      return false;
    }
    //需要处理自增主键
    return true;
  }
}
