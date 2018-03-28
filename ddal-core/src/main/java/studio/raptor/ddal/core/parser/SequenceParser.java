package studio.raptor.ddal.core.parser;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.SequenceWrapper;

/**
 * 序列解析器
 *
 * @author Charley
 * @since 1.0
 */
public class SequenceParser {

  private static final String SEQUENCE_PREFIX = "SELECT";
  private static final String SEQUENCE_SUFFIX = ".NEXTVAL";
  private static final int MIN_LENGTH = SEQUENCE_PREFIX.length() + SEQUENCE_SUFFIX.length() + 2;

  /**
   * 快速校验是否为中间件Sequence语法，select xxx.nextval;
   * @param sql
   * @return
   */
  public static boolean fastCheck(String sql){
    sql = sql.trim();
    //校验长度
    if(sql.length() < MIN_LENGTH){
      return false;
    }

    sql = sql.toUpperCase();
    //前后缀判断
    if(!sql.endsWith(SEQUENCE_SUFFIX) || !sql.startsWith(SEQUENCE_PREFIX)){
      return false;
    }
    return true;
  }

  public static ParseResult parse(DatabaseType dsType, String sql){
    List<String> results = new SequenceLexer(sql).lex();
    //不支持语法异常
    if(results.size() > 3){
      throw new UnsupportedOperationException("This sql is not ddal sequence grammar， SQL: "+sql);
    }
    ParseResult parseResult = new ParseResult(dsType);
    SequenceWrapper sequenceWrapper = new SequenceWrapper(results.get(1).toUpperCase());
    parseResult.setSequenceWrapper(sequenceWrapper);
    return parseResult;
  }

  private static class SequenceLexer{
    private final String sql;
    private final int sqlLength;
    private int index = 0;
    private char ch;

    public SequenceLexer(String sql) {
      this.sql = sql;
      this.sqlLength = sql.length();
    }

    public List<String> lex(){
      List<String> result = new ArrayList<>();
      for(;;){
        if(index >= sqlLength){
          break;
        }
        reader();
        if(' ' == ch){
          continue;
        }
        result.add(readString());
      }
      return result;
    }

    private void reader(){
      ch = sql.charAt(index++);
    }

    private String readString(){
      StringBuffer sb = new StringBuffer().append(ch);
      for(;;){
        if(index >= sqlLength){
          break;
        }
        reader();
        if(' ' == ch || '.' == ch){
          break;
        }
        sb.append(ch);
      }
      return sb.toString();
    }
  }
}
