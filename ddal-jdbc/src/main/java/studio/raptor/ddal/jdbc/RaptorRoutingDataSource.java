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

package studio.raptor.ddal.jdbc;


import java.util.HashMap;
import java.util.Map;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.JdbcErrCodes;
import studio.raptor.ddal.config.config.RoutingDsProperties;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.config.model.shard.VirtualDbs;
import studio.raptor.ddal.jdbc.routing.AbstractRoutingSource;
import studio.raptor.ddal.jdbc.routing.DataSourceKeyHolder;
import studio.raptor.ddal.jdbc.routing.algorithm.RoutingAlgorithm;

/**
 * 带路由功能的数据源
 *
 * @author Charley
 * @since 1.0
 */
public class RaptorRoutingDataSource extends AbstractRoutingSource<RaptorDataSource> {

  private String databaseDialect;
  private RoutingAlgorithm routingAlgorithm;

  /**
   * 读取默认文件、默认路由算法的构造函数
   *
   * @param databaseDialect 数据库类型
   * @throws Exception 异常
   */
  public RaptorRoutingDataSource(String databaseDialect) throws GenericException {
    this.databaseDialect = databaseDialect;
    this.routingAlgorithm = new RoutingAlgorithm();

    initRouter(RoutingDsProperties.getInstance().getMapper(), loadOriginSources());
  }

  /**
   * 读取默认文件、指定路由算法的构造函数
   *
   * @param databaseDialect 数据库类型
   * @param routingAlgorithm 路由算法
   * @throws Exception 异常
   */
  public RaptorRoutingDataSource(String databaseDialect, RoutingAlgorithm routingAlgorithm)
      throws Exception {
    this.databaseDialect = databaseDialect;
    this.routingAlgorithm = routingAlgorithm;

    initRouter(RoutingDsProperties.getInstance().getMapper(), loadOriginSources());
  }

  private void initRouter(Map<String, String> routeMap,
      Map<String, RaptorDataSource> originSources) {
    this.setTargetSources(routeMap);
    this.setOriginSources(originSources);
    this.afterPropertiesSet();
  }

  /**
   * 路由计算
   */
  public RaptorDataSource route(String value) {
    String sourceKey = routingAlgorithm.calculateKey(value);
    DataSourceKeyHolder.set(sourceKey);
    return this.determineTargetSource();
  }

  @Override
  protected String determineCurrentLookupKey() {
    return DataSourceKeyHolder.get();
  }

  private Map<String, RaptorDataSource> loadOriginSources() throws GenericException {
    try {
      ShardConfig shardConfig = ShardConfig.getInstance();
      VirtualDbs vDBs = shardConfig.getVirtualDbs();

      Map<String, RaptorDataSource> dataSources = new HashMap<>(vDBs.size());
      for (VirtualDb vdb : vDBs) {
        String name = vdb.getName();
        dataSources.put(name, new RaptorDataSource(name, databaseDialect));
      }
      return dataSources;
    } catch (Exception e) {
      throw new GenericException(JdbcErrCodes.JDBC_607, "");
    }
  }
}
