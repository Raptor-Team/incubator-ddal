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

package studio.raptor.ddal.server.test.common;

import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.server.common.Status;

/**
 * Server status test case.
 * <p>
 * Created by Sam on 16/11/2016.
 */
public class StatusTest {

    @Test
    public void testStatusValue() {
        Assert.assertThat(Status.STATUS_RUNNING.value(), Is.is(1));
        Assert.assertThat(Status.STATUS_UNINITIALISED.value(), Is.is(0));
        Assert.assertThat(Status.STATUS_SHUTDOWN.value(), Is.is(2));
    }

    @Test
    public void testStatusName() {
        Assert.assertThat("STATUS_UNINITIALISED", Is.is(Status.STATUS_UNINITIALISED.toString()));
        Assert.assertThat("STATUS_RUNNING", Is.is(Status.STATUS_RUNNING.toString()));
        Assert.assertThat("STATUS_SHUTDOWN", Is.is(Status.STATUS_SHUTDOWN.toString()));
    }
}
