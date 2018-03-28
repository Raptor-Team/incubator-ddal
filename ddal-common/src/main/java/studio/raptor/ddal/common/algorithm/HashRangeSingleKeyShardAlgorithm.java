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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 两阶段路由抽象类。
 *
 * 如若使用两阶段路由算法，需使用者实现
 *
 * @author Sam
 * @since 3.0.0
 */
public abstract class HashRangeSingleKeyShardAlgorithm<T extends Comparable<?>> extends AbstractHashRangeShardAlgorithm implements SingleKeyShardAlgorithm<T> {

    private static Logger logger = LoggerFactory.getLogger(HashRangeSingleKeyShardAlgorithm.class);
    protected List<Range> ranges = new ArrayList<>();
    protected Map<String, String> shardTarget = new HashMap<>();

    protected HashRangeSingleKeyShardAlgorithm(String fileName) {
        super(fileName);
        parseRange();
        loadTargetMap();
    }

    /**
     * 解析Range范围
     *
     * 单字段分片时Range为一个List
     * 多字段分片时Range为多个List
     */
    private void parseRange() {
        ranges = createRanges(rangeText);
    }

    protected abstract void loadTargetMap();

    /**
     * 计算范围分片
     *
     * @param shardValue 分片值
     * @return hash
     */
    public abstract int rangeShardValue(T shardValue);

    @Override
    public String doEqual(Collection<String> shards, ShardValue<T> shardValue) {
        T value = shardValue.getValue();
        String suffix = SEPARATOR + shardTarget.get(hashShardValue(value) + "" + rangeShardValue(value));
        for (String shard : shards) {
            if (shard.endsWith(suffix)) {
                return shard;
            }
        }
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<String> doIn(Collection<String> shardings, ShardValue<T> shardValue) {
        Collection<String> r = new HashSet<>();
        for (T value : shardValue.getValues()) {
            String suffix = SEPARATOR + shardTarget.get(hashShardValue(value) + "" + rangeShardValue(value));
            for (String shard : shardings) {
                if (shard.endsWith(suffix)) {
                    r.add(shard);
                }
            }
        }
        return r;
    }

    @Override
    public Collection<String> doBetween(Collection<String> shardings, ShardValue<T> shardValue) {
        throw new UnsupportedOperationException();
    }
}
