package studio.raptor.ddal.dashboard.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

/**
 * @author Sam
 * @since 3.1.0
 */
public interface AppCenterRepository extends PagingAndSortingRepository<AppCenter, Long> {

  List<AppCenter> findByCode(String code);

  @Query("update #{#entityName} e set e.status = 'INVALID' where e.id = ?1")
  @Modifying
  void softDelete(Long id);

  //List<AppCenter> findByNameContainingOrCodeContainingAndStatusEquals (String name, String code, String status);

  List<AppCenter> findAll(Specification<AppCenter> spec);

  Page<AppCenter> findByStatus(String status, Pageable pageable);

  List<AppCenter> findAllByStatusEqualsOrderByIdAsc(String status);
}
