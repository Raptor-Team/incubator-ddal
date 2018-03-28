package studio.raptor.ddal.tests.algorithmn;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class AreaTest extends AutoPrepareTestingEnv {
  /**
   * 单分片Insert
   */
  @Test
  public void testInsertWithSingleShard() {
    String sql = "insert into aglori_test (`id`, `name`) values (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, 2);
      preparedStatement.setString(2, "王城");
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
