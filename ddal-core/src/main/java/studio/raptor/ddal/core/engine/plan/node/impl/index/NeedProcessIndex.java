package studio.raptor.ddal.core.engine.plan.node.impl.index;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 是否存在索引
 *
 * @author Charley
 * @since 1.0
 */
public class NeedProcessIndex extends ForkingNode{

  @Override
  protected int judge(ProcessContext context) {
    return judge0(context) ? 0 : 1;
  }

  private boolean judge0(ProcessContext context) {
    //判断分片表是否有索引
    List<Table> indexTables = new ArrayList<>();
    for(Table shardTable : context.getShardTables()){
      if(shardTable.hasIndex()){
        indexTables.add(shardTable);
      }
    }
    if(indexTables.isEmpty()){
      return false;
    }

    //判断有无索引条件
    ParseResult parseResult = context.getParseResult();
    for(Table indexTable : indexTables){
      int indexCount = 0;
      for(String indexColumn : indexTable.getIndexColumn().keySet()){
        if(parseResult.containsColumn(indexTable.getName(), indexColumn)){
          indexCount++;
        }
      }
      if(indexCount == 1){
        return true;
      }
    }

    return false;
  }
}
