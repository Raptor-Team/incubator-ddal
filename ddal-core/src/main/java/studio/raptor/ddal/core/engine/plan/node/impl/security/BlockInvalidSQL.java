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

package studio.raptor.ddal.core.engine.plan.node.impl.security;

import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.security.GrammarLimiter;

/**
 * 拦截异常SQL。
 * 异常SQL场景列举如下：
 * <ul>
 *   <li>update多张表的语句</li>
 * </ul>
 *
 * @author Sam
 * @since 3.0.0
 */
public class BlockInvalidSQL extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    GrammarLimiter.check(context.getParseResult(), context.getVirtualDb());
  }

  /**
   * 风险控制节点强制不实例化。
   *
   * @return false
   */
  @Override
  public boolean isPersistable() {
    return false;
  }
}
