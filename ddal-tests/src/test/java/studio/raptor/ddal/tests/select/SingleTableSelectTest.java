package studio.raptor.ddal.tests.select;

import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 单分片表测试类
 *
 * @author Charley
 * @since 1.0
 */
public class SingleTableSelectTest extends AutoPrepareTestingEnv {
  /**
   * 单分片Select
   */
  @Test
  public void testSelectWithSingleShard() {
//    String sql = "select * from single_table where cno = ?";
//    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
//      preparedStatement.setLong(1, 112602);
//      try (ResultSet resultSet = preparedStatement.executeQuery()) {
//        Assert.assertTrue(resultSet.next());
//        Assert.assertEquals(112602, resultSet.getInt(1));
//        Assert.assertEquals("Data Structure", resultSet.getString(2));
//        Assert.assertEquals("2012112610", resultSet.getString(3));
//      }
//    } catch (SQLException e) {
//      e.printStackTrace();
//    }
  }
}
