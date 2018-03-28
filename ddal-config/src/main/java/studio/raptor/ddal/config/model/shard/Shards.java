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

package studio.raptor.ddal.config.model.shard;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Shards extends ArrayList<Shard> {

    private static final long serialVersionUID = 1L;

    private Map<String, Shard> _shards = new TreeMap<>();

    private List<String> shardNames = new ArrayList<>();

    @Override
    public boolean add(Shard s) {
        Preconditions.checkArgument(!_shards.containsKey(s.getName()), s.getName() + " exists in shardings");
        _shards.put(s.getName(), s);
        return super.add(s);
    }

    public Shard get(String name) {
        return _shards.get(name);
    }

    public List<String> allShardNames() {
        if(shardNames.isEmpty()){
            shardNames =  new ArrayList<>(_shards.keySet());
        }
        return shardNames;
    }

}
