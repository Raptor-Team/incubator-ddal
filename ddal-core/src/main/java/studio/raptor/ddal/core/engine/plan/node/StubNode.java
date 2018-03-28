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

import studio.raptor.ddal.core.engine.ProcessContext;

/**
 * 桩节点，是简单的路由节点，对整个流程起到承上启下的作用。因为没有具体的执行逻辑，
 * 所以桩节点的实现类都不应有什么逻辑。执行计划执行至当前节点时会直接跳转至后续节点。
 *
 * @author Sam
 * @since 3.0.0
 */
public abstract class StubNode extends LinkedPlanNode {

  @Override
  public void execute0(ProcessContext context) {
    if (null != next()) {
      next().execute0(context);
    }
  }

}
