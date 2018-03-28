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

package studio.raptor.ddal.common.algorithm.embed;

import com.google.common.collect.Range;
import java.util.Collection;
import java.util.LinkedHashSet;
import studio.raptor.ddal.common.algorithm.ShardValue;
import studio.raptor.ddal.common.algorithm.SingleKeyShardAlgorithm;

/**
 * 默认单字段路由算法，取模算法
 *
 * @author Charley
 * @since 1.0
 */
public class DefaultModSingleKeyAlgorithm implements SingleKeyShardAlgorithm<Comparable<?>> {

  private final int count;

  private final String stringFormatter;

  public DefaultModSingleKeyAlgorithm(String param) {
    this.count = Integer.parseInt(param);
    this.stringFormatter = "%0" + String.valueOf(count - 1).length() + "d";
  }

  /**
   * 根据分片值和SQL的=运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称, 一般是数据源或表名称
   */
  @Override
  public String doEqual(Collection<String> shards, ShardValue<Comparable<?>> shardValue) {
    String suffix = figureOutSuffix(shardValue.getValue());
    for (String shard : shards) {
      if (shard.endsWith(suffix)) {
        return shard;
      }
    }
    throw new UnsupportedOperationException();
  }

  /**
   * 根据分片值和SQL的IN运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称集合, 一般是数据源或表名称
   */
  @Override
  public Collection<String> doIn(Collection<String> shards, ShardValue<Comparable<?>> shardValue) {
    Collection<String> result = new LinkedHashSet<>(shards.size());
    Collection<Comparable<?>> values = shardValue.getValues();
    for (Comparable value : values) {
      String suffix = figureOutSuffix(value);
      for (String shard : shards) {
        if (shard.endsWith(suffix)) {
          result.add(shard);
        }
      }
    }
    return result;
  }

  /**
   * 根据分片值和SQL的BETWEEN运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称集合, 一般是数据源或表名称
   */
  @Override
  public Collection<String> doBetween(Collection<String> shards,
      ShardValue<Comparable<?>> shardValue) {
    Collection<String> result = new LinkedHashSet<>(shards.size());
    Range<Comparable<?>> range = shardValue.getValueRange();
    for (Long i = convert2Long(range.lowerEndpoint()); i <= convert2Long(range.upperEndpoint());
        i++) {
      String suffix = figureOutSuffix(i);
      for (String shard : shards) {
        if (shard.endsWith(suffix)) {
          result.add(shard);
        }
      }
    }
    return result;
  }

  private String figureOutSuffix(Comparable value) {
    long suffixNumber = convert2Long(value) % count;
    return String.format(stringFormatter, suffixNumber);
  }

  private Long convert2Long(Comparable<?> input) {
    return Long.parseLong(String.valueOf(input));
  }
}
