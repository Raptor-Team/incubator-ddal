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


import java.util.Map;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.statement.SQLUpdateStatement;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SqlUpdateBuilderImpl extends AbstractSqlBuilder {

  private ThreadLocal<SQLUpdateStatement> stmt;
  private String dbType;

  public SqlUpdateBuilderImpl(SQLUpdateStatement stmt, String dbType) {
    this.stmt = new ThreadLocal<>();
    this.stmt.set(stmt);
    this.dbType = dbType;
  }

  @Override
  public void rewriteSchema(String schema) {
    rewriteSchema(stmt.get().getTableSource(), schema);
  }

  @Override
  public void rewriteTable(String table) {
    rewriteTable(stmt.get().getTableSource(), table);
  }

  @Override
  SQLStatement getSqlStatement() {
    return stmt.get();
  }

  @Override
  String getDbType() {
    return dbType;
  }
}
