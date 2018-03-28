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

public class Shard {

    private String name;
    private String dsGroup;
    private String schema;
    private String shardZone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDsGroup() {
        return dsGroup;
    }

    public void setDsGroup(String dsGroup) {
        this.dsGroup = dsGroup;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public String getShardZone() {
        return shardZone;
    }

    public void setShardZone(String shardZone) {
        this.shardZone = shardZone;
    }

    @Override
    public String toString() {
        return "Shard{" +
                "name='" + name + '\'' +
                ", dsGroup='" + dsGroup + '\'' +
                ", schema='" + schema + '\'' +
                ", shardZone='" + shardZone + '\'' +
                '}';
    }
}
