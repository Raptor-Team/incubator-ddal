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

package studio.raptor.ddal.config.model.rule;

import java.util.Arrays;
import studio.raptor.ddal.common.algorithm.ShardAlgorithm;

/**
 * 分片规则定义
 *
 * @author Charley
 * @since 1.0
 */
public class ShardRule {

  private String name;
  private String[] shardColumns;
  private ShardAlgorithm algorithm;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String[] getShardColumns() {
    return shardColumns;
  }

  public void setShardColumns(String[] shardColumns) {
    this.shardColumns = shardColumns;
  }

  public ShardAlgorithm getAlgorithm() {
    return algorithm;
  }

  public void setAlgorithm(ShardAlgorithm algorithm) {
    this.algorithm = algorithm;
  }

  @Override
  public String toString() {
    return "ShardRule{" +
        "name='" + name + '\'' +
        ", shardColumns=" + Arrays.toString(shardColumns) +
        ", algorithm=" + algorithm +
        '}';
  }
}
