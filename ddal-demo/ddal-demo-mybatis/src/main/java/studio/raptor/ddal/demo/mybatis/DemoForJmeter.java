package studio.raptor.ddal.demo.mybatis;

import java.util.concurrent.atomic.AtomicLong;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import studio.raptor.ddal.demo.mybatis.service.TableService;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DemoForJmeter extends AbstractJavaSamplerClient {
  private static Logger log = LoggerFactory.getLogger(DemoForJmeter.class);
  private static final AtomicLong ID = new AtomicLong(1);
  private static ApplicationContext application = new ClassPathXmlApplicationContext("spring/mybatis-config.xml");

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    try {
      TableService tableService = application.getBean(TableService.class);
      tableService.createTable(ID.getAndIncrement(), "Hello, Raptor!");
      sampleResult.setSuccessful(true);
    } catch (Exception e) {
      log.error("", e);
      sampleResult.setSuccessful(false);
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }
}
