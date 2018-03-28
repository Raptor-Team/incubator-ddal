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

import java.util.Arrays;

public class DataSourceGroup {

    private String name;
    private String relaCluster;
    private String balance;

    private DataSource[] dataSources;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelaCluster() {
        return relaCluster;
    }

    public void setRelaCluster(String relaCluster) {
        this.relaCluster = relaCluster;
    }

    public DataSource[] getDataSources() {
        return dataSources;
    }

    public void setDataSources(DataSource[] dataSources) {
        this.dataSources = dataSources;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "Group [name=" + name + ", relaCluster=" + relaCluster + ", dataSources=" + Arrays.toString(dataSources)
                + "]";
    }

}
