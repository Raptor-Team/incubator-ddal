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

/**
 * @author jack
 * @since 3.0.0
 */
public class LimitCouplingResultDataTest {

//    @Test
//    public void testLimit() {
//        ResultData resultData = createResultData();
//
//
//        LimitCouplingResultData limitCouplingResultData = new LimitCouplingResultData(resultData,createMergeContext(0,2));
//        ResultData result = limitCouplingResultData.couple();
//        Assert.assertEquals(2,result.getRowCount());
//    }
//
//
//    @Test
//    public void testLimit_1() {
//        ResultData resultData = createResultData();
//
//
//        LimitCouplingResultData limitCouplingResultData = new LimitCouplingResultData(resultData,createMergeContext(0,4));
//        ResultData result = limitCouplingResultData.couple();
//        Assert.assertEquals(4,result.getRowCount());
//    }
//
//    @Test
//    public void testLimit_3() {
//        ResultData resultData = createResultData();
//
//
//        LimitCouplingResultData limitCouplingResultData = new LimitCouplingResultData(resultData,createMergeContext(0,5));
//        ResultData result = limitCouplingResultData.couple();
//        Assert.assertEquals(4,result.getRowCount());
//    }
//
//    @Test
//    public void testLimit_4() {
//        ResultData resultData = createResultData();
//
//
//        LimitCouplingResultData limitCouplingResultData = new LimitCouplingResultData(resultData,createMergeContext(3,5));
//        ResultData result = limitCouplingResultData.couple();
//        Assert.assertEquals(1,result.getRowCount());
//    }
//
//
//    private ResultData createResultData() {
//        return MergerTestUtil.mockResultData(Arrays.asList("user_id","order_num"),Arrays.asList(MergerTestUtil.createRow(200,"test"), MergerTestUtil.createRow(10,"cloumn"),MergerTestUtil.createRow(29,"value12"), MergerTestUtil.createRow(90,"value1")));
//    }
//
//    private ParseResult createMergeContext(int offset, int rowNum) {
//        ParseResult result = new ParseResult(DatabaseType.MySQL, new SQLStatement() {
//            @Override
//            public String getDbType() {
//                return null;
//            }
//
//            @Override
//            public void accept(SQLASTVisitor visitor) {
//
//            }
//
//            @Override
//            public SQLObject getParent() {
//                return null;
//            }
//
//            @Override
//            public void setParent(SQLObject parent) {
//
//            }
//
//            @Override
//            public Map<String, Object> getAttributes() {
//                return null;
//            }
//
//            @Override
//            public Object getAttribute(String name) {
//                return null;
//            }
//
//            @Override
//            public void putAttribute(String name, Object value) {
//
//            }
//
//            @Override
//            public Map<String, Object> getAttributesDirect() {
//                return null;
//            }
//
//            @Override
//            public void addBeforeComment(String comment) {
//
//            }
//
//            @Override
//            public void addBeforeComment(List<String> comments) {
//
//            }
//
//            @Override
//            public List<String> getBeforeCommentsDirect() {
//                return null;
//            }
//
//            @Override
//            public void addAfterComment(String comment) {
//
//            }
//
//            @Override
//            public void addAfterComment(List<String> comments) {
//
//            }
//
//            @Override
//            public List<String> getAfterCommentsDirect() {
//                return null;
//            }
//
//            @Override
//            public boolean hasBeforeComment() {
//                return false;
//            }
//
//            @Override
//            public boolean hasAfterComment() {
//                return false;
//            }
//
//            @Override
//            public void output(StringBuffer buf) {
//
//            }
//        });
//        Limit limit = new Limit(offset,rowNum,Optional.<Integer>absent(),Optional.<Integer>absent());
//        result.setLimit(limit);
//        return result;
//    }


}