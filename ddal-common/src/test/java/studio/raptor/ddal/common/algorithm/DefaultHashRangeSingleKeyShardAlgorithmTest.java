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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.algorithm.embed.DefaultHashRangeSingleKeyShardAlgorithm;

/**
 * 二级路由测试
 *
 * @author Sam
 * @since 3.0.0
 */
public class DefaultHashRangeSingleKeyShardAlgorithmTest {

  private static SingleKeyShardAlgorithm<String> hashRangeShard = new DefaultHashRangeSingleKeyShardAlgorithm("single-hash-range.properties");

  private static Collection<String> allShards = new ArrayList<String>() {
    {
      add("shard_0");//00
      add("shard_1");//01
      add("shard_2");//02
      add("shard_3");//10
      add("shard_4");//11
      add("shard_5");//12
    }
  };

  @Test
  public void testDoEqual() {
    Assert.assertThat(hashRangeShard.doEqual(allShards, new ShardValue<>("columnName", "99")),
        Is.is("shard_0"));
    Assert.assertThat(hashRangeShard.doEqual(allShards, new ShardValue<>("columnName", "100")),
        Is.is("shard_3"));
    Assert.assertThat(hashRangeShard.doEqual(allShards, new ShardValue<>("columnName", "101")),
        Is.is("shard_1"));
  }

  @Test
  public void testDoIn() {
    Collection<String> expectedArray = Arrays.asList("shard_3", "shard_0", "shard_1");
    Collection<String> actualArray = hashRangeShard
        .doIn(allShards, new ShardValue<>("columnName", Arrays.asList("99", "100", "101")));
    Assert.assertTrue(
        expectedArray.containsAll(actualArray) && actualArray.containsAll(expectedArray));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testBetween() {
    hashRangeShard
        .doBetween(allShards, new ShardValue<>("columnName", Arrays.asList("99", "100", "101")));
  }
}