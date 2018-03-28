/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.raptor.sqlparser.wall.spi;

import studio.raptor.sqlparser.dialect.postgresql.parser.PGSQLStatementParser;
import studio.raptor.sqlparser.dialect.postgresql.visitor.PGExportParameterVisitor;
import studio.raptor.sqlparser.parser.SQLStatementParser;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.ExportParameterVisitor;
import studio.raptor.sqlparser.wall.WallConfig;
import studio.raptor.sqlparser.wall.WallProvider;
import studio.raptor.sqlparser.wall.WallVisitor;


public class PGWallProvider extends WallProvider {

  public final static String DEFAULT_CONFIG_DIR = "META-INF/raptor/sqlparser/wall/postgres";

  public PGWallProvider() {
    this(new WallConfig(DEFAULT_CONFIG_DIR));
  }

  public PGWallProvider(WallConfig config) {
    super(config, JdbcConstants.POSTGRESQL);
  }

  @Override
  public SQLStatementParser createParser(String sql) {
    return new PGSQLStatementParser(sql);
  }

  @Override
  public WallVisitor createWallVisitor() {
    return new PGWallVisitor(this);
  }

  @Override
  public ExportParameterVisitor createExportParameterVisitor() {
    return new PGExportParameterVisitor();
  }

}
