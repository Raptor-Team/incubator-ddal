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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DefaultModSingleKeyAlgorithmTest extends AbstractSingleKeyShardAlgorithmTest {

  private static SingleKeyShardAlgorithm<Comparable<?>> shardAlgorithm = new DefaultModSingleKeyAlgorithm(allShards.size() + "");


  @Test
  public void testDoEqual() {
    Assert.assertThat(shardAlgorithm.doEqual(allShards, new ShardValue("columnName", 99L)),
        Is.is("shard3"));
    Assert.assertThat(shardAlgorithm.doEqual(allShards, new ShardValue("columnName", 100L)),
        Is.is("shard0"));
    Assert.assertThat(shardAlgorithm.doEqual(allShards, new ShardValue("columnName", 101L)),
        Is.is("shard1"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testDoEqualNoShardFound() {
    Collection<String> allShards = new ArrayList<String>() {
      {
        add("shard0");
        add("shard1");
        add("shard2");
        add("shard2");
      }
    };
    String actualShard = shardAlgorithm.doEqual(allShards, new ShardValue("columnName", 3L));
    Assert.assertThat("shard3", Is.is(actualShard));
  }

  @Test
  public void testDoIn() {
    String expectedArray = "[shard0, shard1]";
    List<String> actualArray = new ArrayList<>(shardAlgorithm
        .doIn(allShards, new ShardValue("columnName", Arrays.asList(1L, 100L, 5L))));
    Collections.sort(actualArray);
    Assert.assertThat(actualArray.toString(), Is.is(expectedArray));
  }

  @Test
  public void testDoBetween1() {
    String expectedArray = "[shard0, shard1, shard2, shard3]";
    List<String> actualArray = new ArrayList<>(
        shardAlgorithm.doBetween(allShards, new ShardValue("columnName", Range.closed(5L, 10L))));
    Collections.sort(actualArray);
    Assert.assertThat(actualArray.toString(), Is.is(expectedArray));
  }

  @Test
  public void testDoBetween2() {
    String expectedArray = "[shard0, shard1]";
    List<String> actualArray = new ArrayList<>(
        shardAlgorithm.doBetween(allShards, new ShardValue("columnName", Range.closed(4L, 5L))));
    Collections.sort(actualArray);
    Assert.assertThat(actualArray.toString(), Is.is(expectedArray));
  }


  @Test
  public void testSuffix() {
    SingleKeyShardAlgorithm<Comparable<?>> shardAlgorithm = new DefaultModSingleKeyAlgorithm( "16");
    Collection<String> shards = new ArrayList<String>() {
      {
        add("shard00");
        add("shard01");
        add("shard02");
        add("shard03");
        add("shard04");
        add("shard05");
        add("shard06");
        add("shard07");
        add("shard08");
        add("shard09");
        add("shard10");
        add("shard11");
        add("shard12");
        add("shard13");
        add("shard14");
        add("shard15");
      }
    };
    Assert.assertThat(shardAlgorithm.doEqual(shards, new ShardValue("columnName", 3L)),
        Is.is("shard03"));
    Assert.assertThat(shardAlgorithm.doEqual(shards, new ShardValue("columnName", 13L)),
        Is.is("shard13"));

    String expectedArray = "[shard01, shard11]";
    List<String> actualArray = new ArrayList<>(shardAlgorithm
        .doIn(shards, new ShardValue("columnName", Arrays.asList(1L, 11))));
    Collections.sort(actualArray);
    Assert.assertThat(actualArray.toString(), Is.is(expectedArray));
  }
}
