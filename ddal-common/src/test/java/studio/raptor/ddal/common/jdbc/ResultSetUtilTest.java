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

package studio.raptor.ddal.common.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import oracle.sql.TIMESTAMP;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.GenericException;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ResultSetUtilTest {

  @Test
  public void convertNullValueTest() {
    Assert.assertThat(ResultSetUtil.convertValue(null, byte.class), Is.is((Object) (byte) 0));
    Assert.assertThat(ResultSetUtil.convertValue(null, short.class), Is.is((Object) (short) 0));
    Assert.assertThat(ResultSetUtil.convertValue(null, int.class), Is.is((Object) (int) 0));
    Assert.assertThat(ResultSetUtil.convertValue(null, long.class), Is.is((Object) (long) 0));
    Assert.assertThat(ResultSetUtil.convertValue(null, double.class), Is.is((Object) (double) 0));
    Assert.assertThat(ResultSetUtil.convertValue(null, float.class), Is.is((Object) (float) 0));
    Assert.assertThat(ResultSetUtil.convertValue(null, Void.class), Is.is((Object) null));
  }

  @Test
  public void convertValueTest() {
    Assert.assertThat(ResultSetUtil.convertValue("Hi, Raptor", String.class),
        Is.is((Object) "Hi, Raptor"));
    Assert.assertThat(ResultSetUtil.convertValue(100, long.class), Is.is((Object) 100L));
    Assert.assertThat(ResultSetUtil.convertValue(100, Long.class), Is.is((Object) 100L));
    Assert.assertThat(ResultSetUtil.convertValue(100, int.class), Is.is((Object) 100));
    Assert.assertThat(ResultSetUtil.convertValue(100, short.class), Is.is((Object) (short) 100));
    Assert.assertThat(ResultSetUtil.convertValue(100, byte.class), Is.is((Object) (byte) 100));
    Assert.assertThat(ResultSetUtil.convertValue(100, double.class), Is.is((Object) (double) 100));
    Assert.assertThat(ResultSetUtil.convertValue(100, float.class), Is.is((Object) (float) 100));
    Assert.assertThat(ResultSetUtil.convertValue(100, BigDecimal.class),
        Is.is((Object) BigDecimal.valueOf(100)));
    Assert.assertThat(ResultSetUtil.convertValue(100, Object.class), Is.is((Object) 100));
    Assert.assertThat(ResultSetUtil.convertValue(100, String.class), Is.is((Object) "100"));

    Object obj = new ArrayList<>();
    Assert.assertThat(ResultSetUtil.convertValue(obj, String.class), Is.is((Object) "[]"));
  }


  @Test(expected = GenericException.class)
  public void convertValueTestUnsupported() {
    Assert.assertThat(ResultSetUtil.convertValue(100, BigInteger.class),
        Is.is((Object) BigInteger.valueOf(100)));
  }

  @Test
  public void testConvertOracleTimestamp() {
    TIMESTAMP ts = new TIMESTAMP(new Timestamp(1492274586930L));
    Timestamp javaTs = (Timestamp) ResultSetUtil.convertValue(ts, Timestamp.class);
//    Assert.assertEquals(javaTs.toString(), "2017-04-16 00:43:06.93");
    Assert.assertNotNull(javaTs);
  }
}
