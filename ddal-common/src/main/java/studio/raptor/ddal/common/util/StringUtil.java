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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

/**
 * @author Comb, Sam
 */
public class StringUtil {

  public static final String EMPTY = "";

  private static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
  private static final Random RANDOM = new Random();
  private static final char[] CHARS = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0', 'q', 'w',
      'e',
      'r', 't', 'y', 'u', 'i', 'o', 'p', 'a', 's', 'd', 'f', 'g', 'h', 'j', 'k', 'l', 'z', 'x', 'c',
      'v', 'b',
      'n', 'm', 'Q', 'W', 'E', 'R', 'T', 'Y', 'U', 'I', 'O', 'P', 'A', 'S', 'D', 'F', 'G', 'H', 'J',
      'K', 'L',
      'Z', 'X', 'C', 'V', 'B', 'N', 'M'};

  /**
   * 字符串hash算法：s[0]*31^(n-1) + s[1]*31^(n-2) + ... + s[n-1] <br>
   * 其中s[]为字符串的字符数组，换算成程序的表达式为：<br>
   * h = 31*h + s.charAt(i); => h = (h << 5) - h + s.charAt(i); <br>
   *
   * @param start hash for s.substring(start, end)
   * @param end hash for s.substring(start, end)
   */
  public static long hash(String s, int start, int end) {
    if (start < 0) {
      start = 0;
    }
    if (end > s.length()) {
      end = s.length();
    }
    long h = 0;
    for (int i = start; i < end; ++i) {
      h = (h << 5) - h + s.charAt(i);
    }
    return h;
  }

  public static byte[] encode(String src, String charset) {
    if (src == null) {
      return null;
    }
    try {
      return src.getBytes(charset);
    } catch (UnsupportedEncodingException e) {
      return src.getBytes();
    }
  }

  public static String decode(byte[] src, String charset) {
    return decode(src, 0, src.length, charset);
  }

  public static String decode(byte[] src, int offset, int length, String charset) {
    try {
      return new String(src, offset, length, charset);
    } catch (UnsupportedEncodingException e) {
      return new String(src, offset, length);
    }
  }

  public static String getRandomString(int size) {
    StringBuilder s = new StringBuilder(size);
    int len = CHARS.length;
    for (int i = 0; i < size; i++) {
      int x = RANDOM.nextInt();
      s.append(CHARS[(x < 0 ? -x : x) % len]);
    }
    return s.toString();
  }

  public static String safeToString(Object object) {
    try {
      return object.toString();
    } catch (Exception e) {
      return "<toString() failure: " + e + ">";
    }
  }

  /**
   * <p>Checks if a CharSequence is whitespace, empty ("") or null.</p>
   *
   * <pre>
   * StringUtils.isBlank(null)      = true
   * StringUtils.isBlank("")        = true
   * StringUtils.isBlank(" ")       = true
   * StringUtils.isBlank("bob")     = false
   * StringUtils.isBlank("  bob  ") = false
   * </pre>
   *
   * @param cs  the CharSequence to check, may be null
   * @return {@code true} if the CharSequence is null, empty or whitespace
   * @since 2.0
   * @since 3.0 Changed signature from isBlank(String) to isBlank(CharSequence)
   */
  public static boolean isBlank(final CharSequence cs) {
    int strLen;
    if (cs == null || (strLen = cs.length()) == 0) {
      return true;
    }
    for (int i = 0; i < strLen; i++) {
      if (!Character.isWhitespace(cs.charAt(i))) {
        return false;
      }
    }
    return true;
  }

  public static boolean isEmpty(final CharSequence cs) {
    return cs == null || cs.length() == 0;
  }

  public static byte[] hexString2Bytes(char[] hexString, int offset, int length) {
    if (hexString == null) {
      return null;
    }
    if (length == 0) {
      return EMPTY_BYTE_ARRAY;
    }
    boolean odd = length << 31 == Integer.MIN_VALUE;
    byte[] bs = new byte[odd ? (length + 1) >> 1 : length >> 1];
    for (int i = offset, limit = offset + length; i < limit; ++i) {
      char high, low;
      if (i == offset && odd) {
        high = '0';
        low = hexString[i];
      } else {
        high = hexString[i];
        low = hexString[++i];
      }
      int b;
      switch (high) {
        case '0':
          b = 0;
          break;
        case '1':
          b = 0x10;
          break;
        case '2':
          b = 0x20;
          break;
        case '3':
          b = 0x30;
          break;
        case '4':
          b = 0x40;
          break;
        case '5':
          b = 0x50;
          break;
        case '6':
          b = 0x60;
          break;
        case '7':
          b = 0x70;
          break;
        case '8':
          b = 0x80;
          break;
        case '9':
          b = 0x90;
          break;
        case 'a':
        case 'A':
          b = 0xa0;
          break;
        case 'b':
        case 'B':
          b = 0xb0;
          break;
        case 'c':
        case 'C':
          b = 0xc0;
          break;
        case 'd':
        case 'D':
          b = 0xd0;
          break;
        case 'e':
        case 'E':
          b = 0xe0;
          break;
        case 'f':
        case 'F':
          b = 0xf0;
          break;
        default:
          throw new IllegalArgumentException(
              "illegal hex-string: " + new String(hexString, offset, length));
      }
      switch (low) {
        case '0':
          break;
        case '1':
          b += 1;
          break;
        case '2':
          b += 2;
          break;
        case '3':
          b += 3;
          break;
        case '4':
          b += 4;
          break;
        case '5':
          b += 5;
          break;
        case '6':
          b += 6;
          break;
        case '7':
          b += 7;
          break;
        case '8':
          b += 8;
          break;
        case '9':
          b += 9;
          break;
        case 'a':
        case 'A':
          b += 10;
          break;
        case 'b':
        case 'B':
          b += 11;
          break;
        case 'c':
        case 'C':
          b += 12;
          break;
        case 'd':
        case 'D':
          b += 13;
          break;
        case 'e':
        case 'E':
          b += 14;
          break;
        case 'f':
        case 'F':
          b += 15;
          break;
        default:
          throw new IllegalArgumentException(
              "illegal hex-string: " + new String(hexString, offset, length));
      }
      bs[(i - offset) >> 1] = (byte) b;
    }
    return bs;
  }

  public static String dumpAsHex(byte[] src, int length) {
    StringBuilder out = new StringBuilder(length * 4);
    int p = 0;
    int rows = length / 8;
    for (int i = 0; (i < rows) && (p < length); i++) {
      int ptemp = p;
      for (int j = 0; j < 8; j++) {
        String hexVal = Integer.toHexString(src[ptemp] & 0xff);
        if (hexVal.length() == 1) {
          out.append('0');
        }
        out.append(hexVal).append(' ');
        ptemp++;
      }
      out.append("    ");
      for (int j = 0; j < 8; j++) {
        int b = 0xff & src[p];
        if (b > 32 && b < 127) {
          out.append((char) b).append(' ');
        } else {
          out.append(". ");
        }
        p++;
      }
      out.append('\n');
    }
    int n = 0;
    for (int i = p; i < length; i++) {
      String hexVal = Integer.toHexString(src[i] & 0xff);
      if (hexVal.length() == 1) {
        out.append('0');
      }
      out.append(hexVal).append(' ');
      n++;
    }
    for (int i = n; i < 8; i++) {
      out.append("   ");
    }
    out.append("    ");
    for (int i = p; i < length; i++) {
      int b = 0xff & src[i];
      if (b > 32 && b < 127) {
        out.append((char) b).append(' ');
      } else {
        out.append(". ");
      }
    }
    out.append('\n');
    return out.toString();
  }

  public static byte[] escapeEasternUnicodeByteStream(byte[] src, String srcString, int offset,
      int length) {
    if ((src == null) || (src.length == 0)) {
      return src;
    }
    int bytesLen = src.length;
    int bufIndex = 0;
    int strIndex = 0;
    ByteArrayOutputStream out = new ByteArrayOutputStream(bytesLen);
    while (true) {
      if (srcString.charAt(strIndex) == '\\') {// write it out as-is
        out.write(src[bufIndex++]);
      } else {// Grab the first byte
        int loByte = src[bufIndex];
        if (loByte < 0) {
          loByte += 256; // adjust for
        }
        // signedness/wrap-around
        out.write(loByte);// We always write the first byte
        if (loByte >= 0x80) {
          if (bufIndex < (bytesLen - 1)) {
            int hiByte = src[bufIndex + 1];
            if (hiByte < 0) {
              hiByte += 256; // adjust for
            }
            // signedness/wrap-around
            out.write(hiByte);// write the high byte here, and
            // increment the index for the high
            // byte
            bufIndex++;
            if (hiByte == 0x5C) {
              out.write(hiByte);// escape 0x5c if
            }
            // necessary
          }
        } else if (loByte == 0x5c) {
          if (bufIndex < (bytesLen - 1)) {
            int hiByte = src[bufIndex + 1];
            if (hiByte < 0) {
              hiByte += 256; // adjust for
            }
            // signedness/wrap-around
            if (hiByte == 0x62) {// we need to escape the 0x5c
              out.write(0x5c);
              out.write(0x62);
              bufIndex++;
            }
          }
        }
        bufIndex++;
      }
      if (bufIndex >= bytesLen) {
        break;// we're done
      }
      strIndex++;
    }
    return out.toByteArray();
  }

  public static String toString(byte[] bytes) {
    if (bytes == null || bytes.length == 0) {
      return "";
    }
    StringBuffer buffer = new StringBuffer();
    for (byte byt : bytes) {
      buffer.append((char) byt);
    }
    return buffer.toString();
  }

  public static boolean equalsIgnoreCase(String str1, String str2) {
    if (str1 == null) {
      return str2 == null;
    }
    return str1.equalsIgnoreCase(str2);
  }

  public static int countChar(String str, char c) {
    if (str == null || str.isEmpty()) {
      return 0;
    }
    final int len = str.length();
    int cnt = 0;
    for (int i = 0; i < len; ++i) {
      if (c == str.charAt(i)) {
        ++cnt;
      }
    }
    return cnt;
  }

  public static String replaceOnce(String text, String repl, String with) {
    return replace(text, repl, with, 1);
  }

  public static String replace(String text, String repl, String with) {
    return replace(text, repl, with, -1);
  }

  public static String replace(String text, String repl, String with, int max) {
    if ((text == null) || (repl == null) || (with == null) || (repl.length() == 0) || (max == 0)) {
      return text;
    }
    StringBuffer buf = new StringBuffer(text.length());
    int start = 0;
    int end = 0;
    while ((end = text.indexOf(repl, start)) != -1) {
      buf.append(text.substring(start, end)).append(with);
      start = end + repl.length();
      if (--max == 0) {
        break;
      }
    }
    buf.append(text.substring(start));
    return buf.toString();
  }

  public static String replaceChars(String str, char searchChar, char replaceChar) {
    if (str == null) {
      return null;
    }
    return str.replace(searchChar, replaceChar);
  }

  public static String replaceChars(String str, String searchChars, String replaceChars) {
    if ((str == null) || (str.length() == 0) || (searchChars == null) || (searchChars.length()
        == 0)) {
      return str;
    }
    char[] chars = str.toCharArray();
    int len = chars.length;
    boolean modified = false;
    for (int i = 0, isize = searchChars.length(); i < isize; i++) {
      char searchChar = searchChars.charAt(i);
      if ((replaceChars == null) || (i >= replaceChars.length())) {// 删除
        int pos = 0;
        for (int j = 0; j < len; j++) {
          if (chars[j] != searchChar) {
            chars[pos++] = chars[j];
          } else {
            modified = true;
          }
        }
        len = pos;
      } else {// 替换
        for (int j = 0; j < len; j++) {
          if (chars[j] == searchChar) {
            chars[j] = replaceChars.charAt(i);
            modified = true;
          }
        }
      }
    }
    if (!modified) {
      return str;
    }
    return new String(chars, 0, len);
  }

  /**
   * insert into tablexxx
   */
  public static String getTableName(String sql) {
    int pos = 0;
    boolean insertFound = false;
    boolean intoFound = false;
    int tableStartIndx = -1;
    int tableEndIndex = -1;
    while (pos < sql.length()) {
      char ch = sql.charAt(pos);
      if (ch <= ' ' || ch == '(') {//
        if (tableStartIndx > 0) {
          tableEndIndex = pos;
          break;
        } else {
          pos++;
          continue;
        }
      } else if (ch == 'i' || ch == 'I') {
        if (intoFound) {
          if (tableStartIndx == -1) {
            tableStartIndx = pos;
          }
          pos++;
        } else if (insertFound) {// into start
          pos = pos + 5;
          intoFound = true;
        } else {
          // insert start
          pos = pos + 7;
          insertFound = true;
        }
      } else {
        if (tableStartIndx == -1) {
          tableStartIndx = pos;
        }
        pos++;
      }

    }
    return sql.substring(tableStartIndx, tableEndIndex);
  }

  public static <T> ArrayList<T> toArray(T ts[]) {
    ArrayList<T> array = new ArrayList<T>(ts.length);
    for (T t : ts) {
      array.add(t);
    }
    return array;
  }

  public static <T> ArrayList<T> toArray(Set<T> ts) {
    ArrayList<T> array = new ArrayList<T>(ts.size());
    Iterator<T> it = ts.iterator();
    while (it.hasNext()) {
      array.add(it.next());
    }
    return array;
  }

  /**
   * Returns the given string, with comments removed
   *
   * @param src the source string
   * @param stringOpens characters which delimit the "open" of a string
   * @param stringCloses characters which delimit the "close" of a string, in counterpart order to
   * <code>stringOpens</code>
   * @param slashStarComments strip slash-star type "C" style comments
   * @param slashSlashComments strip slash-slash C++ style comments to end-of-line
   * @param hashComments strip #-style comments to end-of-line
   * @param dashDashComments strip "--" style comments to end-of-line
   * @return the input string with all comment-delimited data removed
   */
  public static String stripComments(String src, String stringOpens, String stringCloses,
      boolean slashStarComments,
      boolean slashSlashComments, boolean hashComments, boolean dashDashComments) {
    if (src == null) {
      return null;
    }

    StringBuffer buf = new StringBuffer(src.length());

    // It's just more natural to deal with this as a stream
    // when parsing..This code is currently only called when
    // parsing the kind of metadata that developers are strongly
    // recommended to cache anyways, so we're not worried
    // about the _1_ extra object allocation if it cleans
    // up the code

    StringReader sourceReader = new StringReader(src);

    int contextMarker = Character.MIN_VALUE;
    boolean escaped = false;
    int markerTypeFound = -1;

    int ind = 0;

    int currentChar = 0;

    try {
      while ((currentChar = sourceReader.read()) != -1) {

        if (markerTypeFound != -1 && currentChar == stringCloses.charAt(markerTypeFound)
            && !escaped) {
          contextMarker = Character.MIN_VALUE;
          markerTypeFound = -1;
        } else if ((ind = stringOpens.indexOf(currentChar)) != -1 && !escaped
            && contextMarker == Character.MIN_VALUE) {
          markerTypeFound = ind;
          contextMarker = currentChar;
        }

        if (contextMarker == Character.MIN_VALUE && currentChar == '/'
            && (slashSlashComments || slashStarComments)) {
          currentChar = sourceReader.read();
          if (currentChar == '*' && slashStarComments) {
            int prevChar = 0;
            while ((currentChar = sourceReader.read()) != '/' || prevChar != '*') {
              if (currentChar == '\r') {

                currentChar = sourceReader.read();
                if (currentChar == '\n') {
                  currentChar = sourceReader.read();
                }
              } else {
                if (currentChar == '\n') {

                  currentChar = sourceReader.read();
                }
              }
              if (currentChar < 0) {
                break;
              }
              prevChar = currentChar;
            }
            continue;
          } else if (currentChar == '/' && slashSlashComments) {
            while ((currentChar = sourceReader.read()) != '\n' && currentChar != '\r'
                && currentChar >= 0) {
              ;
            }
          }
        } else if (contextMarker == Character.MIN_VALUE && currentChar == '#' && hashComments) {
          // Slurp up everything until the newline
          while ((currentChar = sourceReader.read()) != '\n' && currentChar != '\r'
              && currentChar >= 0) {
            ;
          }
        } else if (contextMarker == Character.MIN_VALUE && currentChar == '-' && dashDashComments) {
          currentChar = sourceReader.read();

          if (currentChar == -1 || currentChar != '-') {
            buf.append('-');

            if (currentChar != -1) {
              buf.append(currentChar);
            }

            continue;
          }

          // Slurp up everything until the newline

          while ((currentChar = sourceReader.read()) != '\n' && currentChar != '\r'
              && currentChar >= 0) {
            ;
          }
        }

        if (currentChar != -1) {
          buf.append((char) currentChar);
        }
      }
    } catch (IOException ioEx) {
      // we'll never see this from a StringReader
    }

    return buf.toString();
  }

  /**
   * Determines whether or not the sting 'searchIn' contains the string
   * 'searchFor', disregarding case and leading whitespace
   *
   * @param searchIn the string to search in
   * @param searchFor the string to search for
   * @return true if the string starts with 'searchFor' ignoring whitespace
   */
  public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor) {
    return startsWithIgnoreCaseAndWs(searchIn, searchFor, 0);
  }

  /**
   * Determines whether or not the sting 'searchIn' contains the string
   * 'searchFor', disregarding case and leading whitespace
   *
   * @param searchIn the string to search in
   * @param searchFor the string to search for
   * @param beginPos where to start searching
   * @return true if the string starts with 'searchFor' ignoring whitespace
   */
  public static boolean startsWithIgnoreCaseAndWs(String searchIn, String searchFor, int beginPos) {
    if (searchIn == null) {
      return searchFor == null;
    }

    int inLength = searchIn.length();

    for (; beginPos < inLength; beginPos++) {
      if (!Character.isWhitespace(searchIn.charAt(beginPos))) {
        break;
      }
    }

    return startsWithIgnoreCase(searchIn, beginPos, searchFor);
  }

  /**
   * Determines whether or not the string 'searchIn' contains the string
   * 'searchFor', dis-regarding case starting at 'startAt' Shorthand for a
   * String.regionMatch(...)
   *
   * @param searchIn the string to search in
   * @param startAt the position to start at
   * @param searchFor the string to search for
   * @return whether searchIn starts with searchFor, ignoring case
   */
  public static boolean startsWithIgnoreCase(String searchIn, int startAt, String searchFor) {
    return searchIn.regionMatches(true, startAt, searchFor, 0, searchFor.length());
  }

  /**
   * if string is null,return ""
   *
   * @return string||""
   */
  public static String stringOrEmpty(String string) {
    if (null == string) {
      return "";
    } else {
      return string;
    }
  }


  /**
   * Returns the first non whitespace char, converted to upper case
   *
   * @param searchIn the string to search in
   * @return the first non-whitespace character, upper cased.
   */
  public static char firstNonWsCharUc(String searchIn) {
    return firstNonWsCharUc(searchIn, 0);
  }

  public static char firstNonWsCharUc(String searchIn, int startAt) {
    if (searchIn == null) {
      return 0;
    }
    int length = searchIn.length();
    for (int i = startAt; i < length; i++) {
      char c = searchIn.charAt(i);
      if (!Character.isWhitespace(c)) {
        return Character.toUpperCase(c);
      }
    }
    return 0;
  }
}
