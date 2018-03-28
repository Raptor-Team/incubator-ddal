package studio.raptor.ddal.tests.algorithmn;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import studio.raptor.ddal.common.algorithm.ShardValue;
import studio.raptor.ddal.common.algorithm.SingleKeyShardAlgorithm;

/**
 * 字符串算法
 *
 * @author Charley
 * @since 1.0
 */
public class SubstringAlgorithm implements SingleKeyShardAlgorithm<String> {

  private Map<String, String> map = new HashMap<>();

  public SubstringAlgorithm() {
    map.put("320", "shard_0");
    map.put("321", "shard_1");
  }

  /**
   * 根据分片值和SQL的=运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称, 一般是数据源或表名称
   */
  @Override
  public String doEqual(Collection<String> shards, ShardValue<String> shardValue) {
    String value = shardValue.getValue();
    String key = value.substring(0,3);
    return map.get(key);
  }

  /**
   * 根据分片值和SQL的IN运算符计算分片结果名称集合.
   *
   * @param shards 所有的可用目标名称集合, 一般是数据源或表名称
   * @param shardValue 分片值
   * @return 分片后指向的目标名称集合, 一般是数据源或表名称
   */
  @Override
  public Collection<String> doIn(Collection<String> shards, ShardValue<String> shardValue) {
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
  public Collection<String> doBetween(Collection<String> shards, ShardValue<String> shardValue) {
    return null;
  }
}
