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

package studio.raptor.ddal.common.builder;


/**
 * @since 3.0.0
 */
public class HashCodeBuilder implements Builder<Integer> {

  /**
   * Constant to use in building the hashCode.
   */
  private final int iConstant;

  /**
   * Running total of the hashCode.
   */
  private int iTotal = 0;

  /**
   * <p>
   * Uses two hard coded choices for the constants needed to build a <code>hashCode</code>.
   * </p>
   */
  public HashCodeBuilder() {
    iConstant = 37;
    iTotal = 17;
  }

  /**
   * <p> Append a <code>hashCode</code> for a <code>boolean</code>. </p> <p> This adds
   * <code>1</code> when true, and <code>0</code> when false to the <code>hashCode</code>. </p> <p>
   * This is in contrast to the standard <code>java.lang.Boolean.hashCode</code> handling, which
   * computes a <code>hashCode</code> value of <code>1231</code> for <code>java.lang.Boolean</code>
   * instances that represent <code>true</code> or <code>1237</code> for
   * <code>java.lang.Boolean</code> instances that represent <code>false</code>. </p> <p> This is in
   * accordance with the <i>Effective Java</i> design. </p>
   *
   * @param value the boolean to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final boolean value) {
    iTotal = iTotal * iConstant + (value ? 0 : 1);
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>boolean</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final boolean[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final boolean element : array) {
        append(element);
      }
    }
    return this;
  }

  // -------------------------------------------------------------------------

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>byte</code>.
   * </p>
   *
   * @param value the byte to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final byte value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  // -------------------------------------------------------------------------

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>byte</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final byte[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final byte element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>char</code>.
   * </p>
   *
   * @param value the char to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final char value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>char</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final char[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final char element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>double</code>.
   * </p>
   *
   * @param value the double to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final double value) {
    return append(Double.doubleToLongBits(value));
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>double</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final double[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final double element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>float</code>.
   * </p>
   *
   * @param value the float to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final float value) {
    iTotal = iTotal * iConstant + Float.floatToIntBits(value);
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>float</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final float[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final float element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>int</code>.
   * </p>
   *
   * @param value the int to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final int value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>int</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final int[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final int element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>long</code>.
   * </p>
   *
   * @param value the long to add to the <code>hashCode</code>
   * @return this
   */
  // NOTE: This method uses >> and not >>> as Effective Java and
  //       Long.hashCode do. Ideally we should switch to >>> at
  //       some stage. There are backwards compat issues, so
  //       that will have to wait for the time being. cf LANG-342.
  public HashCodeBuilder append(final long value) {
    iTotal = iTotal * iConstant + ((int) (value ^ (value >> 32)));
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>long</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final long[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final long element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>Object</code>.
   * </p>
   *
   * @param object the Object to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final Object object) {
    if (object == null) {
      iTotal = iTotal * iConstant;

    } else {
      if (object.getClass().isArray()) {
        // factor out array case in order to keep method small enough
        // to be inlined
        appendArray(object);
      } else {
        iTotal = iTotal * iConstant + object.hashCode();
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an array.
   * </p>
   *
   * @param object the array to add to the <code>hashCode</code>
   */
  private void appendArray(final Object object) {
    // 'Switch' on type of array, to dispatch to the correct handler
    // This handles multi dimensional arrays
    if (object instanceof long[]) {
      append((long[]) object);
    } else if (object instanceof int[]) {
      append((int[]) object);
    } else if (object instanceof short[]) {
      append((short[]) object);
    } else if (object instanceof char[]) {
      append((char[]) object);
    } else if (object instanceof byte[]) {
      append((byte[]) object);
    } else if (object instanceof double[]) {
      append((double[]) object);
    } else if (object instanceof float[]) {
      append((float[]) object);
    } else if (object instanceof boolean[]) {
      append((boolean[]) object);
    } else {
      // Not an array of primitives
      append((Object[]) object);
    }
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for an <code>Object</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final Object[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final Object element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>short</code>.
   * </p>
   *
   * @param value the short to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final short value) {
    iTotal = iTotal * iConstant + value;
    return this;
  }

  /**
   * <p>
   * Append a <code>hashCode</code> for a <code>short</code> array.
   * </p>
   *
   * @param array the array to add to the <code>hashCode</code>
   * @return this
   */
  public HashCodeBuilder append(final short[] array) {
    if (array == null) {
      iTotal = iTotal * iConstant;
    } else {
      for (final short element : array) {
        append(element);
      }
    }
    return this;
  }

  /**
   * <p>
   * Adds the result of super.hashCode() to this builder.
   * </p>
   *
   * @param superHashCode the result of calling <code>super.hashCode()</code>
   * @return this HashCodeBuilder, used to chain calls.
   * @since 2.0
   */
  public HashCodeBuilder appendSuper(final int superHashCode) {
    iTotal = iTotal * iConstant + superHashCode;
    return this;
  }

  /**
   * <p>
   * Return the computed <code>hashCode</code>.
   * </p>
   *
   * @return <code>hashCode</code> based on the fields appended
   */
  public int toHashCode() {
    return iTotal;
  }

  /**
   * Returns the computed <code>hashCode</code>.
   *
   * @return <code>hashCode</code> based on the fields appended
   * @since 3.0
   */
  @Override
  public Integer build() {
    return toHashCode();
  }

  /**
   * <p>
   * The computed <code>hashCode</code> from toHashCode() is returned due to the likelihood
   * of bugs in mis-calling toHashCode() and the unlikeliness of it mattering what the hashCode for
   * HashCodeBuilder itself is.</p>
   *
   * @return <code>hashCode</code> based on the fields appended
   * @since 2.5
   */
  @Override
  public int hashCode() {
    return toHashCode();
  }

  @Override
  public boolean equals(Object obj) {
    return  null != obj && !(obj instanceof HashCodeBuilder) && hashCode() == obj.hashCode();
  }
}
