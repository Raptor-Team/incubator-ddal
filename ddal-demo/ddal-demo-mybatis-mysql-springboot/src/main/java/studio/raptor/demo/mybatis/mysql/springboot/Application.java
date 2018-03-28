package studio.raptor.demo.mybatis.mysql.springboot;

import javax.sql.DataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
@SpringBootApplication
@MapperScan("studio.raptor.demo.mybatis.mysql.springboot.repository")
public class Application {

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  private DataSource dataSource;

  @Bean
  @Primary
  public DataSource dataSource() {
    if(null == dataSource) {
      return (dataSource = new RaptorDataSource("vdb", "mysql"));
    }
    return (dataSource);
  }

  @Bean
  public SqlSessionFactory sqlSessionFactory() throws Exception {
    SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
    if(null == dataSource) {
      dataSource = new RaptorDataSource("vdb", "mysql");
    }
     sessionFactory.setDataSource(dataSource);
    return sessionFactory.getObject();
  }
}
