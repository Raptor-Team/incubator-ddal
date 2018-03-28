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

package studio.raptor.ddal.tests.insert;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 全局表Insert测试
 *
 * @author Charley
 * @since 1.0
 */
public class GlobalTableInsertTest extends AutoPrepareTestingEnv {

    /**
     * 全局表Insert全节点执行
     */
    @Test
    public void testInsert() {
        String sql = "insert into course (cno, cname, tno) values (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, 112621);
            preparedStatement.setString(2, "Sport");
            preparedStatement.setInt(3, 2012112610);
            int affectedRows = preparedStatement.executeUpdate();
            Assert.assertEquals(4, affectedRows);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
