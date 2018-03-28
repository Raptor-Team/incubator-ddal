package studio.raptor.ddal.core.executor.threadPool;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
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
public class CompletionServiceTest {
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    ExecutorService executorService = Executors.newCachedThreadPool();
    CompletionService<String> completionService = new ExecutorCompletionService<String>(executorService);
    List<Future<String>> futures  = new ArrayList<Future<String>>(10);
    // 创建10个任务并执行
    for (int i = 0; i < 10; i++) {
      // 使用ExecutorService执行Callable类型的任务，并将结果保存在future变量中
      futures.add(completionService.submit(new CallableTask(i)));
    }
    // 检查线程池任务执行结果
    try {
      for (int i = 0; i < 10; i++) {
        Future<String> future = completionService.take();
        System.out.println("method2:" + future.get());
      } // 打印各个线程（任务）执行的结果
    } catch (InterruptedException | ExecutionException e) {
      e.printStackTrace();
      for(Future<String> future : futures){
        future.cancel(true);
      }
    }

    executorService.shutdown();
  }
}
