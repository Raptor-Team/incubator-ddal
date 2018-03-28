package studio.raptor.ddal.dashboard.common;

import java.sql.Timestamp;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.DiscardPolicy;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sam
 * @since 3.1.0
 */
public class ThreadExecutor {

  private static Logger log = LoggerFactory.getLogger(ThreadExecutor.class);

  //private static Map<String, String> oldGenStore = new ConcurrentHashMap<>();

  private static BlockingQueue<Runnable> queue = new ArrayBlockingQueue<>(5);
  private static Executor executor = new ThreadPoolExecutor(2, 4, 5, TimeUnit.SECONDS,
      queue,
      new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
          Thread t = new Thread(r);
          t.setDaemon(true);
          return t;
        }
      },
      new DiscardPolicy()
  );

  public static void execute(Runnable task) {
    executor.execute(task);
  }

  public static class TaskObj {

    private final String content;

    public TaskObj(String content) {
      this.content = content;
    }
  }

  public static class TestTask1 implements Runnable {

    private final String mark;
    private final TaskObj taskObj;

    public TestTask1(String mark, TaskObj taskObj) {
      this.mark = mark;
      this.taskObj = taskObj;
    }

    @Override
    public void run() {
      try {
        Thread.sleep(1000);
      } catch (InterruptedException ignore) {
      }
      int length = taskObj.content.length();
      if (length == 0) {
        log.info("empty content");
      }
      log.info(this.mark + " finished at " + new Timestamp(System.currentTimeMillis()));
    }
  }

//  public static void putIntoOldGenStore(String key, String value) {
//    oldGenStore.put(key, value);
//  }
}
