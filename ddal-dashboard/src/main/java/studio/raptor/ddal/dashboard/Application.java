package studio.raptor.ddal.dashboard;

import java.io.IOException;
import java.util.Locale;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import studio.raptor.ddal.dashboard.repository.GlobalIDRepository;
import studio.raptor.ddal.dashboard.service.impl.GlobalIDServiceImpl;
import studio.raptor.ddal.dashboard.service.impl.SequenceServiceImpl;
import studio.raptor.ddal.dashboard.service.interfaces.GlobalIDService;
import studio.raptor.ddal.dashboard.service.interfaces.SequenceService;

/**
 * @author Sam
 * @since 3.1.0
 */
@SpringBootApplication
public class Application {

  private static Logger log = LoggerFactory.getLogger(Application.class);

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @Bean
  public LocaleResolver localeResolver() {
    SessionLocaleResolver slr = new SessionLocaleResolver();
    slr.setDefaultLocale(Locale.CHINA);
    return slr;
  }

  @Bean
  public ReloadableResourceBundleMessageSource messageSource() {
    ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
    messageSource.setBasename("classpath:locale/messages");
    messageSource.setCacheSeconds(3600);
    Properties fileEncoding = new Properties();
    try {
      fileEncoding.load(Application.class.getResourceAsStream("/locale/file-encoding.properties"));
    } catch (IOException ioe) {
      log.error("load i18n message file encoding failed.", ioe);
    }
    messageSource.setFileEncodings(fileEncoding);
    return messageSource;
  }

  /**
   * 构造单例全局序列实例
   *
   * @param idRepository 全局序列数据库访问对象。
   * @return 全局序列服务
   */
  @Bean
  public GlobalIDService initGlobalIdService(GlobalIDRepository idRepository) {
    return new GlobalIDServiceImpl(idRepository, 10);
  }

  /**
   * 开启全局序列的维护线程。
   *
   * @param idService 全局序列服务，一定要是SpringBean，因为使用了spring的事务管理器
   * @return null
   *//*
  @Bean
  public Void startGlobalIdMaintenance(GlobalIDService idService) {
    idService.startFillUpMaintenance(idService);
    return null;
  }*/

  /**
   * 构造单例全局 BreadCrumb序列全局展示与修改服务实例
   * @return 全局序列服务
   */
  @Bean
  public SequenceService initBreadcrumbGidService() {
    Properties systemProperties = new Properties();
    try {
      systemProperties.load(Application.class.getResourceAsStream("/system.properties"));
    } catch (IOException ioe) {
      log.error("load /system.properties file failed.", ioe);
    }
    String zkConnectString=systemProperties.getProperty( "sequence.zk.address" );
    int sessionTimeout =Integer.parseInt(systemProperties.getProperty( "sequence.zk.session_timeout" ).trim());
    int connectionTimeout = Integer.parseInt(systemProperties.getProperty( "sequence.zk.connection_timeout" ).trim());
    if(zkConnectString!=null){
      log.info( "====sequence.zk.address="+ zkConnectString);
      return new SequenceServiceImpl(zkConnectString,sessionTimeout,connectionTimeout);
    }
    return null;
  }

}
