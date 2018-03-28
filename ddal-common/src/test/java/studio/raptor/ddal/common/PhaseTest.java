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

package studio.raptor.ddal.common;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Sam
 * @since 3.0.0
 */
public class PhaseTest {

  @Test
  public void phaseMetaMethodsTest() {
    Assert.assertThat(Phase.ACCEPT.toString(), Is.is("Phase[code=-3,desc=接收]"));
    Assert.assertThat(Phase.ACCEPT.getCode(), Is.is(-3));
    Assert.assertThat(Phase.ACCEPT.getDesc(), Is.is("接收"));
  }

  /**
   * 防止Phase枚举列表被随意改动，此处做一个约束。
   */
  @Test
  public void phaseEnumListTest() {
    Assert.assertThat(Phase.ACCEPT.getCode(), Is.is(-3));
    Assert.assertThat(Phase.HANDSHAKE.getCode(), Is.is(-2));
    Assert.assertThat(Phase.AUTH.getCode(), Is.is(-1));
    Assert.assertThat(Phase.PARSE.getCode(), Is.is(1));
    Assert.assertThat(Phase.REWRITE.getCode(), Is.is(2));
    Assert.assertThat(Phase.ROUTE.getCode(), Is.is(3));
    Assert.assertThat(Phase.PREPARE.getCode(), Is.is(4));
    Assert.assertThat(Phase.EXECUTE.getCode(), Is.is(5));
    Assert.assertThat(Phase.SUBEXECUTE.getCode(), Is.is(6));
    Assert.assertThat(Phase.EXEEND.getCode(), Is.is(7));
    Assert.assertThat(Phase.MERGE.getCode(), Is.is(8));
    Assert.assertThat(Phase.TXSTART.getCode(), Is.is(9));
    Assert.assertThat(Phase.TXING.getCode(), Is.is(10));
    Assert.assertThat(Phase.TXEND.getCode(), Is.is(11));
  }
}
