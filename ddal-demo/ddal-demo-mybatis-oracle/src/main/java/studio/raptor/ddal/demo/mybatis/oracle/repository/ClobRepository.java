package studio.raptor.ddal.demo.mybatis.oracle.repository;

import java.util.Map;
import org.springframework.stereotype.Repository;

/**
 * @author Sam
 * @since 3.0.0
 */
@Repository
public interface ClobRepository {

  Map<String, Object> getClob();
}
