package studio.raptor.ddal.demo.jmeter.oracle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;
import javax.sql.DataSource;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.apache.jmeter.protocol.java.sampler.AbstractJavaSamplerClient;
import org.apache.jmeter.protocol.java.sampler.JavaSamplerContext;
import org.apache.jmeter.samplers.SampleResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.util.StringUtil;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DDALUpdateSingleDBSampler extends AbstractJavaSamplerClient {

  private static Logger logger = LoggerFactory.getLogger(DDALUpdateSingleDBSampler.class);
  private static volatile List<Long> ids;
  private static volatile int ids_size = 0;

  private static DataSource dataSource;

  static {
    try {
      loadPrimaryKeysIntoMemory();
      ids_size = ids.size();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private static void loadPrimaryKeysIntoMemory() throws IOException {
    logger.info("Start to Load update primary keys.");
    ids = new ArrayList<>();
    BufferedReader reader = new BufferedReader(new InputStreamReader(
        DDALUpdateSingleDBSampler.class.getClassLoader()
            .getResourceAsStream("update_singledb.id")));
    String id;
    while (null != (id = reader.readLine())) {
      if (!StringUtil.isEmpty(id) && id.matches("\\d+")) {
        ids.add(Long.parseLong(id));
      }
    }
    reader.close();
    if (logger.isInfoEnabled()) {
      logger.info("{} id loaded.", ids.size());
    }
  }

  private DataSource getDataSource() {
    if (null == dataSource) {
      synchronized (this) {
        if (null == dataSource) {
          dataSource = new RaptorDataSource("school", "oracle");
        }
      }
    }
    return dataSource;
  }

  @Override
  public SampleResult runTest(JavaSamplerContext javaSamplerContext) {
    SampleResult sampleResult = new SampleResult();
    sampleResult.sampleStart();

    try (
        Connection conn = getDataSource().getConnection();
        PreparedStatement statement = conn.prepareStatement(JMeterConstants.UPDATE_SQL)
    ) {
      Long tno = ids.get(RandomUtils.nextInt(0, ids_size));
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
