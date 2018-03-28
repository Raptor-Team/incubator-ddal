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

package studio.raptor.ddal.jdbc.processor;

import java.sql.ResultSet;
import java.sql.SQLException;

import studio.raptor.ddal.core.engine.ProcessEngine;
import studio.raptor.ddal.jdbc.RaptorResultSet;

/**
 * @author Sam
 * @since 3.0.0
 */
public class PreparedStatementProcessor extends StatementProcessor {

  public PreparedStatementProcessor(ProcessEngine engine) {
    super(engine);
  }

  public ResultSet executeQuery() throws SQLException {
    execute();
    return new RaptorResultSet(getProcessContext().getMergedResult());
  }

  /**
   * execute prepared statement update
   *
   * @return affected rows
   * @throws SQLException database access error
   */
  public int executeUpdate() throws SQLException {
    execute();
    return getProcessContext().getMergedResult().getAffectedRows();
  }

  public boolean execute() throws SQLException {
    getProcessContext().setIsPreparedStatement(true);
    executeInternal();
    return true;
  }
}
