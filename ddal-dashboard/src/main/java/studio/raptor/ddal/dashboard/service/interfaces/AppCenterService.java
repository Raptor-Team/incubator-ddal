package studio.raptor.ddal.dashboard.service.interfaces;

import java.util.List;
import org.springframework.data.domain.Page;
import studio.raptor.ddal.dashboard.repository.AppCenter;

/**
 * 应用中心服务
 *
 * @author Sam
 * @since 3.1.0
 */
public interface AppCenterService {

  Page<AppCenter> getAppCenter(Integer page, Integer count);

  //List<AppCenter>
  List<AppCenter> getAllAppCenter();

  List<AppCenter> getAppCenterByNameOrCode(String name, String code);

  List<AppCenter> getAppCenterByCode(String code);

  AppCenter createAppCenter(String centerName, String centerCode, String desc);

  void deleteAppCenter(Long id);
}
