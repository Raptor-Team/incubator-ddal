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
public class DistinctAggregationTest extends AutoPrepareTestingEnv {

    /**
     * 单字段Distinct
     */
    @Test
    public void testDistinctWithSingleFiled() {
        String sql = "select distinct sex from teacher";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    index++;
                }
                Assert.assertEquals(2, index);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多字段Distinct
     */
    @Test
    public void testDistinctWithMultipleFields() {
        String sql = "select distinct tname, sex from teacher";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    System.out.println(resultSet.getString(1) + " - " + resultSet.getString(2));
                    index++;
                }
                Assert.assertEquals(14, index);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
