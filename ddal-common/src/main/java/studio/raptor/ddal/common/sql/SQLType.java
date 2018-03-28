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

package studio.raptor.ddal.common.sql;

import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.ParserErrCodes;

/**
 * @author jackcao
 */
public enum SQLType {
    OTHER(-1, "OTHER"),

    BEGIN(1, "BEGIN"), COMMIT(2, "COMMIT"), DELETE(3, "DELETE"), INSERT(4, "INSERT"), REPLACE(5, "REPLACE"),
    ROLLBACK(6, "ROLLBACK"), SELECT(7, "SELECT"), SET(8, "SET"), SHOW(9, "SHOW"), START(10, "START"), UPDATE(11,
            "UPDATE"),
    KILL(12, "KILL"), SAVEPOINT(13, "SAVEPOINT"), USE(14, "USE"), EXPLAIN(15, "EXPLAIN"), KILL_QUERY(16, "KILL_QUERY"),
    HELP(17, "HELP"), MYSQL_CMD_COMMENT(18, "MYSQL_CMD_COMMENT"), MYSQL_COMMENT(19, "MYSQL_COMMENT"), CALL(20, "CALL"),
    DESCRIBE(21, "DESCRIBE"), ALTER(22, "ALTER"), CREATE(23, "CREATE"), EXIT(24, "EXIT"), WHO(25, "WHO");

    public final int value;

    public final String name;

    SQLType(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public static SQLType valueOf(int i) {
        for (SQLType t : values()) {
            if (t.value() == i) {
                return t;
            }
        }
        throw new GenericException(ParserErrCodes.PARSE_301);
    }

    public int value() {
        return this.value;
    }

    @Override
    public String toString() {
        return "SQLType[value=" + this.value + ",desc=" + this.name + "]";
    }
}
