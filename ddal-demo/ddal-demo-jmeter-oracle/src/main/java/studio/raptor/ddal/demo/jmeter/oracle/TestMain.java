package studio.raptor.ddal.demo.jmeter.oracle;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class TestMain {

  private static Logger logger = LoggerFactory.getLogger(TestMain.class);
  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());
  private static final DataSource dataSource = new RaptorDataSource("school", "oracle");


  private static ExecutorService executorService = Executors.newFixedThreadPool(256);

  public void runTest(){
    for(int i=0; i<256; i++){
      TestTask task = new TestTask(ID, dataSource);
      executorService.submit(task);
    }

    executorService.shutdown();
  }
  public static void main(String[] args) {
    new TestMain().runTest();
  }
}
