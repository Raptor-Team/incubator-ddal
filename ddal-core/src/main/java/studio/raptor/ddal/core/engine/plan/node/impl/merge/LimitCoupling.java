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

package studio.raptor.ddal.core.engine.plan.node.impl.merge;

import java.util.List;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ProcessNode;
import studio.raptor.ddal.core.merger.memory.coupling.LimitCouplingResultData;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 处理Limit。
 *
 * @author Sam
 * @since 3.0.0
 */
public class LimitCoupling extends ProcessNode {

  @Override
  protected void execute(ProcessContext context) {
    ParseResult parseResult = context.getParseResult();
    List<Object> parameters = context.getSqlParameters();
    context.setMergedResult(new LimitCouplingResultData(context.getMergedResult(),
        parseResult.getLimit().parseOffset(parameters),
        parseResult.getLimit().parseRowCount(parameters)).couple());
  }
}
