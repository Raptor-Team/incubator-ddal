package studio.raptor.ddal.tests.wildcard;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 通配符测试
 *
 * @author Charley
 * @since 1.0
 */
public class WildcardTest extends AutoPrepareTestingEnv {

  @Test
  public void testPrefixWildcard(){
    String sql = "insert into wild_test (cno, cname, tno) values (?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, 112621);
      preparedStatement.setString(2, "Sport");
      preparedStatement.setInt(3, 2012112610);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(4, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testSuffixWildcard(){
    String sql = "insert into test_wild (cno, cname, tno) values (?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, 112621);
      preparedStatement.setString(2, "Sport");
      preparedStatement.setInt(3, 2012112610);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(4, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWildcard(){
    String sql = "insert into wildcard_test (cno, cname, tno) values (?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, 112621);
      preparedStatement.setString(2, "Sport");
      preparedStatement.setInt(3, 2012112610);
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(4, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
