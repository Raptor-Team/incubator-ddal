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
public class OrderByAggregationTest extends AutoPrepareTestingEnv {

    /**
     * 单分片Order By ASC
     */
    @Test
    public void testOrderByWithAscOnSingleField() {
        String sql = "select * from scores s where s.sno = ? order by s.grade asc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 200901020101L);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int tempGrand = 0;
                while (resultSet.next()) {
                    Assert.assertTrue(resultSet.getInt(5) >= tempGrand);
                    tempGrand = resultSet.getInt(5);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 单分片Order By Desc
     */
    @Test
    public void testOrderByWithDescOnSingleField() {
        String sql = "select s.grade sg, s.sno ss from scores s where s.sno = ? order by s.grade desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 200901020101L);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int tempGrand = 100;
                while (resultSet.next()) {
                    Assert.assertTrue(resultSet.getInt(1) <= tempGrand);
                    tempGrand = resultSet.getInt(1);
                    System.out.println(tempGrand);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多分片Order By ASC
     */
    @Test
    public void testOrderByWithAscOnMultiFields() {
        String sql = "select * from scores where cno = ? order by grade asc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 112618);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int tempGrand = 0;
                while (resultSet.next()) {
                    Assert.assertTrue(resultSet.getInt(5) >= tempGrand);
                    tempGrand = resultSet.getInt(5);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 多分片Order By Desc
     */
    @Test
    public void testOrderByWithDescOnMultiFields() {
        String sql = "select s.grade as gr from scores s where s.cno = ? order by s.grade desc";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 112618);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int tempGrand = 100;
                while (resultSet.next()) {
                    Assert.assertTrue(resultSet.getInt(1) <= tempGrand);
                    tempGrand = resultSet.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
