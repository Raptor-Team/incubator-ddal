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

package studio.raptor.ddal.core.constants;

/**
 * @author Sam
 * @since 3.0.0
 */
public class EngineConstants {

  /**
   * SQL改写使用的表名占位符前缀
   */
  public static final String REWRITE_PH_TBL_PREFIX = "_0_tbl";
  public static final String REWRITE_PH_TBL_SUFFIX = "1_tbl";

  /**
   * SQL改写使用的SCHEMA占位符前缀
   */
  public static final String REWRITE_PH_SCHEMA = "_0_schema";

  /**
   * 表名改写的模板。第一个%s是前缀，第二个%s是表名。
   */
  public static final String REWRITE_PH_TBL_TEMPLATE = "%s_%s_%s";

  /**
   * 虚拟表名
   */
  public static final String DUAL = "DUAL";
}
