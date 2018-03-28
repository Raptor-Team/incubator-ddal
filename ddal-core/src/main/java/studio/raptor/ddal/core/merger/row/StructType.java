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

package studio.raptor.ddal.core.merger.row;

import javax.annotation.Nonnull;
import java.util.Map;

/**
 * @author jack
 * @since 3.0.0
 */
public class StructType {

    private final Map<String, ColMeta> columToIndx;
    private final int fieldCount;

    private  OrderCol[] orderCols = null;

    public StructType(@Nonnull Map<String,ColMeta> columToIndx, int fieldCount){
        assert fieldCount >=0;
        this.columToIndx = columToIndx;
        this.fieldCount = fieldCount;
    }

    public int length() {
        return fieldCount;
    }

    public Map<String, ColMeta> getColumToIndx() {
        return columToIndx;
    }

    public OrderCol[] getOrderCols() {
        return orderCols;
    }

    public void setOrderCols(OrderCol[] orderCols) {
        this.orderCols = orderCols;
    }

    public long apply(int i) {
        return 0;
    }
}
