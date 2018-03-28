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
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.algorithm.embed.DefaultModMultiKeyAlgorithm;

/**
 * Multi key shard is not supported for now
 *
 * @author Sam
 * @since 3.0.0
 */
public class DefaultModMultiKeyAlgorithmTest{

  private static MultiKeyShardAlgorithm<Comparable<?>> shardAlgorithm = new DefaultModMultiKeyAlgorithm("2");

  private static List<String> shards = new ArrayList<>(4);
  static {
    shards.add("shard_0");
    shards.add("shard_1");
    shards.add("shard_2");
    shards.add("shard_3");
  }

  @Test
  public void testWithTwoShardValue1(){
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<Comparable<?>>("a", 10L));
    shardValues.add(new ShardValue<Comparable<?>>("b", 4L));
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_0", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue2(){
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<Comparable<?>>("a", 10L));
    shardValues.add(new ShardValue<Comparable<?>>("b", 7L));
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_1", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue3(){
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<Comparable<?>>("a", 5L));
    shardValues.add(new ShardValue<Comparable<?>>("b", 4L));
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_2", result.iterator().next());
  }

  @Test
  public void testWithTwoShardValue4(){
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<Comparable<?>>("a", 5L));
    shardValues.add(new ShardValue<Comparable<?>>("b", 3L));
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals("shard_3", result.iterator().next());
  }

  @Test
  public void testWithPrimaryShardValue() {
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<Comparable<?>>("a", 10L));
    shardValues.add(new ShardValue<>());
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(2, result.size());
    Iterator<String> iterator = result.iterator();
    Assert.assertEquals("shard_0", iterator.next());
    Assert.assertEquals("shard_1", iterator.next());
  }

  @Test
  public void testWithSecondShardValue() {
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<>());
    shardValues.add(new ShardValue<Comparable<?>>("b", 3L));
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(2, result.size());
    Iterator<String> iterator = result.iterator();
    Assert.assertEquals("shard_1", iterator.next());
    Assert.assertEquals("shard_3", iterator.next());
  }

  @Test
  public void testWithNoShardValue() {
    Collection<ShardValue<Comparable<?>>> shardValues = new ArrayList<>();
    shardValues.add(new ShardValue<>());
    shardValues.add(new ShardValue<>());
    Collection<String> result = shardAlgorithm.shard(shards, shardValues);
    Assert.assertEquals(4, result.size());
    Iterator<String> iterator = result.iterator();
    Assert.assertEquals("shard_0", iterator.next());
    Assert.assertEquals("shard_1", iterator.next());
    Assert.assertEquals("shard_2", iterator.next());
    Assert.assertEquals("shard_3", iterator.next());
  }
}
