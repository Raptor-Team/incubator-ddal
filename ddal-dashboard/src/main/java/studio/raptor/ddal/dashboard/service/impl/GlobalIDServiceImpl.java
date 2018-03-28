package studio.raptor.ddal.dashboard.service.impl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;
import studio.raptor.ddal.dashboard.common.Constants;
import studio.raptor.ddal.dashboard.repository.GlobalID;
import studio.raptor.ddal.dashboard.repository.GlobalIDRepository;
import studio.raptor.ddal.dashboard.service.interfaces.GlobalIDService;

/**
 * Use blocking queue to store global id.
 *
 * @author Sam
 * @since 3.1.0
 */
public class GlobalIDServiceImpl implements GlobalIDService {

  private static Logger log = LoggerFactory.getLogger(GlobalIDServiceImpl.class);
  private GlobalIDRepository idRepository;
  private volatile BlockingQueue<Long> queue;
  private ExecutorService idUpdateService;
  private Integer bucketSize;

  public GlobalIDServiceImpl(GlobalIDRepository idRepository, Integer bucketSize) {
    this.idRepository = idRepository;
    this.bucketSize = bucketSize;
    this.queue = new ArrayBlockingQueue<>(bucketSize);
    idUpdateService = Executors.newFixedThreadPool(1, new ThreadFactory() {
      @Override
      public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName("GlobalIdUpdateService");
        thread.setDaemon(true);
        return thread;
      }
    });
    //idUpdateService.submit(new QueueFillUpWorker(this.queue, this));
  }

  @Override
  public Long newId(long timeout, TimeUnit timeUnit) {
    try {
      return queue.poll(timeout, timeUnit);
    } catch (InterruptedException e) {
      throw new RuntimeException("Get new id error.", e);
    }
  }

  @Transactional
  public GlobalID getNewGlobalID() {
    int retryCount = 0;
    GlobalID globalID=new GlobalID();
    return globalID;
    /*for (; ; ) {
      GlobalID globalID = idRepository
          .findByNameEqualsAndStatusEquals("default", Constants.STATUS_VALID);
      if(null == globalID) {
        throw new RuntimeException("No global id configuration with name of 'default'" );
      }
      try {
        if (idRepository
            .updateLastNumberCAS(globalID.getLastNumber() + bucketSize, globalID.getLastNumber())
            > 0) {
          return globalID;
        }
      } catch (Exception e) {
        log.error("Update error.", e);
      }

      if (retryCount++ >= 3) {
        throw new RuntimeException("Get new global id error.");
      }
    }*/
  }

  @Override
  public BlockingQueue<Long> getQueue() {
    return queue;
  }

  @Override
  public Integer getBucketSize() {
    return bucketSize;
  }

  @Override
  public void startFillUpMaintenance(GlobalIDService idService) {
    this.idUpdateService.submit(new QueueFillUpWorker(idService));
  }

  public static class QueueFillUpWorker implements Runnable {

    private static Logger log = LoggerFactory.getLogger(QueueFillUpWorker.class);
    private BlockingQueue<Long> queueRef;
    private GlobalIDService globalIDService;

    public QueueFillUpWorker(GlobalIDService globalIDService) {
      this.queueRef = globalIDService.getQueue();
      this.globalIDService = globalIDService;
    }

    @Override
    public void run() {
      boolean err;
      for (; ; ) {
        err = false;
        try {
          if (queueRef.size() < globalIDService.getBucketSize()) {
            GlobalID globalID = globalIDService.getNewGlobalID();
            long start = globalID.getLastNumber();
            for (int i = 0; i < globalIDService.getBucketSize(); i++) {
              try {
                queueRef.put(start++);
              } catch (InterruptedException e) {
                log.error("Fill id queue failed.", e);
              }
            }
          }
        } catch (Exception e) {
          // error occurs, print it to log
          log.error("GlobalId maintains failed and will try after 2 seconds.", e);
          err = true;
        }

        try {
          Thread.sleep(err ? 2000 : 100);
        } catch (InterruptedException ignore) {
        }
      }
    }
  }
}
