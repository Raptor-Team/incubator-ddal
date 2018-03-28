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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 26974 全程toUpperCase操作耗时
 * 4318 使用内存缓存
 * @author Sam
 * @since 3.0.0
 */
public class StringUpperCaseBenchmark {

  public static void main(String[] args) {
    List<String> stringBuckets = new ArrayList<>(1000);
    Map<String, String> cache = new ConcurrentHashMap<>();
    for (int i = 0; i < 1000; i++) {
      stringBuckets.add(UUID.randomUUID().toString());
    }

    Random random = new Random();
    long timeStart = System.currentTimeMillis();
    for (int i = 0; i < 100000000; i++) {
      String oString = stringBuckets.get(random.nextInt(1000));
      String upperCaseString = cache.get(oString);
      if (null == upperCaseString) {
        upperCaseString = oString.toUpperCase();
        cache.put(oString, upperCaseString);
      }
    }
    System.out.println((System.currentTimeMillis() - timeStart));
  }
}
