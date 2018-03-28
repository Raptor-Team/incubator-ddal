shard-config.xml、rule-config.xml是Raptor-DDAL最核心的配置文件，从配置文件中我们可以了解应用访问了哪些表、有哪些物理数据库主机以及数据的分片情况，我们可以先看一下完整的配置文件，[点击查看完整的shard-config.xml](http://git.oschina.net/f150/raptor-ddal/blob/develop/ddal-quickstart/src/main/resources/shard-config.xml)、[rule-config.xml](http://git.oschina.net/f150/raptor-ddal/blob/develop/ddal-quickstart/src/main/resources/rule-config.xml)

### 虚拟数据库 VirtualDB

一组被切分的表组成的虚拟的数据库。

配置示例：

````xml
<virtualDBs>
  <virtualDB name="crmdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <table name="area" type="global"/>
    <table name="customer" databaseRule="id-database-rule"/>
    <table name="order" shards="shard_0" actualTables="order_0,order_1,order_2,order_3"
           tableRule="num-database-rule"/>
    <table name="party" databaseRule="id-database-rule"
           actualTables="party_0,party_1,party_2,party_3" tableRule="num-database-rule"/>
  </virtualDB>
</virtualDBs>
````

**virtualDB**

- name 虚拟数据库名称，唯一性由配置人员约束。
- shardGroup 数据源组名称。表示分片在哪个数据源组上分布，组内包含具体分片。
- rmOwner 是否移除SQL语句中的属主。例如select  * from test.table(test即为属主) 。
- sqlMaxLimit 控制SQL查询语句的结果集大小。-1表示无限制，设置这个属性为了避免查询过多数据。

**table**

- name 表名。如果不分表，此表名对应物理表名。
- shards 数据库分片名集合。此值可不填，默认情况下为虚拟DB上shardGroup，否则为指定分片。
- databaseRule 数据源路由规则名称。即根据条件路由到目标数据源的规则，规则包含分片字段、路由算法。
- actualTables 实际物理表集合。分表场景下，多个实际物理表对应一个虚拟表。
- tableRule 分表路由规则名称。根据条件路由到目标物理表，包含分片字段、路由算法。
- type 虚拟表表类型。用来标识全局表，global表示为全局表，分片表不填充。




### 路由规则 Rule

virtualDB中databaseRule、tableRule的具体规则，包含分片字段、路由算法。

配置示例：

```xml
<shardRules>
  <shardRule name="teacher-database-rule" shardColumn="tno" param="4"         algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/>
  <shardRule name="teacher-table-rule" shardColumn="tno" param="0~1000,1001~2000"          algorithm="studio.raptor.ddal.common.algorithm.embed.HashAlgorithm"/>
  <shardRule name="student-database-rule" shardColumn="sno" param="0,2"          algorithm="studio.raptor.ddal.common.algorithm.embed.SubStringAlgorithm"/>
</shardRules>
```

**shardRule**

- name 规则名称。与table中的规则对应。
- shardColumn 分片字段。指定使用这条规则的表用这些字段进行分片，可以是单个也可以是多个。大多数情况是物理表的字段，当物理表中没有分片字段时可以配置虚拟的分片字段，然后使用线索Hint来进行路由。[点击查看Hint路由用法](http://git.oschina.net/f150/raptor-ddal/blob/develop/doc/DetailedDesign/ddal-hint-routing.md)
- algorithm 路由算法。此条规则根据这个算法进行路由计算，需要实现算法接口。
- param 算法参数。如Mod的模数，Range的范围，SubString的起始等等。

### 分片管理 Shard

shardGroup为分片组，即物理上多个分片组成一个分片组。分片组中shardZone为分片域，用来标识哪些分片来自哪些业务域，可不填。

配置示例：

```xml
<shardGroups>
  <shardGroup name="shardGroup1">
    <shardZone name="BeijingZone">
      <shard name="shard_bj_0" dsGroup="group_1" schema="schema0"/>
      <shard name="shard_bj_1" dsGroup="group_1" schema="schema1"/>
    </shardZone>
    <shardZone name="JiangsuZone">
      <shard name="shard_js_0" dsGroup="group_1" schema="schema1"/>
      <shard name="shard_js_1" dsGroup="group_1" schema="schema1"/>
    </shardZone>
  </shardGroup>
</shardGroups>
```

**shardGroup**

- name 分片组名称。virtualDB中shardGroup指定到此。

**shardZone**

- name 分片域名称。

**shard**

- name 分片名称，定义虚拟数据库的table时指定的分片来自于这里。
- dsGroup 数据源组。参考上一章节数据源组的定义。
- schema 物理数据库的schema，SQL改写之后携带的属主取自这里的定义。



### 物理数据库集群 PhysicalDBCluster

DDAL的物理数据库集群指的是oracle的rac或mysql的复制架构。

配置示例：

````xml
<physicalDBCluster name="cluster1" type="mysql">
  <dbInstance name="inst1" hostname="192.168.199.23" port="3306" sid="" serviceName="" rw="W" role="M0" status="O"/>
  <dbInstance name="inst2" hostname="192.168.199.23" port="3306" sid="" serviceName="" rw="R" role="M0" status="O"/>
</physicalDBCluster>
````

**physicalDBCluster**

- name 物理数据库集群名称
- type 数据库类型，目前支持mysql，oracle和h2。

**dbInstance**

- name 数据库实例名称
- hostname 数据库实例名称或者IP
- port 数据库实例端口
- sid Oracle数据库连接使用sid时使用
- serviceName Oracle数据库连接使用ServiceName时使用
- rw  *可选配置* 实例读写控制（R读W写D不可用）
- role *可选配置* 角色（M0 M1 S0 S1） 
- status *可选配置* 实例状态（O正常D离线R就绪）

### 数据源 DataSource

配置示例：

````xml
<dataSources>
  <group name="group_1" relaCluster="cluster1">
    <dataSource user="root" pwd="root123" dbInstName="inst1" dbDriver="jdbc">
      <params>
        <property name="minIdle" value="1"/>
        <property name="maxIdle" value="5"/>
        <property name="maxTotal" value="10"/>
      </params>
    </dataSource>
    <dataSource user="root" pwd="root123" dbInstName="inst2" dbDriver="jdbc">
      <params>
        <property name="minIdle" value="1"/>
        <property name="maxIdle" value="5"/>
        <property name="maxTotal" value="10"/>
      </params>
    </dataSource>
  </group>
</dataSources>
````

**group**

- name 数据源分组名称
- relaCluster 数据源关联的物理集群

**dataSource**

- user 数据库用户
- pwd 数据库用户密码
- dbInstName 数据库物理实例，是定义在relaCluster中的实例。
- dbDriver 物理连接驱动类型。jdbc表示使用jdbc驱动。

**params**

连接池参数参考文档：[后端物理连接池设计](http://git.oschina.net/f150/raptor-ddal/blob/develop/doc/DetailedDesign/Backend-Connection-Pool-Design-V2.md)



### 进阶配置

**单库分表配置片段**

shard-config.xml

```xml
<virtualDBs>
  <virtualDB name="crmdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <table name="test" actualTables="test_0,test_1,test_2,test_3" tableRule="id-database-rule"/>
  </virtualDB>
</virtualDBs>
<shardGroups>
  <shardGroup name="shardGroup1">
      <shard name="shard" dsGroup="group_1" schema="schema"/>
  </shardGroup>
</shardGroups>
```

rule-config.xml

```xml
<shardRules>
  <shardRule name="id-database-rule" shardColumn="id" param="4" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/>    
</shardRules>
```

> 分表场景下，一般只会有一个库，故shard只有一片，virtualDB上关联shardGroup后，内部所有表都分布在此分片上；单库分表只能解决单表过大的问题，并不能减小数据库实例的压力，建议有条件直接做分库设计；



**数据分库配置片段**

shard-config.xml

```xml
<virtualDBs>
  <virtualDB name="crmdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <table name="test" databaseRule="id-database-rule"/>
  </virtualDB>
</virtualDBs>
<shardGroups>
  <shardGroup name="shardGroup1">
    <shard name="shard_0" dsGroup="group_1" schema="schema0"/>
    <shard name="shard_1" dsGroup="group_1" schema="schema1"/>
    <shard name="shard_2" dsGroup="group_1" schema="schema2"/>
    <shard name="shard_3" dsGroup="group_1" schema="schema3"/>
  </shardGroup>
</shardGroups>
```

> 以上shard可以分布在一个实例上，也可以分布在不同的实例上；MySQL下以Schema进行数据隔离，Oracle下以用户进行数据隔离；

rule-config.xml

```xml
<shardRules>
  <shardRule name="id-database-rule" shardColumn="id" param="4" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/>    
</shardRules>
```

> 表分库可以做到真正意义上的数据水平切分，能够利用较为低廉的硬件设施满足大容量的数据存储与计算；



**分库分表同时存在时的配置片段**

在分表向分库的演进过程中，或某些特殊的使用场景下，会有分库与分表同时存在的可能性；以下便是示例配置，*<u>但不推荐此做法，容易造成概念混淆，以及带来运维上的不便</u>*；

shard-config.xml

```xml
<virtualDBs>
  <virtualDB name="crmdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <!-- 分库配置 -->
    <table name="test" databaseRule="id-database-rule"/>
    <!-- 单库分表, 需指定具体某一分片 -->
    <table name="test" shards="shard_0" tableRule="id-database-rule" actualTables="test_0,test_1,test_2,test_3"/>
    <!-- 分库分表，此场景极为特殊，不建议使用 -->
    <table name="test" databaseRule="id-database-rule" tableRule="num-database-rule" actualTables="test_0,test_1,test_2,test_3"/>
  </virtualDB>
</virtualDBs>
<shardGroups>
  <shardGroup name="shardGroup1">
    <shard name="shard_0" dsGroup="group_1" schema="schema0"/>
    <shard name="shard_1" dsGroup="group_1" schema="schema1"/>
    <shard name="shard_2" dsGroup="group_1" schema="schema2"/>
    <shard name="shard_3" dsGroup="group_1" schema="schema3"/>
  </shardGroup>
</shardGroups>
```

rule-config.xml

```xml
<shardRules>
  <shardRule name="id-database-rule" shardColumn="id" param="4" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/>  
  <shardRule name="num-database-rule" shardColumn="num" param="4" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/> 
</shardRules>
```