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
import studio.raptor.ddal.common.util.StringUtil;

/**
 * @author Sam
 * @since 3.0.0
 */
public class GenericExceptionTest {

  @Test
  public void genericExceptionMetaTest() {
    GenericException ge = new GenericException(CommonErrorCodes.COMMON_500);
    Assert.assertThat(ge.getCode(), Is.is(CommonErrorCodes.COMMON_500.getCode()));
    Assert.assertThat(ge.getDesc(), Is.is(CommonErrorCodes.COMMON_500.getDesc()));
    Assert.assertNull(ge.getAdditionalInfo());

    ge = new GenericException(new IllegalArgumentException("For Testing"));
    ge = new GenericException(CommonErrorCodes.COMMON_500,
        new IllegalArgumentException("For Testing"));
    Assert.assertThat(ge.getCode(), Is.is(CommonErrorCodes.COMMON_500.getCode()));
    Assert.assertThat(ge.getDesc(), Is.is(CommonErrorCodes.COMMON_500.getDesc()));
    Assert.assertThat(ge.getAdditionalInfo(), Is.is(StringUtil.EMPTY));

    ge = new GenericException(CommonErrorCodes.COMMON_500, new Object[]{"java.sql.Driver"});
    Assert.assertThat(ge.getCode(), Is.is(CommonErrorCodes.COMMON_500.getCode()));
    Assert.assertThat(ge.getDesc(), Is.is(CommonErrorCodes.COMMON_500.getDesc()));
    Assert.assertNull(ge.getAdditionalInfo());

    ge = new GenericException(CommonErrorCodes.COMMON_500,
        new IllegalArgumentException("For Testing"), new Object[]{"java.sql.Driver"});
    Assert.assertThat(ge.getCode(), Is.is(CommonErrorCodes.COMMON_500.getCode()));
    Assert.assertThat(ge.getDesc(), Is.is(CommonErrorCodes.COMMON_500.getDesc()));
    Assert.assertNull(ge.getAdditionalInfo());

    ge = new GenericException(CommonErrorCodes.COMMON_500,
        new IllegalArgumentException("For Testing"), "", "java.sql.Driver");
    Assert.assertThat(ge.getCode(), Is.is(CommonErrorCodes.COMMON_500.getCode()));
    Assert.assertThat(ge.getDesc(), Is.is(CommonErrorCodes.COMMON_500.getDesc()));
    Assert.assertEquals("", ge.getAdditionalInfo());
  }
}
