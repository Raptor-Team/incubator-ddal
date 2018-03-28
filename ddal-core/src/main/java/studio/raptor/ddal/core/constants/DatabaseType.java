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
package studio.raptor.ddal.core.constants;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.CommonErrorCodes;
import studio.raptor.ddal.common.exception.code.ParserErrCodes;

/**
 * 支持的数据库类型
 *
 * @author jack
 */

public enum DatabaseType {

  H2, MySQL, Oracle, SQLServer, DB2, PostgreSQL;

  /**
   * 获取数据库类型枚举.
   *
   * @param databaseProductName 数据库类型
   * @return 数据库类型枚举
   */
  public static DatabaseType valueFrom(final String databaseProductName) {
    try {
      return DatabaseType.valueOf(databaseProductName);
    } catch (final IllegalArgumentException ex) {
      throw new GenericException(ParserErrCodes.PARSE_300, new Object[]{databaseProductName});
    }
  }

  public static DatabaseType ordinalFrom(int ordinal) {
    for (DatabaseType databaseType : DatabaseType.values()) {
      if (databaseType.ordinal() == ordinal) {
        return databaseType;
      }
    }
    throw new GenericException(CommonErrorCodes.COMMON_509, ordinal);
  }
}
