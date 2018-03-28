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

package studio.raptor.ddal.core.executor;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行组
 *
 * @author Charley
 * @since 1.0
 */
public class ExecutionGroup {

    private String originalSql;                 //原始SQL
    private int size = 0;                           //执行单元个数
    private List<ExecutionUnit> executionUnits = new ArrayList<>(); //执行单元列表

    public String getOriginalSql() {
        return originalSql;
    }

    public void setOriginalSql(String originalSql) {
        this.originalSql = originalSql;
    }

    public int getSize() {
        return size;
    }

    public List<ExecutionUnit> getExecutionUnits() {
        return executionUnits;
    }

    public void setExecutionUnits(List<ExecutionUnit> executionUnits) {
        this.executionUnits = executionUnits;
        this.size = executionUnits.size();
    }

    public void addExecutionUnit(ExecutionUnit executionUnit) {
        this.executionUnits.add(executionUnit);
        this.size += 1;
    }

}
