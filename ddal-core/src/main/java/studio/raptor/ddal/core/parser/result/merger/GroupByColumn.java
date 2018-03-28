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
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn.OrderByType;

/**
 * 分组列对象.
 * 
 * @author jackcao
 */
public final class GroupByColumn extends AbstractSortableColumn implements IndexColumn {
    
    private int columnIndex;
    
    public GroupByColumn(final Optional<String> owner, final String name, final Optional<String> alias, final OrderByType orderByType) {
        super(owner, Optional.of(name), alias, orderByType);
    }

    public GroupByColumn(final Optional<String> owner, final String name, final OrderByType orderByType) {
        super(owner, Optional.of(name), orderByType);
    }

    @Override
    public Optional<String> getColumnLabel() {
        return getAlias();
    }
    
    @Override
    public Optional<String> getColumnName() {
        return getName();
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
        return "GroupByColumn{" +
                "columnIndex=" + columnIndex +
                "} " + super.toString();
    }
}
