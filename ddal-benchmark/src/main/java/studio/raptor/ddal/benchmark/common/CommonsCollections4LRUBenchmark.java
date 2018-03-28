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

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.apache.commons.collections4.map.LRUMap;
import org.apache.commons.lang3.RandomStringUtils;
import studio.raptor.ddal.benchmark.utils.GCUtils;
import studio.raptor.ddal.common.util.RuntimeUtil;

/**
 * 测试 Commons-Collections4#LRUMap 的效率
 *
 * @author Sam
 * @since 3.0.0
 */
public class CommonsCollections4LRUBenchmark {

  private final Map<String, Object> lruMap;
  private final List<String> keys;
  private final int keyCount = 1000;

  /**
   * 生成LRU缓存测试使用的资源
   *
   * @throws IOException IOException
   */
  private void generateLruResource() throws IOException {
    File randomFile = new File(RuntimeUtil.getRunningPath() + "/lru-resources.txt");
    BufferedWriter bw = new BufferedWriter(new FileWriter(randomFile));
    for (int i = 0; i < keyCount; i++) {
      bw.write(UUID.randomUUID().toString().replace("-", "") + ":" + RandomStringUtils
          .random(128, true, false));
      bw.newLine();
      if (i % 100 == 0) {
        bw.flush();
      }
    }
    bw.flush();
    bw.close();
  }

  private void loadLRUResource() throws IOException {
    BufferedInputStream bis =
        new BufferedInputStream(CommonsCollections4LRUBenchmark.class.getClass().getResourceAsStream("/lru-resources.txt"));
    if(bis.available() > 0) {
      StringBuilder textBuilder = new StringBuilder();
      int colonIndex;
      int ch;
      while((ch = bis.read()) != -1) {
        if(ch == '\n') {
          colonIndex = textBuilder.indexOf(":");
          String key = textBuilder.substring(0, colonIndex);
          keys.add(key);
          lruMap.put(key, textBuilder.substring(colonIndex + 1, textBuilder.length()));
          textBuilder.delete(0, textBuilder.length());
        } else {
          textBuilder.append((char)ch);
        }
      }
    }
    bis.close();
  }

  private CommonsCollections4LRUBenchmark() {
//    lruMap = Collections.synchronizedMap(new LRUMap<String, Object>(keyCount));
    lruMap = new LRUMap<>(keyCount);
    keys = new ArrayList<>(keyCount);
  }

  private void performance() {
    Random random = new Random();
    long startYGC = GCUtils.getYoungGC();
    long startYGCTime = GCUtils.getYoungGCTime();
    long startFGC = GCUtils.getFullGC();
    long startMillis = System.currentTimeMillis();
    for (int i = 0; i < 1000 * 1000 * 10; ++i) {
      execPerformance(keys.get(random.nextInt(keyCount)));
    }
    long millis = System.currentTimeMillis() - startMillis;

    long ygc = GCUtils.getYoungGC() - startYGC;
    long ygct = GCUtils.getYoungGCTime() - startYGCTime;
    long fgc = GCUtils.getFullGC() - startFGC;

    System.out.println("Time(ms) \t" + millis + ", ygc " + ygc + ", ygct " + ygct + ", fgc " + fgc);
  }

  private void execPerformance(String key) {
    Object obj = lruMap.get(key);
    if(null == obj) {
      throw new RuntimeException("No value found mapping to " + key);
    }
  }

  public static void main(String[] args) throws IOException {
    CommonsCollections4LRUBenchmark _this = new CommonsCollections4LRUBenchmark();
    _this.loadLRUResource();
    for (int i = 0; i < 10; i++) {
      _this.performance();
    }
  }
}
