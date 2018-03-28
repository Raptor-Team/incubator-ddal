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

package studio.raptor.ddal.core.executor;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

import java.sql.Timestamp;
import java.util.List;

import studio.raptor.ddal.config.model.shard.Shard;
import studio.raptor.ddal.core.connection.BackendConnection;
import studio.raptor.ddal.core.executor.resultset.ResultData;
import studio.raptor.ddal.core.parser.result.SQLStatementType;

/**
 * 执行单元
 *
 * @author Charley
 * @since 1.0
 */
public class ExecutionUnit {

  private long unitId;
  private String finalSql;
  private SQLStatementType type;
  private Shard shard;
  private boolean isPrepared;
  private List<Object> parameters;
  private BackendConnection connection;
//  private boolean connAutoCommit = true; //default true
  private ResultData resultData;

  public ExecutionUnit() {
    //TODO 序列生成ID
  }

  public long getUnitId() {
    return unitId;
  }

  public void setUnitId(long unitId) {
    this.unitId = unitId;
  }

  public String getFinalSql() {
    return finalSql;
  }

  public void setFinalSql(String finalSql) {
    this.finalSql = finalSql;
  }

  public String getFinalSql(boolean replaceQuestionMark) {
    if (replaceQuestionMark) {
      StringBuilder fsql = new StringBuilder();
      List<String> sqlSplits = Splitter.on('?').splitToList(finalSql);
      for (int i = 0, size = sqlSplits.size(); i < size; i++) {
        fsql.append(sqlSplits.get(i));
        if (i < size - 1) {
          Object param = parameters.get(i);
          if (param instanceof String || param instanceof Timestamp) {
            fsql.append(Joiner.on("").join(new Object[]{'\'', param, '\''}));
          } else {
            fsql.append(parameters.get(i));
          }
        }
      }
      return fsql.toString();
    } else {
      return getFinalSql();
    }
  }

  public SQLStatementType getType() {
    return type;
  }

  public void setType(SQLStatementType type) {
    this.type = type;
  }

  public Shard getShard() {
    return shard;
  }

  public void setShard(Shard shard) {
    this.shard = shard;
  }

  public boolean isPrepared() {
    return isPrepared;
  }

  public void setPrepared(boolean prepared) {
    isPrepared = prepared;
  }

  public List<Object> getParameters() {
    return parameters;
  }

  public void setParameters(List<Object> parameters) {
    this.parameters = parameters;
  }

  public BackendConnection getConnection() {
    return connection;
  }

  public void setConnection(BackendConnection connection) {
    this.connection = connection;
  }

//  public boolean isConnAutoCommit() {
//    return connAutoCommit;
//  }
//
//  public void setConnAutoCommit(boolean connAutoCommit) {
//    this.connAutoCommit = connAutoCommit;
//  }

  public ResultData getResultData() {
    return resultData;
  }

  public void setResultData(ResultData resultData) {
    this.resultData = resultData;
  }

  @Override
  public String toString() {
    return "ExecutionUnit{" +
            "unitId=" + unitId +
            ", finalSql='" + finalSql + '\'' +
            ", type=" + type +
            ", shard=" + shard +
            ", parameters=" + parameters +
            ", connection=" + connection +
            ", resultData=" + resultData +
            '}';
  }
}
