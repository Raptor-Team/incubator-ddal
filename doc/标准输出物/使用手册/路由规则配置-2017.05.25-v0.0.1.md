## 路由规则配置

Raptor-ddal内置了单分片字段取模算法，哈希范围算法；单分片取模算法能够满足大部分业务场景需求；



### 路由算法默认实现

*单分片取模算法使用案例*

```xml
<ruleConfig xmlns="http://ddal.raptor.studio/rule-config"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://ddal.raptor.studio/rule-config">
  <shardRules>
    <shardRule name="id-rule" shardColumn="id" param="4"
      algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm"/>
  </shardRules>
</ruleConfig>
```

> 如上述配置，需在shardRule的algorithm属性上指定`studio.raptor.ddal.common.algorithm.embed.DefaultModSingleKeyAlgorithm`类名；



*哈希范围算法使用案例*

rule-config.xml

```xml
<ruleConfig xmlns="http://ddal.raptor.studio/rule-config"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://ddal.raptor.studio/rule-config">
  <shardRules>
    <shardRule name="id-rule" shardColumn="id" algorithm="studio.raptor.ddal.common.algorithm.embed.DefaultHashRangeSingleKeyShardAlgorithm"/>
  </shardRules>
</ruleConfig>
```

同时指定规则文件hash-range.properties

```properties
mod=2
range=1-100,101-200,201-300
```



### 路由算法自定义实现

若需自定义单分片路由算法，则需实现`SingleKeyShardAlgorithm`接口，接口详情如下：

SingleKeyShardAlgorithm.java

```java
public interface SingleKeyShardAlgorithm<T extends Comparable<?>> extends ShardAlgorithm {
    /**
     * 根据分片值和SQL的=运算符计算分片结果名称集合.
     *
     * @param shards     所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValue 分片值
     * @return 分片后指向的目标名称, 一般是数据源或表名称
     */
    String doEqual(Collection<String> shards, ShardValue<T> shardValue);

    /**
     * 根据分片值和SQL的IN运算符计算分片结果名称集合.
     *
     * @param shards     所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValue 分片值
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    Collection<String> doIn(Collection<String> shards, ShardValue<T> shardValue);

    /**
     * 根据分片值和SQL的BETWEEN运算符计算分片结果名称集合.
     *
     * @param shards     所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValue 分片值
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    Collection<String> doBetween(Collection<String> shards, ShardValue<T> shardValue);
}
```

基于以上接口进行自定义实现，示例如下：

CustomizedSingleKeyShardAlgorithm.java

```java
public class CustomizedSingleKeyShardAlgorithm implements SingleKeyShardAlgorithm<Comparable<?>> {

  private final int count;

  public CustomizeAlgorithm(String param) {
    this.count = Integer.valueOf(param);
  }

  /**
   * 根据分片值和SQL的=运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称, 一般是数据源或表名称
   */
  @Override
  public String doEqual(Collection<String> shards, ShardValue<Comparable<?>> shardValue) {
    String value = String.valueOf(shardValue.getValue()).substring(0,1);
    for (String shard : shards) {
      if (shard.endsWith(Long.parseLong(value) % count + "")) {
        return shard;
      }
    }
    throw new UnsupportedOperationException();
  }

  /**
   * 根据分片值和SQL的IN运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称集合, 一般是数据源或表名称
   */
  @Override
  public Collection<String> doIn(Collection<String> shards, ShardValue<Comparable<?>> shardValue) {
    return null;
  }

  /**
   * 根据分片值和SQL的BETWEEN运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称集合, 一般是数据源或表名称
   */
  @Override
  public Collection<String> doBetween(Collection<String> shards,
      ShardValue<Comparable<?>> shardValue) {
    return null;
  }
}
```

rule-config.xml

```xml
<ruleConfig xmlns="http://ddal.raptor.studio/rule-config"
  xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://ddal.raptor.studio/rule-config">
  <shardRules>
    <shardRule name="id-database-rule" shardColumn="id" param="4"
 algorithm="studio.raptor.ddal.benchmark.algorithm.CustomizedSingleKeyShardAlgorithm"/>
  </shardRules>
</ruleConfig>
```



若需自定义多分片路由算法，则需实现`MultiKeyShardAlgorithm`接口，接口详情如下：

MultiKeyShardAlgorithm.java

```java
/**
 * 多片键分片法接口.
 *
 * @author Charley
 * @since 1.0
 */
public interface MultiKeyShardAlgorithm<T extends Comparable<?>> extends ShardAlgorithm {

    /**
     * 根据分片值计算分片结果名称集合.
     *
     * @param availableTargetNames 所有的可用目标名称集合, 一般是数据源或表名称
     * @param shardValues          分片值集合
     * @return 分片后指向的目标名称集合, 一般是数据源或表名称
     */
    Collection<String> shard(Collection<String> availableTargetNames, Collection<ShardValue<T>> shardValues);
}
```

自定义实现参照单分片路由算法即可，这里只需实现shard方法；