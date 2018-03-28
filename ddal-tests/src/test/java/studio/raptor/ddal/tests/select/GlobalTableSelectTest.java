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

package studio.raptor.ddal.tests.select;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 全局表Select测试
 *
 * @author Charley
 * @since 1.0
 */
public class GlobalTableSelectTest extends AutoPrepareTestingEnv {

    /**
     * 全局表Select随机取一个节点执行
     */
    @Test
    public void randomSingleNodeSelect() {
        String sql = "select * from COURSE";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
                ResultSet resultSet = preparedStatement.executeQuery()
        ) {
            int index = 0;
            while (resultSet.next()) {
                index++;
            }
            Assert.assertEquals(19, index);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
