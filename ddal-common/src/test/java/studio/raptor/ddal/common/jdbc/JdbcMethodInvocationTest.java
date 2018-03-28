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

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.exception.GenericException;

/**
 * @author Sam
 * @since 3.0.0
 */
public class JdbcMethodInvocationTest {

  Map<String, String> dataSet = new HashMap<String, String>() {
    {
      put("key", "value");
    }
  };

  @Test
  public void testMethodInvoke() throws NoSuchMethodException {

    Method method = null;
    for (Method m : Map.class.getDeclaredMethods()) {
      if (m.getName().equals("put")) {
        method = m;
        break;
      }
    }
    new JdbcMethodInvocation(method, new Object[]{"key1", "value1"}).invoke(dataSet);
    Assert.assertThat(dataSet.size(), Is.is(2));
  }

  @Test
  public void assertInvokeSuccess() throws NoSuchMethodException, SecurityException {
    JdbcMethodInvocation actual = new JdbcMethodInvocation(String.class.getMethod("length"),
        new Object[]{});
    actual.invoke("");
  }

  @Test(expected = GenericException.class)
  public void assertInvokeFailure() throws NoSuchMethodException, SecurityException {
    JdbcMethodInvocation actual = new JdbcMethodInvocation(
        String.class.getDeclaredMethod("indexOfSupplementary", int.class, int.class),
        new Object[]{1, 1});
    actual.invoke("");
  }
}
