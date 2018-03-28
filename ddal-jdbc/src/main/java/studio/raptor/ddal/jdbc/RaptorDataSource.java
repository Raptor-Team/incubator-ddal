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

package studio.raptor.ddal.jdbc;


import java.sql.Connection;
import java.sql.SQLException;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.JdbcErrCodes;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.constants.DatabaseType;
//import studio.raptor.ddal.core.transaction.TransactionRecovery;
import studio.raptor.ddal.jdbc.adapter.AbstractDataSourceAdapter;

/**
 * Supports shard data sources.
 *
 * @author Sam
 */
public class RaptorDataSource extends AbstractDataSourceAdapter {

  private final String virtualDb;
  private final String databaseDialect;

  private static Boolean txRecovered = null;
  private static final Object REC_LOCK = new Object();

  public RaptorDataSource(String virtualDb, String databaseDialect) {
    this.virtualDb = virtualDb;
    this.databaseDialect = databaseDialect;
    // check and recover transaction
    if(null == txRecovered) {
      synchronized (REC_LOCK) {
        if(null == txRecovered) {
          txRecovered = true;
          //new TransactionRecovery().recover();
        }
      }
    }
  }

  @Override
  public Connection getConnection() throws SQLException {
    return new RaptorConnection(detectDbType(), virtualDb);
  }

  private DatabaseType detectDbType() {
    if (null == databaseDialect) {
      throw new GenericException(JdbcErrCodes.JDBC_606, "", "");
    }
    if ("MySQL".equalsIgnoreCase(databaseDialect)) {
      return DatabaseType.MySQL;
    } else if ("Oracle".equalsIgnoreCase(databaseDialect)) {
      return DatabaseType.Oracle;
    }
    throw new GenericException(JdbcErrCodes.JDBC_606, "", databaseDialect);
  }
}
