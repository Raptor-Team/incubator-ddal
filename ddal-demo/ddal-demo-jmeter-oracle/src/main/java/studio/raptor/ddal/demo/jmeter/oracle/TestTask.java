package studio.raptor.ddal.demo.jmeter.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.concurrent.atomic.AtomicLong;
import javax.sql.DataSource;
import org.apache.commons.lang3.RandomStringUtils;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class TestTask implements Runnable {

  private static final String INSERT_SQL = "Insert into TEACHER (TNO,TNAME,SEX,AGE,TPHONE) values (?,?,'M ',30,'15889001158')";
  private static final String SELECT_SQL = "select tno, tname, sex, age, tphone from TEACHER where TNO = ?";
  private static final String UPDATE_SQL = "update teacher set tname = ? where tno = ?";
  private static final String DELETE_SQL = "delete from teacher where tno = ?";

  private final AtomicLong ID;
  private final DataSource dataSource;

  public TestTask(AtomicLong ID, DataSource dataSource) {
    this.ID = ID;
    this.dataSource = dataSource;
  }

  public void run() {
    while(true) {
      try {
        Long id = ID.getAndIncrement();
        String tname = RandomStringUtils.randomAlphabetic(18);

        // insert
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(INSERT_SQL);
        ) {
          statement.setLong(1, id);
          statement.setString(2, tname);
          int affectedRows = statement.executeUpdate();
          if (1 != affectedRows) {
            throw new Exception("Insert data error.");
          }
          conn.commit();
        }

        // select
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(SELECT_SQL);
        ) {
          statement.setLong(1, id);
          ResultSet resultSet = statement.executeQuery();
          if (null == resultSet) {
            throw new Exception("Insert data error.");
          }
        }

        // update
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(UPDATE_SQL);
        ) {
          statement.setString(1, RandomStringUtils.randomAlphabetic(15));
          statement.setLong(2, id);
          int affectedRows = statement.executeUpdate();
          if (affectedRows != 1) {
            throw new Exception("Update data error.");
          }
          conn.commit();
        }

        // delete
        try (
            Connection conn = dataSource.getConnection();
            PreparedStatement statement = conn.prepareStatement(DELETE_SQL);
        ) {
          statement.setLong(1, id);
          int affectedRows = statement.executeUpdate();
          if (affectedRows != 1) {
            throw new Exception("Delete data error.");
          }
          conn.commit();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }
}
