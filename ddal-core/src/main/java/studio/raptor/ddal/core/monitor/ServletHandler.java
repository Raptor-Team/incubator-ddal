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

package studio.raptor.ddal.core.monitor;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import studio.raptor.ddal.common.json.JSONObject;

/**
 * 监控指标http接口请求处理器。具体监控哪些指标参考抽象的get方法。
 * 比如Web应用配置监控的URL地址是/monitor/*，那么连接池的状态可使用
 * /monitor/ConnPoolStats请求获取。
 *
 * @author Sam
 * @since 3.0.0
 */
public abstract class ServletHandler implements Serializable {

  /**
   * 连接池状态
   * @return 所有连接池的状态，使用JSON格式
   */
  @SuppressWarnings("unused")
  protected abstract String getConnPoolStats();

  /**
   * 进程维度的SQL执行统计，包含SQL总数，成功和失败，以及修改和查询SQL
   * 的数量统计。
   *
   * @return 进程维度的SQL执行情况统计
   */
  @SuppressWarnings("unused")
  protected abstract String getProcSqlSummary();

  /**
   * 进程维度tps。
   *
   * @return 进程维度tps
   */
  protected abstract String getProcTps();

  /**
   * JVM内存信息
   * @return jvm内存信息
   */
  protected abstract String getProcMemInfo();

  /**
   * 监视请求的入口。根据请求的URI来判断请求监视的指标。
   * 比如请求是/xxx/xx/ConnPoolStats的URI表示请求的是连接池的状态指标。
   *
   * @param requestURI request uri
   * @return 指标明细
   * @throws IOException input/output exception
   */
  String handleRequest(String requestURI) throws IOException {
    String[] uriSplits = requestURI.split("/");
    String metricMethodName = "get" + uriSplits[uriSplits.length - 1];
    String metricStr = null;
    try {
      Method[] methods = this.getClass().getDeclaredMethods();
      for (Method method : methods) {
        if (metricMethodName.equals(method.getName())) {
          Object obj;
          metricStr = null == (obj = method.invoke(this)) ? "" : obj.toString();
          break;
        }
      }
      if (null == metricStr) {
        metricStr = errorMessage("No method found for " + metricMethodName);
      }
    } catch (IllegalAccessException | InvocationTargetException invokeException) {
      metricStr = errorMessage("Get metrics failed in executing method " + metricMethodName);
    }
    return metricStr;
  }

  /**
   * create a new error message formatted of json.
   *
   * @param message error message
   * @return json style error message.
   */
  private String errorMessage(String message) {
    Map<String, String> errorMap = new HashMap<>();
    errorMap.put("error", message);
    return JSONObject.toJSONString(errorMap);
  }
}
