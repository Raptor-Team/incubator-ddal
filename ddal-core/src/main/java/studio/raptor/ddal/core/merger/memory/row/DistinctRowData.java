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

package studio.raptor.ddal.core.merger.memory.row;

import com.google.common.base.Preconditions;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;

import java.util.ArrayList;
import java.util.List;

/**
 * 去重的数据行对象.
 *
 * @author Charley
 * @since 3.0.0
 */
public final class DistinctRowData extends RowData implements Comparable<DistinctRowData> {

    private final int columnCount;

    private final List<Comparable<Object>> comparableColumnValues;

    public DistinctRowData(final Object[] cellValues, final int columnCount) {
        super(cellValues);
        this.columnCount = columnCount;
        this.comparableColumnValues = loadComparableColumnValues();
    }

    private List<Comparable<Object>> loadComparableColumnValues() {
        List<Comparable<Object>> result = new ArrayList<>(this.columnCount);
        for (int i = 0; i < this.columnCount; i++) {
            Object value = getCell(i + 1);
            if (null != value) {
                Preconditions.checkState(value instanceof Comparable, "distinct value must extends Comparable");
                result.add((Comparable) value);
            } else {
                result.add((Comparable)"");
            }
        }
        return result;
    }

    @Override
    public int compareTo(DistinctRowData otherRow) {
        for (int i = 0; i < this.columnCount; i++) {
            int result = this.comparableColumnValues.get(i).compareTo(otherRow.comparableColumnValues.get(i));
            if (0 != result) {
                return result;
            }
        }
        return 0;
    }
}
