package studio.raptor.ddal.core.security;

import java.util.HashSet;
import java.util.Set;
import studio.raptor.ddal.config.model.shard.Table;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * SQL语法限制器
 *
 * @author Charley
 * @since 1.0
 */
public class GrammarLimiter {

  public static void check(ParseResult parseResult, VirtualDb virtualDb) throws RuntimeException{
    switch(parseResult.getSqlType()){
      case UPDATE:
        //校验Update语句的Set字段是否包含分片或索引字段
        if(hasFocusedColumnInUpdate(parseResult, virtualDb)){
          //TODO 异常处理
          throw new RuntimeException("Shard column or index column can't be updated.");
        }
        break;
      default:
        break;
    }
  }

  /**
   * update语法校验
   *
   * 校验Update语句的Set字段是否包含分片字段及索引字段
   *
   * @param parseResult
   * @param virtualDb
   * @return  若存在则返回true，不存在返回false
   */
  public static boolean hasFocusedColumnInUpdate(ParseResult parseResult, VirtualDb virtualDb){
    String tableName = parseResult.getTableNames().iterator().next();
    Table table = virtualDb.getTable(tableName);
    Set<String> focusedColumns = new HashSet<>();
    //分片字段
    if(table.getShardColumns().length > 0){
      for(String column : table.getShardColumns()){
        focusedColumns.add(column);
      }
    }
    //索引字段
    if(table.hasIndex()){
      for(String column : table.getIndexColumn().keySet()){
        focusedColumns.add(column);
      }
    }
    //判断更新字段是够包含关注字段
    for(String assignment : parseResult.getUpdateItems()){
      for(String focusedColumn : focusedColumns){
        if(focusedColumn.equals(assignment.toUpperCase())){
          return true;
        }
      }
    }
    return false;
  }
}
