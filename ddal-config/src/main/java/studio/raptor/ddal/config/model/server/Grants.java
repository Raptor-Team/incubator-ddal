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

package studio.raptor.ddal.config.model.server;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Preconditions;

public class Grants extends ArrayList<Grant> {

    private static final long serialVersionUID = 8470972057541561704L;

    private Map<String, Grant> _grants = new HashMap<String, Grant>();

    @Override
    public boolean add(Grant g) {
      String key = g.getUser() + "_" + g.getVdbName();// user_vdb
        Preconditions.checkArgument(!_grants.containsKey(key), key + " exists in grants");
        _grants.put(key, g);
        return super.add(g);
    }

    public Grant get(String user, String vdb) {
        return _grants.get(user + "_" + vdb);
    }

}
