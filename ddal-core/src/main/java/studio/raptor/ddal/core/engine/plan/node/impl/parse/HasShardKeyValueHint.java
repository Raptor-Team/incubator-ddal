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

package studio.raptor.ddal.core.engine.plan.node.impl.parse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;

/**
 * 是否有隐藏的分片键值对。
 *
 * @author Sam
 * @since 3.0.0
 */
public class HasShardKeyValueHint extends ForkingNode {

  private static Logger logger = LoggerFactory.getLogger(HasShardKeyValueHint.class);

  @Override
  protected int judge(ProcessContext context) {
    return null != context.getSqlHint().getShard() ? 0 : 1;
  }
}
