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

import java.util.Map;

public class DataSource {

  // <dataSource user="root" pwd="8EEC" dbInstName="inst1" dbDriver="jdbc">
  private String user;
  private String pwd;
  private String dbInstName;
  private String dbDriver;
  private String accessLevel;
  private String jndiName;
  private Integer seq;
  private Map<String, String> params;

  public String getAccessLevel() {
    return accessLevel;
  }

  public void setAccessLevel(String accessLevel) {
    this.accessLevel = accessLevel;
  }

  public Map<String, String> getParams() {
    return params;
  }

  public void setParams(Map<String, String> params) {
    this.params = params;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public String getDbInstName() {
    return dbInstName;
  }

  public void setDbInstName(String dbInstName) {
    this.dbInstName = dbInstName;
  }

  public String getDbDriver() {
    return dbDriver;
  }

  public void setDbDriver(String dbDriver) {
    this.dbDriver = dbDriver;
  }

  public String getJndiName() {
    return jndiName;
  }

  public void setJndiName(String jndiName) {
    this.jndiName = jndiName;
  }

  public Integer getSeq() {
    return seq;
  }

  public void setSeq(Integer seq) {
    this.seq = seq;
  }

  @Override
  public String toString() {
    return "DataSource{" +
        "user='" + user + '\'' +
        ", pwd='" + pwd + '\'' +
        ", dbInstName='" + dbInstName + '\'' +
        ", dbDriver='" + dbDriver + '\'' +
        ", accessLevel='" + accessLevel + '\'' +
        ", jndiName='" + jndiName + '\'' +
        ", seq=" + seq +
        ", params=" + params +
        '}';
  }
}
