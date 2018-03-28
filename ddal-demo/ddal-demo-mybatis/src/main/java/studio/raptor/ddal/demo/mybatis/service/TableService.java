package studio.raptor.ddal.demo.mybatis.service;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import studio.raptor.ddal.demo.mybatis.entity.Table;
import studio.raptor.ddal.demo.mybatis.repository.TableRepository;

/**
 * @author Sam
 * @since 3.0.0
 */
@Service
public class TableService {

  @Resource
  private TableRepository repository;

  public Table getTableById(Long id) {
    return repository.getTableById(id);
  }

  @Transactional
  public void createTable(Long id, String name) {
    Map<String, Object> table = new HashMap<>();
    table.put("id", id);
    table.put("name", name);
    table.put("state", 1);
    table.put("createTime", new Timestamp(System.currentTimeMillis()));
    int affectedRows = repository.createTable(table);
    System.out.println(affectedRows);
  }
}
