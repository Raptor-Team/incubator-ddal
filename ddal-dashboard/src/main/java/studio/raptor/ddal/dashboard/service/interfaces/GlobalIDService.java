package studio.raptor.ddal.dashboard.service.interfaces;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import studio.raptor.ddal.dashboard.repository.GlobalID;

/**
 * 全局序列服务
 *
 * @author Sam
 * @since 3.1.0
 */
public interface GlobalIDService {

  Long newId(long timeout, TimeUnit timeUnit);

  GlobalID getNewGlobalID();

  BlockingQueue<Long> getQueue();

  Integer getBucketSize();

  void startFillUpMaintenance(GlobalIDService idService);
}
