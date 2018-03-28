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

package studio.raptor.ddal.common.util.parser;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.common.sql.CharTypes;

/**
 * @author Sam
 * @since 3.0.0
 */
public class CharTypesTest {

  @Test
  public void testIsHex() {
    for (char c = 0; c < 256; ++c) {
      if (c >= 'A' && c <= 'F') {
        Assert.assertTrue(CharTypes.isHex(c));
      } else if (c >= 'a' && c <= 'f') {
        Assert.assertTrue(CharTypes.isHex(c));
      } else if (c >= '0' && c <= '9') {
        Assert.assertTrue(CharTypes.isHex(c));
      }
    }
  }

  @Test
  public void testIsDigital() {
    for (char i = '0'; i <= '9'; i++) {
      Assert.assertTrue(CharTypes.isDigit(i));
    }
  }

  /**
   * A-Z, a-z, 0-9, _, $
   */
  @Test
  public void testIsIdentifier() {

    for (char c = 0; c < 256; ++c) {
      if (c >= 'A' && c <= 'Z') {
        Assert.assertTrue(CharTypes.isIdentifierChar(c));
      } else if (c >= 'a' && c <= 'z') {
        Assert.assertTrue(CharTypes.isIdentifierChar(c));
      } else if (c >= '0' && c <= '9') {
        Assert.assertTrue(CharTypes.isIdentifierChar(c));
      }
    }
    Assert.assertTrue(CharTypes.isIdentifierChar('_'));
    Assert.assertTrue(CharTypes.isIdentifierChar('$'));
    Assert.assertFalse(CharTypes.isIdentifierChar(((char) ('A' - 1))));
  }

  @Test
  public void testIsWhiteSpace() {
    Assert.assertFalse(CharTypes.isWhitespace('_'));
    Assert.assertTrue(CharTypes.isWhitespace(' '));
    Assert.assertTrue(CharTypes.isWhitespace('\n'));
    Assert.assertTrue(CharTypes.isWhitespace('\r'));
    Assert.assertTrue(CharTypes.isWhitespace('\t'));
    Assert.assertTrue(CharTypes.isWhitespace('\f'));
    Assert.assertTrue(CharTypes.isWhitespace('\b'));

  }
}
