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

package studio.raptor.ddal.jdbc.test;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.jdbc.JDBCType;

/**
 * Test case for {@link studio.raptor.ddal.jdbc.JDBCType}
 *
 * @author Sam
 * @since 3.0.0
 */
public class JDBCTypeTest {

  @Test
  public void testJDBCEnumPublicMethods() {
    Assert.assertEquals("java.sql", JDBCType.VARCHAR.getVendor());
    Assert.assertEquals("VARCHAR", JDBCType.VARCHAR.getName());
    Assert.assertThat(JDBCType.VARCHAR.getVendorTypeNumber(), Is.is(12));
    Assert.assertThat(JDBCType.TIMESTAMP, Is.is(JDBCType.valueOf("TIMESTAMP")));
    Assert.assertThat(JDBCType.BIGINT, Is.is(JDBCType.valueOf(-5)));
  }

  /**
   * 测试不支持的jdbc数据类型
   */
  @Test(expected = IllegalArgumentException.class)
  public void testJDBCTypeValueException() {
    JDBCType.valueOf(999);
  }
}
