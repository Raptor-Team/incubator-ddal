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
 * error code for parser exception
 *
 * @author jack
 * @since 3.0.0
 */
public interface ParserErrCodes {

    ErrorCodeDefinition PARSE_300 = new ErrorCodeDefinition("PARSE", 300, "Cannot support database type [%s].", "");
    ErrorCodeDefinition PARSE_301 = new ErrorCodeDefinition("PARSE", 301, "Unsupported SQL statement: [%s]", "please refer to the manual of grammar");
    ErrorCodeDefinition PARSE_302 = new ErrorCodeDefinition("PARSE", 302, "LIMIT offset and row count can not be a negative value", "");
  ErrorCodeDefinition PARSE_303 = new ErrorCodeDefinition("PARSE", 303, "sql parameterize error",
      "Please check the sql and parameters");
  ErrorCodeDefinition PARSE_304 = new ErrorCodeDefinition("PARSE", 304, "sql unparameterize error",
      "please check the sql and parameters");

}
