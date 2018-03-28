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

import java.util.Map;

public class Grant {

    private String user;
  private String vdbName;
    private Map<String, Rule> rules;   // <type,rule obj>

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

  public String getVdbName() {
    return vdbName;
    }

  public void setVdbName(String vdbName) {
    this.vdbName = vdbName;
    }

    public Map<String, Rule> getRules() {
        return rules;
    }

    public void setRules(Map<String, Rule> rules) {
        this.rules = rules;
    }

    public static class Rule {

        private String obj;
        private String type;
        private String privilege;

        public Rule(String obj, String type, String privilege) {
            this.obj = obj;
            this.type = type;
            this.privilege = privilege;
        }

        public Rule() {
        }

        public String getObj() {
            return obj;
        }

        public void setObj(String obj) {
            this.obj = obj;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getPrivilege() {
            return privilege;
        }

        public void setPrivilege(String privilege) {
            this.privilege = privilege;
        }

        @Override
        public String toString() {
            return "Rule [obj=" + obj + ", type=" + type + ", privilege=" + privilege + "]";
        }
    }

    @Override
    public String toString() {
      return "Grant [user=" + user + ", vdbName=" + vdbName + ", rules=" + rules + "]";
    }

}
