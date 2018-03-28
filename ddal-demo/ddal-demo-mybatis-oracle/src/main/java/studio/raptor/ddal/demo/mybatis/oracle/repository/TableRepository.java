package studio.raptor.ddal.demo.mybatis.oracle.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;
import studio.raptor.ddal.demo.mybatis.oracle.entity.Table;

/**
 * @author Sam
 * @since 3.0.0
 */
@Repository
public interface TableRepository {

  Table getTableById(Long id);

  void createTable(Map<String, Object> param);
}
