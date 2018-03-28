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

package studio.raptor.ddal.core.executor;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.helper.H2DatabaseHelper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * 结果集工具测试类
 *
 * @author Charley
 * @since 1.0
 */
public class ResultDataBuilderTest {

    private static H2DatabaseHelper h2DatabaseHelper;
    private static Connection connection;

    @BeforeClass
    public static void setup() throws SQLException, IOException {
        h2DatabaseHelper = new H2DatabaseHelper("~", "db1", "sa", "sa", 2);
        connection = h2DatabaseHelper.tryToGetConnection();
    }

    @AfterClass
    public static void after() throws SQLException {
        if (null != connection) {
            h2DatabaseHelper.releaseConnection(connection);
        }
    }

    @Test
    public void testCreateResultData() throws ClassNotFoundException, SQLException {
        String sql = "select * from schema0.customer";
        try (
                Statement statement = connection.createStatement();
                ResultSet rs = statement.executeQuery(sql);
        ) {
            ResultData resultData = new ResultData(rs);
            Assert.assertEquals(4, resultData.getColumnCount());
            Assert.assertEquals(4, resultData.getRowCount());
        }
    }
}
