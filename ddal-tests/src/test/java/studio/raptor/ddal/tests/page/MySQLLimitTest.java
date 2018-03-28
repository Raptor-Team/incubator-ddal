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

package studio.raptor.ddal.tests.page;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * @author Sam
 * @since 3.0.0
 */
public class MySQLLimitTest extends AutoPrepareTestingEnv {

    @Test
    public void testQueryWithLimit() throws SQLException {
        String expectedOrmString = "[{\"scorceno\":183,\"term\":\"2012-2013第一学期\",\"sno\":200901020108," +
                "\"cno\":112605,\"grade\":85},{\"scorceno\":184,\"term\":\"2012-2013第一学期\",\"sno\":200901020108," +
                "\"cno\":112608,\"grade\":88},{\"scorceno\":185,\"term\":\"2012-2013第一学期\",\"sno\":200901020108," +
                "\"cno\":112610,\"grade\":94},{\"scorceno\":186,\"term\":\"2011-2012第二学期\",\"sno\":200901020108," +
                "\"cno\":112612,\"grade\":87},{\"scorceno\":187,\"term\":\"2011-2012第二学期\",\"sno\":200901020108," +
                "\"cno\":112611,\"grade\":87}]";
        String sql = "/*!hint page(offset=0, count=5)*/select * from scores where sno = ? limit 0, 5";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, 200901020108L);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<Map<String, Object>> ormResultSet = orm(resultSet);
            Assert.assertNotNull(ormResultSet);
            Assert.assertThat(ormResultSet.size(), Is.is(5));
            JSONArray ja = (JSONArray) JSON.toJSON(ormResultSet);

            boolean hasFlag = false;
            for (int i = 0, arrayLength = ja.size(); i < arrayLength; i++) {
                JSONObject obj = ja.getJSONObject(i);
                if (112605 == obj.getIntValue("cno".toUpperCase())) {
                    hasFlag = true;
                }
            }
            Assert.assertTrue("cno:112605 is missing", hasFlag);

            hasFlag = false;
            for (int i = 0, arrayLength = ja.size(); i < arrayLength; i++) {
                JSONObject obj = ja.getJSONObject(i);
                if (112612 == obj.getIntValue("cno".toUpperCase())) {
                    hasFlag = true;
                }
            }
            Assert.assertTrue("cno:112612 is missing", hasFlag);

        }
    }
}
