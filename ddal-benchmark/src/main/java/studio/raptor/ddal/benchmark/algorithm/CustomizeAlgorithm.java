package studio.raptor.ddal.benchmark.algorithm;

import java.util.Collection;
import studio.raptor.ddal.benchmark.select.AlwaysReadStrategy;
import studio.raptor.ddal.common.algorithm.ShardValue;
import studio.raptor.ddal.common.algorithm.SingleKeyShardAlgorithm;
import studio.raptor.ddal.core.executor.strategy.ReadWriteStrategy;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class CustomizeAlgorithm implements SingleKeyShardAlgorithm<Comparable<?>> {

  private final int count;

  public CustomizeAlgorithm(String param) {
    this.count = Integer.parseInt(param);
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

  public static void main(String[] args) {
    System.out.println(ReadWriteStrategy.class.isAssignableFrom(AlwaysReadStrategy.class));
  }
}
