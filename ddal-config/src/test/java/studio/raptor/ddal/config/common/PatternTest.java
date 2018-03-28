package studio.raptor.ddal.config.common;

import java.util.regex.Pattern;
import org.junit.Assert;
import org.junit.Test;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class PatternTest {

  @Test
  public void testPattern(){
    String pattern1 = ".*";
    String pattern2 = "prefix_.*";
    String pattern3 = ".*_suffix";
    String pattern4 = "prefix_.*_suffix";
    String pattern5 = "prefix_.*_middle_.*_suffix";

    Assert.assertTrue(Pattern.matches(pattern1, "table"));
    Assert.assertTrue(Pattern.matches(pattern1, "wildcard"));

    Assert.assertFalse(Pattern.matches(pattern2, "table"));
    Assert.assertTrue(Pattern.matches(pattern2, "prefix_table"));

    Assert.assertFalse(Pattern.matches(pattern3, "table"));
    Assert.assertTrue(Pattern.matches(pattern3, "table_suffix"));

    Assert.assertFalse(Pattern.matches(pattern4, "table"));
    Assert.assertTrue(Pattern.matches(pattern4, "prefix_table_suffix"));

    Assert.assertFalse(Pattern.matches(pattern5, "table"));
    Assert.assertTrue(Pattern.matches(pattern5, "prefix_table1_middle_table2_suffix"));
  }
}
