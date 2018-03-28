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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import studio.raptor.ddal.common.exception.ConfigException;
import studio.raptor.ddal.common.exception.ConfigException.Code;

/**
 * 分片规则集合
 *
 * @author Charley
 * @since 1.0
 */
public class ShardRules extends ArrayList<ShardRule> {

  private static final long serialVersionUID = 1L;
  private Map<String, ShardRule> _shardRules = new HashMap<>();

  @Override
  public boolean add(ShardRule rule) {
    _shardRules.put(rule.getName(), rule);
    return super.add(rule);
  }

  public ShardRule get(String name) {
    if(!_shardRules.containsKey(name)){
      throw ConfigException.create(Code.CONFIG_NOT_FOUND_ERROR, "Rule name: " + name);
    }
    return _shardRules.get(name);
  }
}
