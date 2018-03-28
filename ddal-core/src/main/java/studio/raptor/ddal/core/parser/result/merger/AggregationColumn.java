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

package studio.raptor.ddal.core.parser.result.merger;

import com.google.common.base.Optional;

import java.util.ArrayList;
import java.util.List;

/**
 * 聚合列对象.
 */
public final class AggregationColumn implements IndexColumn {
    
    private final String expression;
    
    private final AggregationType aggregationType;
    
    private final Optional<String> alias;
    
    private final Optional<String> option;
    
    private final List<AggregationColumn> derivedColumns = new ArrayList<>(2);
    
    private int columnIndex = -1;
    
    @Override
    public Optional<String> getColumnLabel() {
        return alias;
    }
    
    @Override
    public Optional<String> getColumnName() {
        return Optional.of(expression);
    }
    
    /**
     * 聚合函数类型.
     * 
     * @author gaohongtao
     */
    public enum AggregationType {
        MAX, MIN, SUM, COUNT, AVG
    }

    public AggregationColumn(String expression, AggregationType aggregationType, Optional<String> alias, Optional<String> option) {
        this.expression = expression;
        this.aggregationType = aggregationType;
        this.alias = alias;
        this.option = option;
    }

    public AggregationColumn(String expression, AggregationType aggregationType, Optional<String> alias, Optional<String> option, int columnIndex) {
        this.expression = expression;
        this.aggregationType = aggregationType;
        this.alias = alias;
        this.option = option;
        this.columnIndex = columnIndex;
    }

    public AggregationColumn(AggregationType aggregationType, Optional<String> alias, Optional<String> option) {
        this.expression = null;
        this.aggregationType = aggregationType;
        this.alias = alias;
        this.option = option;
    }

    public String getExpression() {
        return expression;
    }

    public AggregationType getAggregationType() {
        return aggregationType;
    }

    public Optional<String> getAlias() {
        return alias;
    }

    public Optional<String> getOption() {
        return option;
    }

    public List<AggregationColumn> getDerivedColumns() {
        return derivedColumns;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public void setColumnIndex(int columnIndex) {
        this.columnIndex = columnIndex;
    }

    @Override
    public String toString() {
        return "AggregationColumn{" +
                "expression='" + expression + '\'' +
                ", aggregationType=" + aggregationType +
                ", alias=" + alias +
                ", option=" + option +
                ", derivedColumns=" + derivedColumns +
                ", columnIndex=" + columnIndex +
                '}';
    }
}
