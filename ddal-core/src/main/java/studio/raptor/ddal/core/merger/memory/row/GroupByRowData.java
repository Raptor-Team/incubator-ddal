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


import com.google.common.base.Function;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import studio.raptor.ddal.core.executor.resultset.RowData;
import studio.raptor.ddal.core.merger.aggregation.AggregationUnit;
import studio.raptor.ddal.core.merger.aggregation.AggregationUnitFactory;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;
import studio.raptor.ddal.core.parser.result.merger.GroupByColumn;

import java.util.*;

/**
 * 具有分组功能的数据行对象.
 * 
 * @author jackcao
 * @since 3.0.0
 */
public final class GroupByRowData extends RowData {

    private final List<GroupByColumn> groupByColumns;
    
    private final Map<AggregationColumn, AggregationUnit> aggregationUnitMap;

    private RowData rowData;

    public GroupByRowData(final RowData rowData, final List<GroupByColumn> groupByColumns, final List<AggregationColumn> aggregationColumns) {
        super(rowData.getCellValues());
        this.rowData = rowData;
        this.groupByColumns = groupByColumns;
        aggregationUnitMap = Maps.toMap(aggregationColumns, new Function<AggregationColumn, AggregationUnit>() {
            
            @Override
            public AggregationUnit apply(final AggregationColumn input) {
                return AggregationUnitFactory.create(input.getAggregationType());
            }
        });
    }

    /**
     * 处理聚合函数结果集.
     * 
     */
    public void aggregate()  {
        for (Map.Entry<AggregationColumn, AggregationUnit> each : aggregationUnitMap.entrySet()) {
            each.getValue().merge(getAggregationValues(each.getKey().getDerivedColumns().isEmpty() ? Collections.singletonList(each.getKey()) : each.getKey().getDerivedColumns()));
        }
    }
    
    private List<Comparable<?>> getAggregationValues(final List<AggregationColumn> aggregationColumns) {
        List<Comparable<?>> result = new ArrayList<>(aggregationColumns.size());
        for (AggregationColumn each : aggregationColumns) {
            result.add((Comparable<?>) rowData.getCell(each.getColumnIndex()));
        }
        return result;
    }
    
    /**
     * 生成结果.
     */
    public void generateResult() {
        for (AggregationColumn each : aggregationUnitMap.keySet()) {
            setCell(each.getColumnIndex(), aggregationUnitMap.get(each).getResult());
        }
    }
    
    /**
     * 获取分组值.
     * 
     * @return 分组值集合

     */
    public List<Object> getGroupByValues() {
        List<Object> result = new ArrayList<>(groupByColumns.size());
        for (GroupByColumn each : groupByColumns) {
            result.add(rowData.getCell(each.getColumnIndex()));
        }
        return result;
    }

    public void setRowData(RowData rowData) {
        this.rowData = rowData;
    }


    @Override
    public String toString() {
        StringBuilder result = new StringBuilder("GroupByKey is: ");
        result.append(Lists.transform(groupByColumns, new Function<GroupByColumn, Object>() {
            
            @Override
            public Object apply(final GroupByColumn input) {
                return getCell(input.getColumnIndex());
            }
        }));
        if (aggregationUnitMap.isEmpty()) {
            return result.toString();
        }
        result.append("; Aggregation result is: ").append(Lists.transform(new ArrayList<>(aggregationUnitMap.keySet()), new Function<AggregationColumn, String>() {
            
            @Override
            public String apply(final AggregationColumn input) {
                Object value = getCell(input.getColumnIndex());
                value = null == value ? "null" : value;
                return String.format("{index:%d, type:%s, value:%s}", input.getColumnIndex(), input.getAggregationType(), value);
            }
        }));
        return result.toString();
    }
}
