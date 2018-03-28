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

package studio.raptor.ddal.core.merger.memory.coupling;

import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.merger.fixture.MergerTestUtil;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author jack
 * @since 3.0.0
 */
public class DistinctCouplingResultDataTest {

    @Test
    public void testDistinctAll() {
        DistinctCouplingResultData distinctCouplingResultData = new DistinctCouplingResultData(createResultData());

        ResultData resultData = distinctCouplingResultData.couple();
        Assert.assertEquals(3,resultData.getRowCount());

    }

    @Test
    public void testDistinct() {
        ResultData resultData =   MergerTestUtil.mockResultData(Arrays.asList("user_id","order_num"),Arrays.asList(MergerTestUtil.createRow(200,"test"), MergerTestUtil.createRow(10,"cloumn"),MergerTestUtil.createRow(200,"test1"), MergerTestUtil.createRow(90,"value1")));


        DistinctCouplingResultData distinctCouplingResultData = new DistinctCouplingResultData(resultData);

        ResultData result = distinctCouplingResultData.couple();
        Assert.assertEquals(4,result.getRowCount());

    }

    private ResultData createResultData() {
        return MergerTestUtil.mockResultData(Arrays.asList("user_id","order_num"),Arrays.asList(MergerTestUtil.createRow(200,"test"), MergerTestUtil.createRow(10,"cloumn"),MergerTestUtil.createRow(200,"test"), MergerTestUtil.createRow(90,"value1")));
    }


}