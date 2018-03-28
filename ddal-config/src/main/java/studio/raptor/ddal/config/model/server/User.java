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

import java.util.Arrays;

public class User {
    private String name;
    private String pwd;
    private AccessPolicy accessPolicy;

    public User() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public AccessPolicy getAccessPolicy() {
        return accessPolicy;
    }

    public void setAccessPolicy(AccessPolicy accessPolicy) {
        this.accessPolicy = accessPolicy;
    }

    public static class AccessPolicy {
        private String type;
        private String[] ips;

        public AccessPolicy(String type, String[] ips) {
            super();
            this.type = type;
            this.ips = ips;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String[] getIps() {
            return ips;
        }

        public void setIps(String[] ips) {
            this.ips = ips;
        }

        @Override
        public String toString() {
            return "AccessPolicy [type=" + type + ", ips="
                    + Arrays.toString(ips) + "]";
        }

    }

    @Override
    public String toString() {
        return "User [name=" + name + ", pwd=" + pwd + ", accessPolicy="
                + accessPolicy + "]";
    }

}
