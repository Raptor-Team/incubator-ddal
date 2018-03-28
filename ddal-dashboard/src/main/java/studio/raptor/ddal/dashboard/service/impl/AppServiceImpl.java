package studio.raptor.ddal.dashboard.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import studio.raptor.ddal.dashboard.common.Constants;
import studio.raptor.ddal.dashboard.repository.App;
import studio.raptor.ddal.dashboard.repository.AppRepository;
import studio.raptor.ddal.dashboard.service.interfaces.AppService;

/**
 * @author Sam
 * @since 3.1.0
 */
@Service
public class AppServiceImpl implements AppService {

  private AppRepository appRepository;

  @Autowired
  public AppServiceImpl(AppRepository appRepository) {
    this.appRepository = appRepository;
  }

  @Override
  public List<App> findByCenterName(String center) {
    return appRepository.findByCenter(center);
  }

  @Override
  public App createApp(String appName, String center, String ip, Integer port, String adminUrl) {
    App app = new App();
    app.setAppName(appName);
    app.setCenter(center);
    app.setHostIp(ip);
    app.setPort(port);
    app.setAdminUrl(adminUrl);
    app.setStatus(Constants.STATUS_VALID);
    return appRepository.save(app);
  }
}
