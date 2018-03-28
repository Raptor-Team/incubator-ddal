package studio.raptor.ddal.core.executor.task;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.LockSupport;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class CallableTask implements Callable<String> {
  private int id;
  public CallableTask(int id) {
    this.id = id;
  }
  /**
   * 任务的具体过程，一旦任务传给ExecutorService的submit方法，则该方法自动在一个线程上执行。
   *
   * @return
   * @throws Exception
   */
  public String call() throws Exception {
    System.out.println("call()方法被自动调用,干活！！！             " + Thread.currentThread().getName());
    if (id == 3){
      throw new TaskException("Meet error in task." + Thread.currentThread().getName());
    }
    // 一个模拟耗时的操作
    LockSupport.parkNanos(TimeUnit.NANOSECONDS.convert(10, TimeUnit.SECONDS));
    return "call()方法被自动调用，任务的结果是：" + id + "    " + Thread.currentThread().getName();
  }
}
