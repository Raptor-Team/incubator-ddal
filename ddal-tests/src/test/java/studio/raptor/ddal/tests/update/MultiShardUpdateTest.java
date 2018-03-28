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
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;
import studio.raptor.ddal.tests.util.CloseUtil;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collections;

/**
 * 多分片表 update操作。 当前测试场景中多分片表只有配置表course。
 *
 * @author Sam
 * @since 3.0.0
 */
public class MultiShardUpdateTest extends AutoPrepareTestingEnv {

    /**
     * 多分片各更新一条数据
     */
    @Test
    public void testUpdateSingleShardTable() {
        String sql = "update course set cname='J2SE' where cno = ?";
        PreparedStatement preparedStatement = null;
        try {
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, 112601L);
            Assert.assertThat(preparedStatement.executeUpdate(), Is.is(4));
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            CloseUtil.close(Collections.singletonList(preparedStatement));
        }
    }

}
