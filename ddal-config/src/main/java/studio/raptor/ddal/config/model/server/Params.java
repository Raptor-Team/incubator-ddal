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

import java.util.HashMap;

import com.google.common.base.Strings;

public class Params extends HashMap<String, String> {

    private static final long serialVersionUID = -3255538536487288330L;

    @SuppressWarnings("unchecked")
    public <T> T get(String name, T defaultValue) {
        String value = super.get(name);
        if (Strings.isNullOrEmpty(value)) {
            return defaultValue;
        } else {
            if (defaultValue instanceof String) {
                return (T) value;
            } else if (defaultValue instanceof Integer) {
                return (T) Integer.valueOf(value);
            } else if (defaultValue instanceof Long) {
                return (T) Long.valueOf(value);
            } else if (defaultValue instanceof Float) {
                return (T) Float.valueOf(value);
            } else if (defaultValue instanceof Boolean) {
                return (T) Boolean.valueOf(value);
            } else {
                throw new RuntimeException("defaultValue's data type is not supported");
            }
        }
    }

    public static void main(String[] args) {

        Params params = new Params();

        String a = params.<String>get("a", "a");

        System.out.println(a);
    }

}
