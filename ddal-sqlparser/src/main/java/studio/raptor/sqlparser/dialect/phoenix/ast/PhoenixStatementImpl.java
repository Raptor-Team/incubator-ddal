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
package studio.raptor.sqlparser.dialect.phoenix.ast;

import studio.raptor.sqlparser.ast.SQLStatementImpl;
import studio.raptor.sqlparser.dialect.phoenix.visitor.PhoenixASTVisitor;
import studio.raptor.sqlparser.util.JdbcConstants;
import studio.raptor.sqlparser.visitor.SQLASTVisitor;

/**
 * Created by wenshao on 16/9/14.
 */
public abstract class PhoenixStatementImpl extends SQLStatementImpl implements PhoenixObject {

  public PhoenixStatementImpl() {
    super(JdbcConstants.PHOENIX);
  }

  @Override
  protected void accept0(SQLASTVisitor visitor) {
    if (visitor instanceof PhoenixASTVisitor) {
      accept0((PhoenixASTVisitor) visitor);
      return;
    }

    super.accept0(visitor);
  }

  @Override
  public abstract void accept0(PhoenixASTVisitor visitor);
}
