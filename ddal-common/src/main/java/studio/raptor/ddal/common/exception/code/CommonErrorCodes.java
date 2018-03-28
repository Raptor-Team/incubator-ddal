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
 * @author Sam
 * @since 3.0.0
 */
public interface CommonErrorCodes {

    ErrorCodeDefinition COMMON_500 = new ErrorCodeDefinition("POOL", 500, "Unsupported driver [%s]", "");
    ErrorCodeDefinition COMMON_501 = new ErrorCodeDefinition("POOL", 501, "Initialize connection pool error", "");
    ErrorCodeDefinition COMMON_502 = new ErrorCodeDefinition("POOL", 502, "Prepare connection pool error", "");
    ErrorCodeDefinition COMMON_503 = new ErrorCodeDefinition("POOL", 503, "Connection pool config param [%s] is missing.", "");
    ErrorCodeDefinition COMMON_504 = new ErrorCodeDefinition("POOL", 504, "Invalid connection pool param value [%s] of param [%s]", "");
    ErrorCodeDefinition COMMON_505 = new ErrorCodeDefinition("POOL", 505, "Unsupported pool param [%s] is ignored. ", "");

    ErrorCodeDefinition COMMON_506 = new ErrorCodeDefinition("TX", 506, "Transaction log path is not configured.", "Check system environment variable -Dddal.tx.log.path");
    ErrorCodeDefinition COMMON_507 = new ErrorCodeDefinition("TX", 507, "[%s] is not a valid transaction log path", "");
    ErrorCodeDefinition COMMON_508 = new ErrorCodeDefinition("TX", 508, "Load unfinished transaction failed, log file is %s", "");
    ErrorCodeDefinition COMMON_509 = new ErrorCodeDefinition("TX", 509, "Unsupported database ordinal of [%s]", "");
    ErrorCodeDefinition COMMON_510 = new ErrorCodeDefinition("TX", 510, "Duplicate transaction found, txId is [%s]", "");
    ErrorCodeDefinition COMMON_511 = new ErrorCodeDefinition("TX", 511, "Duplicate transaction commit found, txId is [%s]", "");

    ErrorCodeDefinition COMMON_512 = new ErrorCodeDefinition("POOL", 512, "DataSource is not available.", "");
    ErrorCodeDefinition COMMON_513 = new ErrorCodeDefinition("POOL", 513, "DataSource has no writable connection.", "");
    ErrorCodeDefinition COMMON_514 = new ErrorCodeDefinition("POOL", 514, "No available connection in pool.", "");

}
