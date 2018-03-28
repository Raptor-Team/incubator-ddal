package studio.raptor.ddal.core.executor.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import studio.raptor.ddal.core.executor.task.CallableTask;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ExecutorServiceTest {
  public static void main(String[] args) {
    ExecutorService executorService = Executors.newCachedThreadPool();
    List<Future<String>> resultList = new ArrayList<Future<String>>();
    // 创建10个任务并执行
    for (int i = 0; i < 10; i++) {
      // 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
      Future<String> future = executorService.submit(new CallableTask(i));
      // 将任务执行结果存储到List中
      resultList.add(future);
    }
    // 遍历任务的结果
    for (Future<String> fs : resultList) {
      try {
        System.out.println(fs.get()); // 打印各个线程（任务）执行的结果
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (ExecutionException e) {
        executorService.shutdownNow();
        e.printStackTrace();
        return;
      }
    }
    executorService.shutdown();
  }
}

