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

package studio.raptor.ddal.config.config;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.junit.Assert;
import org.junit.Test;
import studio.raptor.ddal.config.model.shard.Index;
import studio.raptor.ddal.config.model.shard.Table;

/**
 * 分片配置解析单元测试
 *
 * @author Sam
 * @since 3.1.0
 */
public class ShardConfigTest {

  private static final String VDB = "crmdb";

  /**
   * 测试索引表配置解析。
   */
  @Test
  public void testTableIndexConfig() {
    ShardConfig shardConfig = ShardConfig.getInstance();
    assertNotNull(shardConfig);
    assertNotNull(shardConfig.getVirtualDb(VDB));

    Table examTable = shardConfig.getVirtualDb(VDB).getTable("exam");
    assertNotNull(examTable);
    assertTrue(examTable.hasIndex());

    Map<String, Index> idxColumns = examTable.getIndexColumn();
    assertNotNull(idxColumns);
    assertThat(idxColumns.size(), is(2));

    Set<String> expectedRefColumns = new HashSet<>(Arrays.asList("course_id", "student_id"));
    for (String refColumn : idxColumns.keySet()) {
      Assert.assertFalse(expectedRefColumns.add(refColumn));
    }
  }

}
