package studio.raptor.ddal.core.executor.task;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class RunnableTask implements Runnable {
  private int id;
  public RunnableTask(int id) {
    this.id = id;
  }
  /**
   * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行。
   *
   * @return
   */
  public void run() {
    System.out.println("call()方法被自动调用,干活！！！             " + Thread.currentThread().getName());
    // 一个模拟耗时的操作
    LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(50, TimeUnit.SECONDS));
    System.out.println("call()方法被自动调用，任务的结果是：" + id + "    " + Thread.currentThread().getName());
  }
}
