package studio.raptor.ddal.tests.algorithmn;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 单分片表测试类
 *
 * @author Charley
 * @since 1.0
 */
public class SubstringAlgoTest extends AutoPrepareTestingEnv {
  /**
   * 单分片Select
   */
  @Test
  public void testSubStringShard() {
    String sql = "select * from substring_test where id = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, "32012119901232");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("32012119901232", resultSet.getString(1));
        Assert.assertEquals("0", resultSet.getString(2));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
