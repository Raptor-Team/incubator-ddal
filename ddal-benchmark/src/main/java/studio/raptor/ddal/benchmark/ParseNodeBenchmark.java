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

package studio.raptor.ddal.benchmark;

import studio.raptor.ddal.core.constants.DatabaseType;
import studio.raptor.ddal.core.parser.Parser;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * @author Sam
 * @since 3.0.0
 */
public class ParseNodeBenchmark extends AbstractFlowNodeBenchmark {

  /**
   * 增大预热次数，触发jvm使用jit。
   * 或者使用-Xcomp参数直接开启jvm的jit
   */
  private ParseNodeBenchmark() {
    super("sql_parse_single", 1000000);
  }

  public static void main(String[] args) {
    new ParseNodeBenchmark().runBenchmark();
  }

  @Override
  protected void prepareTaskContext() {
  }

  @Override
  protected TaskStats executeFlowNode() {
    String sql = "select a.cno, a.cname, a.tno from course a where a.cno = 112601";
    ParseResult parsedResult = Parser.parse(DatabaseType.MySQL, sql);
    return new TaskStats(1);
  }
}
