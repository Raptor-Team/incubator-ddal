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

/**
 * @author Sam
 * @since 3.0.0
 */
public abstract class LinkedPlanNode extends AbstractPlanNode {

//  protected PlanNode pre;
  protected PlanNode next;

//  PlanNode pre() {
//    return pre;
//  }

  PlanNode next() {
    return next;
  }

//  public void setPre(PlanNode pre) {
//    this.pre = pre;
//  }

  public void setNext(PlanNode next) {
    this.next = next;
  }

  /**
   * Linked plan node is forced to solidify.
   *
   * @return true if this node is forced to solidify
   */
  @Override
  public boolean isPersistable() {
    return true;
  }
}
