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

package studio.raptor.ddal.common.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * DDAL 运行时工具
 *
 * @author Sam
 * @since 3.0.0
 */
public class RuntimeUtil {

  private static final Date STARTUP_DATETIME = Calendar.getInstance().getTime();
  private static final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyMMddhhmmssSSS");

  /**
   * 获取DDAL服务的系统组件启动的时间。
   *
   * @return current datetime in string format
   */
  public static String getSysStartupTime() {
    return simpleDateFormat.format(STARTUP_DATETIME);
  }

  public static String getSysUserHome() {
    return System.getProperty("user.home");
  }

  /**
   * 获取程序运行目录
   *
   * @return 程序运行目录
   */
  public static String getRunningPath() {
    return System.getProperty("user.dir");
  }

  /**
   * 系统变量(-Dddal.tx.log.path)优先级较高，如果系统变量未制定
   * 则选择进程运行的目录记录日志。
   *
   * @return 事务日志目录。
   */
  public static String getTransactionRecordLog() {
    String sysEnvLogPath = System.getProperty("ddal.tx.log.path");
    return StringUtil.isEmpty(sysEnvLogPath) ? getRunningPath() : sysEnvLogPath;
  }
}
