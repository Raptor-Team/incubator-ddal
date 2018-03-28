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

import org.junit.Assert;
import org.junit.Test;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 线索分片测试案例。
 *
 * 访问<url>http://git.oschina.net/f150/raptor-ddal/blob/develop/doc/DetailedDesign/ddal-hint-paging.md</url>
 * 查看更多关于线索分片的使用方法。
 *
 * @author Sam
 * @since 3.0.0
 */
public class HintPaginationTest extends AutoPrepareTestingEnv {

    @Test
    public void testHintPage() throws SQLException {

        // 全量查询男学生信息
        int femaleStudentCount = 0;
        String sql = "select sno, sname, sex from student where sex = '男'";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            boolean hasResultSet = false;
            while (resultSet.next()) {
                count++;
                hasResultSet = true;
            }
            Assert.assertTrue("Student data is empty, which is not expected", hasResultSet);
            femaleStudentCount += count;
        }

        sql = "select count(1) from student where sex = '男'";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            ResultSet resultSet = preparedStatement.executeQuery();
            int count = 0;
            while (resultSet.next()) {
                count++;
            }
            femaleStudentCount += count;
        }

    }
}
