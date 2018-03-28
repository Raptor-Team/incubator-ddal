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

/**
 * orderby 字段
 *
 * @author Charley
 * @since 3.0.0
 */
public class OrderCol {

    public final int orderType;
    public final ColMeta colMeta;

    public static final int COL_ORDER_TYPE_ASC = 0; // ASC
    public static final int COL_ORDER_TYPE_DESC = 1; // DESC

    /**
     * @param colMeta
     * @param orderType OrderCol.COL_ORDER_TYPE_ASC||OrderCol.COL_ORDER_TYPE_DESC
     */
    public OrderCol(ColMeta colMeta, int orderType) {
        super();
        this.colMeta = colMeta;
        this.orderType = orderType;
    }

    @Override
    public String toString() {
        return "OrderCol [ColMeta=" + (colMeta == null ? "null" : colMeta.toString()) + ", orderType=" + orderType
                + "]";
    }

}
