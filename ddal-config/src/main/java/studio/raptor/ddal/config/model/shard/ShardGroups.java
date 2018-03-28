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

import java.util.*;

public class ShardGroups {

    private Map<String, ShardGroup> _shardGroups = new HashMap<>();

    public void addGroup(ShardGroup group) {
        Preconditions.checkArgument(!_shardGroups.containsKey(group.getName()), group.getName() + " exists in shards");
        _shardGroups.put(group.getName(), group);
    }

    public ShardGroup get(String name) {
        return _shardGroups.get(name);
    }

    public static class ShardGroup {

        private String name;

        private String[] shardZones;

        private Shards shards;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String[] getShardZones() {
            return shardZones;
        }

        public void setShardZones(String[] shardZones) {
            this.shardZones = shardZones;
        }

        public Shards getShards() {
            return shards;
        }

        public void setShards(Shards shards) {
            this.shards = shards;
        }
    }
}