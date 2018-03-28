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

package studio.raptor.ddal.core.executor.resultset;

import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * 行数据
 *
 * @author Charley
 * @since 3.0.0
 */
public class RowData {

    private final int cellCount;
    private final Object[] cellValues;


    public RowData(int cellCount) {
        this.cellCount = cellCount;
        this.cellValues = new Object[cellCount];
    }

    public RowData(Object[] cellValues) {
        this.cellCount = cellValues.length;
        this.cellValues = cellValues;
    }

    public int getCellCount() {
        return cellCount;
    }

    public Object[] getCellValues() {
        return cellValues;
    }

    /**
     * 设置数据行数据.
     *
     * @param columnIndex 列索引,  从1开始计数
     * @param value 数据行数据
     */
    public final void setCell(final int columnIndex, final Object value) {
        Preconditions.checkArgument(inRange(columnIndex));
        cellValues[columnIndex - 1] = value;
    }

    /**
     * 通过列索引访问数据行数据.
     *
     * @param columnIndex 列索引,  从1开始计数
     * @return 数据行数据
     */
    public final Object getCell(final int columnIndex) {
        Preconditions.checkArgument(inRange(columnIndex));
        return cellValues[columnIndex - 1];
    }

    /**
     * 判断列索引是否在数据行范围.
     *
     * @param columnIndex 列索引,  从1开始计数
     * @return 列索引是否在数据行范围
     */
    public final boolean inRange(final int columnIndex) {
        return columnIndex > 0 && columnIndex < cellValues.length + 1;
    }

    @Override
    public String toString() {
        StringBuilder rowData = new StringBuilder();
        for(int i=0; i < cellValues.length; i++) {
            rowData.append("index :"+i+ ","+"value: "+ cellValues[i]);
        }
        return rowData.toString();
    }

}
