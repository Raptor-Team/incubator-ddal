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
public class ConfigException extends RuntimeException {

  private Code code;
  private String message;

  ConfigException(Code code) {
    this(code, null);
  }

  ConfigException(Code code, String message) {
    this.code = code;
    this.message = message;
  }

  /**
   * Read the error Code for this exception
   *
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

  public static ConfigException create(Code code) {
    switch (code) {
      case PARSE_CONFIG_XML_ERROR:
        return new ParseConfigXmlException();
      case DECRYPT_KEY_CONFIG_ERROR:
        return new DecryptKeyConfigException();
      case DECRYPT_PASSWORD_ERROR:
        return new DecryptKeyConfigException();
      case READ_DECRYPT_KEY_ERROR:
        return new ReadDecryptKeyException();
      case CONFIG_NOT_FOUND_ERROR:
        return new ConfigNotFoundException();
      case SEQUENCE_INCORRECT_ERROR:
        return new SequenceIncorrectException();
      default:
        throw new IllegalArgumentException("Invalid exception code");
    }
  }

  public static ConfigException create(Code code, String message) {
    ConfigException ee = create(code);
    ee.setMessage(message);
    return ee;
  }

  public enum Code {

    // 解析配置XML异常
    PARSE_CONFIG_XML_ERROR(-127),

    // 解密公钥配置异常
    DECRYPT_KEY_CONFIG_ERROR(-126),

    // 解密异常
    DECRYPT_PASSWORD_ERROR(-125),

    // 读取解密密钥失败
    READ_DECRYPT_KEY_ERROR(-124),

    // 配置未发现
    CONFIG_NOT_FOUND_ERROR(-123),

    // 序列配置异常
    SEQUENCE_INCORRECT_ERROR(-122);

    private static final Map<Integer, Code> lookup = new HashMap<>();

    static {
      for (Code c : EnumSet.allOf(Code.class)) {
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
    public static Code get(int code) {
      return lookup.get(code);
    }
  }

  public static class ParseConfigXmlException extends ConfigException {
    ParseConfigXmlException() {
      super(Code.PARSE_CONFIG_XML_ERROR);
    }
  }

  public static class DecryptKeyConfigException extends ConfigException {
    DecryptKeyConfigException() {
      super(Code.DECRYPT_KEY_CONFIG_ERROR);
    }
  }

  public static class DecryptPasswordException extends ConfigException {
    DecryptPasswordException() {
      super(Code.DECRYPT_PASSWORD_ERROR);
    }
  }

  public static class ReadDecryptKeyException extends ConfigException {
    ReadDecryptKeyException() {
      super(Code.READ_DECRYPT_KEY_ERROR);
    }
  }

  public static class ConfigNotFoundException extends ConfigException {
    ConfigNotFoundException() {
      super(Code.CONFIG_NOT_FOUND_ERROR);
    }
  }

  public static class SequenceIncorrectException extends ConfigException {
    SequenceIncorrectException() {
      super(Code.SEQUENCE_INCORRECT_ERROR);
    }
  }
}
