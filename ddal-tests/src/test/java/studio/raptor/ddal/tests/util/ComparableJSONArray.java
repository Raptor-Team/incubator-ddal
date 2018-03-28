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

package studio.raptor.ddal.tests.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ComparableJSONArray extends JSONArray {

    private JSONArray delegate;

    public ComparableJSONArray(JSONArray jsonArray) {
        this.delegate = jsonArray;
    }

    @Override
    public boolean equals(Object comparative) {
        if (!(comparative instanceof ComparableJSONArray)) {
            return false;
        }
        JSONArray _comparative = ((ComparableJSONArray) comparative).delegate, _this = (JSONArray) delegate.clone();
        int thisSize;
        if (null == _this || (thisSize = _this.size()) != _comparative.size()) {
            return false;
        }
        int equalFlag = 0;
        for (int i = 0, iMax = thisSize; i < iMax; i++) {
            for (int j = 0, jMax = _comparative.size(); j < jMax; j++) {
                if (areJsonObjectEqual(_this.getJSONObject(i), _comparative.getJSONObject(j))) {
                    equalFlag += (i + j);
                }
            }
        }
        return equalFlag == ((thisSize - 1) * thisSize) / 2;
    }

    private boolean areJsonObjectEqual(Object ob1, Object ob2) throws JSONException {
        Object obj1Converted = convertJsonElement(ob1);
        Object obj2Converted = convertJsonElement(ob2);
        return obj1Converted.equals(obj2Converted);
    }

    private Object convertJsonElement(Object elem) throws JSONException {
        if (elem instanceof JSONObject) {
            JSONObject obj = (JSONObject) elem;
            Set<String> keys = obj.keySet();
            Map<String, Object> jsonMap = new HashMap<>();
            for (String key : keys) {
                jsonMap.put(key, convertJsonElement(obj.get(key)));
            }
            return jsonMap;
        } else if (elem instanceof JSONArray) {
            JSONArray arr = (JSONArray) elem;
            Set<Object> jsonSet = new HashSet<>();
            for (int i = 0; i < arr.size(); i++) {
                jsonSet.add(convertJsonElement(arr.get(i)));
            }
            return jsonSet;
        } else {
            return elem;
        }
    }
}
