package studio.raptor.ddal.demo.jmeter.oracle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.RandomUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.executor.resultset.ResultData;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JdbcSelectSampler extends AbstractJavaSamplerClient {

  private static final String SELECT_SQL = "select tno, tname, sex, age, tphone from TEACHER where TNO = ?";
  private static final List<Long> tnoList = new ArrayList<>();
  private static int tno_len = 0;

  static {
    tnoList.add(1496629074335L);
    tnoList.add(1496629075560L);
    tnoList.add(1496629072150L);
    tnoList.add(1496629075355L);
    tnoList.add(1496629074405L);
    tnoList.add(1496629073080L);
    tno_len = tnoList.size();
  }

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    BackendConnection conn = null;
    try {
      Long tno = tnoList.get(RandomUtils.nextInt(0, tno_len));
      conn = BackendDataSourceManager.getBackendConnection("group_1", false, false);
      ResultData resultData = conn
          .executePreparedQuery(SELECT_SQL, Collections.singletonList((Object) tno));
      if (null != resultData) {
        sampleResult.setSuccessful(true);
      } else {
        sampleResult.setSuccessful(false);
      }
    } catch (Exception e) {
      e.printStackTrace();
      sampleResult.setSuccessful(false);
    } finally {
      if (null != conn) {
        try {
          conn.close();
        } catch (Exception ignore) {
        }
      }
    }
    sampleResult.sampleEnd();
    return sampleResult;
  }
}
