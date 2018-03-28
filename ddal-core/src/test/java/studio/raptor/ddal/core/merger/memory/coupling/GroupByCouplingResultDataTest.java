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
 * @author jackcao
 * @since 3.0.0
 */
public class GroupByCouplingResultDataTest {

//    @Test
//    public void assertSelectSum() {
//        ResultData resultData = createResultData();
//        ResultDataMergeContext resultDataMergeContext = new ResultDataMergeContext(Collections.singletonList(createResultData()), createMergeContext("SUM(0)"));
//        GroupByCouplingResultData groupByCouplingResultData = new GroupByCouplingResultData(resultData, resultDataMergeContext);
//        ResultData result = groupByCouplingResultData.couple();
//        Assert.assertEquals(2, result.getRowCount());
//        Assert.assertEquals("40", result.getRows().get(0).getCell(2).toString());
//    }
//
//    @Test
//    public void assertSelectCount() {
//
//    }
//
//
//    private ParseResult createMergeContext(String type) {
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
//        result.getGroupByColumns().add(new GroupByColumn(Optional.<String>absent(), "user_id", Optional.<String>absent(), OrderByColumn.OrderByType.ASC));
//        AggregationColumn aggregationColumn = new AggregationColumn(type, AggregationColumn.AggregationType.SUM, Optional.of("order_num"), Optional.<String>absent());
//        result.getAggregationColumns().add(aggregationColumn);
//        return result;
//    }
//
//
//    private ResultData createResultData() {
//        return MergerTestUtil.mockResultData(Arrays.asList("user_id", "order_num"), Arrays.asList(MergerTestUtil.createRow("1", "10"), MergerTestUtil.createRow("1", "30"), MergerTestUtil.createRow("2", "90")));
//    }
}