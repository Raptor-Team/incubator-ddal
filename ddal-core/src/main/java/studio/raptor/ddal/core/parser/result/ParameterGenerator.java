package studio.raptor.ddal.core.parser.result;

import java.util.ArrayList;
import java.util.List;
import studio.raptor.sqlparser.ast.expr.SQLValuableExpr;
import studio.raptor.sqlparser.ast.expr.SQLVariantRefExpr;

/**
 * 参数生成器
 *
 * @author Charley
 * @since 1.0
 */
public class ParameterGenerator {

  /**
   * 参数化单个参数
   *
   * @param parameters
   * @param value
   * @return
   */
  public static Object parameterize(List<Object> parameters, Object value){
    return parseActualValue(parameters, value);
  }

  /**
   * 参数化数组
   *
   * @param parameters
   * @param values
   * @return
   */
  public static List<Object> parameterizeValues(List<Object> parameters, List<Object> values){
    List<Object> result = new ArrayList<>(values.size());
    for (Object value : values) {
      result.add(parseActualValue(parameters, value));
    }
    return result;
  }

  /**
   * 解析真实值
   *
   * @param parameters
   * @param value
   * @return
   */
  private static Object parseActualValue(List<Object> parameters, Object value){
    Object resultValue;
    resultValue = value instanceof SQLVariantRefExpr
        ? parameters.get(((SQLVariantRefExpr) value).getIndex())
        : value instanceof SQLValuableExpr ? ((SQLValuableExpr) value).getValue() : value;
    return resultValue;
  }
}
