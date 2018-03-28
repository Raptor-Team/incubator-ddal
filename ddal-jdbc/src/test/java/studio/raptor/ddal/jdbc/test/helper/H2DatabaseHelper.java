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

package studio.raptor.ddal.jdbc.test.helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import org.h2.Driver;
import org.h2.tools.DeleteDbFiles;
import studio.raptor.ddal.common.util.StringUtil;

/**
 * H2 Database operate helper
 *
 * @author Sam
 * @since 3.0.0
 */
public class H2DatabaseHelper {

  private String dbUrl;
  private String path;
  private String db;

  public H2DatabaseHelper(String path, String db, int dbMode) throws SQLException {
    Driver.load();
    this.path = path;
    this.db = db;
    if (dbMode == 1) {
      this.dbUrl = String.format("jdbc:h2:mem:%s;FILE_LOCK=SOCKET", db);
    } else if (dbMode == 2) {
      this.dbUrl = String.format("jdbc:h2:%s/%s;FILE_LOCK=SOCKET", path, db);
    }
  }

  public boolean createSchema(String schema) throws SQLException {
    return execute("create schema " + schema);
  }

  private boolean execute(String sql) throws SQLException {
    Statement statement = null;
    Connection connection = null;
    try {
      connection = tryToGetConnection();
      statement = connection.createStatement();
      return statement.execute(sql);
    } finally {
      if (null != statement) {
        statement.close();
      }
      releaseConnection(connection);
    }
  }

  public void prepareH2Database() throws SQLException, IOException {
    DeleteDbFiles.execute(path, db, false);
    System.out.println("Preparing memory database [" + this.dbUrl + "]");
    Statement statement = null;
    Connection connection = null;
    BufferedReader bufferedReader = null;
    try {
      connection = tryToGetConnection();
      statement = connection.createStatement();

      bufferedReader = new BufferedReader(
          new InputStreamReader(H2DatabaseHelper.class.getResourceAsStream("/all_schema.sql")));
      String sqlLine;
      while (null != (sqlLine = bufferedReader.readLine())) {
        if (!StringUtil.isEmpty(sqlLine) && !sqlLine.trim().startsWith("--")) {
          //System.out.println("execute:" + sqlLine);
          statement.execute(sqlLine);
        }
      }
    } finally {
      if (null != statement) {
        statement.close();
      }
      releaseConnection(connection);
      if (null != bufferedReader) {
        bufferedReader.close();
      }
    }
  }

  private Connection tryToGetConnection() throws SQLException {
    String userName = "sa";
    String password = "sa";
    return DriverManager.getConnection(this.dbUrl, userName, password);
  }

  private void releaseConnection(Connection connection) throws SQLException {
    if (null != connection) {
      connection.close();
    }
  }
}
