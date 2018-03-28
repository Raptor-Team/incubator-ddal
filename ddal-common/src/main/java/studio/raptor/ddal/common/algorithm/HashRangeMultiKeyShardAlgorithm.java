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
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
public abstract class HashRangeMultiKeyShardAlgorithm<T extends Comparable<?>> extends AbstractHashRangeShardAlgorithm implements MultiKeyShardAlgorithm<T> {

    private static Logger logger = LoggerFactory.getLogger(HashRangeMultiKeyShardAlgorithm.class);
    private List<Range>[] rangesList = new ArrayList[2];
    private MultiKeyShardTarget shardTarget = new MultiKeyShardTarget();

    protected HashRangeMultiKeyShardAlgorithm(String fileName) {
        super(fileName);
        parseRange();
        loadTargetMap();
    }

    /**
     * 解析Range范围
     */
    private void parseRange(){
        Pattern pattern = Pattern.compile("(?<=\\[)(.*?)(?=\\])");
        Matcher matcher = pattern.matcher(rangeText);
        for(int i=0; i<2; i++){
            if(matcher.find()){
                rangesList[i] = createRanges(matcher.group());
            }
        }
        //目前只支持二维分片
        if(matcher.find()){
            throw new UnsupportedOperationException();
        }
    }

    private void loadTargetMap(){
        int targetIndex = 0;
        for(int i=0; i<mod; i++){
            for(int j=0; j<rangesList[0].size(); j++){
                for(int m=0; m<mod; m++){
                    for(int n=0; n<rangesList[0].size(); n++){
                        shardTarget.put(i+""+j+SEPARATOR+m+""+n, targetIndex++ + "");
                    }
                }
            }
        }
    }

    /**
     * 计算范围分片
     *
     * @param shardValue 分片值
     * @return hash
     */
    public abstract int rangeShardValue(T shardValue, List<Range> ranges);

    /**
     * 根据分片值计算分片结果名称集合.
     *
     * @param availableTargetNames 所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValues 分片值集合
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    @Override
    public Collection<String> shard(Collection<String> availableTargetNames, Collection<ShardValue<T>> shardValues) {
        String[][] dimensionalGroup = new String[2][];
        Iterator<ShardValue<T>> iterator = shardValues.iterator();
        for(int i=0; i<2; i++){
            ShardValue<T> shardValue = iterator.next();
            String[] targetGroup;
            if(shardValue.isEmpty()){
                targetGroup = allNode(rangesList[i]);
            } else {
                switch (shardValue.getType()){
                    case SINGLE:
                        targetGroup = new String[] {hashShardValue(shardValue.getValue()) + "" + rangeShardValue(shardValue.getValue(), rangesList[i])};
                        break;
                    default:
                        throw new UnsupportedOperationException(shardValue.getType().getClass().getName());
                }
            }
            dimensionalGroup[i] = targetGroup;
        }
        return shardTarget.getAfterCartesian(SEPARATOR, availableTargetNames, dimensionalGroup);
    }

    private String[] allNode(List<Range> ranges){
        String[] targetGroup = new String[mod*ranges.size()];
        int index = 0;
        for(int i=0; i<mod; i++){
            for(int j=0; j<ranges.size(); j++){
                targetGroup[index++] = i + "" + j;
            }
        }
        return targetGroup;
    }
}
