### 哈希和范围分片配置

- 哈希和范围分片配置(hash-range.properties)

  ````properties
  mod=1
  range=1-10,11-20
  ````

  mod表示数据中心的个数，数据中心编号从0开始，0,1,2...mod-1。

  range是分片字段值的范围定义，`1-100`表示在[1,100]之间的分片值落在同一个分片。1-10的索引是0，11-20的索引是1，与数据的下标类似。定义分片名称时会使用range的下标。


- shard-config.xml配置

  ````xml
  <virtualDBs>
      <virtualDB name="crmdb" rmOwner="true" sqlMaxLimit="-1">
          <table name="customer" shardingColumn="id" rule="studio.raptor.ddal.common.algorithm.emebed.DefaultHashRangeSingleKeyShardAlgorithm"/>
          <table name="account" shardingColumn="id" rule="studio.raptor.ddal.common.algorithm.emebed.DefaultHashRangeSingleKeyShardAlgorithm"/>
      </virtualDB>
  </virtualDBs>

  <shardingGroups>
      <shardings>
          <shard name="shard00" datasourceGroup="group_1" schema="schema0"/>
          <shard name="shard01" datasourceGroup="group_1" schema="schema1"/>
      </shardings>
      <shardingGroup name="shardingGroup1" scope="shard00,shard01">
          <vdb>crmdb</vdb>
      </shardingGroup>
  </shardingGroups>
  ````

  table必须指定路由规则rule，rule可自定义。

  二级路由的分片名称命名规则是：自定义分片标识符+modValue+rangeIndex。shard00 = shard + 0 + 0，表示hash(id)=0并且 1<= id <= 10。

  ### 算法实现

  二级路由是指经过hash和范围分片两阶段计算确定最终数据分片的一种算法。

  自定义实现的二级路由算法需要实现 `HashRangeSingleKeyShardAlgorithm` 的两个接口。

  ```
  studio.raptor.ddal.common.algorithm.HashRangeSingleKeyShardAlgorithm#hashShardValue
  studio.raptor.ddal.common.algorithm.HashRangeSingleKeyShardAlgorithm#rangeShardValue
  ```

  其中hashShardValue方法计算数据中心，rangeShardValue范围分片计算数据中心内部的物理分片。

  DDAL内置了二级路由算法的实现，可参考以及自定义路由算法

  ```java
  public class DefaultHashRangeSingleKeyShardAlgorithm extends HashRangeSingleKeyShardAlgorithm<String> {

      @Override
      public int hashShardValue(String shardValue) {
          return hash(shardValue) % getModValue();
      }

      @Override
      public int rangeShardValue(String shardValue) {
          int i = 0;
          for (Range range : getRanges()) {
              if (range.inRange(Integer.parseInt(shardValue))) {
                  return i;
              }
              i++;
          }
          throw new RuntimeException("No range found for " + shardValue);
      }

      private int hash(Object key) {
          int h;
          return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
      }
  }
  ```