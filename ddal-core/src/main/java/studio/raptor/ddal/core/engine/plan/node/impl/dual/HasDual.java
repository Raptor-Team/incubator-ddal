package studio.raptor.ddal.core.engine.plan.node.impl.dual;

import java.util.Iterator;
import java.util.Set;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.constants.EngineConstants;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 判断SQL中有无虚拟表
 *
 * @author Charley
 * @since 1.0
 */
public class HasDual extends ForkingNode {



  @Override
  protected int judge(ProcessContext context) {
    return judge0(context.getParseResult()) ? 0 : 1;
  }

  private boolean judge0(ParseResult parseResult){
    //Oracle
    if(DatabaseType.Oracle.equals(parseResult.getDbType())){
      Set<String> tableNames = parseResult.getTableNames();
      Iterator<String> it = tableNames.iterator();
      if(tableNames.size() == 1 && EngineConstants.DUAL.equals(it.next())){
        return true;
      }
    }
    //Mysql
    //TODO
    return false;
  }
}
