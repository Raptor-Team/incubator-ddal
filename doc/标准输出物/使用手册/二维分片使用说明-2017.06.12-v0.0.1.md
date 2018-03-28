为应对较为复杂的分片切分场景，DDAL引入多维分片概念，理论分析详见[多维分片理论分析](../../详细设计/DD-多维分片理论分析与设计-2017.05.26-v0.0.1.md)；目前DDAL支撑到二维分片，更高维度的场景暂不支持。

DDAL分片数量**强烈建议**使用2的N次方片，如（2、4、8、16...）；



### 二维分片配置说明

*shard-config.xml片段*

````xml
<!-- 虚拟DB配置 -->
<virtualDBs>
  <virtualDB name="crmdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <table name="multi_shard_table" databaseRule="multi-shard-database-rule"/>
  </virtualDB>
</virtualDBs>
<!-- 分片组配置 -->
<shardGroups>
  <shardGroup name="shardGroup1">
    <shard name="shard_0" dsGroup="group_1" schema="ddal_test_0"/>
    <shard name="shard_1" dsGroup="group_1" schema="ddal_test_1"/>
    <shard name="shard_2" dsGroup="group_1" schema="ddal_test_2"/>
    <shard name="shard_3" dsGroup="group_1" schema="ddal_test_3"/>
  </shardGroup>
</shardGroups>
````

*rule-config.xml片段*

```xml
<!-- 
	说明：
 	① 二维分片的两个字段填入shardColumn中，需要注意的是，两个字段在前的是主分片字段、在后的为次分片字段，定义好之后不能顺序修改，否则会造成分片混乱；
	② param值表示分片算法所传入的参数，以取模为例，当主次字段都按2取模，最终计算出的分片为4个，具体计算公式参照理论分析文档；
	③ algorithm表示分片算法，可自定义；
-->
<shardRules>
  <shardRule name="multi-shard-database-rule" shardColumn="primary_key,second_key" param="2" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModMultiKeyAlgorithm"/>
</shardRules>
```



### CRM推荐配置

**CRM业务推荐使用DefaultHashRangeMultiKeyShardAlgorithm算法**

*shard-config.xml片段*

```xml
<!-- 虚拟DB配置 -->
<virtualDBs>
  <virtualDB name="crmdb" shardGroup="shardGroup1" rmOwner="true" sqlMaxLimit="-1">
    <table name="multi_shard_table" databaseRule="multi-shard-database-rule"/>
  </virtualDB>
</virtualDBs>
<!-- 分片组配置 -->
<shardGroups>
  <shardGroup name="shardGroup1">
    <shard name="shard_0" dsGroup="group_1" schema="ddal_test_0"/>
    <shard name="shard_1" dsGroup="group_1" schema="ddal_test_1"/>
    <shard name="shard_2" dsGroup="group_1" schema="ddal_test_2"/>
    <shard name="shard_3" dsGroup="group_1" schema="ddal_test_3"/>
    <shard name="shard_4" dsGroup="group_1" schema="ddal_test_4"/>
    <shard name="shard_5" dsGroup="group_1" schema="ddal_test_5"/>
    <shard name="shard_6" dsGroup="group_1" schema="ddal_test_6"/>
    <shard name="shard_7" dsGroup="group_1" schema="ddal_test_7"/>
    <shard name="shard_8" dsGroup="group_1" schema="ddal_test_8"/>
    <shard name="shard_9" dsGroup="group_1" schema="ddal_test_9"/>
    <shard name="shard_10" dsGroup="group_1" schema="ddal_test_10"/>
    <shard name="shard_11" dsGroup="group_1" schema="ddal_test_11"/>
    <shard name="shard_12" dsGroup="group_1" schema="ddal_test_12"/>
    <shard name="shard_13" dsGroup="group_1" schema="ddal_test_13"/>
    <shard name="shard_14" dsGroup="group_1" schema="ddal_test_14"/>
    <shard name="shard_15" dsGroup="group_1" schema="ddal_test_15"/>
  </shardGroup>
</shardGroups>
```

*rule-config.xml片段*

```xml
<!-- 
	说明：
 	① 二维分片的两个字段填入shardColumn中，需要注意的是，两个字段在前的是主分片字段、在后的为次分片字段，定义好之后不能顺序修改，否则会造成分片混乱；
	② param值表示分片算法所传入的参数，HashRange算法为配置文件名；
	③ algorithm表示分片算法，一是能够时数据均匀分布在个分片上，二是方便数据迁移；
-->
<shardRules>
  <shardRule name="multi-shard-database-rule" shardColumn="primary_key,second_key" param="hash-range.properties" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultHashRangeMultiKeyShardAlgorithm"/>
</shardRules>
```



hash-range.properties片段*

```properties
#分片字段mod值
mod=2
#范围分片
range=[0-99,100-199],[0-999,1000-1999]
```