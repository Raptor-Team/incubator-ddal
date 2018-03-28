package studio.raptor.ddal.dashboard.repository;

import java.util.List;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * 业务应用
 *
 * @author Sam
 * @since 3.1.0
 */
public interface AppRepository extends PagingAndSortingRepository<App, Long> {

  /**
   * 查询业务中心下的应用列表
   *
   * @param center 业务中心
   * @return 应用列表
   */
  List<App> findByCenter(String center);
}
