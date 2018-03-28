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

package studio.raptor.ddal.core.parser;

import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.dialect.mysql.parser.MySqlStatementParser;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlOutputVisitor;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import studio.raptor.ddal.core.util.TestUtils;

/**
 * @author Sam
 * @since 3.0.0
 */
public class DalParserMySqlPerfTest {
  private String sql;

  @Before
  public void setUp() throws Exception {
    sql = "SELECT * FROM T";
    sql = "SELECT ID, NAME, AGE FROM USER WHERE ID = 1000  and name = 'Jack' order by age desc limit 0, 100";
//        sql = Utils.readFromResource("benchmark/sql/ob_sql.txt");
    sql = "insert into t_table a (a.id, a.name) values (1000, 2000)";
  }

  @Test
  public void test_pert() throws Exception {
//    for (int i = 0; i < 10; ++i) {
//      perfMySql(sql);
//    }
  }

  long perfMySql(String sql) {
    long startYGC = TestUtils.getYoungGC();
    long startYGCTime = TestUtils.getYoungGCTime();
    long startFGC = TestUtils.getFullGC();

    long startMillis = System.currentTimeMillis();
    for (int i = 0; i < 1000 * 1000; ++i) {
      execMySql(sql);
    }
    long millis = System.currentTimeMillis() - startMillis;

    long ygc = TestUtils.getYoungGC() - startYGC;
    long ygct = TestUtils.getYoungGCTime() - startYGCTime;
    long fgc = TestUtils.getFullGC() - startFGC;

    System.out.println("MySql\t" + millis + ", ygc " + ygc + ", ygct " + ygct + ", fgc " + fgc);
    return millis;
  }

  private String execMySql(String sql) {
    StringBuilder out = new StringBuilder();
    MySqlOutputVisitor visitor = new MySqlOutputVisitor(out);
    MySqlStatementParser parser = new MySqlStatementParser(sql);
    List<SQLStatement> statementList = parser.parseStatementList();
    // for (SQLStatement statement : statementList) {
    // statement.accept(visitor);
    // visitor.println();
    // }
    return out.toString();
  }
}
