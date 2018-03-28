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

package studio.raptor.ddal.benchmark.common;

import java.util.concurrent.TimeUnit;
import studio.raptor.ddal.common.sql.SQLHintParser;

/**
 * SQL注释解析器性能测试
 *
 * @author Sam
 * @since 3.0.0
 */
public class SQLHintParserBenchmark {

  public static void main(String[] args) {
    long startNano = System.nanoTime();
    String sql = "/*!hint datasource(group_1.inst1); page(offset=0, count=10); shard(id=10000);*/ select * from t_user";
    for(int i=0;i<10000000;i++) {
      SQLHintParser.parse(sql);
    }
    System.out.println("Costs:" + TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNano) + "ms");
  }
}
