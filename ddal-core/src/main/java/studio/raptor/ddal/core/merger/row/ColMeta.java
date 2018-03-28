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
 * mysql column meta
 * TODO 待验证有无用处
 *
 * @author Charley
 * @since 1.0
 */
public class ColMeta {

    public final int colIndex;
    public final int colType;

    /**
     * @param colIndex column index,start from 0
     * @param colType  column type,e.g. MySQL_COM.FIELD_TYPE_DECIMAL
     */
    public ColMeta(int colIndex, int colType) {
        super();
        this.colIndex = colIndex;
        this.colType = colType;
    }

    @Override
    public String toString() {
        return "ColMeta [colIndex=" + colIndex + ", colType=" + colType + "]";
    }
}
