package studio.raptor.ddal.core.engine.plan.node.impl.index;

import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;

/**
 * 是否存在索引
 *
 * @author Charley
 * @since 1.0
 */
public class HasIndexInTable extends ForkingNode {

  @Override
  protected int judge(ProcessContext context) {
    return judge0(context) ? 0 : 1;
  }

  private boolean judge0(ProcessContext context) {
    //判断分片表是否有索引
    for (Table shardTable : context.getShardTables()) {
      if (shardTable.hasIndex()) {
        return true;
      }
    }
    return false;
  }
}
