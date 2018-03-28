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

package studio.raptor.ddal.core.merger.fixture;

import com.google.common.base.Optional;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.executor.resultset.RowData;
import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * @author jackcao
 * @since 3.0.0
 */
public class MergerTestUtil {

    private MergerTestUtil(){}


    public static RowData createRow(Object ... cells) {
        return  new RowData(cells);
    }

    public static ResultData mockResultData(final List<String> columnNames) {
        try {
            return new ResultData(mockResult(columnNames));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ResultData mockResultData(final List<String> columnNames,final List<RowData> resultSetRows) {
        try {
            return new ResultData(mockResult(columnNames, resultSetRows));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static ResultSet mockResult(final List<String> columnNames) throws SQLException {
        ResultSet result = getResultSet(columnNames);
        when(result.next()).thenReturn(true, false);
        return result;
    }

    public static ResultSet mockResult(final List<String> columnNames, final List<RowData> resultSetRows) throws SQLException {
        ResultSet result = getResultSet(columnNames);
        expectNext(result, resultSetRows);
        expectGetData(result, columnNames, resultSetRows);
        return result;
    }

    private static void expectNext(final ResultSet result, final List<RowData> resultSetRows) throws SQLException {
        Boolean[] hasNext = new Boolean[resultSetRows.size()];
        for (int i = 0; i < resultSetRows.size(); i++) {
            hasNext[i] = i != resultSetRows.size() - 1;
        }
        when(result.next()).thenReturn(true, hasNext);
    }

    private static void expectGetData(final ResultSet result, final List<String> columnNames, final List<RowData> resultSetRows) throws SQLException {
        for (int i = 0; i < columnNames.size(); i++) {
            Object[] resultData = new Object[resultSetRows.size() - 1];
            for (int j = 1; j < resultSetRows.size(); j++) {
                resultData[j - 1] = resultSetRows.get(j).getCell(i + 1);
            }
            when(result.getObject(i + 1)).thenReturn(resultSetRows.get(0).getCell(i + 1), resultData);
        }
    }

    private static ResultSet getResultSet(final List<String> columnNames) throws SQLException {
        ResultSet result = mock(ResultSet.class);
        ResultSetMetaData resultSetMetaData = mock(ResultSetMetaData.class);
        when(result.next()).thenReturn(true);
        when(result.getMetaData()).thenReturn(resultSetMetaData);
        when(resultSetMetaData.getColumnCount()).thenReturn(columnNames.size());
        int count = 1;
        for (String each : columnNames) {
            when(resultSetMetaData.getColumnLabel(count)).thenReturn(each);
            count++;
        }
        return result;
    }


    public static AggregationColumn createAggregationColumn(final AggregationColumn.AggregationType aggregationType, final String name, final String alias, final int index) {
        AggregationColumn result = new AggregationColumn(name, aggregationType, Optional.fromNullable(alias), Optional.<String>absent(), index);
        if (AggregationColumn.AggregationType.AVG.equals(aggregationType)) {
            result.getDerivedColumns().add(
                    new AggregationColumn(AggregationColumn.AggregationType.COUNT.name(), AggregationColumn.AggregationType.COUNT, Optional.of("sharding_gen_1"), Optional.<String>absent()));
            result.getDerivedColumns().add(
                    new AggregationColumn(AggregationColumn.AggregationType.SUM.name(), AggregationColumn.AggregationType.SUM, Optional.of("sharding_gen_2"), Optional.<String>absent()));
        }
        return result;
    }
}