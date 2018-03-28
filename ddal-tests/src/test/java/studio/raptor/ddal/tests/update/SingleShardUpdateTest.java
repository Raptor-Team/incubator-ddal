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

package studio.raptor.ddal.tests.update;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;
import studio.raptor.ddal.common.exception.code.ExecErrCodes;
import studio.raptor.ddal.tests.util.CloseUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

/**
 * 分片表 单表update操作
 *
 * @author Sam
 * @since 3.0.0
 */
public class SingleShardUpdateTest extends AutoPrepareTestingEnv {

    /**
     * 单分片更新一条数据
     */
    @Test
    public void testUpdateSingleShardTable() {
        logger.debug("测试单分片更新一条数据");
        String sql = "update student set sphone= '18052029122' where sno = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, 200901020104L);
            Assert.assertThat(preparedStatement.executeUpdate(), Is.is(1));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(Collections.singletonList(preparedStatement));
        }
    }

    /**
     * 测试不支持 Update语法中第二种assignment的写法
     */
    @Test
    public void testUnsupportedAssignmentUpdate() {
        logger.debug("DDAL不支持的Update写法测试");
        String sql = "update student set (sphone, sdept) = ('18052029122', '网络工程') where sno = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, 200901020104L);
            preparedStatement.executeUpdate();
        } catch (Exception e) {
            if (e instanceof GenericException) {
                Assert.assertThat(((GenericException) e).getCode(), Is.is(ExecErrCodes.EXEC_202.getCode()));
            }
        }
    }

    /**
     * 多分片更新一条数据。 为了避免修改数据过多，增加非分片字段sname作为update条件
     */
    @Test
    public void testUpdateMultiShardAffectedOneTable() {
        logger.debug("测试不带分片字段导致多分片执行update语句，但只更新一条数据");
        String sql = "update student set sphone= '18052029123' where sname = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, "简邦余");
            Assert.assertThat(preparedStatement.executeUpdate(), Is.is(1));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
