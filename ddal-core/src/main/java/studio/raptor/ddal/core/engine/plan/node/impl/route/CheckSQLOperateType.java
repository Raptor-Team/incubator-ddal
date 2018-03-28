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

package studio.raptor.ddal.core.engine.plan.node.impl.route;

import studio.raptor.ddal.core.engine.ProcessContext;
import studio.raptor.ddal.core.engine.plan.node.ForkingNode;

/**
 * 判断SQL的操作类型。
 * DML，DDL，DCL，DAL语句的处理流程各不相同。
 *
 * @author Sam
 * @since 3.0.0
 */
public class CheckSQLOperateType extends ForkingNode {

  @Override
  protected int judge(ProcessContext context) {
    switch (context.getSqlStatementType().getOperate()) {
            /*
             * DML(数据操作语言) SELECT, UPDATE, INSERT, DELETE, CALL
             */
      case "DML":
        return 0;

            /*
             * DDL(数据定义语言) CREATE TABLE, CREATE INDEX, ALTER TABLE, ALTER INDEX
             */
      case "DDL":
            /*
             * DCL(数据库控制语言): COMMIT, ROLLBACK, SET READONLY | AUTOCOMMIT | TRANSACTIONISOLACTION
             */
        return 1;
      case "DCL":
            /*
             * DAL(分布式平台控制语言) SHOW STATUS ALL | POOL | NETWORK | CACHE, SHOW DATABASES, SHOW TABLES LIKE ,
             * SHOW USER LIKE, DESCRIBE(DESC) | DATABASE.TABLE, EXPLAIN SQL(DML | DDL), USE DATABASE
             */
      case "DAL":
            /*
             * 不支持的操作
             */
      default:
        return 2;
    }
  }

}
