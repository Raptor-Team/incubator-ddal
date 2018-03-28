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

package studio.raptor.ddal.core.engine.plan.node;

import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.core.engine.ProcessContext;

/**
 * 执行计划的判断节点
 *
 * @author Sam
 * @since 3.0.0
 */
public abstract class ForkingNode extends AbstractPlanNode {

  private static Logger logger = LoggerFactory.getLogger(ForkingNode.class);
  private List<PlanNode> forkingList = new ArrayList<>();

  protected abstract int judge(ProcessContext context);

  @Override
  public final void execute0(ProcessContext context) {
    int forkingIdx = judge(context);
    PlanNode forkingTo = forkingList.get(forkingIdx);
    if (logger.isDebugEnabled()) {
      logger.debug("{} -> {}", Strings.padStart(getNodeId(), PADDING_LEN, PADDING_CHAR),
          forkingTo.getNodeId());
    }
    forkingTo.execute0(context);
  }

  public void addForking(PlanNode forkingNode) {
    this.forkingList.add(forkingNode);
  }

  public List<PlanNode> getForkingList() {
    return forkingList;
  }

  /**
   * Forking is not to be solidified by default.
   *
   * @return false if node is not solidified.
   */
  @Override
  public boolean isPersistable() {
    return false;
  }
}
