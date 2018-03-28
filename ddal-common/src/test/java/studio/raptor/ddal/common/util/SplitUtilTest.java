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

package studio.raptor.ddal.common.util;

import java.util.Arrays;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * 单元测试 {@link studio.raptor.ddal.common.util.SplitUtil}
 *
 * Coverage: Method(90%) Line(89%)
 *
 * @author Sam
 * @since 3.0.0
 */
public class SplitUtilTest {

  private static final String[] EMPTY_STRING_ARRAY = new String[0];

  @Test
  public void startLessThanEndSplit2Test() {
    Assert.assertThat(Arrays.toString(SplitUtil.split2("mysql_db$0-2", '$', '-')),
        Is.is("[mysql_db[0], mysql_db[1], mysql_db[2]]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split2("mysql_db$0", '$', '-')),
        Is.is("[mysql_db[0]]"));
    Assert.assertThat(SplitUtil.split2(null, '$', '-'), Is.is(EMPTY_STRING_ARRAY));
    Assert.assertThat(SplitUtil.split2("", '$', '-'), Is.is(EMPTY_STRING_ARRAY));
  }

  @Test
  public void startEqualsEndSplit2Test() {
    Assert.assertThat(Arrays.toString(SplitUtil.split2("mysql_db$2-2", '$', '-')),
        Is.is("[mysql_db[2]]"));
  }

  @Test
  public void startGreaterThanEndSplit2Test() {
    Assert.assertThat(Arrays.toString(SplitUtil.split2("mysql_db$3-2", '$', '-')), Is.is("[]"));
  }

  @Test
  public void splitUsingWhiteSpace() {
    Assert.assertThat(Arrays.toString(SplitUtil.split("hello World")), Is.is("[hello, World]"));
  }

  @Test
  public void normalSplitTest() {
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hello", "o")), Is.is("[Hell]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("HelloWorld", " ")), Is.is("[HelloWorld]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hello", 'o')), Is.is("[Hell]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hello", "l")), Is.is("[He, o]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hello", 'l')), Is.is("[He, o]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hello", "abc")), Is.is("[Hello]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hello", "ll")), Is.is("[He, o]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hell o", 'l', true)), Is.is("[He, o]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("Hell o", 'l', false)), Is.is("[He,  o]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("mysql_db$0-2", '$', '-', '0', '0')),
        Is.is("[mysql_db0, mysql_db1, mysql_db2]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("mysql_db$0-2", '|', '-', '0', '0')),
        Is.is("[mysql_db$0-2]"));
    Assert.assertThat(SplitUtil.split(null, '$', '-', '0', '0'), Is.is(EMPTY_STRING_ARRAY));
    Assert.assertThat(SplitUtil.split("", '$', '-', '0', '0'), Is.is(EMPTY_STRING_ARRAY));
    Assert.assertThat(Arrays.toString(SplitUtil.split("mysql_db$0-2", '$', '-', '[', ']')),
        Is.is("[mysql_db[0], mysql_db[1], mysql_db[2]]"));
    Assert.assertThat(Arrays.toString(SplitUtil.split("mysql_db$0-2|mysql_db$5-9", '|', '$', '-')),
        Is.is(
            "[mysql_db0, mysql_db1, mysql_db2, mysql_db5, mysql_db6, mysql_db7, mysql_db8, mysql_db9]"));
  }

}
