package studio.raptor.ddal.demo.jmeter.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DDALUpdateMultiDBSampler extends AbstractJavaSamplerClient {

  private static final AtomicLong id = new AtomicLong(1496629071985L);
  private static Logger logger = LoggerFactory.getLogger(DDALUpdateMultiDBSampler.class);
  private static DataSource dataSource = new RaptorDataSource("school", "oracle");

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();

    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(JMeterConstants.UPDATE_SQL)
    ) {
      long tno = id.getAndIncrement();
      String tname = RandomStringUtils.randomAlphabetic(18);
      statement.setString(1, tname);
      statement.setLong(2, tno);
      int affectedRows = statement.executeUpdate();
      sampleResult.setSuccessful(true);
      if (!(affectedRows == 1)) {
        logger.error("tno {} not hit.", tno);
      }
      conn.commit();
    } catch (Exception e) {
      e.printStackTrace();
      sampleResult.setSuccessful(false);
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }

}
