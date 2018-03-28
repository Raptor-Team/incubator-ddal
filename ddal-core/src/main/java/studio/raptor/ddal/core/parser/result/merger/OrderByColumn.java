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

import studio.raptor.sqlparser.ast.SQLOrderingSpecification;
import com.google.common.base.Objects;
import com.google.common.base.Optional;


/**
 * 排序列对象.
 */
public final class OrderByColumn extends AbstractSortableColumn implements IndexColumn {
    
    private final Optional<Integer> index;
    
    private int columnIndex;
    
    public OrderByColumn(final Optional<String> owner, final String name, final Optional<String> alias, final OrderByType orderByType) {
        super(owner, Optional.of(name), alias, orderByType);
        index =  Optional.absent();
    }
    
    public OrderByColumn(final String name, final OrderByType orderByType) {
        super(Optional.<String>absent(), Optional.of(name), Optional.<String>absent(), orderByType);
        index =  Optional.absent();
    }
    
    public OrderByColumn(final int index, final OrderByType orderByType) {
        super(Optional.<String>absent(), Optional.<String>absent(), Optional.<String>absent(), orderByType);
        this.index = Optional.of(index);
        columnIndex = index;
    }
    
    @Override
    public void setColumnIndex(final int index) {
        if (this.index.isPresent()) {
            return;
        }
        columnIndex = index;
    }
        
    @Override
    public Optional<String> getColumnLabel() {
        return getAlias();
    }
    
    @Override
    public Optional<String> getColumnName() {
        return getName();
    }

    public Optional<Integer> getIndex() {
        return index;
    }

    @Override
    public int getColumnIndex() {
        return columnIndex;
    }

    @Override
    public String toString() {
        return "OrderByColumn{" +
                "index=" + index +
                ", columnIndex=" + columnIndex +
                "} super=" + super.toString();
    }

    /**
     * 排序类型.
     */
    public enum OrderByType {
        ASC, 
        DESC;
        
        /**
         * 适配Druid的枚举类型.
         * 
         * @param sqlOrderingSpecification Druid的枚举类型
         * @return 排序类型
         */
        public static OrderByType valueOf(final SQLOrderingSpecification sqlOrderingSpecification) {
            return OrderByType.valueOf(sqlOrderingSpecification.name());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderByColumn)) {
            return false;
        }
        OrderByColumn that = (OrderByColumn) o;
        return getOrderByType() == that.getOrderByType() && (columnIndex == that.columnIndex 
                || index.isPresent() && that.index.isPresent() && index.get().equals(that.index.get())
                || getName().isPresent() && that.getName().isPresent() && getName().get().equals(that.getName().get())
                || getAlias().isPresent() && that.getAlias().isPresent() && getAlias().get().equals(that.getAlias().get()));
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(getOrderByType(), columnIndex);
    }
}
