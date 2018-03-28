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

package studio.raptor.ddal.config.config;

import java.util.concurrent.TimeUnit;
import studio.raptor.ddal.common.event.NativeEventBus;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.config.common.ConfigConstant;
import studio.raptor.ddal.config.common.FileLoader;
import studio.raptor.ddal.config.event.ReloadingEventListener;
import studio.raptor.ddal.config.io.FileHandler;
import studio.raptor.ddal.config.io.FileLocator.FileLocatorBuilder;
import studio.raptor.ddal.config.model.shard.DataSourceGroups;
import studio.raptor.ddal.config.model.shard.PhysicalDBCluster;
import studio.raptor.ddal.config.model.shard.PhysicalDBClusters;
import studio.raptor.ddal.config.model.shard.ShardGroups;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.config.model.shard.VirtualDbs;
import studio.raptor.ddal.config.parser.ShardConfigParser;
import studio.raptor.ddal.config.reloading.FileHandlerReloadingDetector;
import studio.raptor.ddal.config.reloading.PeriodicReloadingTrigger;
import studio.raptor.ddal.config.reloading.ReloadingController;

public class ShardConfig extends AbstractConfig {

  private static final String SHARD_CONFIG_XSD = "shard-config.xsd";

  private RuleConfig ruleConfig;
  private VirtualDbs virtualDbs;
  private PhysicalDBClusters physicalDBClusters;
  private DataSourceGroups dataSourceGroups;
  private ShardGroups shardGroups;

  private static ShardConfig instance = new ShardConfig();

  public static ShardConfig getInstance() {
    return instance;
  }

  private ShardConfig() throws GenericException {
    // load at initialize stage
    loadShardConfig();
    // register event subscriber
    NativeEventBus.get().register(new ReloadingEventListener());
    registerReloadingController();
  }

  private void loadShardConfig() {
    String xmlContent = this.getFileString(this.configFetcher.getShardConfigPath());
    this.validate(xmlContent, FileLoader.readLocalFile(SHARD_CONFIG_XSD));
    ruleConfig = RuleConfig.getInstance();
    ShardConfigParser.parse(this, xmlContent);
  }

  /**
   * Shard Config部分配置支持重载
   *
   * 3.0.0 新增的功能
   * @since 3.0.0
   */
  @Override
  public void reload() {
    loadShardConfig();
  }

  private void registerReloadingController() {
    if (ConfigConstant.FETCH_MODE_LOCAL.equals(this.configFetcher.fetchingMode())) {
      new PeriodicReloadingTrigger(
          new ReloadingController(
          new FileHandlerReloadingDetector(new FileHandler(
              new FileLocatorBuilder(null).sourceURL(
                  ShardConfig.class.getClassLoader().getResource(this.configFetcher.getShardConfigPath())).create()
          ))
          )
          , null, 5000, TimeUnit.MILLISECONDS).start();
    }
  }

  public RuleConfig getRuleConfig() {
    return ruleConfig;
  }

  public VirtualDbs getVirtualDbs() {
    return virtualDbs;
  }

  public void setVirtualDbs(VirtualDbs virtualDbs) {
    this.virtualDbs = virtualDbs;
  }

  public VirtualDb getVirtualDb(String dbName) {
    return this.virtualDbs.get(dbName);
  }

  public PhysicalDBClusters getPhysicalDBClusters() {
    return physicalDBClusters;
  }

  public void setPhysicalDBClusters(PhysicalDBClusters physicalDBClusters) {
    this.physicalDBClusters = physicalDBClusters;
  }

  public PhysicalDBCluster getPhysicalDBCluster(String clusterName) {
    return this.physicalDBClusters.get(clusterName);
  }

  public DataSourceGroups getDataSourceGroups() {
    return dataSourceGroups;
  }

  public void setDataSourceGroups(DataSourceGroups dataSourceGroups) {
    this.dataSourceGroups = dataSourceGroups;
  }

  public ShardGroups getShardGroups() {
    return shardGroups;
  }

  public void setShardGroups(ShardGroups shardGroups) {
    this.shardGroups = shardGroups;
  }

  @Override
  public String toString() {
    return "ShardConfig{" +
        "virtualDbs=" + virtualDbs +
        ", physicalDBClusters=" + physicalDBClusters +
        ", dataSourceGroups=" + dataSourceGroups +
        ", shardGroups=" + shardGroups +
        '}';
  }
}
