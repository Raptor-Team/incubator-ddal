package studio.raptor.ddal.core.engine.plan.node.impl.sequence;

import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.SQLStatementType;

/**
 * 判断解析结果是否存在SEQUENCE
 *
 * @author Charley
 * @since 1.0
 */
public class HasSequence extends ForkingNode {

  @Override
  protected int judge(ProcessContext context) {
    ParseResult parseResult = context.getParseResult();
    return parseResult.getSqlType().equals(SQLStatementType.SEQUENCE) && null != parseResult.getSequenceWrapper() ? 0 : 1;
  }
}
