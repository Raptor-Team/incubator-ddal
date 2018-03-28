## 动态数据源（RaptorRoutingDataSource）使用说明

实际应用中会出现多个VirtualDB的场景，应用在使用DDAL时可通过`RaptorRoutingDataSource`实现虚拟DB的路由；



### 动态数据源配置

*shard-config.xml片段*

```xml
<virtualDBs>
  <virtualDB name="vdb1" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <table name="tableA" databaseRule="aid-database-rule"/>
  </virtualDB>
  <virtualDB name="vdb2" shardGroup="shardGroup2" rmOwner="true" sqlMaxLimit="-1">
    <table name="tableB" databaseRule="bid-database-rule"/>
  </virtualDB>
  <virtualDB name="vdb3" shardGroup="shardGroup3" rmOwner="true" sqlMaxLimit="-1">
    <table name="tableC" databaseRule="cid-database-rule"/>
  </virtualDB>
</virtualDBs>
```

> 上述配置中有多个虚拟数据库，假设按地区划分，则代表各地区的虚拟DB



*routing-ds.properties*

```properties
021=vdb1
025=vdb2
028=vdb3
```

> 此配置表示虚拟DB的路由表，假设按地区划分，则地区编码对应不同地区的虚拟DB
>



### JAVA代码案例

*JAVA示例片段*

```java
//数据源路由器
RaptorRoutingDataSource router = new RaptorRoutingDataSource("mysql");
//021地区所指向的VDB数据源
RaptorDataSource dataSource1 = router.route("021");
//025地区所指向的VDB数据源
RaptorDataSource dataSource2 = router.route("025");
```
