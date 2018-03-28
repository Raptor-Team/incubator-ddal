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

package studio.raptor.ddal.core.parser.builder;


import studio.raptor.ddal.core.parser.result.merger.Limit;

/**
 * @author Sam
 * @since 3.0.0
 */
public interface SqlBuilder {

  /**
   * 改写schema
   *
   * @param schema 实际执行的表属主
   */
  void rewriteSchema(String schema);

  /**
   * 改写表名
   */
  void rewriteTable(String table);

  /**
   * 改写SQL的分页语句
   * @param limit limit
   */
  void rewriteLimit(Limit limit);

  /**
   * 索引模式-副本模式特殊处理,不再 嵌套两层 SELECT * FROM；后续环节会处理
   * @return
   */
  String toStringForIndexTable();
}
