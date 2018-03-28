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

package studio.raptor.ddal.common.bytes;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.util.StringUtil;

/**
 * Test cases for {@link BytesTools}
 *
 * @author Sam
 * @since 3.0.0
 */
public class BytesToolsTest {

  //########################################################
  // 读取字节码第一个整形
  //########################################################
  @Test
  public void testGetInt() {
    Assert.assertThat(BytesTools.getInt(" 1Hello".getBytes()), Is.is(1));
    Assert.assertThat(BytesTools.getInt(" +1Hello".getBytes()), Is.is(1));
    Assert.assertThat(BytesTools.getInt(" -1Hello".getBytes()), Is.is(-1));
    Assert.assertThat(BytesTools.getInt("1$Hello".getBytes()), Is.is(1));
    Assert.assertThat(BytesTools.getInt("1024Hello".getBytes()), Is.is(1024));
  }

  @Test(expected = NumberFormatException.class)
  public void testGetIntFromNoIntBuffer() {
    Assert.assertThat(BytesTools.getInt("Hello".getBytes()), Is.is(1024));
  }

  @Test(expected = NumberFormatException.class)
  public void testGetIntFromAllWhiteSpace() {
    Assert.assertThat(BytesTools.getInt("   ".getBytes()), Is.is(1));
  }

  @Test(expected = NumberFormatException.class)
  public void testIntegerOverflow() {
    Assert.assertThat(BytesTools.getInt("2147483648$Hello".getBytes()), Is.is(2147483647));
  }


  //########################################################
  // 读取字节码第一个长整形
  //########################################################
  @Test
  public void testGetLong() {
    Assert.assertThat(BytesTools.getLong(" 1Hello".getBytes()), Is.is(1L));
    Assert.assertThat(BytesTools.getLong(" +1Hello".getBytes()), Is.is(1L));
    Assert.assertThat(BytesTools.getLong(" -1Hello".getBytes()), Is.is(-1L));
    Assert.assertThat(BytesTools.getLong("1$Hello".getBytes()), Is.is(1L));
    Assert.assertThat(BytesTools.getLong("1024Hello".getBytes()), Is.is(1024L));
  }

  @Test(expected = NumberFormatException.class)
  public void testGetLongFromNoIntBuffer() {
    Assert.assertThat(BytesTools.getLong("Hello".getBytes()), Is.is(1024L));
  }

  @Test(expected = NumberFormatException.class)
  public void testGetLongFromAllWhiteSpace() {
    Assert.assertThat(BytesTools.getLong("   ".getBytes()), Is.is(1L));
  }

  @Test(expected = NumberFormatException.class)
  public void testLongOverflow() {
    Assert.assertThat(BytesTools.getLong("9223372036854775808L".getBytes()),
        Is.is(9223372036854775807L));
  }


  //########################################################
  // 读取字节码第一个短整形
  //########################################################
  @Test
  public void testGetShort() {
    Assert.assertThat(BytesTools.getShort(" 1Hello".getBytes()), Is.is((short) 1));
    Assert.assertThat(BytesTools.getShort(" +1Hello".getBytes()), Is.is((short) 1));
    Assert.assertThat(BytesTools.getShort(" -1Hello".getBytes()), Is.is((short) -1));
    Assert.assertThat(BytesTools.getShort("1$Hello".getBytes()), Is.is((short) 1));
    Assert.assertThat(BytesTools.getShort("1024Hello".getBytes()), Is.is((short) 1024));
  }

  @Test(expected = NumberFormatException.class)
  public void testGetShortFromNoIntBuffer() {
    Assert.assertThat(BytesTools.getShort("Hello".getBytes()), Is.is((short) 1024));
  }

  @Test(expected = NumberFormatException.class)
  public void testGetShortFromAllWhiteSpace() {
    Assert.assertThat(BytesTools.getShort("   ".getBytes()), Is.is((short) 1));
  }

  @Test(expected = NumberFormatException.class)
  public void testShortOverflow() {
    Assert.assertThat(BytesTools.getShort("32768".getBytes()), Is.is((short) 32767));
  }

  @Test
  public void testGetFloat() throws UnsupportedEncodingException {
    Assert.assertThat(BytesTools.getFloat("1024".getBytes()), Is.is((float) 1024.0));
  }

  @Test
  public void testGetDouble() throws UnsupportedEncodingException {
    Assert.assertThat(BytesTools.getDouble("1024".getBytes()), Is.is(1024.0));
  }

  @Test
  public void testNumberToBytes() throws UnsupportedEncodingException {

    byte[] expectedBytes = new byte[]{49, 48, 50, 52};
    Assert.assertArrayEquals(expectedBytes, BytesTools.long2Bytes(1024));
    Assert.assertArrayEquals(expectedBytes, BytesTools.int2Bytes(1024));
    Assert.assertArrayEquals(expectedBytes, BytesTools.short2Bytes((short) 1024));
    expectedBytes = new byte[]{49, 49, 49, 49};
    Assert.assertArrayEquals(expectedBytes, BytesTools.long2Bytes(1111));
    Assert.assertArrayEquals(expectedBytes, BytesTools.short2Bytes((short) 1111));
    Assert.assertArrayEquals(expectedBytes, BytesTools.int2Bytes(1111));

    expectedBytes = new byte[]{49, 48, 50, 52, 46, 48};
    Assert.assertArrayEquals(expectedBytes, BytesTools.float2Bytes(1024.0f));
    Assert.assertArrayEquals(expectedBytes, BytesTools.double2Bytes(1024.0d));
  }

  @Test
  public void testReadBytesFromBuffer() {
    byte[] srcBytes = new byte[]{49, 48, 50, 52, 46, 48};
    Assert.assertArrayEquals(srcBytes, BytesTools.toBytes(ByteBuffer.wrap(srcBytes)));
  }

  @Test
  public void testBytesToString() {
    byte[] srcBytes = new byte[]{49, 48, 50, 52, 46, 48};
    Assert.assertThat(BytesTools.toString(srcBytes), Is.is("1024.0"));
    Assert.assertNull(BytesTools.toString(null));

    Assert.assertThat(BytesTools.toString(srcBytes, 2), Is.is("24.0"));
    Assert.assertNull(BytesTools.toString(null, 2));
    Assert.assertThat(BytesTools.toString(srcBytes, 100), Is.is(""));

    Assert.assertThat(BytesTools.toString(srcBytes, 2, 4), Is.is("24.0"));
    Assert.assertNull(BytesTools.toString(null, 2, 1));
    Assert.assertThat(BytesTools.toString(srcBytes, 2, 0), Is.is(""));
  }

  @Test
  public void testJoinTwoByteArray() {
    Assert.assertThat(
        BytesTools
            .toString(new byte[]{72, 101, 108, 108, 111}, ", ", new byte[]{87, 111, 114, 108, 100}),
        Is.is("Hello, World"));
  }

  @Test
  public void testToStringBinary() throws UnsupportedEncodingException {
    byte[] b = new byte[]{72, 101, 108, 108, 111};
    Assert.assertThat(BytesTools.toStringBinary(b), Is.is("Hello"));
    b = null;
    Assert.assertThat(BytesTools.toStringBinary(b), Is.is("null"));

    b = new byte[]{72, 101, 108, 108, 111};
    ByteBuffer buffer = ByteBuffer.wrap(b);
    Assert.assertThat(BytesTools.toStringBinary(buffer), Is.is("Hello"));
    buffer = null;
    Assert.assertThat(BytesTools.toStringBinary(buffer), Is.is("null"));

    buffer = ByteBuffer.wrap(new byte[]{});
    buffer = buffer.asReadOnlyBuffer();
    Assert.assertThat(BytesTools.toStringBinary(buffer), Is.is(StringUtil.EMPTY));

    Assert.assertThat(BytesTools.toStringBinary("中文".getBytes("UTF-8")),
        Is.is("\\xE4\\xB8\\xAD\\xE6\\x96\\x87"));
  }

  @Test
  public void testToBinaryFromHex() {
    Assert.assertThat(BytesTools.toBinaryFromHex((byte) 'A'), Is.is((byte) 10));
    Assert.assertThat(BytesTools.toBinaryFromHex((byte) '1'), Is.is((byte) 1));
  }

  @Test
  public void testToBytes() throws UnsupportedEncodingException {
    Assert.assertThat(BytesTools.toBytes("Hello"), Is.is("Hello".getBytes("UTF-8")));
    Assert.assertThat(BytesTools.toBytes(true), Is.is(new byte[]{-1}));
    Assert.assertThat(BytesTools.toBytes(false), Is.is(new byte[]{0}));
    Assert.assertThat(Arrays.toString(BytesTools.toBytes(256)), Is.is("[0, 0, 0, 0, 0, 0, 1, 0]"));
  }

  @Test
  public void testToBoolean() {
    Assert.assertThat(BytesTools.toBoolean(new byte[]{1}), Is.is(true));
    Assert.assertThat(BytesTools.toBoolean(new byte[]{0}), Is.is(false));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testToBooleanIllegalException() {
    Assert.assertThat(BytesTools.toBoolean(new byte[]{10, 1}), Is.is(false));
  }

  @Test
  public void testCompareTo() {
    Assert.assertThat(BytesTools.compareTo(new byte[]{10, 13}, new byte[]{10, 12}), Is.is(1));
    Assert.assertThat(BytesTools.compareTo(new byte[]{10, 13}, new byte[]{10, 13}), Is.is(0));
    Assert.assertThat(BytesTools.compareTo(new byte[]{10, 11}, new byte[]{10, 12}), Is.is(-1));
    Assert.assertThat(BytesTools.compareTo(new byte[]{10, 13}, 0, 2, new byte[]{10, 12}, 0, 2),
        Is.is(1));
    Assert.assertThat(BytesTools.compareTo(new byte[]{10, 13}, 0, 2, new byte[]{10, 13}, 0, 2),
        Is.is(0));
    Assert.assertThat(BytesTools.compareTo(new byte[]{10, 11}, 0, 2, new byte[]{10, 12}, 0, 2),
        Is.is(-1));
  }

  @Test
  public void testEquals() {
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, new byte[]{10, 12}), Is.is(false));
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, new byte[]{10, 13}), Is.is(true));
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, 0, 2, new byte[]{10, 12}, 0, 2),
        Is.is(false));
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, 0, 2, new byte[]{10, 13}, 0, 2),
        Is.is(true));
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, 0, 2, new byte[]{10, 13, 12}, 0, 3),
        Is.is(false));
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, ByteBuffer.wrap(new byte[]{10, 13})),
        Is.is(true));
    Assert.assertThat(BytesTools.equals(new byte[]{10, 13}, ByteBuffer.wrap(new byte[]{10, 12})),
        Is.is(false));

    byte[] left = new byte[]{1, 2, 3}, right = new byte[]{1, 2, 3};
    Assert.assertThat(BytesTools.equals(left, 0, 2, left, 0, 2), Is.is(true));
    Assert.assertThat(BytesTools.equals(left, 0, 0, right, 0, 0), Is.is(true));
  }

  @Test
  public void testToByteBinary() {
    Assert.assertThat(Arrays.toString(BytesTools.toBytesBinary("1024")), Is.is("[49, 48, 50, 52]"));
    Assert.assertThat(Arrays.toString(BytesTools.toBytesBinary("\\x7A")), Is.is("[122]"));
    Assert.assertThat(Arrays.toString(BytesTools.toBytesBinary("\\x7AHe\\xlloWorld")),
        Is.is("[122, 72, 101, 120, 108, 108, 111, 87, 111, 114, 108, 100]"));
  }

  @Test
  public void testStartsWith() {
    Assert.assertThat(BytesTools.startsWith(new byte[]{18, 34, 100}, new byte[]{18}), Is.is(true));
    Assert.assertThat(BytesTools.startsWith(new byte[]{18, 34, 100}, new byte[]{18, 1}),
        Is.is(false));
  }

  @Test
  public void testPadding() {
    Assert.assertThat(Arrays.toString(BytesTools.paddingInt(new byte[]{127, 127, 127, 127})),
        Is.is("[127, 127, 127, 127]"));
    Assert.assertThat(Arrays.toString(BytesTools.paddingInt(new byte[]{127, 127})),
        Is.is("[0, 0, 127, 127]"));
    Assert.assertThat(Arrays.toString(BytesTools.paddingLong(new byte[]{127, 127})),
        Is.is("[0, 0, 0, 0, 0, 0, 127, 127]"));
    Assert.assertThat(
        Arrays.toString(BytesTools.paddingLong(new byte[]{127, 127, 127, 127, 127, 127, 127, 127})),
        Is.is("[127, 127, 127, 127, 127, 127, 127, 127]"));
    Assert.assertThat(Arrays.toString(BytesTools.paddingShort(new byte[]{127, 127})),
        Is.is("[127, 127]"));
    Assert.assertThat(Arrays.toString(BytesTools.paddingShort(new byte[]{127})), Is.is("[0, 127]"));
  }
}
