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

package studio.raptor.ddal.common.helper;

import org.hamcrest.core.Is;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * path helper test case
 *
 * Created by Sam on 17/11/2016.
 */
public class PathHelperTest {

  @Before
  public void setup() {
    System.setProperty("ddal.sever.home", "/opt/ddalserver");
  }

  @After
  public void cleanup() {
    System.clearProperty("ddal.sever.home");
  }

  @Test
  public void testGetDdalHome() {
    Assert.assertThat(PathHelper.getDdalServerHome(), Is.is("/opt/ddalserver"));
  }

  @Test
  public void testGetDdalConf() {
    Assert.assertThat(PathHelper.getDdalServerConfDir(), Is.is("/opt/ddalserver/conf"));
  }

  @Test
  public void testGetDdalBin() {
    Assert.assertThat(PathHelper.getDdalServerBinDir(), Is.is("/opt/ddalserver/bin"));
  }
}
