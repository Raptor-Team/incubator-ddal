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

package studio.raptor.ddal.tests;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.jdbc.RaptorRoutingDataSource;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class AutoPrepareTestingEnv {
  protected static Logger logger = LoggerFactory.getLogger(AutoPrepareTestingEnv.class);
  protected static Connection connection;
  protected static RaptorRoutingDataSource dataSourceRouter;

  @BeforeClass
  public static void setup() throws SQLException, IOException {

    H2DatabaseHelper h2DatabaseHelper = new H2DatabaseHelper("~", "db_w", "root", "root", 2);
    h2DatabaseHelper.initialize(new String[]{"/sql/all_table.sql", "/sql/data_rw.sql"});

    // readonly_1
    h2DatabaseHelper = new H2DatabaseHelper("~", "db_r_1", "root", "root", 2);
    h2DatabaseHelper.initialize(new String[]{"/sql/all_table.sql", "/sql/data_r_1.sql"});

    // readonly_2
    h2DatabaseHelper = new H2DatabaseHelper("~", "db_r_2", "root", "root", 2);
    h2DatabaseHelper.initialize(new String[]{"/sql/all_table.sql", "/sql/data_r_2.sql"});

    // readonly_3
    h2DatabaseHelper = new H2DatabaseHelper("~", "db_r_3", "root", "root", 2);
    h2DatabaseHelper.initialize(new String[]{"/sql/all_table.sql", "/sql/data_r_3.sql"});

    dataSourceRouter = new RaptorRoutingDataSource("mysql");
    connection = dataSourceRouter.route("school").getConnection();
    connection.setAutoCommit(false);
  }

  @AfterClass
  public static void after() throws SQLException {
    if (null != connection) {
      connection.commit();
      connection.close();
    }
  }

  protected List<Map<String, Object>> orm(ResultSet resultSet) throws SQLException {
    List<Map<String, Object>> result = new ArrayList<>();
    ResultSetMetaData metaData = resultSet.getMetaData();
    int columnCount = metaData.getColumnCount();
    while (resultSet.next()) {
      Map<String, Object> rowData = new HashMap<>();
      for (int i = 1; i <= columnCount; i++) {
        rowData.put(metaData.getColumnLabel(i), resultSet.getObject(i));
      }
      result.add(rowData);
    }
    return result;
  }
}
