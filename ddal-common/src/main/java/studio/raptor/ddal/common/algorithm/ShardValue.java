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

package studio.raptor.ddal.common.algorithm;

import com.google.common.collect.Range;
import java.util.Collection;
import java.util.Collections;

/**
 * 分片值.
 * <p>
 * 目前支持{@code =, IN, BETWEEN};
 * 不支持{@code , >, <=, >=, LIKE, NOT, NOT IN}.
 * </p>
 *
 * @author Charley
 * @since 1.0
 */
public final class ShardValue<T extends Comparable<?>> {

  private boolean empty = false;

  private final String columnName;

  private final T value;

  private final Collection<T> values;

  private final Range<T> valueRange;

  private ShardValue(final String columnName, final T value, final Collection<T> values,
      final Range<T> valueRange) {
    this.columnName = columnName;
    this.value = value;
    this.values = values;
    this.valueRange = valueRange;
  }

  public ShardValue(final String columnName, final T value) {
    this(columnName, value, Collections.<T>emptyList(), null);
  }

  public ShardValue(final String columnName, final Collection<T> values) {
    this(columnName, null, values, null);
  }

  public ShardValue(final String columnName, final Range<T> valueRange) {
    this(columnName, null, Collections.<T>emptyList(), valueRange);
  }

  public ShardValue() {
    this(null, null, null, null);
    this.empty = true;
  }

  /**
   * 获取分片值类型.
   *
   * @return 分片值类型
   */
  public ShardValueType getType() {
    if (null != value) {
      return ShardValueType.SINGLE;
    }
    if (null != values && !values.isEmpty()) {
      return ShardValueType.LIST;
    }
    if (null != valueRange) {
      return ShardValueType.RANGE;
    }
    return ShardValueType.OTHER;
  }

  public boolean isEmpty() {
    return empty;
  }

  public void setEmpty(boolean empty) {
    this.empty = empty;
  }

  public String getColumnName() {
    return columnName;
  }

  public T getValue() {
    return value;
  }

  public Collection<T> getValues() {
    return values;
  }

  public Range<T> getValueRange() {
    return valueRange;
  }

  @Override
  public String toString() {
    return "ShardValue{" +
        "columnName='" + columnName + '\'' +
        ", value=" + value +
        ", values=" + values +
        ", valueRange=" + valueRange +
        '}';
  }

  /**
   * 分片值类型.
   */
  public enum ShardValueType {
    SINGLE, LIST, RANGE, OTHER
  }
}
