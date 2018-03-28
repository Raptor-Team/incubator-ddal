## 配置通配符设计与实现

当同样配置的表过多时，为简化配置，引入通配符概念；目前通配符仅支持以下两种场景：

- 全局表通配
- 分片数为1的分片表



### 配置格式

```xml
  <virtualDBs>
    <virtualDB name="vdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
      <table name="teacher" databaseRule="teacher-database-rule"/>

      <!-- 全局表通配符配置，需要指定表type="global" -->
      <table name="*" type="global"/>
      
      <!-- 分片为1的表通配符配置，需要指定shards，当virtualDB关联edshardGroup为一片时可不填 -->
      <table name="shard_*" shards="shard_0"/>
    </virtualDB>
  </virtualDBs>
```

> **※特别注意※**
>
> `<table name="*" type="global"/>`与`<table name="*" shards="shard_0"/>`两条配置**不能同时**使用；



### 配置示例

*全局表*

```xml
<!-- 全局通配，配置中不存在的表都使用此配置，存在此配置，其他则失效 -->
<table name="*" type="global"/>

<!-- 前缀匹配，表名以此前缀开头使用此配置 -->
<table name="prefix_*" type="global"/>

<!-- 后缀匹配，表名以此后缀开头使用此配置 -->
<table name="*_suffix" type="global"/>

<!-- 中间匹配，表名以此前后缀开头使用此配置 -->
<table name="prefix_*_suffix" type="global"/>

<!-- 指定作用分片 -->
<table name="*" shards="shard_0,shard_1" type="global"/>
```
*分片表*

```xml
<!-- 全局通配，配置中不存在的表都使用此配置，存在此配置，其他则失效 -->
<table name="*" shards="shard_0"/>

<!-- 前缀匹配，表名以此前缀开头使用此配置 -->
<table name="prefix_*" shards="shard_0"/>

<!-- 后缀匹配，表名以此后缀开头使用此配置 -->
<table name="*_suffix" shards="shard_0"/>

<!-- 中间匹配，表名以此前后缀开头使用此配置 -->
<table name="prefix_*_suffix" shards="shard_0"/>

<!-- 当virtualDB关联的shardGroup分片数为1时，分片表通配可不指定shards -->
<virtualDB name="vdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
	<table name="*"/>
</virtualDB>
<shardGroup name="shardGroup1">
  <shard name="shard_0" dsGroup="group_1" schema="ddal_test_0"/>
</shardGroup>
```

> **※再次强调※**
>
> `<table name="*" type="global"/>`与`<table name="*" shards="shard_0"/>`两条配置**不能同时**使用；