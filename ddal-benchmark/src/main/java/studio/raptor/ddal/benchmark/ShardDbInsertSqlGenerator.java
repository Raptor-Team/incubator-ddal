/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package studio.raptor.ddal.benchmark;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import oracle.jdbc.driver.OracleDriver;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ShardDbInsertSqlGenerator {

  public static void main(String[] args) throws ClassNotFoundException, SQLException {
    Class.forName(OracleDriver.class.getName());
    Connection conn = DriverManager.getConnection("jdbc:oracle:thin:@132.224.197.38:1621:bsshps", "ddal_test", "ddal_test123");
    Statement statement = conn.createStatement();
    ResultSet resultSet = statement.executeQuery("select * from ddal_test.scores");

    while(resultSet.next()) {
      System.out.println(
          String.format("insert into ddal_test_%s.scores(scoreno, sno, cno, term, grade) values (%s, %s, %s, '%s', %s);",
              resultSet.getLong(2)%5,
              resultSet.getLong(1),
              resultSet.getLong(2),
              resultSet.getLong(3),
              resultSet.getString(4),
              resultSet.getInt(5)
              )
      );
    }
    resultSet.close();
    statement.close();
    conn.close();
  }
}
