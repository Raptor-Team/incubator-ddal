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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Sam
 * @since 3.0.0
 */
public class HashMapVsConcurrentHashMap {
  private static Logger logger = LoggerFactory.getLogger(HashMapVsConcurrentHashMap.class);
  private static int size = 1000;
  private static Long benchmarkCount = 100000000L;
  private static List<String> keys = new ArrayList<>(1000);
  private static Random random = new Random();

  public static void main(String[] args) {
    // 460
    Map<String, String> map = new HashMap<>();

    //
//    Map<String, String> map = new ConcurrentHashMap<>();

    for (int i = 0; i < size; i++) {
      String key = UUID.randomUUID().toString();
      keys.add(key);
      map.put(key, RandomStringUtils.random(100));
    }

    long timestart = System.currentTimeMillis();
    for (Long i = 0L; i < benchmarkCount; i++) {
      map.get(keys.get(random.nextInt(1000)));
    }
    logger.info((System.currentTimeMillis() - timestart) + "ms");
  }

}
