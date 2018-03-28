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

package studio.raptor.ddal.common.util;

import java.io.Serializable;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ObjectUtil {

  /**
   * <p>Singleton used as a {@code null} placeholder where
   * {@code null} has another meaning.</p>
   *
   * <p>For example, in a {@code HashMap} the
   * {@link java.util.HashMap#get(java.lang.Object)} method returns
   * {@code null} if the {@code Map} contains {@code null} or if there
   * is no matching key. The {@code Null} placeholder can be used to
   * distinguish between these two cases.</p>
   *
   * <p>Another example is {@code Hashtable}, where {@code null}
   * cannot be stored.</p>
   *
   * <p>This instance is Serializable.</p>
   */
  public static final Null NULL = new Null();

  /**
   * <p>{@code ObjectUtils} instances should NOT be constructed in
   * standard programming. Instead, the static methods on the class should
   * be used, such as {@code ObjectUtils.defaultIfNull("a","b");}.</p>
   *
   * <p>This constructor is public to permit tools that require a JavaBean
   * instance to operate.</p>
   */
  public ObjectUtil() {
    super();
  }


  /**
   * <p>Returns a default value if the object passed is {@code null}.</p>
   *
   * <pre>
   * ObjectUtils.defaultIfNull(null, null)      = null
   * ObjectUtils.defaultIfNull(null, "")        = ""
   * ObjectUtils.defaultIfNull(null, "zz")      = "zz"
   * ObjectUtils.defaultIfNull("abc", *)        = "abc"
   * ObjectUtils.defaultIfNull(Boolean.TRUE, *) = Boolean.TRUE
   * </pre>
   *
   * @param <T> the type of the object
   * @param object  the {@code Object} to test, may be {@code null}
   * @param defaultValue  the default value to return, may be {@code null}
   * @return {@code object} if it is not {@code null}, defaultValue otherwise
   */
  public static <T> T defaultIfNull(final T object, final T defaultValue) {
    return object != null ? object : defaultValue;
  }

  /**
   * <p>Class used as a null placeholder where {@code null}
   * has another meaning.</p>
   *
   * <p>For example, in a {@code HashMap} the
   * {@link java.util.HashMap#get(java.lang.Object)} method returns
   * {@code null} if the {@code Map} contains {@code null} or if there is
   * no matching key. The {@code Null} placeholder can be used to distinguish
   * between these two cases.</p>
   *
   * <p>Another example is {@code Hashtable}, where {@code null}
   * cannot be stored.</p>
   */
  public static class Null implements Serializable {
    /**
     * Required for serialization support. Declare serialization compatibility with Commons Lang 1.0
     *
     * @see java.io.Serializable
     */
    private static final long serialVersionUID = 7092611880189329093L;

    /**
     * Restricted constructor - singleton.
     */
    Null() {
      super();
    }

    /**
     * <p>Ensure singleton.</p>
     *
     * @return the singleton value
     */
    private Object readResolve() {
      return ObjectUtil.NULL;
    }
  }

}
