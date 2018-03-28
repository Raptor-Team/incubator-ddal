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

package studio.raptor.ddal.core.merger;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import studio.raptor.ddal.core.executor.resultset.ColumnDefinition;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.parser.result.ParseResult;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;
import studio.raptor.ddal.core.parser.result.merger.IndexColumn;

/**
 * 结果集归并上下文.
 * @author jackcao
 * @since 3.0.0
 */
public class ResultDataMergeContext {

    private final List<ResultData> resultDataList;

    private final ParseResult parseResult;

    public ResultDataMergeContext(final List<ResultData> resultDataList, final ParseResult parseResult)  {
        this.resultDataList = resultDataList;
        this.parseResult = parseResult;
        init();
    }

    private void init() {
        setColumnIndex(generateColumnLabelIndexMap());
    }

    private void setColumnIndex(final Map<String, Integer> columnLabelIndexMap) {
        for (IndexColumn each : getAllFocusedColumns()) {
            if (each.getColumnIndex() > 0) {
                continue;
            }
            Preconditions.checkState(
                    columnLabelIndexMap.containsKey(each.getColumnLabel().orNull()) || columnLabelIndexMap.containsKey(each.getColumnName().orNull()), String.format("%s has not index", each));
            if (each.getColumnLabel().isPresent() && columnLabelIndexMap.containsKey(each.getColumnLabel().get())) {
                each.setColumnIndex(columnLabelIndexMap.get(each.getColumnLabel().get()));
            } else if (each.getColumnName().isPresent() && columnLabelIndexMap.containsKey(each.getColumnName().get())) {
                each.setColumnIndex(columnLabelIndexMap.get(each.getColumnName().get()));
            }
        }
    }


    private List<IndexColumn> getAllFocusedColumns() {
        List<IndexColumn> result = new LinkedList<>();
        result.addAll(parseResult.getGroupByColumns());
        result.addAll(parseResult.getOrderByColumns());
        LinkedList<AggregationColumn> allAggregationColumns = Lists.newLinkedList(parseResult.getAggregationColumns());
        while (!allAggregationColumns.isEmpty()) {
            AggregationColumn firstElement = allAggregationColumns.poll();
            result.add(firstElement);
            if (!firstElement.getDerivedColumns().isEmpty()) {
                allAggregationColumns.addAll(firstElement.getDerivedColumns());
            }
        }
        return result;
    }

    private Map<String, Integer> generateColumnLabelIndexMap()  {
        List<ColumnDefinition> columnDefinitions = resultDataList.get(0).getHead();
        int columnCount = columnDefinitions.size();
        Map<String, Integer> result = new HashMap<>(columnCount);
        for (ColumnDefinition columnDefinition: columnDefinitions) {
            result.put(columnDefinition.getName().toUpperCase(), columnDefinition.getIndex());
        }
        return result;
    }

    /**
     * 判断排序归并是否需要内存排序.
     *
     * @return 排序归并是否需要内存排序
     */
    public boolean isNeedMemorySortForOrderBy() {
        return !parseResult.getOrderByColumns().isEmpty();
    }

    public ParseResult getParseResult() {
        return parseResult;
    }

    public List<ResultData> getResultDataList() {
        return resultDataList;
    }
}
