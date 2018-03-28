package studio.raptor.ddal.dashboard.service.interfaces;

import java.util.List;
import studio.raptor.ddal.dashboard.repository.App;

/**
 * @author Sam
 * @since 3.1.0
 */
public interface AppService {

  List<App> findByCenterName(String center);

  App createApp(String appName, String center, String ip, Integer port, String adminUrl);
}
