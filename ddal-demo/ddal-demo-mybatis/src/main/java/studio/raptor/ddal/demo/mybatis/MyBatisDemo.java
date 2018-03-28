package studio.raptor.ddal.demo.mybatis;

import java.util.concurrent.atomic.AtomicLong;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import studio.raptor.ddal.demo.mybatis.entity.Table;
import studio.raptor.ddal.demo.mybatis.service.TableService;

/**
 * @author Sam
 * @since 3.0.0
 */
public class MyBatisDemo {

  private static Logger log = LoggerFactory.getLogger(MyBatisDemo.class);
  private static final AtomicLong ID = new AtomicLong(System.currentTimeMillis());

  public static void main(String[] args) {
    ApplicationContext application = new ClassPathXmlApplicationContext("spring/mybatis-config.xml");
    TableService tableService = application.getBean(TableService.class);
    Long id = ID.getAndIncrement();
    log.info("#### Random id {}", id);
    tableService.createTable(id, "Hello, Raptor!");
    Table table = tableService.getTableById(id);
    log.info("#### Query result {}", table);
  }
}
