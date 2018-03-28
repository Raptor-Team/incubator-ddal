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

import com.google.common.base.Strings;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ThreadLocalRandom;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import studio.raptor.ddal.common.exception.ConfigException;
import studio.raptor.ddal.common.exception.ConfigException.Code;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.CommonErrorCodes;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.config.common.ConfigConstant;
import studio.raptor.ddal.config.common.ConfigTools;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.config.SystemProperties;
import studio.raptor.ddal.config.model.shard.DataSource;
import studio.raptor.ddal.config.model.shard.DataSourceGroup;
import studio.raptor.ddal.config.model.shard.DataSourceGroups;
import studio.raptor.ddal.config.model.shard.PhysicalDBCluster;
import studio.raptor.ddal.config.model.shard.PhysicalDBCluster.DBInstance;
import studio.raptor.ddal.core.connection.jdbc.HikariConfig;
import studio.raptor.ddal.core.connection.jdbc.HikariDataSource;
import studio.raptor.ddal.core.connection.jdbc.JdbcBackendConnection;
import studio.raptor.ddal.core.constants.ConnectionRwMode;
import studio.raptor.ddal.core.constants.DataSourceAccessLevel;

/**
 * Backend connection pool manager
 *
 * @author Sam
 * @since 3.0.0
 */
public enum BackendDataSourceManager {

  INSTANCE;
  /**
   * 后端连接池组对象模型。
   */
  private transient Map<String, Map<String, List<javax.sql.DataSource>>> shardDataSourceGroup;
  private transient Map<String, javax.sql.DataSource> poolNameIndexedDataSources;

  /**
   * key是DataSourceGroup.Name和DataSource.seq组合。用于
   * 快速找物理数据源。
   */
  private transient Map<String, javax.sql.DataSource> groupSeqDataSources;

  // 连接池支持的属性
  private Map<String, Integer> coreAttributes;

  // DDAL抽象出来的属性与连接池核心属性的映射。
  private Map<String, String> abstractAttributes;


  private final Integer NONNULL_VALUE_INT = 1;
  private final String NULL_VALUE_STRING = "";

  /**
   * 后端连接池管理器构造器。
   *
   * @throws GenericException 初始化连接时连接数据库发生的异常
   */
  BackendDataSourceManager() throws GenericException {
    shardDataSourceGroup = new HashMap<>();
    groupSeqDataSources = new HashMap<>();
    poolNameIndexedDataSources = new HashMap<>();
    initSupportedConf();
    try {
      initializePool();
      // 初始化成功之后注册连接池配置变化事件监听
      //NativeEventBus.get().register(new DSChangeEventListener());
    } catch (SQLException sqlException) {
      throw new GenericException(CommonErrorCodes.COMMON_501, sqlException);
    }
  }

  private void initSupportedConf() {
    coreAttributes = new HashMap<>();
    coreAttributes.put("connectionTimeout", NONNULL_VALUE_INT);
    coreAttributes.put("validationTimeout", NONNULL_VALUE_INT);
    coreAttributes.put("idleTimeout", NONNULL_VALUE_INT);
    coreAttributes.put("leakDetectionThreshold", NONNULL_VALUE_INT);
    coreAttributes.put("maxLifetime", NONNULL_VALUE_INT);
    coreAttributes.put("maximumPoolSize", NONNULL_VALUE_INT);
    coreAttributes.put("minimumIdle", NONNULL_VALUE_INT);
    coreAttributes.put("initializationFailTimeout", NONNULL_VALUE_INT);
    coreAttributes.put("password", NONNULL_VALUE_INT);
    coreAttributes.put("poolName", NONNULL_VALUE_INT);
    coreAttributes.put("username", NONNULL_VALUE_INT);

    abstractAttributes = new HashMap<>();
    abstractAttributes.put("minIdle", "minimumIdle");
    abstractAttributes.put("maxIdle", NULL_VALUE_STRING); // 忽略的属性
    abstractAttributes.put("maxTotal", "maximumPoolSize");
  }

/*  *//**
   * 从数据源里获取后端物理连接。
   *
   * 物理连接的autoCommit属性是jdbc事务的关键。
   *
   * @param groupName 数据源组名
   * @param isReadOnly 是否只读
   * @return Connection
   * @throws SQLException SQLException
   *//*
  public static BackendConnection getBackendConnection(String groupName, boolean isReadOnly,
      boolean autoCommit) throws SQLException {
    Map<String, List<javax.sql.DataSource>> groupDataSources = getGroupDataSource(groupName);

    List<javax.sql.DataSource> backendDataSources =
        isReadOnly ? groupDataSources.get(ConnectionRwMode.R.getCode())
            : groupDataSources.get(ConnectionRwMode.W.getCode());

    //checkBackendDataSources(backendDataSources);
    javax.sql.DataSource dataSource = backendDataSources.get(0);
    // checkDataSourceAccessLevel(isReadOnly, dataSource);
    Connection conn = dataSource.getConnection();
    conn.setAutoCommit(autoCommit);
    return new JdbcBackendConnection(conn);
  }*/

  /**
   * 获取读写连接。目前暂不支持多个写连接，所以每个数据源组下只有一个写连接。
   *
   * @param groupName 数据源组名
   * @param autoCommit 是否自动提交
   * @return 写连接
   * @throws SQLException 获取连接异常
   */
  public static BackendConnection getReadWriteConnection(String groupName, boolean autoCommit)
      throws SQLException {
    Map<String, List<javax.sql.DataSource>> groupDataSources = getGroupDataSource(groupName);
    List<javax.sql.DataSource> backendDataSources = groupDataSources
        .get(ConnectionRwMode.W.getCode());
    javax.sql.DataSource dataSource = backendDataSources.get(0);
    Connection conn = dataSource.getConnection();
    conn.setAutoCommit(autoCommit);
    return new JdbcBackendConnection(conn);
  }

  public Map<String, javax.sql.DataSource> getPoolNameIndexedDataSources() {
    return poolNameIndexedDataSources;
  }

  /**
   * 获取只读连接。
   *
   * 若只读连接顺序不为空，则获取入参指定顺序数据源的连接。否则，使用DDAL默认的
   * 只读连接随机负载策略。
   *
   * @param groupName 数据源组名
   * @param autoCommit 是否自动提交
   * @param seqArray 只读数据源的序号。序号的定义参考配置{@link DataSource#getSeq()}
   * @return 写连接
   * @throws SQLException 获取连接异常
   */
  public static BackendConnection getReadOnlyConnection(String groupName, boolean autoCommit,
      List<Integer> seqArray) throws SQLException {

    javax.sql.DataSource dataSource;
    if (null != seqArray && seqArray.size() > 0) {
      String dsSeqKey = groupSeqDsKey(groupName, ConnectionRwMode.R.getCode(), seqArray.get(0));
      dataSource = INSTANCE.groupSeqDataSources.get(dsSeqKey);
      if (null == dataSource) {
        throw new RuntimeException("No datasource found, key: " + dsSeqKey);
      }
    } else {
      Map<String, List<javax.sql.DataSource>> groupDataSources = getGroupDataSource(groupName);
      List<javax.sql.DataSource> backendDataSources =
          groupDataSources.get(ConnectionRwMode.R.getCode());
      dataSource = backendDataSources.get(randomReadonlyIdx(backendDataSources.size()));
    }

    Connection conn = dataSource.getConnection();
    conn.setAutoCommit(autoCommit);
    return new JdbcBackendConnection(conn);
  }

  private static String groupSeqDsKey(String groupName, String readwrite, Integer seq) {
    return groupName + "^" + readwrite + "^" + seq;
  }

  /**
   * 线程安全的只读数据源下标获取方法。
   *
   * @param datasourceCount 只读数据源的个数
   * @return 只读数据源的随机访问下标
   */
  private static int randomReadonlyIdx(int datasourceCount) {
    return ThreadLocalRandom.current().nextInt(datasourceCount);
  }

//
//  /**
//   * 根据数据源组名和数据库实例名获取后端连接。
//   *
//   * @param groupInstKey 物理数据源的唯一标记。
//   * @return 后端物理连接。
//   */
//  public static BackendConnection getBackendConnection(String groupInstKey, boolean autoCommit) throws SQLException {
//    javax.sql.DataSource groupInstDs = INSTANCE.groupInstDataSources.get(groupInstKey);
//    Connection conn = groupInstDs.getConnection();
//    conn.setAutoCommit(autoCommit);
//    return new JdbcBackendConnection(conn);
//  }

//  private static void checkDataSourceAccessLevel(boolean isReadOnly, BackendDataSource dataSource) {
//    int level = dataSource.getAccessLevel().getLevel() & DataSourceAccessLevel.MASK;
//    // 禁止访问连接校验
//    if (level == DataSourceAccessLevel.BLOCK.getLevel()) {
//      throw new GenericException(CommonErrorCodes.COMMON_512);
//    }
//    // AccessLevel是只读的数据源限制借写连接。
//    if (level == DataSourceAccessLevel.R.getLevel()) {
//      if (!isReadOnly) {
//        throw new GenericException(CommonErrorCodes.COMMON_513);
//      }
//    }
//  }

  static Map<String, List<javax.sql.DataSource>> getGroupDataSource(String groupName) {
    return INSTANCE.getShardDataSourceGroup(groupName);
  }

  private DataSourceAccessLevel reflectAccessLevel(String accessLevelTexture) {
    DataSourceAccessLevel accessLevel;
    if (StringUtil.isEmpty(accessLevelTexture)) {
      accessLevel = DataSourceAccessLevel.RW;
    } else {
      accessLevel = DataSourceAccessLevel.textureOf(accessLevelTexture);
    }
    return accessLevel;
  }

  /**
   * init connection pool
   *
   * @throws SQLException Database access error
   */
  @SuppressWarnings("unchecked")
  private void initializePool() throws SQLException {
    ShardConfig shardConf = ShardConfig.getInstance();
    DataSourceGroups dataSourceGroups = shardConf.getDataSourceGroups();

    for (DataSourceGroup dsGroup : dataSourceGroups) {
      PhysicalDBCluster cluster = shardConf.getPhysicalDBCluster(dsGroup.getRelaCluster());
      DataSource[] dataSources = dsGroup.getDataSources();

      if (null == dataSources || dataSources.length < 1) {
        continue;
      }
      // 将一个数据源组下的数据源全部都做一次排序，可以保证只读数据源同样有序
      // 按照seq升序排。
      List<DataSource> dsList = Arrays.asList(dataSources);
      Collections.sort(dsList, new Comparator<DataSource>() {
        @Override
        public int compare(DataSource o1, DataSource o2) {
          return o1.getSeq() - o2.getSeq();
        }
      });

      for (DataSource dataSource : dsList) {
        DBInstance dbInstance = cluster.get(dataSource.getDbInstName());
        ConnectionRwMode dbInsRwMode = ConnectionRwMode.fromCode(dbInstance.getRw());

        List<javax.sql.DataSource> groupAndModeDataSources =
            getGroupedAndModeDataSource(
                dsGroup.getName(), ConnectionRwMode.fromCode(dbInstance.getRw()));
        if (dbInsRwMode == ConnectionRwMode.W || dbInsRwMode == ConnectionRwMode.R) {
          HikariDataSource ds;
          // jndi datasource
          String jndiName;
          if (!StringUtil.isEmpty(jndiName = dataSource.getJndiName())) {
            HikariConfig jndiConfig = buildJndiConfig(jndiName, dataSource);
            ds = new HikariDataSource(jndiConfig);
            groupAndModeDataSources.add(ds);
          }

          // jdbc datasource
          else if ("jdbc".equals(dataSource.getDbDriver())) {
            HikariConfig config = buildJdbcConfig(cluster, dbInstance, dataSource, dsGroup);
            ds = new HikariDataSource(config);
            groupAndModeDataSources.add(ds);
          } else {
            throw new GenericException(CommonErrorCodes.COMMON_500, "", dataSource.getDbDriver());
          }
//          groupSeqDataSources.put(dsGroup.getName() + "^" + dbInsRwMode.getCode() + "^" + dataSource.getSeq(), ds);
          poolNameIndexedDataSources.put(ds.getPoolName(), ds);
          groupSeqDataSources
              .put(groupSeqDsKey(dsGroup.getName(), dbInsRwMode.getCode(), dataSource.getSeq()),
                  ds);
        }
      }
    }
  }

  private HikariConfig buildJndiConfig(String jndiName, DataSource dataSource) {
    try {
      Properties configProp = new Properties();
      constructConnPoolConfig(configProp, dataSource);

      Context context = new InitialContext();
      javax.sql.DataSource jndiDS = (javax.sql.DataSource) context.lookup(jndiName);
      if (jndiDS != null) {
        HikariConfig config = new HikariConfig(configProp);
        // 设置物理数据源参数
        setPhysicalDataSourceProp(config, dataSource);
        config.setDataSource(jndiDS);
        return config;
      } else {
        throw new RuntimeException("JNDI context lookup failed for dataSourceJNDI:" + jndiName);
      }
    } catch (NamingException ne) {
      throw new RuntimeException("JNDI context lookup failed for dataSourceJNDI:" + jndiName, ne);
    }
  }

  private HikariConfig buildJdbcConfig(PhysicalDBCluster cluster,
      DBInstance dbInstance, DataSource dataSource, DataSourceGroup dataSourceGroup) {
    Properties configProp = new Properties();
    // 设置jdbc驱动类
    chooseDriverClass(configProp, cluster, dbInstance);
    // 构造连接池参数
    constructConnPoolConfig(configProp, dataSource);

    HikariConfig poolConfig = new HikariConfig(configProp);
    poolConfig.setUsername(dataSource.getUser());
    poolConfig.setPassword(decryptPassword(dataSource.getPwd()));
    // 设置物理数据源参数
    setPhysicalDataSourceProp(poolConfig, dataSource);

    if (StringUtil.isEmpty(poolConfig.getPoolName())) {
      // 设置默认连接池的名称
      poolConfig.setPoolName(dataSourceGroup.getName() + "_" + dataSource.getDbInstName());
    }
    return poolConfig;
  }

  private void chooseDriverClass(Properties configProp, PhysicalDBCluster cluster,
      DBInstance dbInstance) {
    String dbType = cluster.getType().toLowerCase();
    String url = dbInstance.getUrl();
    switch (dbType) {
      case "mysql":
        if(Strings.isNullOrEmpty(url)){
          url = "jdbc:mysql://" + dbInstance.getHostname() + ":" + dbInstance.getPort();
        }
        configProp.put("jdbcUrl",url);
        configProp.put("driverClassName", "com.mysql.jdbc.Driver");
        break;
      case "oracle":
        if(Strings.isNullOrEmpty(url)){
          if(!StringUtil.isEmpty(dbInstance.getSid())) {
            url = String.format("jdbc:oracle:thin:@%s:%s:%s", dbInstance.getHostname(), dbInstance.getPort(), dbInstance.getSid());
          } else if(!StringUtil.isEmpty(dbInstance.getServiceName())) {
            // 兼容oracle serviceName模式
            url = String.format("jdbc:oracle:thin:@%s:%s/%s", dbInstance.getHostname(), dbInstance.getPort(), dbInstance.getServiceName());
          }
        }
        configProp.put("jdbcUrl", url);
        configProp.put("driverClassName", "oracle.jdbc.OracleDriver");
        break;
      case "h2":
        if(Strings.isNullOrEmpty(url)) {
          url = String.format("jdbc:h2:%s/%s;IFEXISTS=TRUE;FILE_LOCK=SOCKET", dbInstance.getH2dir(), dbInstance.getH2db());
        }
        configProp.put("jdbcUrl", url);
        configProp.put("driverClassName", "org.h2.Driver");
        break;
      default:
        throw new IllegalArgumentException("Unsupported db type " + cluster.getType());
    }
  }

  private void constructConnPoolConfig(Properties configProp, DataSource dataSource) {
    Map<String, String> params = dataSource.getParams();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (abstractAttributes.containsKey(entry.getKey())
          && !NULL_VALUE_STRING.equals(abstractAttributes.get(entry.getKey()))) {
        configProp.put(abstractAttributes.get(entry.getKey()), entry.getValue());
      } else if (coreAttributes.containsKey(entry.getKey())) {
        configProp.put(entry.getKey(), entry.getValue());
      }
    }
  }

  private void setPhysicalDataSourceProp(HikariConfig config, DataSource dataSource) {
    Map<String, String> params = dataSource.getParams();
    for (Map.Entry<String, String> entry : params.entrySet()) {
      if (!abstractAttributes.containsKey(entry.getKey())
          && !coreAttributes.containsKey(entry.getKey())) {
        config.addDataSourceProperty(entry.getKey(), entry.getValue());
      }
    }
  }

  private String decryptPassword(String cipherText) {
    String password = cipherText;
    if (!"true"
        .equalsIgnoreCase(
            SystemProperties.getInstance().get(ConfigConstant.PROP_KEY_CONFIG_DECRYPT_ENABLED))) {
      return password;
    }
    ConfigTools.loadSysDecryptKey();
    try {
      password = ConfigTools.decrypt(ConfigTools.getSysDecryptKey(), cipherText);
    } catch (Exception e) {
      throw ConfigException.create(Code.DECRYPT_PASSWORD_ERROR);
    }
    return password;
  }

  /**
   * 获取数据源组的读写连接池
   *
   * @param groupName 数据源组
   * @return 读写连接池
   */
  private Map<String, List<javax.sql.DataSource>> getShardDataSourceGroup(String groupName) {
    return shardDataSourceGroup.get(groupName);
  }

  /**
   * 获取数据源组中某种读写模式的连接池。PhysicalDBCluster节点下, 同一种
   * 读写模式可能会配置多个DbInstance，所以这个方法返回多个后端连接池。
   *
   * @param groupName datasource group name
   * @param rwMode read-write mode，{@link ConnectionRwMode}
   * @return 连接池组，在physicalDBCluster节点下
   */
  private List<javax.sql.DataSource> getGroupedAndModeDataSource(String groupName,
      ConnectionRwMode rwMode) {
    Map<String, List<javax.sql.DataSource>> shardDataSources = getShardDataSourceGroup(groupName);
    if (null == shardDataSources) {
      shardDataSources = new HashMap<>();
      this.shardDataSourceGroup.put(groupName, shardDataSources);
    }
    List<javax.sql.DataSource> sameModeDataSources = shardDataSources.get(rwMode.getCode());
    if (null == sameModeDataSources) {
      sameModeDataSources = new ArrayList<>();
      shardDataSources.put(rwMode.getCode(), sameModeDataSources);
    }
    return sameModeDataSources;
  }
}
