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
import studio.raptor.ddal.core.merger.util.ResultSetUtil;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn;

import java.util.ArrayList;
import java.util.List;

/**
 * 具有排序功能的数据行对象.
 * 
 * @author jackcao
 * @since 3.0.0
 */
public final class OrderByRowData extends RowData implements Comparable<OrderByRowData> {
    
    private final List<OrderByColumn> orderByColumns;
    
    private final List<Comparable<?>> orderByValues;

    public OrderByRowData(final Object[] cellValues, final List<OrderByColumn> orderByColumns) {
        super(cellValues);
        this.orderByColumns = orderByColumns;
        orderByValues = loadOrderByValues();
    }
    
    private List<Comparable<?>> loadOrderByValues() {
        List<Comparable<?>> result = new ArrayList<>(orderByColumns.size());
        for (OrderByColumn each : orderByColumns) {
            Object value = getCell(each.getColumnIndex());
            Preconditions.checkState(value instanceof Comparable, "order by value must extends Comparable");
            result.add((Comparable<?>) value);
        }
        return result;
    }
    
    @Override
    public int compareTo(final OrderByRowData otherOrderByValue) {
        for (int i = 0; i < orderByColumns.size(); i++) {
            OrderByColumn thisOrderByColumn = orderByColumns.get(i);
            int result = ResultSetUtil.compareTo(orderByValues.get(i), otherOrderByValue.orderByValues.get(i), thisOrderByColumn.getOrderByType());
            if (0 != result) {
                return result;
            }
        }
        return 0;
    }
    
    @Override
    public String toString() {
        return String.format("Order by columns value is %s", orderByValues);
    }
}
