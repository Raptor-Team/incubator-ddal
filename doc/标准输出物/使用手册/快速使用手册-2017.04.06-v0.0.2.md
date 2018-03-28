[TOC]

## 快速使用

Raptor DDAL支持集成和代理两种使用方式。当应用以集成方式使用DDAL时，DDAL提供给应用的是数据源(兼容JDBC的DataSource)。当应用以代理方式使用DDAL时，DDAL提供给应用的是基于MySQL协议的连接。

通过阅读此快速使用指南，你将了解如何以应用集成的方式使用DDAL。

### 必备条件

为了能成功编译安装Raptor DDAL，请确保本地环境满足以下几个条件：

- JDK 7 或更新版本
- Maven 3.3.x 或更新版本

### 准备分片数据库

DDAL可直接嵌入到可运行的应用里，如果您的应用已经具备了数据库访问的能力，并且已有现成的数据库可以使用，则可直接在应用中嵌入ddal来访问数据库。如果这些您都没有，那么可以继续阅读此章节。当然，此章节的重点并不是讲述如何搭建数据库与模型设计。通过阅读此章节您可以对数据的分片存储有个大概的了解，这才是这个章节的重点。

我们选择MySQL作为这个Demo的数据库，请在本地自行（或请求团队的DBA协助）安装MySQL数据库。我们使用MySQL的schema模拟不同的物理数据库。

假设我们的数据分布在四个分片，那我们就需要创建4个schema来模拟这四个物理数据库。

````sql
-- 删除已存在的schema
DROP SCHEMA IF EXISTS ddal_schema_0;
DROP SCHEMA IF EXISTS ddal_schema_1;
DROP SCHEMA IF EXISTS ddal_schema_2;
DROP SCHEMA IF EXISTS ddal_schema_3;
-- 创建schema
CREATE DATABASE ddal_schema_0 DEFAULT CHARACTER SET utf8;
CREATE DATABASE ddal_schema_1 DEFAULT CHARACTER SET utf8;
CREATE DATABASE ddal_schema_2 DEFAULT CHARACTER SET utf8;
CREATE DATABASE ddal_schema_3 DEFAULT CHARACTER SET utf8;
````

我们的Demo只查询一张表，表名及建表语句如下：

````sql
CREATE TABLE `ddal_table` (
  `id` int(11) unsigned NOT NULL,
  `name` varchar(32) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `state` int(1) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
````

接下来，我们需要在各个分片上都创建此表，并且预置一些查询所需的数据。

````sql
-- 创建各个分片的表
DROP TABLE IF EXISTS `ddal_schema_0`.`ddal_table`;
CREATE TABLE `ddal_schema_0`.`ddal_table` (`id` int(11) unsigned NOT NULL, `name` varchar(32) DEFAULT NULL, `create_time` datetime DEFAULT NULL, `state` int(1) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ddal_schema_1`.`ddal_table`;
CREATE TABLE `ddal_schema_1`.`ddal_table` (`id` int(11) unsigned NOT NULL, `name` varchar(32) DEFAULT NULL, `create_time` datetime DEFAULT NULL, `state` int(1) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ddal_schema_2`.`ddal_table`;
CREATE TABLE `ddal_schema_2`.`ddal_table` (`id` int(11) unsigned NOT NULL, `name` varchar(32) DEFAULT NULL, `create_time` datetime DEFAULT NULL, `state` int(1) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `ddal_schema_3`.`ddal_table`;
CREATE TABLE `ddal_schema_3`.`ddal_table` (`id` int(11) unsigned NOT NULL, `name` varchar(32) DEFAULT NULL, `create_time` datetime DEFAULT NULL, `state` int(1) DEFAULT NULL, PRIMARY KEY (`id`) ) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 预置查询数据
INSERT INTO `ddal_schema_1`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (1, 'Kelly', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_2`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (2, 'Lynch', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_3`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (3, 'Madden', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_0`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (4, 'Madeline', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_1`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (5, 'Kelsey', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_2`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (6, 'Padgett', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_3`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (7, 'Pamela', '2017-01-09 14:24:00', 1);
INSERT INTO `ddal_schema_0`.`ddal_table` (`id`, `name`, `create_time`, `state`) VALUES (8, 'Calvert', '2017-01-09 14:24:00', 1);
````

从预置的查询数据的主键似乎能猜到数据分布的规律，即

| 分片      | 数据主键 |
| ------- | ---- |
| shard-0 | 4,8  |
| shard-1 | 1,5  |
| shard-2 | 2,6  |
| shard-3 | 3,7  |

这种是比较常见的按主键取模的分片方式，即mod(id, 4)。

### 使用Maven创建Java App

使用maven命令快速创建一个Java app。

````shell
mvn archetype:generate -DgroupId=studio.raptor -DartifactId=ddal-quickstart -DarchetypeArtifactId=maven-archetype-quickstart -X
````

此命令未禁用Maven的交互模式，所以在创建app的过程中会有一些交互需要您确认。待Maven命令执行结束，可以将此工程导入至您常用的开发工具中进行编辑。

添加ddal和jdbc connector的依赖

````xml
<dependencies>
  <dependency>
    <groupId>studio.raptor</groupId>
    <artifactId>ddal-jdbc</artifactId>
    <version>3.0.0-SNAPSHOT</version>
  </dependency>
  <dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>5.1.39</version>
  </dependency>
</dependencies>
````

### Raptor DDAL配置

DDAL核心配置文件

**shard-config.xml**

````xml
<dbShard xmlns="http://ddal.raptor.studio/shard-config" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://ddal.raptor.studio/shard-config">
    <!-- 虚拟数据库管理 -->
    <virtualDBs>
        <virtualDB name="quickstart" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
            <table name="ddal_table" databaseRule="database-rule"/>
        </virtualDB>
    </virtualDBs>
    <shardGroups>
        <shardGroup name="shardGroup1">
            <shard name="shard_0" dsGroup="group_1" schema="ddal_schema_0"/>
            <shard name="shard_1" dsGroup="group_1" schema="ddal_schema_1"/>
            <shard name="shard_2" dsGroup="group_1" schema="ddal_schema_2"/>
            <shard name="shard_3" dsGroup="group_1" schema="ddal_schema_3"/>
        </shardGroup>
    </shardGroups>
    <!-- 物理数据库集群管理 -->
    <physicalDBClusters>
        <physicalDBCluster name="cluster1" type="mysql">
            <dbInstance name="inst1" hostname="192.168.199.23" port="3306" sid="" rw="W" role="M0" status="O"/>
            <dbInstance name="inst2" hostname="192.168.199.23" port="3306" sid="" rw="R" role="M0" status="O"/>
        </physicalDBCluster>
    </physicalDBClusters>
    <!--数据源管理-->
    <dataSources>
        <group name="group_1" relaCluster="cluster1">
            <dataSource user="root" pwd="root123" dbInstName="inst1" dbDriver="jdbc">
                <params>
                    <property name="minIdle" value="1"/>
                    <property name="maxIdle" value="5"/>
                    <property name="maxTotal" value="10"/>
                    <property name="timeBetweenEvictionRunsMillis" value="5000"/>
                    <property name="testWhileIdle" value="true"/>
                </params>
            </dataSource>
            <dataSource user="root" pwd="root123" dbInstName="inst2" dbDriver="jdbc">
                <params>
                    <property name="minIdle" value="1"/>
                    <property name="maxIdle" value="5"/>
                    <property name="maxTotal" value="10"/>
                    <property name="timeBetweenEvictionRunsMillis" value="5000"/>
                    <property name="testWhileIdle" value="true"/>
                </params>
            </dataSource>
        </group>
    </dataSources>
</dbShard>
````

**rule-config.xml**

```xml
<ruleConfig xmlns="http://ddal.raptor.studio/rule-config"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://ddal.raptor.studio/rule-config">

  <shardRules>
    <shardRule name="database-rule" shardColumn="id" param="4"
      algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/>
  </shardRules>
</ruleConfig>
```

**/META-INF/services/studio.raptor.ddal.config.fetcher.ConfigFetcher**

目录结构如下：

```txt
│── main
└── resources
    ├── META-INF
    │   └── services
    │       └── studio.raptor.ddal.config.fetcher.ConfigFetcher
```

配置获取使用了SPI机制，若使用本地配置文件加载则使用以下配置，或直接实现studio.raptor.ddal.config.fetcher.LocalConfigFetcher接口。

```txt
studio.raptor.ddal.config.fetcher.embed.DefaultLocalConfigFetcher
```

在QuickStart里暂不对配置文件做太多介绍，若想了解更多关于配置文件相关的内容，请看考Raptor DDAL 配置文件详解。



### 使用DDAL查询数据

修改App的main函数代码

````java
public class App {
    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new RaptorDataSource("quickstart", "mysql");
        try (
                Connection connection = dataSource.getConnection();
        ) {
            PreparedStatement preparedStatement =
                    connection.prepareStatement("select id, name, create_time, state from ddal_table where id = ?");
            preparedStatement.setLong(1, 2);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("id:" + resultSet.getLong(1));
                System.out.println("name:" + resultSet.getString(2));
                System.out.println("create_time:" + resultSet.getTimestamp(3));
                System.out.println("state:" + resultSet.getInt(4));
            }
        }
    }
}
````

执行此Main方法，Console的输出如下：

````log
01/09 15:34:24.634  INFO [main] (ShardConfig.java:153) memory config file load completed > 'memory.properties' 
01/09 15:34:25.124  INFO [main] (ShardConfig.java:162) local config files load completed > 'dbShard.xml' 
01/09 15:34:26.018 DEBUG [main] (ContextLogProxy.java:95) 
原始SQL: select id, name, create_time, state from ddal_table where id = ?
SQL解析: 耗时[0.337162]ms, SQL类型[DML:SELECT], 表[ddal_table], 预编译参数:[2]
SQL路由: 耗时[0.048909]ms, 路由分片列表：[shard_2]
SQL重写: 耗时[0.034308]ms, 重写后SQL:[SELECT id, name, create_time, state FROM ddal_schema_2.ddal_table WHERE id = ?]
SQL执行: 耗时[2.460174]ms
结果集处理: 耗时[0.014705]ms, 结果集条数[1], 数据库变更条数[0]
DDAL执行总耗时:2.895258 ms 
id:2
name:Lynch
create_time:2017-01-09 14:24:00.0
state:1
````