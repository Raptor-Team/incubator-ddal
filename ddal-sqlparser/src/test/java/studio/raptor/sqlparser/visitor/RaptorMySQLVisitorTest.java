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

package studio.raptor.sqlparser.visitor;

import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.sqlparser.SQLUtils;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.util.JdbcConstants;

/**
 * @author Sam
 * @since 3.0.0
 */
public class RaptorMySQLVisitorTest {

  @Test
  public void testNativeVisitor() {
    String sql = "select id, order_id, order_name from t_order where id = 100 and name = 'Jackson'";
    RaptorMySQLVisitor visitor = new RaptorMySQLVisitor();
    List<SQLStatement> statementList = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
    for(SQLStatement statement : statementList) {
      statement.accept(visitor);
    }
    Assert.assertArrayEquals(new String[]{"id", "order_id", "order_name"}, visitor.getSelectItems().toArray());
  }

}
