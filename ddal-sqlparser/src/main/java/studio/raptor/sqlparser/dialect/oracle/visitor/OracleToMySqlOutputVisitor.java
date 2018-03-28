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
package studio.raptor.sqlparser.dialect.oracle.visitor;

import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOperator;
import studio.raptor.sqlparser.ast.expr.SQLIdentifierExpr;
import studio.raptor.sqlparser.ast.expr.SQLIntegerExpr;
import studio.raptor.sqlparser.ast.statement.SQLSelect;
import studio.raptor.sqlparser.ast.statement.SQLSelectItem;
import studio.raptor.sqlparser.ast.statement.SQLSelectQueryBlock;
import studio.raptor.sqlparser.ast.statement.SQLSelectStatement;
import studio.raptor.sqlparser.ast.statement.SQLSubqueryTableSource;
import studio.raptor.sqlparser.ast.statement.SQLTableSource;
import studio.raptor.sqlparser.dialect.oracle.ast.stmt.OracleSelectQueryBlock;

public class OracleToMySqlOutputVisitor extends OracleOutputVisitor {

  public OracleToMySqlOutputVisitor(Appendable appender, boolean printPostSemi) {
    super(appender, printPostSemi);
  }

  public OracleToMySqlOutputVisitor(Appendable appender) {
    super(appender);
  }

  static boolean isRowNumber(SQLExpr expr) {
    if (expr instanceof SQLIdentifierExpr) {
      String lownerName = ((SQLIdentifierExpr) expr).getLowerName();
      return "rownum".equals(lownerName);
    }

    return false;
  }

  public boolean visit(OracleSelectQueryBlock x) {
    boolean parentIsSelectStatment = false;
    {
      if (x.getParent() instanceof SQLSelect) {
        SQLSelect select = (SQLSelect) x.getParent();
        if (select.getParent() instanceof SQLSelectStatement || select
            .getParent() instanceof SQLSubqueryTableSource) {
          parentIsSelectStatment = true;
        }
      }
    }

    if (!parentIsSelectStatment) {
      return super.visit(x);
    }

    if (x.getWhere() instanceof SQLBinaryOpExpr //
        && x.getFrom() instanceof SQLSubqueryTableSource //
        ) {
      int rownum;
      String ident;
      SQLBinaryOpExpr where = (SQLBinaryOpExpr) x.getWhere();
      if (where.getRight() instanceof SQLIntegerExpr && where
          .getLeft() instanceof SQLIdentifierExpr) {
        rownum = ((SQLIntegerExpr) where.getRight()).getNumber().intValue();
        ident = ((SQLIdentifierExpr) where.getLeft()).getName();
      } else {
        return super.visit(x);
      }

      SQLSelect select = ((SQLSubqueryTableSource) x.getFrom()).getSelect();
      SQLSelectQueryBlock queryBlock = null;
      SQLSelect subSelect = null;
      SQLBinaryOpExpr subWhere = null;
      boolean isSubQueryRowNumMapping = false;

      if (select.getQuery() instanceof SQLSelectQueryBlock) {
        queryBlock = (SQLSelectQueryBlock) select.getQuery();
        if (queryBlock.getWhere() instanceof SQLBinaryOpExpr) {
          subWhere = (SQLBinaryOpExpr) queryBlock.getWhere();
        }

        for (SQLSelectItem selectItem : queryBlock.getSelectList()) {
          if (isRowNumber(selectItem.getExpr())) {
            if (isSubQuery(where, selectItem)) {
              isSubQueryRowNumMapping = true;
            }
          }
        }

        SQLTableSource subTableSource = queryBlock.getFrom();
        if (subTableSource instanceof SQLSubqueryTableSource) {
          subSelect = ((SQLSubqueryTableSource) subTableSource).getSelect();
        }
      }

      if ("ROWNUM".equalsIgnoreCase(ident)) {
        SQLBinaryOperator op = where.getOperator();
        Integer limit = null;
        if (op == SQLBinaryOperator.LessThanOrEqual) {
          limit = rownum;
        } else if (op == SQLBinaryOperator.LessThan) {
          limit = rownum - 1;
        }

        if (limit != null) {
          select.accept(this);
          println();
          print0(ucase ? "LIMIT " : "limit ");
          print(limit);
          return false;
        }
      } else if (isSubQueryRowNumMapping && null != subWhere) {
        SQLBinaryOperator op = where.getOperator();
        SQLBinaryOperator subOp = subWhere.getOperator();

        if (isRowNumber(subWhere.getLeft()) //
            && subWhere.getRight() instanceof SQLIntegerExpr) {

          int subRownum = ((SQLIntegerExpr) subWhere.getRight()).getNumber().intValue();

          Integer offset = null;
          if (op == SQLBinaryOperator.GreaterThanOrEqual) {
            offset = rownum + 1;
          } else if (op == SQLBinaryOperator.GreaterThan) {
            offset = rownum;
          }

          if (offset != null) {
            Integer limit = null;
            if (subOp == SQLBinaryOperator.LessThanOrEqual) {
              limit = subRownum - offset;
            } else if (subOp == SQLBinaryOperator.LessThan) {
              limit = subRownum - 1 - offset;
            }

            if (limit != null && null != subSelect) {
              subSelect.accept(this);
              println();
              print0(ucase ? "LIMIT " : "limit ");
              print(offset);
              print0(", ");
              print(limit);
              return false;
            }
          }
        }
      }
    }
    return super.visit(x);
  }

  private static boolean isSubQuery(SQLBinaryOpExpr where, SQLSelectItem selectItem){
    return where.getLeft() instanceof SQLIdentifierExpr
        && ((SQLIdentifierExpr) where.getLeft()).getName().equals(selectItem.getAlias());
  }
}
