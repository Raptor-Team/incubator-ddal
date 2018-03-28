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

package studio.raptor.ddal.benchmark.utils;

import java.lang.management.ManagementFactory;
import javax.management.MBeanServer;
import javax.management.ObjectName;

/**
 * @author Sam
 * @since 3.0.0
 */
public class GCUtils {
  public static long getYoungGC() {
    try {
      // java.lang:type=GarbageCollector,name=G1 Young Generation
      // java.lang:type=GarbageCollector,name=G1 Old Generation
      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName objectName;
      if (mbeanServer.isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=ParNew"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=ParNew");
      } else if (mbeanServer
          .isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=Copy"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=Copy");
      } else if (mbeanServer.isRegistered(
          new ObjectName("java.lang:type=GarbageCollector,name=G1 Young Generation"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=G1 Young Generation");
      } else {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=PS Scavenge");
      }

      return (Long) mbeanServer.getAttribute(objectName, "CollectionCount");
    } catch (Exception e) {
      throw new RuntimeException("error");
    }
  }

  public static long getYoungGCTime() {
    try {
      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName objectName;
      if (mbeanServer.isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=ParNew"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=ParNew");
      } else if (mbeanServer
          .isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=Copy"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=Copy");
      } else if (mbeanServer.isRegistered(
          new ObjectName("java.lang:type=GarbageCollector,name=G1 Young Generation"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=G1 Young Generation");
      } else {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=PS Scavenge");
      }

      return (Long) mbeanServer.getAttribute(objectName, "CollectionTime");
    } catch (Exception e) {
      throw new RuntimeException("error", e);
    }
  }

  public static long getFullGC() {
    try {
      MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
      ObjectName objectName;

      if (mbeanServer.isRegistered(
          new ObjectName("java.lang:type=GarbageCollector,name=ConcurrentMarkSweep"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=ConcurrentMarkSweep");
      } else if (mbeanServer
          .isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=MarkSweepCompact"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=MarkSweepCompact");
      } else if (mbeanServer
          .isRegistered(new ObjectName("java.lang:type=GarbageCollector,name=G1 Old Generation"))) {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=G1 Old Generation");
      } else {
        objectName = new ObjectName("java.lang:type=GarbageCollector,name=PS MarkSweep");
      }

      return (Long) mbeanServer.getAttribute(objectName, "CollectionCount");
    } catch (Exception e) {
      throw new RuntimeException("error");
    }
  }
}
