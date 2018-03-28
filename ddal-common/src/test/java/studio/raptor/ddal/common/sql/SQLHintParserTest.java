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
import studio.raptor.ddal.common.sql.SQLHintParser.Page;
import studio.raptor.ddal.common.sql.SQLHintParser.SQLHint;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SQLHintParserTest {

  @Test
  public void testSqlHintParse_1() {
    String sql = "/*!hint page(offset=0, count=10); shard(id=10000);*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert
        .assertEquals("/*!hint page(offset=0, count=10); shard(id=10000); */", sqlHint.toString());
    Page page = sqlHint.getPage();
    Assert.assertNotNull(page);
    Assert.assertEquals(0, page.getOffset());
    Assert.assertEquals(10, page.getCount());
    Assert.assertNotNull(sqlHint.getShard());
    Assert.assertEquals("id", sqlHint.getShard().getColumn());
    Assert.assertEquals("10000", sqlHint.getShard().getValue());
  }

  @Test
  public void testSqlHintParse_2() {
    String sql = "/*!hint page(offset=0, count=5); */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint page(offset=0, count=5); */", sqlHint.toString());
    Page page = sqlHint.getPage();
    Assert.assertNotNull(page);
    Assert.assertEquals(0, page.getOffset());
    Assert.assertEquals(5, page.getCount());
  }

  @Test
  public void testSqlHintParse_3() {
    String sql = "/*!hint shard(id=10000);*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint shard(id=10000); */", sqlHint.toString());
    Assert.assertNotNull(sqlHint.getShard());
    Assert.assertEquals("id", sqlHint.getShard().getColumn());
    Assert.assertEquals("10000", sqlHint.getShard().getValue());
  }

  @Test
  public void testSqlHintParse_4() {
    String sql = "/*!hint */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    assertThat(sqlHint.toString(), is("/*!hint */"));
    Assert.assertNull(sqlHint.getPage());
    Assert.assertNull(sqlHint.getShard());
  }

  @Test
  public void testSqlHintParse_5() {
    String sql = "select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNull(sqlHint);
  }

  @Test
  public void testSqlHintParse_6() {
    String sql = "/*!hint page(offset=0, count=10); shard(id=10000); readonly;*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals(
        "/*!hint page(offset=0, count=10); shard(id=10000); readonly; */",
        sqlHint.toString()
    );
    Page page = sqlHint.getPage();
    Assert.assertNotNull(page);
    Assert.assertEquals(0, page.getOffset());
    Assert.assertEquals(10, page.getCount());
    Assert.assertNotNull(sqlHint.getShard());
    Assert.assertEquals("id", sqlHint.getShard().getColumn());
    Assert.assertEquals("10000", sqlHint.getShard().getValue());
    assertThat(sqlHint.getReadonly().getValue(), is("readonly"));
  }

  @Test
  public void testDataSourceHintParse() {
    String sql = "/*!hint datasource(group_1.inst1); page(offset=0, count=10); shard(id=10000);*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals(
        "/*!hint datasource(group_1.inst1); page(offset=0, count=10); shard(id=10000); */",
        sqlHint.toString()
    );
    Assert.assertNotNull(sqlHint.getDatasource());
    Assert.assertEquals("group_1.inst1", sqlHint.getDatasource().getDsName());
    Assert.assertNotNull(sqlHint.getPage());
    Assert.assertEquals(0, sqlHint.getPage().getOffset());
    Assert.assertEquals(10, sqlHint.getPage().getCount());
    Assert.assertNotNull(sqlHint.getShard());
    Assert.assertEquals("id", sqlHint.getShard().getColumn());
    Assert.assertEquals("10000", sqlHint.getShard().getValue());
  }

  @Test
  public void testEmptyDataSourceHintParse() {
    String sql = "/*!hint datasource()*/ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint */", sqlHint.toString());
    Assert.assertNull(sqlHint.getDatasource());
  }

  @Test
  public void testReadonlySeqHintParse() {
    String sql = "/*!hint readonly(3,4) */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint readonly(3,4); */", sqlHint.toString());
  }

  @Test
  public void testReadonlySeqHintWithBlankSpaceParse() {
    String sql = "/*!hint readonly    (3,4) */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint readonly(3,4); */", sqlHint.toString());
  }


  @Test
  public void testReadonlySeqHintWithoutSeqNumberParse() {
    String sql = "/*!hint readonly() */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint readonly; */", sqlHint.toString());
  }

  @Test
  public void testReadonlySeqHintWithoutSeqNumberWithBlankSpacesParse() {
    String sql = "/*!hint readonly  () */ select * from t_user";
    SQLHint sqlHint = SQLHintParser.parse(sql);
    Assert.assertNotNull(sqlHint);
    Assert.assertEquals("/*!hint readonly; */", sqlHint.toString());
  }

}
