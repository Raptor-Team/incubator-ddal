package studio.raptor.ddal.core.executor.threadPool.concurrent;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ListenableFutureTest {
  private final static int COUNT = 10;
  private final ListeningExecutorService executorService =  MoreExecutors.listeningDecorator(MoreExecutors.getExitingExecutorService(
      new ThreadPoolExecutor(2, 4, 30L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>())));

  public List<String> execute() {
    ListenableFuture<List<String>> futures = submitFutures();
    addCallback(futures);
    return getFutureResults(futures);
  }

  private ListenableFuture<List<String>> submitFutures() {
    Set<ListenableFuture<String>> result = new HashSet<>(COUNT);
    for (int i=0; i<COUNT; i++) {
      result.add(executorService.submit(new Worker(i)));
    }
    return Futures.allAsList(result);
  }

  private void addCallback(final ListenableFuture<List<String>> allFutures) {
    Futures.addCallback(allFutures, new FutureCallback<List<String>>() {
      @Override
      public void onSuccess(final List<String> result) {
        System.out.println(result.toString());
        System.out.println("Success");
      }

      @Override
      public void onFailure(final Throwable thrown) {
        thrown.printStackTrace();
        System.out.println("Failure");
      }
    });
  }

  private List<String> getFutureResults(final ListenableFuture<List<String>> futures) {
    try {
      return futures.get();
    } catch (final InterruptedException | ExecutionException ex) {
      return null;
    }
  }

  class Worker implements Callable {
    int myRow;

    Worker(int row) {
      myRow = row;
    }

    @Override
    public String call() throws Exception {
      return "output+" + myRow;
    }
  }

  public static void main(String[] args) {
    ListenableFutureTest listenableFutureTest = new ListenableFutureTest();
    listenableFutureTest.execute();
  }
}
