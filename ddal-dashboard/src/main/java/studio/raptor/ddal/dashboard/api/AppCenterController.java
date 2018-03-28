package studio.raptor.ddal.dashboard.api;

import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import studio.raptor.ddal.dashboard.common.Constants;
import studio.raptor.ddal.dashboard.repository.AppCenter;
import studio.raptor.ddal.dashboard.service.interfaces.AppCenterService;

/**
 * @author Sam
 * @since 3.1.0
 */
@RestController
public class AppCenterController {

  private static Logger log = LoggerFactory.getLogger(AppCenterController.class);
  private AppCenterService appCenterService;

  @Autowired
  public AppCenterController(AppCenterService appCenterService) {
    this.appCenterService = appCenterService;
  }

  @RequestMapping(value = "/appcenter/{centerCode}", method = RequestMethod.GET)
  public List<AppCenter> getAppCenter(@PathVariable String centerCode) {
    //ThreadExecutor.putIntoOldGenStore(RandomStringUtils.randomAlphabetic(1024), RandomStringUtils.randomAlphabetic(4096));
    //ThreadExecutor.execute(new TestTask1(centerCode, new TaskObj(RandomStringUtils.randomAlphabetic(1024))));
    return appCenterService.getAppCenterByCode(centerCode);
  }

  @RequestMapping(value = "/appcenter/list", method = RequestMethod.GET)
  public Page<AppCenter> listAppCenter(
      @RequestParam(value = "page", required = false) Integer page) {
    if (null == page || page <= 0) {
      page = 1;
    }
    return appCenterService.getAppCenter(page - 1, Constants.DEFAULT_PAGE_SIZE);
  }

  /**
   * 页面下拉搜索框使用
   *
   * @param pickerParam 搜索参数
   * @return 业务中心列表
   */
  @RequestMapping(value = "/appcenter/list", method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<AppCenter> listAppCenter(@RequestBody AjaxPickerParam pickerParam) {
    if (log.isInfoEnabled()) {
      log.info("AppCenter ajax picker:" + pickerParam.toString());
    }
    String searchText;
    if (null == pickerParam || StringUtils.isEmpty((searchText = pickerParam.getQ()))) {
      return appCenterService.getAllAppCenter();
    }
    return appCenterService.getAppCenterByNameOrCode(searchText, searchText);
  }

  @RequestMapping(value = "/appcenter/create",
      method = RequestMethod.POST,
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE
  )
  public AppCenter createAppCenter(@RequestBody AppCenter appCenter) {
    return appCenterService
        .createAppCenter(appCenter.getName(), appCenter.getCode(), appCenter.getDescription());
  }

  @RequestMapping(value = "/appcenter/{id}", method = RequestMethod.DELETE)
  public void deleteAppCenter(@PathVariable Long id) {
    appCenterService.deleteAppCenter(id);
  }

  @Getter
  @Setter
  @ToString
  private static class AjaxPickerParam {

    private String q;
  }
}
