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

import org.junit.BeforeClass;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class PrepareH2TestingEnv {

    @BeforeClass
    public static void setup() throws SQLException, IOException {
        H2DatabaseHelper h2DatabaseHelper = new H2DatabaseHelper("~", "db1", "sa", "sa", 2);
        long prepareTimeStart = System.currentTimeMillis();
        h2DatabaseHelper.prepareH2Database();
        System.out.println(String.format("Prepare h2 db cost %s ms", System.currentTimeMillis() - prepareTimeStart));

    }

    protected List<Map<String, Object>> orm(ResultSet resultSet) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<>();
        while (resultSet.next()) {
            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();
            Map<String, Object> rowData = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                rowData.put(metaData.getColumnLabel(i), resultSet.getObject(i));
            }
            result.add(rowData);
        }
        return result;
    }
}
