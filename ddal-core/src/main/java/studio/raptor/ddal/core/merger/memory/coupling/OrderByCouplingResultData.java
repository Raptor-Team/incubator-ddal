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

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;
import studio.raptor.ddal.core.merger.ResultDataMergeContext;
import studio.raptor.ddal.core.merger.memory.row.OrderByRowData;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn;

/**
 * 根据排序列内存排序的连接结果集.
 *
 * @author jack
 * @since 3.0.0
 */
public class OrderByCouplingResultData implements CouplingResultData {

    private final List<OrderByColumn> orderByColumns;

    private ResultData resultData;

    public OrderByCouplingResultData(ResultData resultData, final ResultDataMergeContext resultSetMergeContext) {
        this.resultData = resultData;
        orderByColumns = resultSetMergeContext.getParseResult().getOrderByColumns();
    }

    @Override
    public ResultData couple() {
        List<OrderByRowData> orderByResultSetRows = new LinkedList<>();
        for (RowData each : resultData.getRows()) {
            orderByResultSetRows.add(new OrderByRowData(each.getCellValues(), orderByColumns));
        }
        Collections.sort(orderByResultSetRows);
        resultData.getRows().addAll(orderByResultSetRows);
        resultData.clearAndAddRows(orderByResultSetRows);
        return resultData;
    }
}
