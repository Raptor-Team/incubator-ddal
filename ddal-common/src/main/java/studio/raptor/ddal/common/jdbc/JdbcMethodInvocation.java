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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.ExecErrCodes;

/**
 * Reflection calls JDBC related methods of utils
 *
 * @author jack
 * @since 3.0.0
 */
public class JdbcMethodInvocation {

    private final Method method;

    private final Object[] arguments;

    public JdbcMethodInvocation(Method method, Object[] arguments) {
        this.method = method;
        this.arguments = arguments;
    }

    /**
     *  invoke the method
     *
     * @param target target object
     */
    public void invoke(final Object target) {
        try {
            method.invoke(target, arguments);
        } catch (final IllegalAccessException | InvocationTargetException ex) {
            throw new GenericException(ExecErrCodes.EXEC_204, ex);
        }
    }
}
