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

package studio.raptor.ddal.benchmark.select;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import javax.sql.DataSource;
import org.weakref.jmx.internal.guava.base.Strings;
import studio.raptor.ddal.benchmark.AbstractLongFlowBenchmark;
import studio.raptor.ddal.benchmark.TaskStats;
import studio.raptor.ddal.jdbc.RaptorDataSource;

/**
 * @author Sam
 * @since 3.0.0
 */
public class SelectBenchmark extends AbstractLongFlowBenchmark {

  private DataSource dataSource;
  TaskStats taskStats = new TaskStats(1);

  public SelectBenchmark() {
    super("SELECT查询语句", 0, 10);
  }

  @Override
  protected void prepareLongFlowContext() {
    dataSource = new RaptorDataSource("school", "oracle");
  }

  @Override
  protected void destroyLongFlowContext() {

  }

  @Override
  protected TaskStats executeLongFlow() {
    String sql;
    ///*!hint readonly */
//    sql = "select * from teacher";
//    sql = "/*!hint readonly;*/select * from course";
//    sql = "select * from course";
//    sql = "/*!hint shard(sno=2001)*/select * from scores";
//    sql = "select * from scores order by grade desc";
//    sql = "/*!hint page(offset=10, count=10)*/select * from scores order by grade desc";
//    sql = "Insert into COURSE (CNO,CNAME,TNO) values (300006,'YuWen',1004)";
     sql = "select tno, tname, age, tphone from teacher order by tno";
//    String sql = "select cno as \"课程编号\", cname as \"课程名称\", tno \"教师编号\" from course order by cno";
//    sql = "select tno, tname, age, tphone from teacher where tno = 1001";
//    sql = "select cno, cname, tno from course where cno = 300001";
//    sql = "select tno, tname, sex, age, tphone from teacher where tno in (1001, 1002, 1003)";
//    sql = "select tno, tname, sex, age, tphone from teacher where tno BETWEEN 1001 and 1004";
//    sql = "select tno, tname, sex, age, tphone from teacher where tno > 1001";
//    sql = "select tno, tname, sex, age, tphone from teacher where tno != 1001";
//    sql = "select tno, tname, sex, age, tphone from teacher where tno = 1001 or tno = 1003";
//    sql = "select tno, tname, sex, age, tphone from teacher where tno = 1001 and tno = 1003";
//    sql = "select sum(grade) from scores where cno =300001";
//    sql = "select count(1) from scores where cno =300001";
//    sql = "select avg(grade) from scores where cno =300001";
//    sql = "select max(grade) from scores where cno =300001";
//    sql = "select min(grade) from scores where cno =300001";
    //sql = "select sno, count(sno) from scores group by sno";
//    sql = "select term, count(term) from scores group by term";
//    sql = "select scoreno, sno, grade from scores where cno = 300001 order by grade";
//    sql = "select distinct(cno) from scores";
//    sql = "select distinct(sno) from scores";
//    sql = "select a.scoreno, a.sno, a.cno, a.term, a.grade, b.cname from scores a join course b on a.cno = b.CNO where a.sno = 2002";
//      sql = "select a.scoreno, a.sno, a.cno, a.term, a.grade, b.sname from scores a join student b on a.sno = b.sno where a.sno = 2008";
//    sql = "select a.scoreno, a.sno, a.cno, a.term, a.grade, b.cname from scores a, course b where  a.cno = b.CNO and a.sno = 2002";
//    sql = "select a.scoreno, a.sno, a.cno, a.term, a.grade, b.sname from scores a, student b where a.sno = b.sno and a.sno = 2008";
//    sql = "select a.scoreno, a.sno, a.cno, a.term, a.grade, b.cname from scores a join course b on a.cno = b.CNO";
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
    return taskStats;
  }

  public static void main(String[] args) {
    new SelectBenchmark().runBenchmark();
  }
}
