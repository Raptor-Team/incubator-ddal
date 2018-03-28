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

package studio.raptor.ddal.core.merger.memory.coupling;

import java.util.ArrayList;
import java.util.List;

import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;
import studio.raptor.ddal.core.merger.ResultDataMergeContext;
import studio.raptor.ddal.core.merger.memory.row.GroupByRowData;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;
import studio.raptor.ddal.core.parser.result.merger.GroupByColumn;

/**
 * 分组的连接结果集.
 *
 * @author jackcao
 * @since 3.0.0
 */
public class GroupByCouplingResultData implements CouplingResultData {

    private List<GroupByColumn> groupByColumns;

    private List<AggregationColumn> aggregationColumns;

    private ResultData resultData;

    public GroupByCouplingResultData(ResultData resultData, ResultDataMergeContext resultSetMergeContext) {
        this.resultData = resultData;
        this.groupByColumns = resultSetMergeContext.getParseResult().getGroupByColumns();
        this.aggregationColumns = resultSetMergeContext.getParseResult().getAggregationColumns();
    }

    @Override
    public ResultData couple() {
        List<List<Object>> aggregateGroupByValue = new ArrayList<>();
        List<RowData> groupByResult = new ArrayList<>();
        for (RowData rowData : resultData.getRows()) {
            GroupByRowData groupByRowData = new GroupByRowData(rowData, groupByColumns, aggregationColumns);
            List<Object> groupByValues = groupByRowData.getGroupByValues();
            //判断是否已经分组聚合处理过了
            if (aggregateGroupByValue.contains(groupByValues)) {
                continue;
            }
            for (RowData otherRowData : resultData.getRows()) {
                groupByRowData.setRowData(otherRowData);
                if (groupByColumns.isEmpty() || groupByValues.equals(groupByRowData.getGroupByValues())) {
                    aggregateGroupByValue.add(groupByRowData.getGroupByValues());
                    groupByRowData.aggregate();
                }
            }
            groupByRowData.generateResult();
            groupByResult.add(groupByRowData);
        }
        resultData.setRows(groupByResult);
        return resultData;
    }
}
