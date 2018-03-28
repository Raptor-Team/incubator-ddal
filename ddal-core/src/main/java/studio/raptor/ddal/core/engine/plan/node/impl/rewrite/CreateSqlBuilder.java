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

import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.parser.builder.impl.DefaultBuilderImpl;
import studio.raptor.ddal.core.parser.builder.impl.SqlDeleteBuilderImpl;
import studio.raptor.ddal.core.parser.builder.impl.SqlInsertBuilderImpl;
import studio.raptor.ddal.core.parser.builder.impl.SqlSelectBuilderImpl;
import studio.raptor.ddal.core.parser.builder.impl.SqlUpdateBuilderImpl;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.sqlparser.ast.statement.SQLDeleteStatement;
import studio.raptor.sqlparser.ast.statement.SQLInsertStatement;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;

/**
 * 构造SqlBuilder
 *
 * @author Sam
 * @since 3.0.0
 */
public class CreateSqlBuilder extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    ParseResult parseResult = context.getParseResult();
    String dbType = parseResult.getDbType().name().toLowerCase();
    switch (context.getParseResult().getSqlType()) {
      case SELECT:
        context.setSqlBuilder(new SqlSelectBuilderImpl(
            (SQLSelectStatement) parseResult.getStatement(), dbType));
        break;
      case DELETE:
        context.setSqlBuilder(new SqlDeleteBuilderImpl(
            (SQLDeleteStatement) parseResult.getStatement(), dbType));
        break;
      case INSERT:
        context.setSqlBuilder(new SqlInsertBuilderImpl(
            (SQLInsertStatement) parseResult.getStatement(), dbType));
        break;
      case UPDATE:
        context.setSqlBuilder(new SqlUpdateBuilderImpl(
            (SQLUpdateStatement) parseResult.getStatement(), dbType));
        break;
      default:
        context.setSqlBuilder(new DefaultBuilderImpl(parseResult, dbType));
        break;
    }
  }
}
