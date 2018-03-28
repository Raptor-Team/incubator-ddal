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

package studio.raptor.ddal.common.sql;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SQLUtilTest {

  @Test
  public void testGetExactValue() {
    Assert.assertThat(
        SQLUtil.getExactlyValue("select * from `table` where id = \"1000\" and name = 'name'"),
        Is.is("select * from table where id = 1000 and name = name")
    );
  }

  @Test
  public void testRMSchema() {
    Assert.assertThat(SQLUtil.rmTableSchema("schema.table"), Is.is("table"));
    Assert.assertThat(SQLUtil.rmTableSchema("table"), Is.is("table"));
  }

  @Test
  public void testFindStartOfStatement() {
    String sql = "select * from any_table";
    Assert.assertEquals(0, SQLUtil.findStartOfStatement(sql));

    sql = "/*!hint page(offset=0,count=10)*/ select any_column from any_table";
    Assert.assertEquals("select any_column from any_table", sql.substring(SQLUtil.findStartOfStatement(sql)));

    sql = "/*!hint page(offset=0,count=10)*/select any_column from any_table";
    Assert.assertEquals("select any_column from any_table", sql.substring(SQLUtil.findStartOfStatement(sql)));
  }


}
