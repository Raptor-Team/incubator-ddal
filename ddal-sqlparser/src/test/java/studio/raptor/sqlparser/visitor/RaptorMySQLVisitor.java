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

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlOutputVisitor;

/**
 * @author Sam
 * @since 3.0.0
 */
public class RaptorMySQLVisitor extends MySqlOutputVisitor {

  private List<String> selectItems;

  public RaptorMySQLVisitor() {
    super(null);
    selectItems = new ArrayList<>();
  }

  @Override
  public boolean visit(SQLSelectItem x) {
    if(x.getExpr() instanceof SQLIdentifierExpr) {
      selectItems.add(((SQLIdentifierExpr) x.getExpr()).getName());
    }
    return true;
  }

  public List<String> getSelectItems() {
    return selectItems;
  }
}
