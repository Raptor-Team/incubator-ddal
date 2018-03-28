package studio.raptor.ddal.core.connection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.junit.Test;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class BackendConnectionNoCloseTest /*extends PrepareH2TestingEnv*/ {

  /**
   * 执行语句任务
   */
  private class ExecuteTask implements Runnable {
    private final Connection connection;
    private final String sql = "update schema0.customer set name='张全蛋' where id = ?";

    public ExecuteTask(Connection connection){
      this.connection = connection;
    }

    @Override
    public void run(){
      System.out.println("ExecuteThread is running!");
      try(PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
        preparedStatement.setInt(1, 2);
        preparedStatement.executeUpdate();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("ExecuteTask is completed!");
    }
  }

  /**
   * 回滚任务
   */
  private class RollbackTask implements Runnable {
    private final Connection connection;

    public RollbackTask(Connection connection){
      this.connection = connection;
    }

    @Override
    public void run(){
      System.out.println("RollbackThread is running!");
      try {
        connection.rollback();
      } catch (Exception e) {
        e.printStackTrace();
      }
      System.out.println("RollbackTask is completed!");
    }
  }
}
