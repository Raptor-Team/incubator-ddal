package studio.raptor.ddal.benchmark.datasource;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.weakref.jmx.internal.guava.base.Strings;
import studio.raptor.ddal.jdbc.RaptorRoutingDataSource;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class RoutingDatasourceBenchmark {

  public void benchmark(){
    try{
      RaptorRoutingDataSource routingDataSource = new RaptorRoutingDataSource("oracle");
      DataSource schoolDataSource = routingDataSource.route("s");
      DataSource hospitalDataSource = routingDataSource.route("h");

      String schoolSql = "select * from teacher";

      String hospitalSql = "select * from doctor";

      execute(schoolDataSource, schoolSql);

      execute(hospitalDataSource, hospitalSql);
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  private void execute(DataSource dataSource, String sql){
    try (Connection connection = dataSource.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sql)
    ) {
      ResultSetMetaData rsmd = resultSet.getMetaData();
      System.out.println("Query [" + sql + "]");
      int columnsNumber = rsmd.getColumnCount();
      int rowCount = 0;
      while (resultSet.next()) {
        for (int i = 1; i <= columnsNumber; i++) {
          String columnValue = resultSet.getString(i);
          System.out.print(rsmd.getColumnName(i) + ':' + Strings.padEnd(columnValue, 12, ' '));
        }
        System.out.println("");
        rowCount++;
      }
      System.out.println("Result total count:" + rowCount);
    } catch (SQLException e) {
      throw new RuntimeException("");
    }
  }

  public static void main(String[] args) {
    new RoutingDatasourceBenchmark().benchmark();
  }
}
