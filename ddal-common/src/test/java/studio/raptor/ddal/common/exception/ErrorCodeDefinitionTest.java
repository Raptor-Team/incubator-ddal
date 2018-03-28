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

package studio.raptor.ddal.common.exception;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.code.CommonErrorCodes;
import studio.raptor.ddal.common.exception.code.ExecErrCodes;
import studio.raptor.ddal.common.exception.code.JdbcErrCodes;
import studio.raptor.ddal.common.exception.code.RouteErrCodes;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ErrorCodeDefinitionTest {

  @Test
  public void errorCodeDefinitionMetaTest() {
    Assert.assertThat(RouteErrCodes.ROUTE_400.getCode(), Is.is(400));
    Assert.assertThat(CommonErrorCodes.COMMON_500.getType(), Is.is("POOL"));
    Assert.assertThat(ExecErrCodes.EXEC_200.getDesc(), Is.is("Unsupported data type: %s"));
    Assert.assertThat(JdbcErrCodes.JDBC_600.getSolution(), Is.is(""));
  }
}
