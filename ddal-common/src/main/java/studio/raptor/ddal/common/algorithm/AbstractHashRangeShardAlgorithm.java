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

package studio.raptor.ddal.common.algorithm;

import com.google.common.base.Splitter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.common.exception.code.RouteErrCodes;

/**
 * 两阶段路由抽象类。
 *
 * 如若使用两阶段路由算法，需使用者实现
 *
 * @author Sam
 * @since 3.0.0
 */
public abstract class AbstractHashRangeShardAlgorithm<T extends Comparable<?>> {

  private static Logger logger = LoggerFactory.getLogger(AbstractHashRangeShardAlgorithm.class);
  protected Integer mod;
  protected String rangeText;

  private static final String BASIC_DIR = "ddal/";
  private final String fileName;

  protected AbstractHashRangeShardAlgorithm(String fileName) {
    this.fileName = fileName;
    readFile();
  }

  /**
   * 读取文件中Mod值与Range范围文本
   */
  private void readFile() {
    try {
      Properties hashRangeProperties = new Properties();
      hashRangeProperties.load(AbstractHashRangeShardAlgorithm.class.getClassLoader()
          .getResourceAsStream(BASIC_DIR + fileName));
      String modPlainText = hashRangeProperties.getProperty("mod");
      if (null != modPlainText && modPlainText.matches("\\d+")) {
        this.mod = Integer.parseInt(modPlainText);
      } else {
        throw new GenericException(RouteErrCodes.ROUTE_427);
      }
      String rangeText = hashRangeProperties.getProperty("range");
      if (null != rangeText && rangeText.length() > 0 && rangeText.indexOf('-') > 0) {
        this.rangeText = rangeText;
      } else {
        throw new GenericException(RouteErrCodes.ROUTE_429, new Object[]{""});
      }
    } catch (IOException ioe) {
      logger.error("Read hash range prop failed.", ioe);
      throw new GenericException(RouteErrCodes.ROUTE_428);
    }
  }

  protected List<Range> createRanges(String rangeContext){
    List<Range> ranges = new ArrayList<>();
    Iterable<String> rangeMetaArray = Splitter.on(",").trimResults().split(rangeContext);
    for (String rangeMeta : rangeMetaArray) {
      List<String> rangeItem = Splitter.on("-").trimResults().splitToList(rangeMeta);
      if (rangeItem.size() == 2) {
        ranges.add(new Range(Integer.parseInt(rangeItem.get(0)), Integer.parseInt(rangeItem.get(1))));
      } else {
        logger.error("Invalid range configuration.", rangeText);
        throw new GenericException(RouteErrCodes.ROUTE_429, new Object[]{rangeMeta});
      }
    }
    return ranges;
  }

  /**
   * 根据分片值计算hash
   *
   * @param shardValue 分片值
   * @return hash
   */
  public abstract int hashShardValue(T shardValue);

  public static class Range {

    private int start;
    private int end;

    Range(int start, int end) {
      this.start = start;
      this.end = end;
    }

    public boolean inRange(long value) {
      return value >= start && value <= end;
    }
  }
}
