package studio.raptor.ddal.demo.mybatis.oracle.service;

import java.sql.Clob;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import studio.raptor.ddal.demo.mybatis.oracle.repository.ClobRepository;

/**
 * @author Sam
 * @since 3.0.0
 */
@Service
public class ClobService {

  @Resource
  private ClobRepository repository;

  public void getClob() {
    try {
      Map<String, Object> clobMap = repository.getClob();
      Clob clob = (Clob) clobMap.get("NAME");
      System.out.println(clob.getSubString(1, (int) clob.length()));
    }catch(Exception e){
      e.printStackTrace();
    }
  }

}
