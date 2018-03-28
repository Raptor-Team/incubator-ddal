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
 * Process error code definition.
 *
 * @author Sam
 * @since 1.0
 */
public interface ProcessErrCodes {
    ErrorCodeDefinition PROCESS_500 = new ErrorCodeDefinition("PROCESS", 500, "Could not deal with this operate", "Solution advice");
    ErrorCodeDefinition PROCESS_501 = new ErrorCodeDefinition("PROCESS", 501, "Can not issue data manipulation statements with executeQuery().", "");
    ErrorCodeDefinition PROCESS_502 = new ErrorCodeDefinition("PROCESS", 502, "Unsupported SQL statement: [%s]", "");
    ErrorCodeDefinition PROCESS_503 = new ErrorCodeDefinition("PROCESS", 503, "Invalid processor [%s]", "");
    ErrorCodeDefinition PROCESS_504 = new ErrorCodeDefinition("PROCESS", 504, "No virtual database found in this session, please check configuration.", "");
    ErrorCodeDefinition PROCESS_EXAMPLE_500 = new ErrorCodeDefinition("PROCESS_EXAMPLE", 500, "Could not deal with this operate, with args %s", "Solution advice");

    ErrorCodeDefinition PLAN_700 = new ErrorCodeDefinition("PLAN", 700, "Unsupported SQL statement: [%s]", "");

}
