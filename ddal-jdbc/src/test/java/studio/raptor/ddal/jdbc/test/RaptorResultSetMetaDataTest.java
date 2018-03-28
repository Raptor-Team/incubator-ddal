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

package studio.raptor.ddal.jdbc.test;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * 结果集元数据单元测试
 *
 * @author Sam
 * @since 3.0.0
 */
public class RaptorResultSetMetaDataTest extends AutoPrepareTestingEnv {

  @Test
  public void testMetaData() throws SQLException {
    try (
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery("select * from customer where id = 1")
    ) {
      Assert.assertNotNull(resultSet);
      ResultSetMetaData metaData = resultSet.getMetaData();
      Assert.assertThat(metaData.getColumnCount(), Is.is(4));
      Assert.assertThat(metaData.isAutoIncrement(1), Is.is(false));
      Assert.assertThat(metaData.isCaseSensitive(1), Is.is(true));
      Assert.assertThat(metaData.isSearchable(1), Is.is(true));
      Assert.assertThat(metaData.isCurrency(1), Is.is(false));
      Assert.assertThat(metaData.isNullable(1), Is.is(0));
      Assert.assertThat(metaData.isSigned(1), Is.is(true));
      Assert.assertThat(metaData.getColumnDisplaySize(1), Is.is(11));
      Assert.assertThat(metaData.getColumnLabel(1), Is.is("ID"));
      Assert.assertThat(metaData.getColumnName(1), Is.is("ID"));
      //Assert.assertThat(metaData.getSchemaName(1), Is.is("DB1"));
      Assert.assertThat(metaData.getTableName(1), Is.is("CUSTOMER"));
      Assert.assertThat(metaData.getColumnType(1), Is.is(4));
      Assert.assertThat(metaData.getColumnTypeName(1), Is.is("INTEGER"));
    }
  }
}
