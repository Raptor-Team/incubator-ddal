package studio.raptor.ddal.demo.jmeter.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang3.RandomUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DdalSelectSampler extends AbstractJavaSamplerClient {

  private static final String SELECT_SQL = "select tno, tname, sex, age, tphone from TEACHER where TNO = ?";
  private static final List<Long> tnoList = new ArrayList<>();
  private static int tno_len = 0;
  private static DataSource dataSource = new RaptorDataSource("school", "oracle");

  static {
    tnoList.add(1496629074335L);
    tnoList.add(1496629074336L);
    tnoList.add(1496629074337L);
    tnoList.add(1496629074338L);
    tnoList.add(1496629074339L);
    tno_len = tnoList.size();
  }

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    try (
        Connection conn = dataSource.getConnection();
        PreparedStatement statement = conn.prepareStatement(SELECT_SQL);
    ) {
      statement.setLong(1, tnoList.get(RandomUtils.nextInt(0, tno_len)));
      ResultSet resultSet = statement.executeQuery();
      if (resultSet.next()) {
        sampleResult.setSuccessful(true);
      } else {
        sampleResult.setSuccessful(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
      sampleResult.setSuccessful(false);
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }

}
