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

import java.util.Collection;
import java.util.Iterator;
import studio.raptor.ddal.common.algorithm.MultiKeyShardAlgorithm;
import studio.raptor.ddal.common.algorithm.MultiKeyShardTarget;
import studio.raptor.ddal.common.algorithm.ShardValue;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class DefaultModMultiKeyAlgorithm implements MultiKeyShardAlgorithm<Comparable<?>> {


    private MultiKeyShardTarget shardTarget = new MultiKeyShardTarget();

    private final int count;

    public DefaultModMultiKeyAlgorithm(String param) {
        this.count = Integer.parseInt(param);
        loadTargetMap();
    }

    private void loadTargetMap(){
        int targetIndex = 0;
        for(int i=0; i<count; i++){
            for(int j=0; j<count; j++){
                shardTarget.put(i+SEPARATOR+j, targetIndex++ + "");
            }
        }
    }

    /**
     * 根据分片值计算分片结果名称集合.
     *
     * @param availableTargetNames 所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValues          分片值集合
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    @Override
    public Collection<String> shard(Collection<String> availableTargetNames, Collection<ShardValue<Comparable<?>>> shardValues) {
        Integer[][] dimensionalGroup = new Integer[2][];
        Iterator<ShardValue<Comparable<?>>> iterator = shardValues.iterator();
        for(int i=0; i<2; i++){
            ShardValue shardValue = iterator.next();
            Integer[] targetGroup;
            if(shardValue.isEmpty()){
                targetGroup = allNode();
            } else {
                switch (shardValue.getType()){
                    case SINGLE:
                        targetGroup = new Integer[]{((Long) (convert2Long(shardValue.getValue()) % count)).intValue()};
                        break;
                    default:
                        throw new UnsupportedOperationException(shardValue.getType().getClass().getName());
                }
            }
            dimensionalGroup[i] = targetGroup;
        }
        return shardTarget.getAfterCartesian(SEPARATOR, availableTargetNames, dimensionalGroup);
    }

    private Long convert2Long(Comparable<?> input){
        return Long.parseLong(String.valueOf(input));
    }

    private Integer[] allNode(){
        Integer[] targetGroup = new Integer[count];
        for(int i=0; i<count; i++){
            targetGroup[i] = i;
        }
        return targetGroup;
    }
}
