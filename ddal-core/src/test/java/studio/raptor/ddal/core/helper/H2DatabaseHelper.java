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

package studio.raptor.ddal.core.helper;

import org.h2.Driver;
import org.h2.tools.DeleteDbFiles;
import studio.raptor.ddal.common.util.StringUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * H2 Database operate helper
 *
 * @author Sam
 * @since 3.0.0
 */
public class H2DatabaseHelper {

    private String userName;
    private String dbUrl;
    private String password;
    private String path;
    private String db;

    public H2DatabaseHelper(String path, String db, String userName, String password, int dbMode) throws SQLException {
        Driver.load();
        this.path = path;
        this.db = db;
        if (dbMode == 1) {
            this.dbUrl = String.format("jdbc:h2:mem:%s;FILE_LOCK=SOCKET", db);
        } else if (dbMode == 2) {
            this.dbUrl = String.format("jdbc:h2:%s/%s;FILE_LOCK=SOCKET", path, db);
        }
        this.password = password;
        this.userName = userName;
    }

    @SuppressWarnings("unused")
    private boolean execute(String sql) throws SQLException {
        try (
                Connection connection = tryToGetConnection();
                Statement statement = connection.createStatement()
        ) {
            return statement.execute(sql);
        }
    }

    void prepareH2Database() throws SQLException, IOException {
        DeleteDbFiles.execute(path, db, false);
        System.out.println("Preparing memory database [" + this.dbUrl + "]");

        String[] filePaths = new String[]{"/all_schema.sql"};
        BufferedReader[] bufferedReaders = new BufferedReader[1];
        try (
                Connection connection = tryToGetConnection();
                Statement statement = connection.createStatement()
        ) {
            for (int i = 0; i < filePaths.length; i++) {
                bufferedReaders[i] = new BufferedReader(new InputStreamReader(H2DatabaseHelper.class.getResourceAsStream(filePaths[i])));
            }

            for (BufferedReader bufferedReader : bufferedReaders) {
                String sqlLine;
                while (null != (sqlLine = bufferedReader.readLine())) {
                    if (!StringUtil.isEmpty(sqlLine) && !sqlLine.trim().startsWith("--")) {
                        statement.execute(sqlLine);
                    }
                }
            }

        } finally {
            for (BufferedReader bufferedReader : bufferedReaders) {
                if(null != bufferedReader) {
                    bufferedReader.close();
                }
            }
        }

        System.out.println("Data has been loaded!");
    }

    public Connection tryToGetConnection() throws SQLException {
        return DriverManager.getConnection(this.dbUrl, this.userName, this.password);
    }

    public void releaseConnection(Connection connection) throws SQLException {
        if (null != connection) {
            connection.close();
        }
    }
}
