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

package studio.raptor.ddal.tests.where;

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
public class InClauseTest extends AutoPrepareTestingEnv {

    /**
     * in条件在单分片执行
     */
    @Test
    public void testInConditionOnSingleShard() throws SQLException {
        String sql = "select * from student where sno in (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 200901020101L);
            preparedStatement.setLong(2, 200901020105L);
            preparedStatement.setLong(3, 200901020109L);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    index++;
                }
                Assert.assertEquals(3, index);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * in条件在多分片执行
     */
    @Test
    public void testInConditionOnMultiShards() throws SQLException {
        String sql = "select * from student where sno in (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 200901020152L);
            preparedStatement.setLong(2, 200901020153L);
            preparedStatement.setLong(3, 200901020102L);
            preparedStatement.setLong(4, 200901020103L);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                int index = 0;
                while (resultSet.next()) {
                    index++;
                }
                Assert.assertEquals(4, index);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
