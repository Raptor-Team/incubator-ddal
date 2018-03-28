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
import studio.raptor.ddal.common.exception.ConfigException;
import studio.raptor.ddal.common.exception.ConfigException.Code;

public class VirtualDbs extends ArrayList<VirtualDb> {

    private static final long      serialVersionUID = -1321913929789732474L;
    private Map<String, VirtualDb> virtualDbs       = new HashMap<String, VirtualDb>();

    @Override
    public boolean add(VirtualDb v) {
        Preconditions.checkArgument(!virtualDbs.containsKey(v.getName()), v.getName() + " exists in virtualdbs");
        virtualDbs.put(v.getName(), v);
        return super.add(v);
    }

    public VirtualDb get(String name) {
        if(!virtualDbs.containsKey(name)){
            throw ConfigException.create(Code.CONFIG_NOT_FOUND_ERROR, "VirtualDB name: "+name);
        }
        return virtualDbs.get(name);
    }
}
