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

package studio.raptor.ddal.tests.aggregation;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author Sam
 * @since 3.0.0
 */
public class GroupByAggregationTest extends AutoPrepareTestingEnv {

    /**
     * 单字段GroupBy
     */
    @Test
    public void testGroupByWithSingleField() {
        String sql = "select count(*), sex from teacher group by sex";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    Assert.assertNotNull(resultSet.getInt(1));
                    Assert.assertNotNull(resultSet.getString(2));
                    index++;
                }
                Assert.assertEquals(2, index);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多字段GroupBy
     */
    @Test
    public void testGroupByWithMultiFields() {
        String sql = "select count(*), age, sex from teacher group by age, sex";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    Assert.assertNotNull(resultSet.getInt(1));
                    Assert.assertNotNull(resultSet.getInt(2));
                    Assert.assertNotNull(resultSet.getString(3));
                    index++;
                }
                Assert.assertEquals(4, index);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
