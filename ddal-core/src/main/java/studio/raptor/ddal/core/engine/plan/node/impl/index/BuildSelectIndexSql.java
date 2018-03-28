package studio.raptor.ddal.core.engine.plan.node.impl.index;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import studio.raptor.ddal.config.model.shard.Index;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.core.constants.EngineConstants;
import studio.raptor.ddal.core.engine.IndexTableOps;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.parser.builder.SqlBuilder;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 构造Select语句Index执行SQL
 *
 * @author Charley
 * @since 1.0
 */
public class BuildSelectIndexSql extends ProcessNode{

  @Override
  protected void execute(ProcessContext context) {
    Map<String, Index> indexTables = getIndexTable(context);
    IndexTableOps indexTableOps = assembleIndexOps(context.getSqlBuilder(), context.getSqlParameters(), indexTables);
    context.getIndexTableOpsList().add(indexTableOps);
  }

  /**
   * 获取需要处理的索引信息
   *
   * @param context
   * @return
   */
  private Map<String, Index> getIndexTable(ProcessContext context){
    Map<String, Index> indexTables = new HashMap<>();
    ParseResult parseResult = context.getParseResult();
    for(Table shardTable : context.getShardTables()){
      if(shardTable.hasIndex()){
        int indexCount = 0;
        Index index = null;
        for(String indexColumn : shardTable.getIndexColumn().keySet()){
          if(parseResult.containsColumn(shardTable.getName(), indexColumn)){
            index = shardTable.getIndexColumn().get(indexColumn);
            indexCount++;
          }
        }
        if(indexCount == 1){
          indexTables.put(shardTable.getName(), index);
        }
      }
    }
    return indexTables;
  }

  private IndexTableOps assembleIndexOps(SqlBuilder sqlBuilder, List<Object> parameters,  Map<String, Index> indexTables){
    Map.Entry<String, Index> entry = null;
    Iterator<Entry<String, Index>> iterator = indexTables.entrySet().iterator();
    while(iterator.hasNext()){
      entry = iterator.next();
    }
    sqlBuilder.rewriteTable(entry.getKey());
//    String sql = sqlBuilder.toString();
    String sql = sqlBuilder.toStringForIndexTable();
    sql = sql.replaceAll(String.format(
        EngineConstants.REWRITE_PH_TBL_TEMPLATE,
        EngineConstants.REWRITE_PH_TBL_PREFIX, entry.getKey(), EngineConstants.REWRITE_PH_TBL_SUFFIX),
        entry.getValue().getName());
    IndexTableOps ops = new IndexTableOps();
    ops.getParameters().addAll(parameters);
    ops.setIdxSql(sql);
    return ops;
  }
}
