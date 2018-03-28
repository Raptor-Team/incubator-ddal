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

import static java.util.concurrent.TimeUnit.NANOSECONDS;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试String.equalsIgnoreCase效率
 *
 * @author Sam
 * @since 3.0.0
 */
public class EqualsIgnoreCaseBenchmark {

  private static final int measureTimes = 100000000;
  private static Map<String, Integer> boolBag;
  private static BagEntry<Integer> entryHead;
  private static Integer[] hashTable;

  static {
    boolBag = new HashMap<>();
    entryHead = new BagEntry<>(null);
    hashTable = new Integer[16];
    char[]
        t = {'t', 'T'},
        r = {'r', 'R'},
        u = {'u', 'U'},
        e = {'e', 'E'};

    for (int i = 0; i < 16; i++) {
      String bool = ""
          + t[(i & 0b1000) >> 3]
          + r[(i & 0b0100) >> 2]
          + u[(i & 0b0010) >> 1]
          + e[i & 0b0001];
      hashTable[i] = hash(bool);
      //boolBag.put(, 1);
//      BagEntry<Integer> c = entryHead;
//      while (true) {
//        if (null == c.next) {
//          c.next = new BagEntry<>(hash(bool));
//          break;
//        }
//        c = c.next;
//      }
    }
  }

  static class BagEntry<T> {

    T value;
    BagEntry<T> next;

    BagEntry(T v) {
      this.value = v;
    }
  }

  private static int hash(Object key) {
    return key.hashCode();
  }

  private static boolean containsValue(String key) {
//    BagEntry<Integer> c = entryHead.next;
//    for (; null != c.next; c = c.next) {
//      if (hash(key) == c.value) {
//        return true;
//      }
//    }
//    return false;
    for(Integer hash : hashTable) {
      if(hash(key) == hash) {
        return true;
      }
    }
    return false;
  }

  public static void main(String[] args) {

    for (int c = 0; c < 10; c++) {
        /**/
      long startNanos3 = System.nanoTime();
      for (int i = 0; i < measureTimes; i++) {
        magicEqualsIgnoreCase();
      }
      System.out.println("Hash key Costs:" + NANOSECONDS.toMillis(System.nanoTime() - startNanos3));

   /* long startNanos1 = System.nanoTime();
    for (int i = 0; i < measureTimes; i++) {
      boolEquals();
    }
    System.out
        .println("Boolean.equals Costs:" + NANOSECONDS.toMillis(System.nanoTime() - startNanos1));

   long startNanos2 = System.nanoTime();
    for (int i = 0; i < measureTimes; i++) {
      jdkEqualsIgnoreCase();
    }
    System.out.println(
        "Jdk.equalsIgnoreCase Costs:" + NANOSECONDS.toMillis(System.nanoTime() - startNanos2));*/
    }

  }

  private static void boolEquals() {
    // True --> user input
    if (Boolean.valueOf("True") == Boolean.TRUE) {
    }
  }

  private static void jdkEqualsIgnoreCase() {
    // true --> expected value
    // True --> user input
    if ("true".equalsIgnoreCase("True")) {
    }
  }

  private static void magicEqualsIgnoreCase() {
    if (containsValue("True")) {
    }
  }

}
