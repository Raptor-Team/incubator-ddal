package studio.raptor.ddal.demo.mysql.springweb.dao.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import studio.raptor.ddal.demo.mysql.springweb.dao.interfaces.DemoDAO;

/**
 * @author Sam
 * @since 3.0.0
 */
@Repository
public class DemoDAOImpl implements DemoDAO {

  @Resource
  private JdbcTemplate jdbcTemplate;
  private Map<String, Object> emptyMap = new HashMap<>();

  @Override
  public Map<String, Object> queryCustomerById(Long id) {
    List<Map<String, Object>> objList = jdbcTemplate.queryForList("select * from customer where id = ?", id);
    if(null == objList || objList.size() < 1) {
      return emptyMap;
    }
    return objList.get(0);
  }
}
