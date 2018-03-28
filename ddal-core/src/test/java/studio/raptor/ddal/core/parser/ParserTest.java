package studio.raptor.ddal.core.parser;

import org.junit.Test;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ParserTest {
  @Test
  public void testStringPerf(){
    String sql = "select xxx.nextval from dual";
    String flag = ".NEXTVAL";
    long count = 10000000;
    long start1 = System.currentTimeMillis();
    for(long i=0; i< count; i++){
      sql.toUpperCase().contains(flag);
    }
    System.out.println(System.currentTimeMillis() - start1);

    long start2 = System.currentTimeMillis();
    for(long i=0; i< count; i++){
      sql.toUpperCase().indexOf(flag);
    }
    System.out.println(System.currentTimeMillis() - start2);
  }

  @Test
  public void testParseSequence(){
    String sql = "select     seq_test.nextval    ";
    ParseResult parseResult = Parser.parse(DatabaseType.MySQL, sql);
    parseResult.getSequenceWrapper();
  }
}
