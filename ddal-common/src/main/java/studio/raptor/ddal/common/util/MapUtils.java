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

import java.util.Map;

/**
 * @author Sam
 * @since 3.0.0
 */
public class MapUtils {

  /**
   * Gets a String from a Map in a null-safe manner.
   * <p>
   * The String is obtained via <code>toString</code>.
   *
   * @param <K>  the key type
   * @param map  the map to use
   * @param key  the key to look up
   * @return the value in the Map as a String, <code>null</code> if null map input
   */
  public static <K> String getString(final Map<? super K, ?> map, final K key) {
    if (map != null) {
      final Object answer = map.get(key);
      if (answer != null) {
        return answer.toString();
      }
    }
    return null;
  }

  /**
   * Looks up the given key in the given map, converting the result into
   * a string, using the default value if the the conversion fails.
   *
   * @param <K>  the key type
   * @param map  the map whose value to look up
   * @param key  the key of the value to look up in that map
   * @param defaultValue  what to return if the value is null or if the
   *   conversion fails
   * @return  the value in the map as a string, or defaultValue if the
   *   original value is null, the map is null or the string conversion fails
   */
  public static <K> String getString(final Map<? super K, ?> map, final K key, final String defaultValue) {
    String answer = getString(map, key);
    if (answer == null) {
      answer = defaultValue;
    }
    return answer;
  }
}
