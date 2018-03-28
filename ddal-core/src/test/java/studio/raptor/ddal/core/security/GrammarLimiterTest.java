package studio.raptor.ddal.core.security;

import org.junit.Test;
import studio.raptor.ddal.config.config.ShardConfig;
import studio.raptor.ddal.config.model.shard.VirtualDb;
import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.Parser;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class GrammarLimiterTest {
  @Test(expected = RuntimeException.class)
  public void testUpdateItemVerify(){
    String sql = "update customer set id = 1000 where name = 'charley'";
    ParseResult parseResult = Parser.parse(DatabaseType.MySQL, sql);
    VirtualDb virtualDb = ShardConfig.getInstance().getVirtualDb("crmdb");

    GrammarLimiter.check(parseResult, virtualDb);
  }
}
