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

package studio.raptor.ddal.core.engine;

import java.util.List;
import studio.raptor.ddal.common.collections.FastArrayList;

/**
 * SQL语句执行时产生的索引操作。包括索引语句及绑定变量。
 *
 * @author Sam
 * @since 3.0.0
 */
public class IndexTableOps {

  /**
   * 索引表操作语句
   */
  private String idxSql;

  /**
   * SQL语句的绑定变量
   */
  private List<Object> parameters;

  public IndexTableOps() {
    this.parameters = new FastArrayList<>();
  }

  public String getIdxSql() {
    return idxSql;
  }

  public void setIdxSql(String idxSql) {
    this.idxSql = idxSql;
  }

  public List<Object> getParameters() {
    return parameters;
  }

}
