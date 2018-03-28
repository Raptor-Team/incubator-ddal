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

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.sql.SQLHintParser.SQLHint;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SQLHintSpecTest {

  @Test
  public void testSqlHintSpec_1() {
    String sql = "/*!hint page(offset=0, count=10); shard(id=10000);*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert
        .assertEquals("/*!hint page(offset=0, count=10); shard(id=10000); */", sqlHint.toString());
    assertThat(sqlHint.toSpec(), is("DDALHintSpec;page;shard+id"));
  }

  @Test
  public void testSqlHintSpec_2() {
    String sql = "/*!hint page(offset=0, count=5); */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    assertThat(sqlHint.toSpec(), is("DDALHintSpec;page"));
  }

  @Test
  public void testSqlHintSpec_3() {
    String sql = "/*!hint shard(user_id=10000);*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint shard(user_id=10000); */", sqlHint.toString());
    assertThat(sqlHint.toSpec(), is("DDALHintSpec;shard+user_id"));
  }

  @Test
  public void testSqlHintSpec_4() {
    String sql = "/*!hint page(offset=0, count=5); readonly; */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    assertThat(sqlHint.toSpec(), is("DDALHintSpec;page;readonly"));
  }

}
