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

package studio.raptor.ddal.jdbc.test;

import java.io.IOException;
import java.io.Reader;
import java.sql.PreparedStatement;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import org.junit.Test;

/**
 * 预编译语句（RaptorPreparedStatement）不支持方法 测试案例。
 *
 * @author Sam Tsai
 * @since 3.0.0
 */
public class RaptorPreparedStatementUnsupportedTest extends AutoPrepareTestingEnv {

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setRowId() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setRowId(1, new RowId() {
        @Override
        public byte[] getBytes() {
          return new byte[0];
        }
      });
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setArray() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.setArray(1, null);
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getMetaData() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.getMetaData();
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setNClob2() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setNClob(1, new Reader() {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
          return 0;
        }

        @Override
        public void close() throws IOException {

        }
      });
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setNClob3() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setNClob(1, new Reader() {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
          return 0;
        }

        @Override
        public void close() throws IOException {

        }
      }, 1);
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_addBatch() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql);
    ) {
      statement.addBatch();
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getParameterMetaData() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.getParameterMetaData();
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setNString() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setNString(1, "");
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setNCharacterStream1() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setNCharacterStream(1, new Reader() {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
          return 0;
        }

        @Override
        public void close() throws IOException {

        }
      });
    }
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_setNCharacterStream2() throws SQLException {
    String sql = "insert into customer(id, name, gender, create_date) values (?,?,?,?)";
    try (

        PreparedStatement statement = connection.prepareStatement(sql)
    ) {
      statement.setNCharacterStream(1, new Reader() {
        @Override
        public int read(char[] cbuf, int off, int len) throws IOException {
          return 0;
        }

        @Override
        public void close() throws IOException {

        }
      }, 1);
    }
  }

}