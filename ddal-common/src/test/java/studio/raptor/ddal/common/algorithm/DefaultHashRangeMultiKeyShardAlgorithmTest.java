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

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.algorithm.embed.DefaultHashRangeMultiKeyShardAlgorithm;

/**
 * 二级路由测试
 *
 * @author Sam
 * @since 3.0.0
 */
public class DefaultHashRangeMultiKeyShardAlgorithmTest {

  private static MultiKeyShardAlgorithm<String> hashRangeShardAlgorithm = new DefaultHashRangeMultiKeyShardAlgorithm("multi-hash-range.properties");

  private static Collection<String> allShards = new ArrayList<String>() {
    {
      add("shard_0");
      add("shard_1");
      add("shard_2");
      add("shard_3");
      add("shard_4");
      add("shard_5");
      add("shard_6");
      add("shard_7");
      add("shard_8");
      add("shard_9");
      add("shard_10");
      add("shard_11");
      add("shard_12");
      add("shard_13");
      add("shard_14");
      add("shard_15");
    }
  };

  @Test
  public void testWithTwoShardValue1(){
    Collection<String> result = calculator("0", "0");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_0", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue2(){
    Collection<String> result = calculator("0", "1000");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_1", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue3(){
    Collection<String> result = calculator("0", "1");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_2", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue4(){
    Collection<String> result = calculator("0", "1001");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_3", result.iterator().next());
  }

  /*------------------------------------------------------------------*/

  @Test
  public void testWithTwoShardValue5(){
    Collection<String> result = calculator("100", "0");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_4", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue6(){
    Collection<String> result = calculator("100", "1000");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_5", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue7(){
    Collection<String> result = calculator("100", "1");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_6", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue8(){
    Collection<String> result = calculator("100", "1001");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_7", result.iterator().next());
  }

  /*------------------------------------------------------------------*/

  @Test
  public void testWithTwoShardValue9(){
    Collection<String> result = calculator("1", "0");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_8", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue10(){
    Collection<String> result = calculator("1", "1000");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_9", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue11(){
    Collection<String> result = calculator("1", "1");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_10", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue12(){
    Collection<String> result = calculator("1", "1001");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_11", result.iterator().next());
  }

  /*------------------------------------------------------------------*/

  @Test
  public void testWithTwoShardValue13(){
    Collection<String> result = calculator("101", "0");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_12", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue14(){
    Collection<String> result = calculator("101", "1000");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_13", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue15(){
    Collection<String> result = calculator("101", "1");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_14", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue16(){
    Collection<String> result = calculator("101", "1001");
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_15", result.iterator().next());
  }

  /*------------------------------------------------------------------*/

  @Test
  public void testWithPrimaryShardValue() {
    Collection<String> result = calculator("101", null);
    Assert.assertEquals(4, result.size());
    Iterator<String> iterator = result.iterator();
    Assert.assertEquals("shard_12", iterator.next());
    Assert.assertEquals("shard_13", iterator.next());
    Assert.assertEquals("shard_14", iterator.next());
    Assert.assertEquals("shard_15", iterator.next());
  }

  @Test
  public void testWithSecondShardValue() {
    Collection<String> result = calculator(null, "1");
    Assert.assertEquals(4, result.size());
    Iterator<String> iterator = result.iterator();
    Assert.assertEquals("shard_2", iterator.next());
    Assert.assertEquals("shard_6", iterator.next());
    Assert.assertEquals("shard_10", iterator.next());
    Assert.assertEquals("shard_14", iterator.next());
  }

  @Test
  public void testWithNoShardValue() {
    Collection<String> result = calculator(null, null);
    Assert.assertEquals(16, result.size());
    Iterator<String> iterator = result.iterator();
    Assert.assertEquals("shard_0", iterator.next());
    Assert.assertEquals("shard_1", iterator.next());
    Assert.assertEquals("shard_2", iterator.next());
    Assert.assertEquals("shard_3", iterator.next());
    Assert.assertEquals("shard_4", iterator.next());
    Assert.assertEquals("shard_5", iterator.next());
    Assert.assertEquals("shard_6", iterator.next());
    Assert.assertEquals("shard_7", iterator.next());
    Assert.assertEquals("shard_8", iterator.next());
    Assert.assertEquals("shard_9", iterator.next());
    Assert.assertEquals("shard_10", iterator.next());
    Assert.assertEquals("shard_11", iterator.next());
    Assert.assertEquals("shard_12", iterator.next());
    Assert.assertEquals("shard_13", iterator.next());
    Assert.assertEquals("shard_14", iterator.next());
    Assert.assertEquals("shard_15", iterator.next());
  }

  /**
   * 计算器
   * @param primary
   * @param second
   * @return
   */
  private Collection<String> calculator(String primary, String second){
    Collection<ShardValue<String>> shardValues = new ArrayList<>();
    shardValues.add(Strings.isNullOrEmpty(primary) ? new ShardValue<String>() : new ShardValue<>("primary", primary));
    shardValues.add(Strings.isNullOrEmpty(second) ? new ShardValue<String>() :new ShardValue<>("second", second));
    return hashRangeShardAlgorithm.shard(allShards, shardValues);
  }

}