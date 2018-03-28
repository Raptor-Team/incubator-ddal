package studio.raptor;

import studio.raptor.ddal.jdbc.RaptorDataSource;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Hello world!
 */
public class App {

    public static void main(String[] args) throws SQLException {
        DataSource dataSource = new RaptorDataSource("quickstart", "mysql");
        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement preparedStatement =
                        connection.prepareStatement("select id, name, create_time, state from ddal_table where id = ?");
        ) {
            preparedStatement.setLong(1, 2);
            try (
                    ResultSet resultSet = preparedStatement.executeQuery();
            ) {
                while (resultSet.next()) {
                    System.out.println("id:" + resultSet.getLong(1));
                    System.out.println("name:" + resultSet.getString(2));
                    System.out.println("create_time:" + resultSet.getTimestamp(3));
                    System.out.println("state:" + resultSet.getInt(4));
                }
            }
        }
    }
}
