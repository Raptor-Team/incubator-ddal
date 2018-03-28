package studio.raptor.ddal.core.engine.plan.node.impl.sequence;

import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.parser.result.SequenceWrapper;
import studio.raptor.ddal.core.sequence.DefaultIdGenerator;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class HandleSequence extends ProcessNode{

  @Override
  protected void execute(ProcessContext context) {
    SequenceWrapper sequenceWrapper = context.getParseResult().getSequenceWrapper();
    String sequenceName = sequenceWrapper.getName();
    long nextId = DefaultIdGenerator.getInstance().nextId(sequenceName);
    ResultData resultData = new ResultData(sequenceName, nextId);
    context.setMergedResult(resultData);
  }
}
