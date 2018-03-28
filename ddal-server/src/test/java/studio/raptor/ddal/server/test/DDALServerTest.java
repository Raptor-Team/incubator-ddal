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

package studio.raptor.ddal.server.test;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import studio.raptor.ddal.server.DDALServer;

import java.util.Random;

/**
 * Low-level test cases of DDAL-Server
 * <p>
 * Created by Sam on 17/11/2016.
 */
public class DDALServerTest {

    private int testServerPort = 3307;
    private DDALServer rds;

    @Before
    public void before() {
        int luckyNumber = new Random().nextInt(2);
        if(luckyNumber == 1) {
            rds = new DDALServer(testServerPort);
        } else {
            rds = new DDALServer(testServerPort, 0, 0);
        }
    }

    @After
    public void after() {
        rds = null;
    }

    @Test
    public void testServerPort() {
        Assert.assertThat(rds.getPort(), Is.is(testServerPort));
    }
}
