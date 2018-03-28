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

package studio.raptor.ddal.common.exception.code;

import studio.raptor.ddal.common.exception.ErrorCodeDefinition;

/**
 * error code for jdbc exception
 *
 * @author jack
 * @since 3.0.0
 */
public interface JdbcErrCodes {

  ErrorCodeDefinition JDBC_600 = new ErrorCodeDefinition("JDBC", 600, "Unsupported data type [%s]",
      "");
  ErrorCodeDefinition JDBC_601 = new ErrorCodeDefinition("JDBC", 601, "Unsupported date type [%s]",
      "");
  ErrorCodeDefinition JDBC_602 = new ErrorCodeDefinition("JDBC", 602, "Malformed url [%s]", "");
  ErrorCodeDefinition JDBC_603 = new ErrorCodeDefinition("JDBC", 603,
      "[%s] is out of range [-127, 127]", "");
  ErrorCodeDefinition JDBC_604 = new ErrorCodeDefinition("JDBC", 604,
      "Column index [%s] is out of range", "");
  ErrorCodeDefinition JDBC_605 = new ErrorCodeDefinition("JDBC", 605,
      "Bad format for BigDecimal ''{0}'' in column {1}.", "");
  ErrorCodeDefinition JDBC_606 = new ErrorCodeDefinition("JDBC", 606,
      "Unsupported database dialect [%s]", "");
  ErrorCodeDefinition JDBC_607 = new ErrorCodeDefinition("JDBC", 607, "Init dataSource error [%s]",
      "");
}
