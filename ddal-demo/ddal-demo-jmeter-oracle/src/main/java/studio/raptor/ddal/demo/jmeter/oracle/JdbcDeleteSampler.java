package studio.raptor.ddal.demo.jmeter.oracle;

import java.util.Collections;
import java.util.concurrent.atomic.AtomicLong;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JdbcDeleteSampler extends AbstractJavaSamplerClient {

  private static final String DELETE_SQL = "delete from ddal_test_0.teacher where tno = ?";
  private static volatile AtomicLong ID = new AtomicLong(1496733235330L);

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();
    BackendConnection conn = null;
    try {
      conn = BackendDataSourceManager.getBackendConnection("group_1", false, false);
      conn.executePreparedUpdate(DELETE_SQL,
          Collections.singletonList((Object) ID.getAndAdd(5L)));
      conn.commit();
      sampleResult.setSuccessful(true);
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
