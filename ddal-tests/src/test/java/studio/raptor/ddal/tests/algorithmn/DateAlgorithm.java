package studio.raptor.ddal.tests.algorithmn;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import studio.raptor.ddal.common.algorithm.ShardValue;
import studio.raptor.ddal.common.algorithm.SingleKeyShardAlgorithm;

/**
 * 字符串算法
 *
 * @author Charley
 * @since 1.0
 */
public class DateAlgorithm implements SingleKeyShardAlgorithm<String> {

  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
  private long baseNum;

  public DateAlgorithm() {
    try{
      this.baseNum = formatter.parse("2017-08-01").getTime();
    }catch(Exception e){
      throw new RuntimeException("Algorithm error");
    }
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
    try{
      String value = shardValue.getValue();
      Date date = formatter.parse(value);
      Long dateNum = date.getTime();
      if(dateNum<baseNum){
        return "shard_0";
      }else{
        return "shard_1";
      }
    }catch(Exception e){
      throw new RuntimeException("Algorithm error");
    }
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
