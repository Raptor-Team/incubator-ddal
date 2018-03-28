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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class PhysicalDBClusters extends ArrayList<PhysicalDBCluster> {

    private static final long serialVersionUID = -6641589879498649483L;

    private Map<String, PhysicalDBCluster> _clusters = new HashMap<String, PhysicalDBCluster>();

    @Override
    public boolean add(PhysicalDBCluster c) {
        Preconditions.checkArgument(!_clusters.containsKey(c.getName()), c.getName() + " exists in clusters");
        _clusters.put(c.getName(), c);
        return super.add(c);
    }

    public PhysicalDBCluster get(String name) {
        return _clusters.get(name);
    }

}
