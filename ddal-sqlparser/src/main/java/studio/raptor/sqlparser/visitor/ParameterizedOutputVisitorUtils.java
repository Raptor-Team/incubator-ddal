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
package studio.raptor.sqlparser.visitor;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.SQLDataType;
import studio.raptor.sqlparser.ast.SQLExpr;
import studio.raptor.sqlparser.ast.SQLObject;
import studio.raptor.sqlparser.ast.SQLStatement;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOpExpr;
import studio.raptor.sqlparser.ast.expr.SQLBinaryOperator;
import studio.raptor.sqlparser.ast.expr.SQLIntegerExpr;
import studio.raptor.sqlparser.ast.expr.SQLLiteralExpr;
import studio.raptor.sqlparser.ast.expr.SQLVariantRefExpr;
import studio.raptor.sqlparser.ast.statement.SQLColumnDefinition;
import studio.raptor.sqlparser.ast.statement.SQLSelectOrderByItem;
import studio.raptor.sqlparser.dialect.db2.visitor.DB2OutputVisitor;
import studio.raptor.sqlparser.dialect.mysql.visitor.MySqlOutputVisitor;
import studio.raptor.sqlparser.dialect.oracle.visitor.OracleParameterizedOutputVisitor;
import studio.raptor.sqlparser.dialect.phoenix.visitor.PhoenixOutputVisitor;
import studio.raptor.sqlparser.dialect.postgresql.visitor.PGOutputVisitor;
import studio.raptor.sqlparser.dialect.sqlserver.ast.SQLServerTop;
import studio.raptor.sqlparser.dialect.sqlserver.visitor.SQLServerOutputVisitor;
import studio.raptor.sqlparser.parser.SQLParserUtils;
import studio.raptor.sqlparser.parser.SQLStatementParser;
import studio.raptor.sqlparser.util.JdbcUtils;

public class ParameterizedOutputVisitorUtils {

  public static final String ATTR_PARAMS_SKIP = "druid.parameterized.skip";
  public final static String ATTR_MERGED = "parameterized.mergedList";

  public static String parameterize(String sql, String dbType) {
    SQLStatementParser parser = SQLParserUtils.createSQLStatementParser(sql, dbType);
    List<SQLStatement> statementList = parser.parseStatementList();
    if (statementList.size() == 0) {
      return sql;
    }

    StringBuilder out = new StringBuilder(sql.length());
    ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);

    for (int i = 0; i < statementList.size(); i++) {
      if (i > 0) {
        out.append(";\n");
      }
      SQLStatement stmt = statementList.get(i);

      if (stmt.hasBeforeComment()) {
        stmt.getBeforeCommentsDirect().clear();
      }
      stmt.accept(visitor);
    }

    if (visitor.getReplaceCount() == 0
        && parser.getLexer().getCommentCount() == 0) {
      return sql;
    }

    return out.toString();
  }

  public static String parameterize(List<SQLStatement> statementList, String dbType) {
    StringBuilder out = new StringBuilder();
    ParameterizedVisitor visitor = createParameterizedOutputVisitor(out, dbType);

    for (int i = 0; i < statementList.size(); i++) {
      if (i > 0) {
        out.append(";\n");
      }
      SQLStatement stmt = statementList.get(i);

      if (stmt.hasBeforeComment()) {
        stmt.getBeforeCommentsDirect().clear();
      }
      stmt.accept(visitor);
    }

    return out.toString();
  }

  public static ParameterizedVisitor createParameterizedOutputVisitor(Appendable out,
      String dbType) {
    if (JdbcUtils.ORACLE.equals(dbType) || JdbcUtils.ALI_ORACLE.equals(dbType)) {
      return new OracleParameterizedOutputVisitor(out);
    }

    if (JdbcUtils.MYSQL.equals(dbType)
        || JdbcUtils.MARIADB.equals(dbType)
        || JdbcUtils.H2.equals(dbType)) {
      return new MySqlOutputVisitor(out, true);
    }

    if (JdbcUtils.POSTGRESQL.equals(dbType)
        || JdbcUtils.ENTERPRISEDB.equals(dbType)) {
      return new PGOutputVisitor(out, true);
    }

    if (JdbcUtils.SQL_SERVER.equals(dbType) || JdbcUtils.JTDS.equals(dbType)) {
      return new SQLServerOutputVisitor(out, true);
    }

    if (JdbcUtils.DB2.equals(dbType)) {
      return new DB2OutputVisitor(out, true);
    }

    if (JdbcUtils.PHOENIX.equals(dbType)) {
      return new PhoenixOutputVisitor(out, true);
    }

    return new SQLASTOutputVisitor(out, true);
  }

  public static boolean checkParameterize(SQLObject x) {
    if (Boolean.TRUE.equals(x.getAttribute(ParameterizedOutputVisitorUtils.ATTR_PARAMS_SKIP))) {
      return false;
    }

    SQLObject parent = x.getParent();

    if (parent instanceof SQLDataType //
        || parent instanceof SQLColumnDefinition //
        || parent instanceof SQLServerTop //
        //|| parent instanceof SQLAssignItem //
        || parent instanceof SQLSelectOrderByItem //
        ) {
      return false;
    }

    return true;
  }

  public static boolean visit(ParameterizedVisitor v, SQLVariantRefExpr x) {
    v.print('?');
    v.incrementReplaceCunt();

    if (v instanceof ExportParameterVisitor) {
      ExportParameterVisitorUtils.exportParameter(((ExportParameterVisitor) v).getParameters(), x);
    }
    return false;
  }

  static void putMergedArribute(SQLObject object, SQLObject item) {
    List<SQLObject> mergedList = (List<SQLObject>) object.getAttribute(ATTR_MERGED);
    if (mergedList == null) {
      mergedList = new ArrayList<SQLObject>();
      object.putAttribute(ATTR_MERGED, mergedList);
    }
    mergedList.add(item);
  }

  public static SQLBinaryOpExpr merge(ParameterizedVisitor v, SQLBinaryOpExpr x) {
    SQLExpr left = x.getLeft();
    SQLExpr right = x.getRight();
    SQLObject parent = x.getParent();

    if (left instanceof SQLLiteralExpr && right instanceof SQLLiteralExpr) {
      if (x.getOperator() == SQLBinaryOperator.Equality //
          || x.getOperator() == SQLBinaryOperator.NotEqual) {
        if ((left instanceof SQLIntegerExpr) && (right instanceof SQLIntegerExpr)) {
          if (((SQLIntegerExpr) left).getNumber().intValue() < 100) {
            left.putAttribute(ATTR_PARAMS_SKIP, true);
          }
          if (((SQLIntegerExpr) right).getNumber().intValue() < 100) {
            right.putAttribute(ATTR_PARAMS_SKIP, true);
          }
        } else {
          left.putAttribute(ATTR_PARAMS_SKIP, true);
          right.putAttribute(ATTR_PARAMS_SKIP, true);
        }
      }
      return x;
    }

    for (; ; ) {
      if (x.getRight() instanceof SQLBinaryOpExpr) {
        if (x.getLeft() instanceof SQLBinaryOpExpr) {
          SQLBinaryOpExpr leftBinaryExpr = (SQLBinaryOpExpr) x.getLeft();
          if (leftBinaryExpr.getRight().equals(x.getRight())) {
            x = leftBinaryExpr;
            v.incrementReplaceCunt();
            continue;
          }
        }
        SQLExpr mergedRight = merge(v, (SQLBinaryOpExpr) x.getRight());
        if (mergedRight != x.getRight()) {
          x = new SQLBinaryOpExpr(x.getLeft(), x.getOperator(), mergedRight);
          v.incrementReplaceCunt();
        }
        x.setParent(parent);
      }

      break;
    }

    if (x.getLeft() instanceof SQLBinaryOpExpr) {
      SQLExpr mergedLeft = merge(v, (SQLBinaryOpExpr) x.getLeft());
      if (mergedLeft != x.getLeft()) {
        SQLBinaryOpExpr tmp = new SQLBinaryOpExpr(mergedLeft, x.getOperator(), x.getRight());
        tmp.setParent(parent);
        x = tmp;
        v.incrementReplaceCunt();
      }
    }

    // ID = ? OR ID = ? => ID = ?
    if (x.getOperator() == SQLBinaryOperator.BooleanOr) {
      if ((x.getLeft() instanceof SQLBinaryOpExpr) && (x.getRight() instanceof SQLBinaryOpExpr)) {
        SQLBinaryOpExpr leftBinary = (SQLBinaryOpExpr) x.getLeft();
        SQLBinaryOpExpr rightBinary = (SQLBinaryOpExpr) x.getRight();

        if (mergeEqual(leftBinary, rightBinary)) {
          v.incrementReplaceCunt();
          leftBinary.setParent(x.getParent());
          putMergedArribute(leftBinary, rightBinary);
          return leftBinary;
        }

        if (isLiteralExpr(leftBinary.getLeft()) //
            && leftBinary.getOperator() == SQLBinaryOperator.BooleanOr) {
          if (mergeEqual(leftBinary.getRight(), x.getRight())) {
            v.incrementReplaceCunt();
            putMergedArribute(leftBinary, rightBinary);
            return leftBinary;
          }
        }
      }
    }

    return x;
  }

  private static boolean mergeEqual(SQLExpr a, SQLExpr b) {
    if (!(a instanceof SQLBinaryOpExpr)) {
      return false;
    }
    if (!(b instanceof SQLBinaryOpExpr)) {
      return false;
    }

    SQLBinaryOpExpr binaryA = (SQLBinaryOpExpr) a;
    SQLBinaryOpExpr binaryB = (SQLBinaryOpExpr) b;

    if (binaryA.getOperator() != SQLBinaryOperator.Equality) {
      return false;
    }

    if (binaryB.getOperator() != SQLBinaryOperator.Equality) {
      return false;
    }

    if (!(binaryA.getRight() instanceof SQLLiteralExpr || binaryA
        .getRight() instanceof SQLVariantRefExpr)) {
      return false;
    }

    if (!(binaryB.getRight() instanceof SQLLiteralExpr || binaryB
        .getRight() instanceof SQLVariantRefExpr)) {
      return false;
    }

    return binaryA.getLeft().toString().equals(binaryB.getLeft().toString());
  }

  private static boolean isLiteralExpr(SQLExpr expr) {
    if (expr instanceof SQLLiteralExpr) {
      return true;
    }

    if (expr instanceof SQLBinaryOpExpr) {
      SQLBinaryOpExpr binary = (SQLBinaryOpExpr) expr;
      return isLiteralExpr(binary.getLeft()) && isLiteralExpr(binary.getRight());
    }

    return false;
  }
}
