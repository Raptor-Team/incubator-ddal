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

package studio.raptor.ddal.jdbc.adapter;

import java.sql.SQLException;
import java.sql.Wrapper;
import java.util.ArrayList;
import java.util.Collection;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.jdbc.JdbcMethodInvocation;

/**
 * JDBC Warpper adapter class
 *
 * @author jack
 * @since 3.0
 */
public class WrapperAdapter implements Wrapper {

  private final Collection<JdbcMethodInvocation> jdbcMethodInvocations = new ArrayList<>();


  @Override
  @SuppressWarnings("unchecked")
  public <T> T unwrap(Class<T> iface) throws SQLException {
    if (isWrapperFor(iface)) {
      return (T) this;
    }
    throw new SQLException(String.format("[%s] cannot be unwrapped as [%s]", getClass().getName(), iface.getName()));
  }

  @Override
  public boolean isWrapperFor(Class<?> iface) throws SQLException {
    return iface.isInstance(this);
  }


  /**
   * Record the method call.
   *
   * @param targetClass target class
   */
  protected final void recordMethodInvocation(final Class<?> targetClass, final String methodName, final Class<?>[] argumentTypes, final Object[] arguments) {
    try {
      jdbcMethodInvocations.add(new JdbcMethodInvocation(targetClass.getMethod(methodName, argumentTypes), arguments));
    } catch (final NoSuchMethodException ex) {
      throw new GenericException(ex);
    }
  }

  /**
   * Playback record method call.
   *
   * @param target target object
   */
  protected final void replayMethodsInvocation(final Object target) {
    for (JdbcMethodInvocation each : jdbcMethodInvocations) {
      each.invoke(target);
    }
  }

}
