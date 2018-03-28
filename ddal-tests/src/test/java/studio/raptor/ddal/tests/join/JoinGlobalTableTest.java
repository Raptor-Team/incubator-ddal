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

package studio.raptor.ddal.tests.join;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * 配置表与全局表关联查询
 *
 * @author Sam
 * @since 3.0.0
 */
public class JoinGlobalTableTest extends AutoPrepareTestingEnv {


    @Test
    public void testTwoTableJoin() throws SQLException {
        String sql = "select a.scorceno, a.sno, a.cno, a.term, a.grade, b.cname from crmc.scores a, course b where a.sno = ? and a.cno = b.cno";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, 200901020104L);
            List<Map<String, Object>> allRowData = orm(preparedStatement.executeQuery());
            Assert.assertNotNull(allRowData);
            Assert.assertThat(allRowData.size(), Is.is(13));
            Assert.assertThat(allRowData.get(0).get("cname".toUpperCase()).toString(), Is.is("SSH"));
        }
    }

    /**
     * 两表带Join关键字联查
     */
    @Test
    public void testTwoTableJoinKeyword() throws SQLException {
        String sql = "select a.scorceno, a.sno, a.cno, a.term, a.grade, b.cname from scores a join course b on a.cno = b.cno where a.sno = ?";
        try (
                PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ) {
            preparedStatement.setLong(1, 200901020104L);
            List<Map<String, Object>> allRowData = orm(preparedStatement.executeQuery());
            Assert.assertNotNull(allRowData);
            Assert.assertThat(allRowData.size(), Is.is(13));
            Assert.assertThat(allRowData.get(0).get("cname".toUpperCase()).toString(), Is.is("SSH"));
        }
    }
}
