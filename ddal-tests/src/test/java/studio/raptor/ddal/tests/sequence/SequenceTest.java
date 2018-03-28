package studio.raptor.ddal.tests.sequence;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.curator.test.TestingServer;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import studio.raptor.ddal.tests.AutoPrepareTestingEnv;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class SequenceTest extends AutoPrepareTestingEnv {

  private static volatile Long compared = 0L;
  private static ReentrantLock lock = new ReentrantLock();

  private static TestingServer server;

  @BeforeClass
  public static void beforeClass() {
    try {
      server = new TestingServer(2181);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @AfterClass
  public static void afterClass() {
    try {
      server.stop();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetId() {
    String sql = "select auto_key_seq.nextval";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery()) {
      Assert.assertTrue(resultSet.next());
      System.out.println(resultSet.getLong(1));
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testGetIdConcurrently(){
    int concurrent = 4;
    final CountDownLatch latch = new CountDownLatch(concurrent);
    try {
      for(int i=0; i<concurrent; i++){
        Thread thread = new Thread(new Task(latch));
        thread.start();
      }
      latch.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public static class Task implements Runnable{
    private static final String sql = "select auto_key_seq.nextval";
    private final CountDownLatch latch;
    public Task(CountDownLatch latch) {
      this.latch = latch;
    }

    @Override
    public void run() {
      try(Connection myConnection = dataSourceRouter.route("school").getConnection()){
        for(int i=0; i<10; i++){
          try (
              PreparedStatement preparedStatement = myConnection.prepareStatement(sql);
              ResultSet resultSet = preparedStatement.executeQuery()) {
            Assert.assertTrue(resultSet.next());
            Assert.assertTrue(resultSet.getLong(1) != compared);
            lock.lock();
            compared = resultSet.getLong(1);
            System.out.println(compared);
            lock.unlock();
          }
        }
      }catch (Exception e) {
        e.printStackTrace();
      }finally {
        latch.countDown();
      }
    }
  }

  @Test
  public void testAutoIncrement(){
    AtomicInteger id = new AtomicInteger(0);
    String sql = "insert into auto_increment (id, name) values (?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id.incrementAndGet());
      preparedStatement.setString(2, "AutoIncrement");
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void testWithoutAutoIncrement(){
    AtomicInteger id = new AtomicInteger(0);
    String sql = "insert into auto_increment (auto_key, id, name) values (?, ?, ?)";
    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setInt(1, id.incrementAndGet());
      preparedStatement.setInt(2, id.incrementAndGet());
      preparedStatement.setString(3, "AutoIncrement");
      int affectedRows = preparedStatement.executeUpdate();
      Assert.assertEquals(1, affectedRows);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
