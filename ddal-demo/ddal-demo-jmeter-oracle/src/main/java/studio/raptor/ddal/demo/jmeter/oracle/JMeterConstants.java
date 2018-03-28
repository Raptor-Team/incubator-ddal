package studio.raptor.ddal.demo.jmeter.oracle;

import java.util.Arrays;
import java.util.List;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JMeterConstants {

  public static final String SELECT_SQL = "select tno, tname, sex, age, tphone from TEACHER where TNO = 1496629073550";
  public static final String SELECT_GLOBAL_SQL = "select CNO, CNAME, TNO from course";
  public static final String SELECT_PREPARE_SQL = "select CNO, CNAME, TNO from ddal_test_0.course where cno = ?";
  public static final String INSERT = "Insert into ddal_test_0.TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (?,'Abell','FM',32,'15889001197')";

  public static final String UPDATE_SQL = "update ddal_test_0.teacher set tname = ? where tno = ?";

  public static final List<Object> PARAMS = Arrays.asList((Object) 300004L);
}
