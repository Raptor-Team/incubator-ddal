/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements. See the NOTICE file distributed with this
 * work for additional information regarding copyright ownership. The ASF
 * licenses this file to You under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0 Unless required by applicable law
 * or agreed to in writing, software distributed under the License is
 * distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */

package studio.raptor.ddal.tests.algorithmn;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.algorithm.ShardValue;
import studio.raptor.ddal.common.algorithm.SingleKeyShardAlgorithm;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.RouteErrCodes;

/**
 * 四川按地区分片算法.
 *
 * @author Wu Xingwang
 * @since 3.0.0
 */
public class InstAreaIdSCShardAlgorithm implements SingleKeyShardAlgorithm<Comparable<?>>{

	private static Logger logger = LoggerFactory.getLogger(InstAreaIdSCShardAlgorithm.class);
	private static final String SPLITER = ",";
	// ddal/areaIdRange-sc.properties
	// shard_0=1,2,3
	// shard_1=4,7,8
	private final String fileName;
	// 分片与地区映射表
	private Map<String,List<Long>> shardToAreaMap = new HashMap<>();

	public InstAreaIdSCShardAlgorithm(String fileName){
		this.fileName = fileName;
		loadFile();
	}

	private void loadFile(){
		try{
			Properties areaIdRangeProp = new Properties();
			areaIdRangeProp.load(InstAreaIdSCShardAlgorithm.class.getClassLoader().getResourceAsStream(fileName));

			for(Object key : areaIdRangeProp.keySet()){
				Object value = areaIdRangeProp.get(key);
				if(null == value){
					logger.error("Properties file incorrect, value of key '" + key + "' is null");
					throw new RuntimeException("Properties file incorrect, value of key '" + key + "' is null");
				}
				List<Long> areaIds = convertLongArray(((String)value).split(SPLITER));
				shardToAreaMap.put((String)key,areaIds);
			}
		}catch(IOException ioe){
			logger.error("Read properties failed.",ioe);
			throw new GenericException(RouteErrCodes.ROUTE_428);
		}
	}

	private List<Long> convertLongArray(String[] values){
		List<Long> result = new ArrayList<>(values.length);
		for(String value : values){
			Long number = Long.parseLong(value);
			if(null == number){
				logger.error("Properties file incorrect, area is null");
				throw new RuntimeException("Properties file incorrect, area is null");
			}
			result.add(number);
		}
		return result;
	}

	/**
	 * 根据分片值和SQL的=运算符计算分片结果名称集合.
	 *
	 * @param shards
	 *            所有的可用目标名称集合, 一般是数据源或表名称
	 * @param shardValue
	 *            分片值
	 * @return 分片后指向的目标名称, 一般是数据源或表名称
	 */
	@Override
	public String doEqual(Collection<String> shards,ShardValue<Comparable<?>> shardValue){
		return compute(Long.parseLong(String.valueOf(shardValue.getValue())));
	}

	/**
	 * 根据分片值和SQL的IN运算符计算分片结果名称集合.
	 *
	 * @param shards
	 *            所有的可用目标名称集合, 一般是数据源或表名称
	 * @param shardValue
	 *            分片值
	 * @return 分片后指向的目标名称集合, 一般是数据源或表名称
	 */
	@Override
	public Collection<String> doIn(Collection<String> shards,ShardValue<Comparable<?>> shardValue){
		Collection<String> result = new LinkedHashSet<>(shards.size());
		Collection<Comparable<?>> values = shardValue.getValues();
		for(Comparable value : values){
			result.add(compute(Long.parseLong(String.valueOf(value))));
		}
		return result;
	}

	/**
	 * 根据分片值和SQL的BETWEEN运算符计算分片结果名称集合.
	 *
	 * @param shards
	 *            所有的可用目标名称集合, 一般是数据源或表名称
	 * @param shardValue
	 *            分片值
	 * @return 分片后指向的目标名称集合, 一般是数据源或表名称
	 */
	@Override
	public Collection<String> doBetween(Collection<String> shards,ShardValue<Comparable<?>> shardValue){
		throw new UnsupportedOperationException();
	}

	private String compute(long value){
		for(Map.Entry<String,List<Long>> entry : shardToAreaMap.entrySet()){
			List<Long> areaIds = entry.getValue();
			if(areaIds.contains(value)){
				return entry.getKey();
			}
		}
		throw new UnsupportedOperationException();
	}
}
