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

package studio.raptor.ddal.core.merger.aggregation;


import studio.raptor.ddal.core.parser.result.merger.AggregationColumn;

/**
 * 聚合函数结果集归并单元工厂.
 * 
 * @author jackcao
 */
public final class AggregationUnitFactory {

    private AggregationUnitFactory(){}
    
    public static AggregationUnit create(final AggregationColumn.AggregationType type) {
        switch (type) {
            case MAX:
                return new ComparableAggregationUnit(false);
            case MIN:
                return new ComparableAggregationUnit(true);
            case SUM:
            case COUNT:
                return new AccumulationAggregationUnit();
            case AVG:
                return new AverageAggregationUnit();
            default:
                throw new UnsupportedOperationException(type.name());
        }
    }
}
