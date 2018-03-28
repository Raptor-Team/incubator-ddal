package studio.raptor.ddal.demo.mysql.springweb.dao.interfaces;

import java.util.Map;

/**
 * @author Sam
 * @since 3.0.0
 */
public interface DemoDAO {
  Map<String, Object> queryCustomerById(Long id);
}
