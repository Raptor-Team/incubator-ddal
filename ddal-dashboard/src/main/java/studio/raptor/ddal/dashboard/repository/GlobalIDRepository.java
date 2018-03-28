package studio.raptor.ddal.dashboard.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * GlobalID repository provides crud operations.
 *
 * @author Sam
 * @since 3.1.0
 */
public interface GlobalIDRepository extends PagingAndSortingRepository<GlobalID, Long> {

  GlobalID findByNameEqualsAndStatusEquals(String name, String status);

  @Query(value = "update #{#entityName} e set e.lastNumber = ?1 where e.lastNumber = ?2")
  @Modifying
  Integer updateLastNumberCAS(Long newLastNum, Long oldLastNum) throws Exception;
}
