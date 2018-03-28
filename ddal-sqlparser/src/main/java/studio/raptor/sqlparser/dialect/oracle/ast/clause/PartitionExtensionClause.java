/*
 * Copyright 1999-2017 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package studio.raptor.sqlparser.dialect.oracle.ast.clause;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLName;
import studio.raptor.sqlparser.dialect.oracle.ast.OracleSQLObjectImpl;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleASTVisitor;

public class PartitionExtensionClause extends OracleSQLObjectImpl {

  private final List<SQLName> target = new ArrayList<SQLName>();
  private boolean subPartition;
  private SQLName partition;

  public boolean isSubPartition() {
    return subPartition;
  }

  public void setSubPartition(boolean subPartition) {
    this.subPartition = subPartition;
  }

  public SQLName getPartition() {
    return partition;
  }

  public void setPartition(SQLName partition) {
    this.partition = partition;
  }

  public List<SQLName> getFor() {
    return target;
  }

  @Override
  public void accept0(OracleASTVisitor visitor) {
    if (visitor.visit(this)) {
      acceptChild(visitor, partition);
      acceptChild(visitor, target);
    }
    visitor.endVisit(this);
  }

}
