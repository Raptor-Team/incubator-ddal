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

import java.util.HashMap;
import java.util.Map;

public class PhysicalDBCluster {

  private String name;
  private String type;

  private Map<String, DBInstance> dbInstances = new HashMap<String, DBInstance>();

  public void add(DBInstance instance) {
    dbInstances.put(instance.getName(), instance);
  }

  public DBInstance get(String name) {
    return dbInstances.get(name);
  }

  public Map<String, DBInstance> getDbInstances() {
    return dbInstances;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public static class DBInstance {

    private String name;
    private String hostname;
    private int port;
    private String sid;
    // 兼容oracle的serviceName
    private String serviceName;
    private String rw;
    private String role;
    private String status;
    private String h2dir;
    private String h2db;
    private String url;

    public DBInstance(String name, String hostname, int port, String sid, String rw, String role,
        String status) {
      super();
      this.name = name;
      this.hostname = hostname;
      this.port = port;
      this.sid = sid;
      this.rw = rw;
      this.role = role;
      this.status = status;
    }

    public String getH2dir() {
      return h2dir;
    }

    public void setH2dir(String h2dir) {
      this.h2dir = h2dir;
    }

    public String getH2db() {
      return h2db;
    }

    public void setH2db(String h2db) {
      this.h2db = h2db;
    }

    public String getRw() {
      return rw;
    }

    public void setRw(String rw) {
      this.rw = rw;
    }

    public String getRole() {
      return role;
    }

    public void setRole(String role) {
      this.role = role;
    }

    public String getStatus() {
      return status;
    }

    public void setStatus(String status) {
      this.status = status;
    }

    public DBInstance() {
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
    }

    public String getHostname() {
      return hostname;
    }

    public void setHostname(String hostname) {
      this.hostname = hostname;
    }

    public int getPort() {
      return port;
    }

    public void setPort(int port) {
      this.port = port;
    }

    public String getSid() {
      return sid;
    }

    public void setSid(String sid) {
      this.sid = sid;
    }

    public String getServiceName() {
      return serviceName;
    }

    public void setServiceName(String serviceName) {
      this.serviceName = serviceName;
    }

    public String getUrl() {
      return url;
    }

    public void setUrl(String url) {
      this.url = url;
    }

    @Override
    public String toString() {
      return "DBInstance{" +
          "name='" + name + '\'' +
          ", hostname='" + hostname + '\'' +
          ", port=" + port +
          ", sid='" + sid + '\'' +
          ", serviceName='" + serviceName + '\'' +
          ", rw='" + rw + '\'' +
          ", role='" + role + '\'' +
          ", status='" + status + '\'' +
          ", h2dir='" + h2dir + '\'' +
          ", h2db='" + h2db + '\'' +
          ", url='" + url + '\'' +
          '}';
    }
  }

  @Override
  public String toString() {
    return "PhysicalDBCluster [name=" + name + ", type=" + type + ", dbInstances=" + dbInstances
        .toString() + "]";
  }

}
