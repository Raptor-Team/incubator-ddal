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
public class RouteException extends RuntimeException {

  private Code code;
  private String message;

  RouteException(Code code) {
    this(code, null);
  }

  RouteException(Code code, String message) {
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

  public static RouteException create(Code code) {
    switch (code) {
      case DIFFERENT_SHARDS_ERROR:
        return new DifferentShardsException();
      case INIT_ALGORITHM_ERROR:
        return new InitAlgorithmException();
      default:
        throw new IllegalArgumentException("Invalid exception code");
    }
  }

  public static RouteException create(Code code, String message) {
    RouteException ee = create(code);
    ee.setMessage(message);
    return ee;
  }

  public enum Code {

    // SQL所包含表
    DIFFERENT_SHARDS_ERROR(-127),

    //初始化路由算法异常
    INIT_ALGORITHM_ERROR(-126),
    ;

    private static final Map<Integer,RouteException.Code> lookup = new HashMap<>();

    static {
      for(RouteException.Code c : EnumSet.allOf(RouteException.Code.class)) {
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
    public static RouteException.Code get(int code) {
      return lookup.get(code);
    }
  }

  public static class DifferentShardsException extends RouteException {
    DifferentShardsException() {
      super(Code.DIFFERENT_SHARDS_ERROR);
    }
  }

  public static class InitAlgorithmException extends RouteException {
    InitAlgorithmException() {
      super(Code.INIT_ALGORITHM_ERROR);
    }
  }
}
