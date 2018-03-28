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

package studio.raptor.ddal.core.engine;

import com.google.common.hash.Hashing;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import studio.raptor.ddal.common.helper.JMXHelper;
import studio.raptor.ddal.core.engine.plan.PlanNodeChain;
import studio.raptor.ddal.core.parser.result.ParseResult;

/**
 * 存储与SQL语句相关的解析结果和执行计划实例
 *
 * @author Sam
 * @since 3.0.0
 */
public class MemoryObjectsBasedOnSQL implements MemoryObjectsBasedOnSQLMBean {

  static{
    JMXHelper.registerMBean(new MemoryObjectsBasedOnSQL(), "DDAL:type=SqlReplace");
  }

  private static Map<String, SqlMetaData> sqlMetaDataCache = new ConcurrentHashMap<>();
  private static Map<String, PlanNodeChain> planInstanceCache = new ConcurrentHashMap<>();
  private static Map<String, ParseResult> parseResultCache = new ConcurrentHashMap<>();

  public static void cacheSql(String sql, SqlMetaData metaData){
    String fingerPrint = md5(sql);
    metaData.setOriginFingerprint(fingerPrint);
    sqlMetaDataCache.put(fingerPrint, metaData);
  }

  public static SqlMetaData getCacheSql(String sql){
    return sqlMetaDataCache.get(md5(sql));
  }

  public static void cachePlan(String planKey, PlanNodeChain pnc) {
    planInstanceCache.put(md5(planKey), pnc);
  }

  public static PlanNodeChain getCachedPlan(String planKey) {
    return planInstanceCache.get(md5(planKey));
  }

  public static void memorizeParseResult(String sql, ParseResult parseResult) {
    parseResultCache.put(md5(sql), parseResult);
  }

  public static ParseResult getMemoryParseResult(String sql) {
    return parseResultCache.get(md5(sql));
  }

  private static String md5(String sql) {
    return Hashing.md5().hashString(sql, Charset.forName("UTF-8")).toString();
  }

  /*---------------Replace Mbean Methods---------------*/
  @Override
  public Collection<String> getSqlList() {
    Collection<String> sqlList = new ArrayList<>();
    for(SqlMetaData sqlMetaData : sqlMetaDataCache.values()){
      sqlList.add(sqlMetaData.toString());
    }
    return sqlList;
  }

  @Override
  public SqlMetaData getSqlByFingerprint(String fingerprint) {
    return sqlMetaDataCache.get(fingerprint);
  }

  @Override
  public String getSqlByOriginSql(String originSql) {
    Collection<SqlMetaData> rows = sqlMetaDataCache.values();
    for(SqlMetaData metaData : rows){
      if(metaData.getOriginSql().equals(originSql)){
        return metaData.toString();
      }
    }
    return null;
  }

  @Override
  public SqlMetaData getSqlByReplaceSql(String replaceSql) {
    Collection<SqlMetaData> rows = sqlMetaDataCache.values();
    for(SqlMetaData metaData : rows){
      if(metaData.getReplaceSql().equals(replaceSql)){
        return metaData;
      }
    }
    return null;
  }

  @Override
  public boolean replaceSql(String fingerprint, String newSql) {
    SqlMetaData metaData = sqlMetaDataCache.get(fingerprint);
    if(null != metaData){
      metaData.setReplaced(true);
      metaData.setReplaceSql(newSql);
      return true;
    }else{
      return false;
    }
  }

  @Override
  public void cancelReplace(String fingerprint) {
    sqlMetaDataCache.remove(fingerprint);
  }
}