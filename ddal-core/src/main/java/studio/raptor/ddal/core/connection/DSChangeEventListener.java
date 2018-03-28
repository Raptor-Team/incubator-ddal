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

package studio.raptor.ddal.core.connection;

import com.google.common.eventbus.Subscribe;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.event.DataSourceChangeEvent;
import studio.raptor.ddal.config.model.shard.DataSourceGroup;
import studio.raptor.ddal.config.model.shard.DataSourceGroups;

/**
 * 数据源变更监听器。
 *
 * @author Sam
 * @since 3.0.0
 */
public class DSChangeEventListener {

  private static Logger logger = LoggerFactory.getLogger(DSChangeEventListener.class);

  /**
   * 此订阅者在分片配置文件发生改动时触发。
   *
   * @param event 数据源变化事件
   */
  @SuppressWarnings("unused")
  @Subscribe
  public void datasourceChange(DataSourceChangeEvent event) {
    logger.info("DataSource change received @ " + new Date(System.currentTimeMillis()));
    DataSourceGroups dsGroups = ShardConfig.getInstance().getDataSourceGroups();

    if (null == dsGroups || dsGroups.size() < 1) {
      logger.info("No datasource group detected!");
      return;
    }
    for (DataSourceGroup dsGroup : dsGroups) {
      // fixme 考虑数据源访问级别的实现
//      Map<String, List<BackendDataSource>> groupDataSource =
//          BackendDataSourceManager.getGroupDataSource(dsGroup.getName());
//      if (null == groupDataSource || groupDataSource.isEmpty()) {
//        continue;
//      }
//      // 配置数据源
//      for (DataSource dataSource : dsGroup.getDataSources()) {
//        // 运行时数据源实例
//        resetBackgroundDSAccessLevel(dsGroup.getName(), dataSource, groupDataSource);
//      }
    }
  }

//  private void resetBackgroundDSAccessLevel(String groupName, DataSource srcDataSource,
//      Map<String, List<BackendDataSource>> managedGroupDS) {
//    for (Map.Entry<String, List<BackendDataSource>> entry : managedGroupDS.entrySet()) {
//      if (null == entry.getValue() || entry.getValue().size() < 1) {
//        continue;
//      }
//      for (BackendDataSource bds : entry.getValue()) {
//        if (bds.getDbInstanceName().equals(srcDataSource.getDbInstName())) {
//          DataSourceAccessLevel accessLevel;
//          if (StringUtil.isEmpty(srcDataSource.getAccessLevel())) {
//            accessLevel = DataSourceAccessLevel.RW;
//          } else {
//            accessLevel = DataSourceAccessLevel.textureOf(srcDataSource.getAccessLevel());
//          }
//          bds.setAccessLevel(accessLevel);
//          logger.info("Update access level of {} to {}", srcDataSource.toString(groupName),
//              accessLevel);
//        }
//      }
//    }
//  }
}
