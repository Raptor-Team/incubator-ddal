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

import java.util.Collection;

/**
 * 多片键分片法接口.
 *
 * @author Charley
 * @since 1.0
 */
public interface MultiKeyShardAlgorithm<T extends Comparable<?>> extends ShardAlgorithm {

    /**
     * 根据分片值计算分片结果名称集合.
     *
     * @param availableTargetNames 所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValues          分片值集合
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    Collection<String> shard(Collection<String> availableTargetNames, Collection<ShardValue<T>> shardValues);
}
