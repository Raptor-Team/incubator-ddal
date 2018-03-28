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

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;

/**
 * ByteUnit Test Cases
 *
 * @author Sam
 * @since 3.0.0
 */
public class ByteUnitTest {

  @Test
  public void testByteUnit() {
    Assert.assertThat(ByteUnit.KiB.toBytes(1), Is.is(1024.0));
    Assert.assertThat(ByteUnit.KiB.convertFrom(1, ByteUnit.MiB), Is.is(1024L));
    Assert.assertThat(ByteUnit.MiB.convertFrom(1024, ByteUnit.KiB), Is.is(1L));
    Assert.assertThat(ByteUnit.KiB.toBytes(1), Is.is(1024d));
    Assert.assertThat(ByteUnit.MiB.toKiB(1), Is.is(1024L));
    Assert.assertThat(ByteUnit.GiB.toMiB(1), Is.is(1024L));
    Assert.assertThat(ByteUnit.TiB.toGiB(1), Is.is(1024L));
    Assert.assertThat(ByteUnit.PiB.toTiB(1), Is.is(1024L));
    Assert.assertThat(ByteUnit.TiB.toPiB(1024), Is.is(1L));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentsException() {
    Assert.assertThat(ByteUnit.KiB.toBytes(-1), Is.is(1024d));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIllegalArgumentsException2() {
    Assert.assertThat(ByteUnit.PiB.toTiB(Long.MAX_VALUE), Is.is(1024L));
  }
}
