package studio.raptor.ddal.dashboard.service.impl;

import java.sql.Timestamp;
import java.util.List;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.raptor.ddal.dashboard.common.Constants;
import studio.raptor.ddal.dashboard.repository.AppCenter;
import studio.raptor.ddal.dashboard.repository.AppCenterRepository;
import studio.raptor.ddal.dashboard.service.interfaces.AppCenterService;
import studio.raptor.ddal.dashboard.service.interfaces.MessageByLocaleService;

/**
 * 业务中心服务实现
 *
 * @author Sam
 * @since 3.1.0
 */
@Service
public class AppCenterServiceImpl implements AppCenterService {

  private AppCenterRepository appCenterRepository;
  private MessageByLocaleService messageByLocaleService;

  @Autowired
  public AppCenterServiceImpl(AppCenterRepository appCenterRepository,
      MessageByLocaleService messageByLocaleService) {
    this.appCenterRepository = appCenterRepository;
    this.messageByLocaleService = messageByLocaleService;
  }

  @Override
  public List<AppCenter> getAllAppCenter() {
    return appCenterRepository.findAllByStatusEqualsOrderByIdAsc(Constants.STATUS_VALID);
  }

  @Override
  public Page<AppCenter> getAppCenter(Integer page, Integer count) {
    Pageable pageable = new PageRequest(page, count, Direction.DESC, "id");
    return appCenterRepository.findByStatus(Constants.STATUS_VALID, pageable);
  }

  /**
   * 模糊匹配中心名称和编码，默认查询状态为可用的数据。
   *
   * @param name 中心名称
   * @param code 中心编码
   * @return 中心列表
   */
  @Override
  public List<AppCenter> getAppCenterByNameOrCode(final String name, final String code) {
    return appCenterRepository.findAll(new Specification<AppCenter>() {
      @Override
      public Predicate toPredicate(Root<AppCenter> root, CriteriaQuery<?> query,
          CriteriaBuilder cb) {
        Predicate nameOrCodeLike = cb.or(
            cb.like(root.<String>get("name"), "%" + name + "%"),
            cb.like(root.<String>get("code"), "%" + code + "%"));
        Predicate statusEqual = cb.equal(root.<String>get("status"), Constants.STATUS_VALID);
        return cb.and(nameOrCodeLike, statusEqual);
      }
    });
  }

  @Override
  public List<AppCenter> getAppCenterByCode(String code) {
    return appCenterRepository.findByCode(code);
  }

  @Transactional
  @Override
  public AppCenter createAppCenter(String centerName, String centerCode, String desc) {
    // check center code exists
    List<AppCenter> appCenters = appCenterRepository.findByCode(centerCode);
    if (null != appCenters && !appCenters.isEmpty()) {
      throw new RuntimeException(
          messageByLocaleService.getMessage("center_already_exist", new Object[]{centerCode}));
    }
    AppCenter ac = new AppCenter();
    ac.setName(centerName);
    ac.setCode(centerCode);
    ac.setDescription(desc);
    ac.setRegisterTime(new Timestamp(System.currentTimeMillis()));
    ac.setStatus(Constants.STATUS_VALID);
    appCenterRepository.save(ac);
    return ac;
  }

  @Transactional
  @Override
  public void deleteAppCenter(Long id) {
    appCenterRepository.softDelete(id);
  }
}
