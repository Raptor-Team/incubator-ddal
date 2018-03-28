package studio.raptor.ddal.tests.ddl;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 单分片表测试类
 *
 * @author Charley
 * @since 1.0
 */
public class CreateCommandTest extends AutoPrepareTestingEnv {
  /**
   * 单分片Select
   */
  @Test
  public void testCreateTable() {
    String createSql = "CREATE TABLE ddl_create_table ( `cno` int(6) UNSIGNED not null COMMENT '课程编号')";
    try (Statement statement = connection.createStatement()) {
      boolean result = statement.execute(createSql);
      Assert.assertTrue(result);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String insertSql = "insert into ddl_create_table (cno) values (?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {
      preparedStatement.setInt(1, 20171116);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(4, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }

    String selectSql = "select * from ddl_create_table";
    try (PreparedStatement preparedStatement = connection.prepareStatement(selectSql);
      ResultSet resultSet = preparedStatement.executeQuery()) {
        Assert.assertTrue(resultSet.next());
        Assert.assertEquals(20171116, resultSet.getInt(1));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
