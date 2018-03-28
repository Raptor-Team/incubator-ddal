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

import org.junit.Before;
import org.junit.Test;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.connection.BackendDataSourceManager;
import studio.raptor.ddal.core.helper.PrepareH2TestingEnv;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * 功能描述
 *
 * @author Charley
 * @since 1.0
 */
public class ExecutorEngineTest extends PrepareH2TestingEnv{

    private ExecutionGroup executionGroup = new ExecutionGroup();

    private ExecutionEngine executorEngine = new ExecutionEngine();

    @Before
    public void before() throws SQLException {
        BackendConnection bc1 = BackendDataSourceManager.getReadWriteConnection("group_1", true);
        List<ExecutionUnit> executionUnits = new ArrayList<>();
        ExecutionUnit unit1 = new ExecutionUnit();
        unit1.setFinalSql("select * from crm_js_1.bill limit 0, 10");
        unit1.setConnection(bc1);
        executionUnits.add(unit1);

        BackendConnection bc2 = BackendDataSourceManager.getReadWriteConnection("group_1", true);
        ExecutionUnit unit2 = new ExecutionUnit();
        unit2.setFinalSql("select * from crm_js_1.bill limit 0, 10");
        unit2.setConnection(bc2);
        executionUnits.add(unit2);

        BackendConnection bc3 = BackendDataSourceManager.getReadWriteConnection("group_1", true);
        ExecutionUnit unit3 = new ExecutionUnit();
        unit3.setFinalSql("select * from crm_js_1.bill limit 0, 10");
        unit3.setConnection(bc3);
        executionUnits.add(unit3);

        BackendConnection bc4 = BackendDataSourceManager.getReadWriteConnection("group_1", true);
        ExecutionUnit unit4 = new ExecutionUnit();
        unit4.setFinalSql("select * from crm_js_1.bill limit 0, 10");
        unit4.setConnection(bc4);
        executionUnits.add(unit4);

        BackendConnection bc5 = BackendDataSourceManager.getReadWriteConnection("group_1", true);
        ExecutionUnit unit5 = new ExecutionUnit();
        unit5.setFinalSql("select * from crm_js_1.bill limit 0, 10");
        unit5.setConnection(bc5);
        executionUnits.add(unit5);

        executionGroup.setExecutionUnits(executionUnits);
    }

    //todo 单元测试需重写
    @Test
    public void testExecuteQuery() {
//        try {
//            Map<String, BackendConnection> heldConncetions = new HashMap<>();
//            executorEngine.executeDQL(heldConncetions, executionGroup);
//            for (ExecutionUnit unit : executionGroup.getExecutionUnits()) {
//                Assert.assertNotNull(unit.getResultData());
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
