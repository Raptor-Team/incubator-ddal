package studio.raptor.ddal.tests.select;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 库内分表测试
 *
 * @author Charley
 * @since 1.0
 */
public class DistributionOnShardTableTest extends AutoPrepareTestingEnv {

  @Test
  public void testDistribution() {
    String sql = "select * from table_shard_test where id = ? and code = ?";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setLong(1, 3);
      preparedStatement.setLong(2, 1);
      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        ResultSetMetaData metaData = resultSet.getMetaData();
        Assert.assertTrue(metaData.getSchemaName(1).endsWith("1"));
        Assert.assertTrue(metaData.getTableName(1).endsWith("1"));
        System.out.println("执行库为："+metaData.getSchemaName(1));
        System.out.println("执行表为："+metaData.getTableName(1));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
