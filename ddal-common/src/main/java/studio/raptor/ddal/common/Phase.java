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

package studio.raptor.ddal.common;

/**
 * Phase of sql execution
 *
 * @author Sam
 * @since 3.0.0
 */
public enum Phase {

  ACCEPT(-3, "接收"), HANDSHAKE(-2, "握手"), AUTH(-1, "登陆认证"), PARSE(1, "PARSE"), REWRITE(2, "REWRITE"), ROUTE(3, "ROUTE"),

  PREPARE(4, "预编译准备"), EXECUTE(5, "EXECUTE"), SUBEXECUTE(6, "子句执行"), EXEEND(7, "SQL执行结束"), MERGE(8, "MERGE"),

  TXSTART(9, "事务启动"), TXING(10, "事务执行"), TXEND(11, "事务结束");

  private int code;
  private String desc;

  Phase(int code, String desc) {
    this.code = code;
    this.desc = desc;
  }

  public int getCode() {
    return code;
  }

  public String getDesc() {
    return desc;
  }

  @Override
  public String toString() {
    return "Phase[code=" + this.code + ",desc=" + this.desc + "]";
  }
}
