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
public class ExecuteException extends RuntimeException {

  private Code code;

  ExecuteException(Code code, Throwable throwable) {
    super(throwable);
    this.code = code;
  }

  /**
   * Read the error Code for this exception
   *
   * @return the error Code for this exception
   */
  public Code code() {
    return code;
  }

  public static ExecuteException create(Code code, Throwable throwable) {
    switch (code) {
      case READ_WRITE_STRATEGY_CONFIG_ERROR:
        return new ReadWriteStrategyCfgErrException(throwable);
      case READ_WRITE_STRATEGY_INSTANCE_ERROR:
        return new ReadWriteStrategyInstanceException(throwable);
      case GET_READONLY_CONNECTION_FAILED_ERROR:
        return new GetReadOnlyConnectionFailedException(throwable);
      case GET_READWRITE_CONNECTION_FAILED_ERROR:
        return new GetReadWriteConnectionFailedException(throwable);
      case EXECUTION_ERROR_ON_PHYSICAL_DB:
        return new ExecutionErrorOnPhysicalDBException(throwable);
      default:
        throw new IllegalArgumentException("Invalid exception code");
    }
  }

  public enum Code {

    // 读写控制策略配置异常
    READ_WRITE_STRATEGY_CONFIG_ERROR(-127),
    // 读写控制策略实例化异常
    READ_WRITE_STRATEGY_INSTANCE_ERROR(-126),
    // 获取只读连接失败
    GET_READONLY_CONNECTION_FAILED_ERROR(-125),
    // 获取读写连接失败
    GET_READWRITE_CONNECTION_FAILED_ERROR(-124),
    // sql 执行异常
    EXECUTION_ERROR_ON_PHYSICAL_DB(-123),;

    private static final Map<Integer, ExecuteException.Code> lookup = new HashMap<>();

    static {
      for (ExecuteException.Code c : EnumSet.allOf(ExecuteException.Code.class)) {
        lookup.put(c.code, c);
      }
    }

    private final int code;

    Code(int code) {
      this.code = code;
    }

    /**
     * Get the int value for a particular Code.
     *
     * @return error code as integer
     */
    public int intValue() {
      return code;
    }

    /**
     * Get the Code value for a particular integer error code
     *
     * @param code int error code
     * @return Code value corresponding to specified int code, or null
     */
    public static ExecuteException.Code get(int code) {
      return lookup.get(code);
    }
  }

  public static class ReadWriteStrategyCfgErrException extends ExecuteException {

    ReadWriteStrategyCfgErrException(Throwable throwable) {
      super(Code.READ_WRITE_STRATEGY_CONFIG_ERROR, throwable);
    }
  }

  public static class ReadWriteStrategyInstanceException extends ExecuteException {

    ReadWriteStrategyInstanceException(Throwable throwable) {
      super(Code.READ_WRITE_STRATEGY_INSTANCE_ERROR, throwable);
    }
  }

  public static class GetReadOnlyConnectionFailedException extends ExecuteException {

    GetReadOnlyConnectionFailedException(Throwable throwable) {
      super(Code.GET_READONLY_CONNECTION_FAILED_ERROR, throwable);
    }
  }

  public static class GetReadWriteConnectionFailedException extends ExecuteException {

    GetReadWriteConnectionFailedException(Throwable throwable) {
      super(Code.GET_READWRITE_CONNECTION_FAILED_ERROR, throwable);
    }
  }

  public static class ExecutionErrorOnPhysicalDBException extends ExecuteException {

    ExecutionErrorOnPhysicalDBException(Throwable throwable) {
      super(Code.EXECUTION_ERROR_ON_PHYSICAL_DB, throwable);
    }
  }
}
