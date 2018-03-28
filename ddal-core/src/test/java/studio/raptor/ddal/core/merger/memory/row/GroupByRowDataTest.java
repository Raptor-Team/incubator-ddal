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

import com.google.common.base.Optional;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;
import studio.raptor.ddal.core.merger.fixture.MergerTestUtil;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;
import studio.raptor.ddal.core.parser.result.merger.GroupByColumn;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn;

import java.sql.ResultSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.*;

/**
 * @author jackcao
 * @since 3.0.0
 */
public class GroupByRowDataTest {


    @Test
    public void assertGetGroupByValues() {
        RowData rowData = MergerTestUtil.createRow("group_1","group_2","other");

        List<Object> actual = new GroupByRowData(rowData,Arrays.asList(createGroupByColumn("group_col_1", 1),createGroupByColumn("group_col_2", 2)),
                Collections.singletonList(new AggregationColumn("SUM(0)", AggregationColumn.AggregationType.SUM, Optional.<String>absent(), Optional.<String>absent()))).getGroupByValues();

        assertThat(actual.size(), is(2));
        assertThat(actual.get(0).toString(), is("group_1"));
        assertThat(actual.get(1).toString(), is("group_2"));
    }


    @Test
    public void assertToString() throws Exception {

        ResultData resultData = new ResultData().withQueryMode();

        RowData rowData1 = MergerTestUtil.createRow("1","10");
        RowData rowData2 = MergerTestUtil.createRow("1","20");

        resultData.getRows().add(rowData1);
        resultData.getRows().add(rowData2);

        GroupByColumn groupByColumn = new GroupByColumn(Optional.<String>absent(), "user_id", Optional.<String>absent(), OrderByColumn.OrderByType.ASC);
        groupByColumn.setColumnIndex(1);
        AggregationColumn aggregationColumn = new AggregationColumn("SUM(0)", AggregationColumn.AggregationType.SUM, Optional.<String>absent(), Optional.<String>absent());
        aggregationColumn.setColumnIndex(2);
        GroupByRowData row = new GroupByRowData(rowData1, Collections.singletonList(groupByColumn), Collections.singletonList(aggregationColumn));


        row.aggregate();
        row.setRowData(rowData2);
        row.aggregate();

        row.generateResult();
        resultData.getRows().add(row);

        Assert.assertEquals("30",row.getCell(2).toString());

    }

    private GroupByColumn createGroupByColumn(final String columnName, final int columnIndex) {
        GroupByColumn result = new GroupByColumn(Optional.<String>absent(), columnName, Optional.<String>absent(), OrderByColumn.OrderByType.ASC);
        result.setColumnIndex(columnIndex);
        return result;
    }


}