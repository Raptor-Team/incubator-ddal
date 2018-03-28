package studio.raptor.ddal.core.engine.plan.node.impl.dual;

import java.util.Collections;
import java.util.List;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.constants.EngineConstants;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.router.result.FullRouteResult;
import studio.raptor.ddal.core.router.result.RouteNode;
import studio.raptor.ddal.core.router.result.RouteResult;

/**
 * 处理虚拟表
 *
 * @author Charley
 * @since 1.0
 */
public class HandleDual extends ProcessNode{

  @Override
  protected void execute(ProcessContext context) {
    VirtualDb virtualDb = context.getVirtualDb();

    RouteResult routeResult = new FullRouteResult();
    routeResult.setShards(virtualDb.getShards());
    routeResult.setDbShard(Collections.singleton(getShard(virtualDb)));
    routeResult.setParameters(context.getSqlParameters());
    RouteNode routeNode = routeResult.getRouteNodes().get(0);
    routeNode.setFinalSql(context.getOriginSql());
    context.setRouteResult(routeResult);
  }

  private String getShard(VirtualDb virtualDb){
    Table table = virtualDb.getTable(EngineConstants.DUAL);
    List<String> shardNames = table.getDatabaseShards();
    if(null == shardNames || shardNames.size() != 1){
      throw new UnsupportedOperationException("Dual operation must point one shard!");
    }

    return shardNames.get(0);
  }
}
