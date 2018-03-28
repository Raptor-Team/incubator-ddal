package studio.raptor.ddal.core.parser;

import org.junit.Test;
import studio.raptor.ddal.core.constants.DatabaseType;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class SequenceParserTest {

  @Test
  public void testLexer(){
    String sql = "select xxx.nextval";
    SequenceParser.fastCheck(sql);
    SequenceParser.parse(DatabaseType.MySQL, sql);
  }
}
