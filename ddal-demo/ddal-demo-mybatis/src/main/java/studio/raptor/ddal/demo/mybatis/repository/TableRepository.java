package studio.raptor.ddal.demo.mybatis.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;
import studio.raptor.ddal.demo.mybatis.entity.Table;

/**
 * @author Sam
 * @since 3.0.0
 */
@Repository
public interface TableRepository {

  Table getTableById(Long id);

  int createTable(Map<String, Object> param);
}
