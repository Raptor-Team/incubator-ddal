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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.core.engine.ProcessContext;

/**
 * 过程节点。此种类型的节点是最常用的逻辑节点。该节点执行结束
 * 会流转至下一个节点执行。
 *
 * @author Sam
 * @since 3.0.0
 */
public abstract class ProcessNode extends LinkedPlanNode {

  private static Logger logger = LoggerFactory.getLogger(ProcessNode.class);
  protected abstract void execute(ProcessContext context);

  /**
   * 执行ProcessNode，并路由至下一级节点。
   *
   * @param context 执行上下文
   */
  @Override
  public void execute0(ProcessContext context) {
    execute(context);
    if (isPersistable() && context.isPlanPersistable()) {
      context.getPlanInstance().appendInsNode(newInstance());
    }
    if (null != next()) {
      if(logger.isDebugEnabled()) {
        logger.debug("{} -> {}", Strings.padStart(getNodeId(), PADDING_LEN, PADDING_CHAR), next().getNodeId());
      }
      next().execute0(context);
    } else {
      if(logger.isDebugEnabled()) {
        logger.debug("Final node {} finished.", getNodeId());
      }
    }
  }

  private ProcessNode newInstance() {
    try {
      ProcessNode pn = this.getClass().newInstance();
      pn.setNodeId(getNodeId());
      return pn;
    } catch (IllegalAccessException | InstantiationException e) {
      throw new RuntimeException("Can't create instance of " + this.getClass());
    }
  }
}
