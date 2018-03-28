package studio.raptor.ddal.dashboard.api;

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import studio.raptor.ddal.dashboard.repository.App;
import studio.raptor.ddal.dashboard.service.interfaces.AppService;
import studio.raptor.ddal.dashboard.service.interfaces.GlobalIDService;

/**
 * 业务应用接口
 *
 * @author Sam
 * @since 3.1.0
 */
@RestController
public class AppController {

  private static Logger log = LoggerFactory.getLogger(AppController.class);
  private AppService appService;
  private GlobalIDService globalIDService;

  @Autowired
  public AppController(AppService appService, GlobalIDService globalIDService) {
    this.appService = appService;
    this.globalIDService = globalIDService;
  }

  @RequestMapping(value = "/app/under/{center}")
  public @ResponseBody
  List<App> findAppByCenterName(@PathVariable String center) {
    //log.info(globalIDService.newId(1, TimeUnit.SECONDS) + "");
    return appService.findByCenterName(center);
  }

  @RequestMapping(value = "/app/create", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public @ResponseBody
  App createApp(@RequestBody App app) {
    return null;
  }
}
