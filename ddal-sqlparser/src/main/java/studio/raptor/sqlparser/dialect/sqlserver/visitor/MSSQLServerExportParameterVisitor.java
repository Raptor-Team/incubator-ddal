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
package studio.raptor.sqlparser.dialect.sqlserver.visitor;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLOrderBy;
import studio.raptor.sqlparser.ast.expr.SQLBetweenExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLInListExpr;
import studio.raptor.sqlparser.ast.expr.SQLMethodInvokeExpr;
import studio.raptor.sqlparser.ast.statement.SQLSelectGroupByClause;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.visitor.ExportParameterVisitor;
import studio.raptor.sqlparser.visitor.ExportParameterVisitorUtils;

public class MSSQLServerExportParameterVisitor extends SQLServerOutputVisitor implements
    ExportParameterVisitor {

  /**
   * true= if require parameterized sql output
   */
  private final boolean requireParameterizedOutput;

  public MSSQLServerExportParameterVisitor(final List<Object> parameters, final Appendable appender,
      final boolean wantParameterizedOutput) {
    super(appender, true);
    this.parameters = parameters;
    this.requireParameterizedOutput = wantParameterizedOutput;
  }

  public MSSQLServerExportParameterVisitor() {
    this(new ArrayList<Object>());
  }

  public MSSQLServerExportParameterVisitor(final List<Object> parameters) {
    this(parameters, new StringBuilder(), false);
  }

  public MSSQLServerExportParameterVisitor(final Appendable appender) {
    this(new ArrayList<Object>(), appender, true);
  }

  public List<Object> getParameters() {
    return parameters;
  }

  @Override
  public boolean visit(SQLSelectItem x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }
    return false;
  }

  @Override
  public boolean visit(SQLOrderBy x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }
    return false;
  }

  @Override
  public boolean visit(SQLSelectGroupByClause x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }
    return false;
  }

  @Override
  public boolean visit(SQLMethodInvokeExpr x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }

    ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getParameters());

    return true;
  }

  @Override
  public boolean visit(SQLInListExpr x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }
    ExportParameterVisitorUtils.exportParamterAndAccept(this.parameters, x.getTargetList());

    return true;
  }

  @Override
  public boolean visit(SQLBetweenExpr x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }
    ExportParameterVisitorUtils.exportParameter(this.parameters, x);
    return true;
  }

  public boolean visit(SQLBinaryOpExpr x) {
    if (requireParameterizedOutput) {
      return super.visit(x);
    }
    ExportParameterVisitorUtils.exportParameter(this.parameters, x);
    return true;
  }

}
