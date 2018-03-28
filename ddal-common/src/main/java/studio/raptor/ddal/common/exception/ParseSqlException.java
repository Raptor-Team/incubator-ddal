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

package studio.raptor.ddal.common.exception;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ParseSqlException extends RuntimeException {

  private Code code;
  private String message;

  ParseSqlException(Code code) {
    this(code, null);
  }

  ParseSqlException(Code code, String message) {
    this.code = code;
    this.message = message;
  }
  /**
   * Read the error Code for this exception
   * @return the error Code for this exception
   */
  public Code code() {
    return code;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  @Override
  public String getMessage() {
    return message;
  }

  public static ParseSqlException create(Code code) {
    switch (code) {
      case UNSUPPORTED_SQL_ERROR:
        return new UnSupportSqlException();
      case PARSING_SQL_ERROR:
        return new ParsingSqlException();
      default:
        throw new IllegalArgumentException("Invalid exception code");
    }
  }

  public static ParseSqlException create(Code code, String message) {
    ParseSqlException ee = create(code);
    ee.setMessage(message);
    return ee;
  }

  public enum Code {

    // 不支持语句异常
    UNSUPPORTED_SQL_ERROR(-127),
    // 解析语句异常
    PARSING_SQL_ERROR(-126),
    ;

    private static final Map<Integer,ParseSqlException.Code> lookup = new HashMap<>();

    static {
      for(ParseSqlException.Code c : EnumSet.allOf(ParseSqlException.Code.class)) {
        lookup.put(c.code, c);
      }
    }

    private final int code;
    Code(int code) {
      this.code = code;
    }

    /**
     * Get the int value for a particular Code.
     * @return error code as integer
     */
    public int intValue() { return code; }

    /**
     * Get the Code value for a particular integer error code
     * @param code int error code
     * @return Code value corresponding to specified int code, or null
     */
    public static ParseSqlException.Code get(int code) {
      return lookup.get(code);
    }
  }

  public static class UnSupportSqlException extends ParseSqlException {
    UnSupportSqlException() {
      super(Code.UNSUPPORTED_SQL_ERROR);
    }
  }

  public static class ParsingSqlException extends ParseSqlException {
    ParsingSqlException() {
      super(Code.PARSING_SQL_ERROR);
    }
  }
}
