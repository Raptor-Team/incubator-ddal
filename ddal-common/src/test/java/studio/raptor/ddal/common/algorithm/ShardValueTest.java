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

package studio.raptor.ddal.common.algorithm;

import com.google.common.collect.Range;
import java.util.Arrays;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for shard value
 *
 * @author Sam
 * @since 3.0.0
 */
public class ShardValueTest {

  @Test
  public void testSingleShardValue() {
    ShardValue<String> singleShardValue = new ShardValue<>("columnName", "columnValue");
    Assert.assertThat(singleShardValue.toString(), Is.is(
        "ShardValue{columnName='columnName', value=columnValue, values=[], valueRange=null}"));
    Assert.assertThat(singleShardValue.getType(), Is.is(ShardValue.ShardValueType.SINGLE));
    Assert.assertThat(singleShardValue.getColumnName(), Is.is("columnName"));
  }


  @Test
  public void testListShardValue() {
    ShardValue<String> listShardValue = new ShardValue<>("columnName",
        Arrays.asList("columnValue1", "columnValue2"));
    Assert.assertThat(listShardValue.toString(), Is.is(
        "ShardValue{columnName='columnName', value=null, values=[columnValue1, columnValue2], valueRange=null}"));
    Assert.assertThat(listShardValue.getType(), Is.is(ShardValue.ShardValueType.LIST));
  }


  @Test
  public void testRangeShardValue() {
    ShardValue<Long> rangeShardValue = new ShardValue<>("columnName", Range.open(10L, 30L));
    Assert.assertThat(rangeShardValue.toString(),
        Is.is("ShardValue{columnName='columnName', value=null, values=[], valueRange=(10..30)}"));
    Assert.assertThat(rangeShardValue.getType(), Is.is(ShardValue.ShardValueType.RANGE));
  }
}
