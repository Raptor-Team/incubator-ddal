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

package studio.raptor.ddal.core.merger.row;

/**
 * 聚合函数,支持count、sum、min、max四种
 *
 * @author Charley
 * @since 1.0
 */
public class MergeCol {

    public static final int MERGE_COUNT = 1;
    public static final int MERGE_SUM = 2;
    public static final int MERGE_MIN = 3;
    public static final int MERGE_MAX = 4;
    public static final int MERGE_COUNT_DISTINCT = 5;
    public static final int MERGE_UNSUPPORT = -1;
    public static final int MERGE_NOMERGE = -2;
    public final int mergeType;
    public final ColMeta colMeta;

    /**
     * @param colMeta
     * @param mergeType <li>MERGE_COUNT = 1 <li>MERGE_SUM = 2 <li>MERGE_MIN = 3
     *                  <li>MERGE_MAX = 4
     */
    public MergeCol(ColMeta colMeta, int mergeType) {
        super();
        this.colMeta = colMeta;
        this.mergeType = mergeType;
    }

    public static int getMergeType(String mergeType) {
        String upper = mergeType.toUpperCase();
        if (upper.startsWith("COUNT")) {
            if (upper.startsWith("DISTINCT(", 6) || upper.startsWith("UNIQUE(", 6)) {
                return MERGE_COUNT_DISTINCT;
            }
            return MERGE_COUNT;
        } else if (upper.startsWith("SUM")) {
            return MERGE_SUM;
        } else if (upper.startsWith("MIN")) {
            return MERGE_MIN;
        } else if (upper.startsWith("MAX")) {
            return MERGE_MAX;
        } else {
            return MERGE_UNSUPPORT;
        }
    }

    /**
     * if column can be agg
     *
     * @param column
     * @return
     */
    public static int tryParseAggCol(String column) {
        // MIN(*),MAX(*),COUNT(*),SUM
        if (column.length() < 5) {
            return -1;
        }
        column = column.toUpperCase();

        if (column.startsWith("COUNT(")) {
            if (column.startsWith("DISTINCT(", 6) || column.startsWith("UNIQUE(", 6)) {
                return MERGE_COUNT_DISTINCT;
            }
            return MERGE_COUNT;
        } else if (column.startsWith("SUM(")) {
            return MERGE_SUM;
        } else if (column.startsWith("MIN(")) {
            return MERGE_MIN;
        } else if (column.startsWith("MAX(")) {
            return MERGE_MAX;
        } else if (column.startsWith("AVG(")) {
            return MERGE_UNSUPPORT;
        } else {
            return MERGE_NOMERGE;
        }
    }

    @Override
    public String toString() {
        return "MergeCol [ColMeta=" + (colMeta == null ? "null" : colMeta.toString()) + ", mergeType=" + mergeType
                + "]";
    }
}
