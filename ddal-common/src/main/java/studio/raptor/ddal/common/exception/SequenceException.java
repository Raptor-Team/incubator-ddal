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
public class SequenceException extends RuntimeException {

  private Code code;
  private String message;

  SequenceException(Code code) {
    this(code, null);
  }

  SequenceException(Code code, String message) {
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

  public static SequenceException create(Code code) {
    switch (code) {
      case CREATE_SEQUENCE_SERVER_ERROR:
        return new CreateSequenceServerException();
      case LOAD_SEQUENCE_ERROR:
        return new LoadSequenceException();
      case FETCH_SEQUENCE_ERROR:
        return new FetchSequenceException();
      default:
        throw new IllegalArgumentException("Invalid exception code");
    }
  }

  public static SequenceException create(Code code, String message) {
    SequenceException ee = create(code);
    ee.setMessage(message);
    return ee;
  }

  public enum Code {

    /**
     * 创建序列服务异常
     */
    CREATE_SEQUENCE_SERVER_ERROR(-127),

    /**
     * 加载Sequence异常
     */
    LOAD_SEQUENCE_ERROR(-126),

    /**
     * 获取序列异常
     */
    FETCH_SEQUENCE_ERROR(-125),
    ;

    private static final Map<Integer,SequenceException.Code> lookup = new HashMap<>();

    static {
      for(SequenceException.Code c : EnumSet.allOf(SequenceException.Code.class)) {
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
    public static SequenceException.Code get(int code) {
      return lookup.get(code);
    }
  }

  public static class CreateSequenceServerException extends SequenceException {
    CreateSequenceServerException() {
      super(Code.CREATE_SEQUENCE_SERVER_ERROR);
    }
  }

  public static class LoadSequenceException extends SequenceException {
    LoadSequenceException() {
      super(Code.LOAD_SEQUENCE_ERROR);
    }
  }

  public static class FetchSequenceException extends SequenceException {
    FetchSequenceException() {
      super(Code.FETCH_SEQUENCE_ERROR);
    }
  }
}
