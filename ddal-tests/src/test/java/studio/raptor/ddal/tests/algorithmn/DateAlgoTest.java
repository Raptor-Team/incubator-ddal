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
public class DateAlgoTest extends AutoPrepareTestingEnv {
  /**
   * 单分片Select
   */
  @Test
  public void testDateShard() {
    String sql = "select id,time_string from time_test where time_string = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, "2017-07-31");
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals("1", resultSet.getString(1));
        Assert.assertEquals("2017-07-31", resultSet.getString(2));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
