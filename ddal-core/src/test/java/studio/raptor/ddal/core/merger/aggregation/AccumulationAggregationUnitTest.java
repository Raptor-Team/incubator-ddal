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

import org.junit.Test;

import java.util.Collections;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * @author jack
 * @since 3.0.0
 */
public class AccumulationAggregationUnitTest {

    @Test
    public void assertAccumulationAggregation() {
        AccumulationAggregationUnit accumulationAggregationUnit = new AccumulationAggregationUnit();
        accumulationAggregationUnit.merge(null);
        accumulationAggregationUnit.merge(Collections.<Comparable<?>>singletonList(null));
        accumulationAggregationUnit.merge(Collections.<Comparable<?>>singletonList(1));
        accumulationAggregationUnit.merge(Collections.<Comparable<?>>singletonList(1));
        accumulationAggregationUnit.merge(Collections.<Comparable<?>>singletonList(10));
        assertThat(((Number) accumulationAggregationUnit.getResult()).intValue(), is(12));
    }

}