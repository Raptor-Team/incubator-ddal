package studio.raptor.ddal.dashboard.util;

import java.io.IOException;
import org.apache.curator.test.TestingServer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ZkServerFaker {

  protected static TestingServer server;


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
}
