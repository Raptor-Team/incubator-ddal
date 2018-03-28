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

package studio.raptor.ddal.core.parser.builder.impl;


import java.util.Collection;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.statement.SQLExprTableSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DefaultBuilderImpl extends AbstractSqlBuilder {

  private ThreadLocal<SQLStatement> stmt;
  private Collection<SQLExprTableSource> tableSources;
  private String dbType;

  public DefaultBuilderImpl(ParseResult parseResult, String dbType) {
    this.stmt = new ThreadLocal<>();
    this.stmt.set(parseResult.getStatement());
    this.tableSources = parseResult.getTableSources().values();
    this.dbType = dbType;
  }

  @Override
  public void rewriteSchema(String schema) {
    rewriteSchema(tableSources.iterator().next(), schema);
  }

  @Override
  public void rewriteTable(String table) {
    rewriteTable(tableSources.iterator().next(), table);
  }

  @Override
  SQLStatement getSqlStatement() {
    return this.stmt.get();
  }

  @Override
  String getDbType() {
    return this.dbType;
  }
}
