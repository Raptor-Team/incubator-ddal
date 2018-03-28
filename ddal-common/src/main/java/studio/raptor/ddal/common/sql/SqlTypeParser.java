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

/**
 * @author Sam
 * @since 1.0
 */
public class SqlTypeParser {

    private SqlTypeParser() {
        // do nothing
    }

    public static int parse(String stmt) {
        int lenth = stmt.length();
        for (int i = 0; i < lenth; ++i) {
            switch (stmt.charAt(i)) {
                case ' ':
                case '\t':
                case '\r':
                case '\n':
                    continue;
                case '/':
                    // such as /*!40101 SET character_set_client =
                    // @saved_cs_client
                    // */;
                    if (i == 0 && stmt.charAt(1) == '*' && stmt.charAt(2) == '!' && stmt.charAt(lenth - 2) == '*'
                            && stmt.charAt(lenth - 1) == '/') {
                        return SQLType.MYSQL_CMD_COMMENT.value;
                    }
                case '#':
                    i = ParseUtil.comment(stmt, i);
                    if (i + 1 == lenth) {
                        return SQLType.MYSQL_COMMENT.value;
                    }
                    continue;
                case 'A':
                case 'a':
                    return alterCheak(stmt, i);
                case 'B':
                case 'b':
                    return beginCheck(stmt, i);
                case 'C':
                case 'c':
                    return commitOrCallOrCreateCheck(stmt, i);
                case 'D':
                case 'd':
                    return dCheck(stmt, i);
                case 'E':
                case 'e':
                    return explainOrExitCheck(stmt, i);
                case 'I':
                case 'i':
                    return insertCheck(stmt, i);
                case 'R':
                case 'r':
                    return rCheck(stmt, i);
                case 'S':
                case 's':
                    return sCheck(stmt, i);
                case 'U':
                case 'u':
                    return uCheck(stmt, i);
                case 'K':
                case 'k':
                    return killCheck(stmt, i);
                case 'H':
                case 'h':
                    return helpCheck(stmt, i);
                case 'W':
                case 'w':
                    return whoCheck(stmt, i);
                default:
                    return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // ALTER
    static int alterCheak(String stmt, int offset) {
        if (stmt.length() > offset + "LTER ".length()) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            if ((c1 == 'L' || c1 == 'l') && (c2 == 'T' || c2 == 't') && (c3 == 'E' || c3 == 'e')
                    && (c4 == 'R' || c4 == 'r')) {
                return (offset << 8) | SQLType.ALTER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // HELP' '
    static int helpCheck(String stmt, int offset) {
        if (stmt.length() > offset + "ELP ".length()) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            if ((c1 == 'E' || c1 == 'e') && (c2 == 'L' || c2 == 'l') && (c3 == 'P' || c3 == 'p')) {
                return (offset << 8) | SQLType.HELP.value;
            }
        }
        return SQLType.OTHER.value;
    }

    static int explainOrExitCheck(String stmt, int offset) {
        int sqlType = SQLType.OTHER.value;
        switch (stmt.charAt((offset + 2))) {
            case 'P':
            case 'p':
                sqlType = explainCheck(stmt, offset);
                break;
            case 'I':
            case 'i':
                sqlType = exitCheck(stmt, offset);
                break;
            default:
                sqlType = SQLType.OTHER.value;
        }
        return sqlType;
    }

    // EXPLAIN' '
    static int explainCheck(String stmt, int offset) {
        if (stmt.length() > offset + "XPLAIN ".length()) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            char c6 = stmt.charAt(++offset);
            char c7 = stmt.charAt(++offset);
            if ((c1 == 'X' || c1 == 'x') && (c2 == 'P' || c2 == 'p') && (c3 == 'L' || c3 == 'l')
                    && (c4 == 'A' || c4 == 'a') && (c5 == 'I' || c5 == 'i') && (c6 == 'N' || c6 == 'n')
                    && (c7 == ' ' || c7 == '\t' || c7 == '\r' || c7 == '\n')) {
                return (offset << 8) | SQLType.EXPLAIN.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // exit
    static int exitCheck(String stmt, int offset) {
        if (stmt.length() > offset + "xit".length()) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            if ((c1 == 'X' || c1 == 'x') && (c2 == 'I' || c2 == 'i') && (c3 == 'T' || c3 == 't')) {
                return (offset << 8) | SQLType.EXIT.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // KILL' '
    static int killCheck(String stmt, int offset) {
        if (stmt.length() > offset + "ILL ".length()) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            if ((c1 == 'I' || c1 == 'i') && (c2 == 'L' || c2 == 'l') && (c3 == 'L' || c3 == 'l')
                    && (c4 == ' ' || c4 == '\t' || c4 == '\r' || c4 == '\n')) {
                while (stmt.length() > ++offset) {
                    switch (stmt.charAt(offset)) {
                        case ' ':
                        case '\t':
                        case '\r':
                        case '\n':
                            continue;
                        case 'Q':
                        case 'q':
                            return killQueryCheck(stmt, offset);
                        default:
                            return (offset << 8) | SQLType.KILL.value;
                    }
                }
                return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // KILL QUERY' '
    static int killQueryCheck(String stmt, int offset) {
        if (stmt.length() > offset + "UERY ".length()) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            if ((c1 == 'U' || c1 == 'u') && (c2 == 'E' || c2 == 'e') && (c3 == 'R' || c3 == 'r')
                    && (c4 == 'Y' || c4 == 'y') && (c5 == ' ' || c5 == '\t' || c5 == '\r' || c5 == '\n')) {
                while (stmt.length() > ++offset) {
                    switch (stmt.charAt(offset)) {
                        case ' ':
                        case '\t':
                        case '\r':
                        case '\n':
                            continue;
                        default:
                            return (offset << 8) | SQLType.KILL_QUERY.value;
                    }
                }
                return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // BEGIN
    static int beginCheck(String stmt, int offset) {
        if (stmt.length() > offset + 4) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            if ((c1 == 'E' || c1 == 'e') && (c2 == 'G' || c2 == 'g') && (c3 == 'I' || c3 == 'i')
                    && (c4 == 'N' || c4 == 'n') && (stmt.length() == ++offset || ParseUtil.isEOF(stmt.charAt(offset)))) {
                return SQLType.BEGIN.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // COMMIT
    static int commitCheck(String stmt, int offset) {
        if (stmt.length() > offset + 5) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            if ((c1 == 'O' || c1 == 'o') && (c2 == 'M' || c2 == 'm') && (c3 == 'M' || c3 == 'm')
                    && (c4 == 'I' || c4 == 'i') && (c5 == 'T' || c5 == 't')
                    && (stmt.length() == ++offset || ParseUtil.isEOF(stmt.charAt(offset)))) {
                return SQLType.COMMIT.value;
            }
        }

        return SQLType.OTHER.value;
    }

    // CALL
    static int callCheck(String stmt, int offset) {
        if (stmt.length() > offset + 3) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            if ((c1 == 'A' || c1 == 'a') && (c2 == 'L' || c2 == 'l') && (c3 == 'L' || c3 == 'l')) {
                return SQLType.CALL.value;
            }
        }

        return SQLType.OTHER.value;
    }

    static int createCheck(String stmt, int offset) {
        if (stmt.length() > offset + 5) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            if ((c1 == 'R' || c1 == 'r') && (c2 == 'E' || c2 == 'e') && (c3 == 'A' || c3 == 'a')
                    && (c4 == 'T' || c4 == 't') && (c5 == 'E' || c5 == 'e')) {
                return SQLType.CREATE.value;
            }
        }

        return SQLType.OTHER.value;
    }

    static int commitOrCallOrCreateCheck(String stmt, int offset) {
        int sqlType = SQLType.OTHER.value;
        switch (stmt.charAt((offset + 1))) {
            case 'O':
            case 'o':
                sqlType = commitCheck(stmt, offset);
                break;
            case 'A':
            case 'a':
                sqlType = callCheck(stmt, offset);
                break;
            case 'R':
            case 'r':
                sqlType = createCheck(stmt, offset);
                break;
            default:
                sqlType = SQLType.OTHER.value;
        }
        return sqlType;
    }

    // DESCRIBE or desc or SQLType.DELETE.value' '
    static int dCheck(String stmt, int offset) {
        if (stmt.length() > offset + 4) {
            int res = describeCheck(stmt, offset);
            if (res == SQLType.DESCRIBE.value) {
                return res;
            }
        }
        // continue check
        if (stmt.length() > offset + 6) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            char c6 = stmt.charAt(++offset);
            if ((c1 == 'E' || c1 == 'e') && (c2 == 'L' || c2 == 'l') && (c3 == 'E' || c3 == 'e')
                    && (c4 == 'T' || c4 == 't') && (c5 == 'E' || c5 == 'e')
                    && (c6 == ' ' || c6 == '\t' || c6 == '\r' || c6 == '\n')) {
                return SQLType.DELETE.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // DESCRIBE' ' 或 desc' '
    static int describeCheck(String stmt, int offset) {
        // desc
        if (stmt.length() > offset + 4) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            if ((c1 == 'E' || c1 == 'e') && (c2 == 'S' || c2 == 's') && (c3 == 'C' || c3 == 'c')
                    && (c4 == ' ' || c4 == '\t' || c4 == '\r' || c4 == '\n')) {
                return SQLType.DESCRIBE.value;
            }
            // describe
            if (stmt.length() > offset + 4) {
                char c5 = stmt.charAt(++offset);
                char c6 = stmt.charAt(++offset);
                char c7 = stmt.charAt(++offset);
                char c8 = stmt.charAt(++offset);
                if ((c1 == 'E' || c1 == 'e') && (c2 == 'S' || c2 == 's') && (c3 == 'C' || c3 == 'c')
                        && (c4 == 'R' || c4 == 'r') && (c5 == 'I' || c5 == 'i') && (c6 == 'B' || c6 == 'b')
                        && (c7 == 'E' || c7 == 'e') && (c8 == ' ' || c8 == '\t' || c8 == '\r' || c8 == '\n')) {
                    return SQLType.DESCRIBE.value;
                }
            }
        }
        return SQLType.OTHER.value;
    }

    // INSERT' '
    static int insertCheck(String stmt, int offset) {
        if (stmt.length() > offset + 6) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            char c6 = stmt.charAt(++offset);
            if ((c1 == 'N' || c1 == 'n') && (c2 == 'S' || c2 == 's') && (c3 == 'E' || c3 == 'e')
                    && (c4 == 'R' || c4 == 'r') && (c5 == 'T' || c5 == 't')
                    && (c6 == ' ' || c6 == '\t' || c6 == '\r' || c6 == '\n')) {
                return SQLType.INSERT.value;
            }
        }
        return SQLType.OTHER.value;
    }

    static int rCheck(String stmt, int offset) {
        if (stmt.length() > ++offset) {
            switch (stmt.charAt(offset)) {
                case 'E':
                case 'e':
                    return replaceCheck(stmt, offset);
                case 'O':
                case 'o':
                    return rollabckCheck(stmt, offset);
                default:
                    return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // REPLACE' '
    static int replaceCheck(String stmt, int offset) {
        if (stmt.length() > offset + 6) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            char c6 = stmt.charAt(++offset);
            if ((c1 == 'P' || c1 == 'p') && (c2 == 'L' || c2 == 'l') && (c3 == 'A' || c3 == 'a')
                    && (c4 == 'C' || c4 == 'c') && (c5 == 'E' || c5 == 'e')
                    && (c6 == ' ' || c6 == '\t' || c6 == '\r' || c6 == '\n')) {
                return SQLType.REPLACE.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // SQLType.ROLLBACK.value
    static int rollabckCheck(String stmt, int offset) {
        if (stmt.length() > offset + 6) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            char c6 = stmt.charAt(++offset);
            if ((c1 == 'L' || c1 == 'l') && (c2 == 'L' || c2 == 'l') && (c3 == 'B' || c3 == 'b')
                    && (c4 == 'A' || c4 == 'a') && (c5 == 'C' || c5 == 'c') && (c6 == 'K' || c6 == 'k')
                    && (stmt.length() == ++offset || ParseUtil.isEOF(stmt.charAt(offset)))) {
                return SQLType.ROLLBACK.value;
            }
        }
        return SQLType.OTHER.value;
    }

    static int sCheck(String stmt, int offset) {
        if (stmt.length() > ++offset) {
            switch (stmt.charAt(offset)) {
                case 'A':
                case 'a':
                    return savepointCheck(stmt, offset);
                case 'E':
                case 'e':
                    return seCheck(stmt, offset);
                case 'H':
                case 'h':
                    return showCheck(stmt, offset);
                case 'T':
                case 't':
                    return startCheck(stmt, offset);
                default:
                    return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // SAVEPOINT
    static int savepointCheck(String stmt, int offset) {
        if (stmt.length() > offset + 8) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            char c5 = stmt.charAt(++offset);
            char c6 = stmt.charAt(++offset);
            char c7 = stmt.charAt(++offset);
            char c8 = stmt.charAt(++offset);
            if ((c1 == 'V' || c1 == 'v') && (c2 == 'E' || c2 == 'e') && (c3 == 'P' || c3 == 'p')
                    && (c4 == 'O' || c4 == 'o') && (c5 == 'I' || c5 == 'i') && (c6 == 'N' || c6 == 'n')
                    && (c7 == 'T' || c7 == 't') && (c8 == ' ' || c8 == '\t' || c8 == '\r' || c8 == '\n')) {
                return SQLType.SAVEPOINT.value;
            }
        }
        return SQLType.OTHER.value;
    }

    static int seCheck(String stmt, int offset) {
        if (stmt.length() > ++offset) {
            switch (stmt.charAt(offset)) {
                case 'L':
                case 'l':
                    return selectCheck(stmt, offset);
                case 'T':
                case 't':
                    if (stmt.length() > ++offset) {
                        char c = stmt.charAt(offset);
                        if (c == ' ' || c == '\r' || c == '\n' || c == '\t' || c == '/' || c == '#') {
                            return (offset << 8) | SQLType.SET.value;
                        }
                    }
                    return SQLType.OTHER.value;
                default:
                    return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // SELECT' '
    static int selectCheck(String stmt, int offset) {
        if (stmt.length() > offset + 4) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            if ((c1 == 'E' || c1 == 'e') && (c2 == 'C' || c2 == 'c') && (c3 == 'T' || c3 == 't')
                    && (c4 == ' ' || c4 == '\t' || c4 == '\r' || c4 == '\n' || c4 == '/' || c4 == '#')) {
                return (offset << 8) | SQLType.SELECT.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // SHOW' '
    static int showCheck(String stmt, int offset) {
        if (stmt.length() > offset + 3) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            if ((c1 == 'O' || c1 == 'o') && (c2 == 'W' || c2 == 'w')
                    && (c3 == ' ' || c3 == '\t' || c3 == '\r' || c3 == '\n')) {
                return (offset << 8) | SQLType.SHOW.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // START' '
    static int startCheck(String stmt, int offset) {
        if (stmt.length() > offset + 4) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            char c3 = stmt.charAt(++offset);
            char c4 = stmt.charAt(++offset);
            if ((c1 == 'A' || c1 == 'a') && (c2 == 'R' || c2 == 'r') && (c3 == 'T' || c3 == 't')
                    && (c4 == ' ' || c4 == '\t' || c4 == '\r' || c4 == '\n')) {
                return (offset << 8) | SQLType.START.value;
            }
        }
        return SQLType.OTHER.value;
    }

    // UPDATE' ' | USE' '
    static int uCheck(String stmt, int offset) {
        if (stmt.length() > ++offset) {
            switch (stmt.charAt(offset)) {
                case 'P':
                case 'p':
                    if (stmt.length() > offset + 5) {
                        char c1 = stmt.charAt(++offset);
                        char c2 = stmt.charAt(++offset);
                        char c3 = stmt.charAt(++offset);
                        char c4 = stmt.charAt(++offset);
                        char c5 = stmt.charAt(++offset);
                        if ((c1 == 'D' || c1 == 'd') && (c2 == 'A' || c2 == 'a') && (c3 == 'T' || c3 == 't')
                                && (c4 == 'E' || c4 == 'e') && (c5 == ' ' || c5 == '\t' || c5 == '\r' || c5 == '\n')) {
                            return SQLType.UPDATE.value;
                        }
                    }
                    break;
                case 'S':
                case 's':
                    if (stmt.length() > offset + 2) {
                        char c1 = stmt.charAt(++offset);
                        char c2 = stmt.charAt(++offset);
                        if ((c1 == 'E' || c1 == 'e') && (c2 == ' ' || c2 == '\t' || c2 == '\r' || c2 == '\n')) {
                            return (offset << 8) | SQLType.USE.value;
                        }
                    }
                    break;
                default:
                    return SQLType.OTHER.value;
            }
        }
        return SQLType.OTHER.value;
    }

    static int whoCheck(String stmt, int offset) {
        if (stmt.length() > offset + 2) {
            char c1 = stmt.charAt(++offset);
            char c2 = stmt.charAt(++offset);
            if ((c1 == 'H' || c1 == 'h') && (c2 == 'O' || c2 == 'o')
                    && (stmt.length() == ++offset || ParseUtil.isEOF(stmt.charAt(offset)))) {
                return (offset << 8) | SQLType.WHO.value;
            }
        }
        return SQLType.OTHER.value;
    }
}
