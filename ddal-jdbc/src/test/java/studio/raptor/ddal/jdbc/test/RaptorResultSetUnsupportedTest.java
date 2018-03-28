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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.Reader;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.HashMap;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialClob;
import org.junit.Test;

/**
 * @author Sam
 * @since 3.0.0
 */
public class RaptorResultSetUnsupportedTest extends AutoPrepareTestingEnv {

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getObject1() throws SQLException {
    try (

        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getObject(1, new HashMap<String, Class<?>>());
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getObject2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getObject("", new HashMap<String, Class<?>>());
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getArray1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getArray(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getArray() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getArray("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_first() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.first();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getRef1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getRef(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getRef() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getRef("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_previous() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.previous();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_isLast() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.isLast();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_relative() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.relative(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_absolute() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.absolute(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getBinaryStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getBinaryStream(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getBinaryStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getBinaryStream("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getBlob1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getBlob(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getBlob2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getBlob("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getAsciiStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getAsciiStream(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getAsciiStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getAsciiStream("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_isFirst() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.isFirst();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_isBeforeFirst() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.isBeforeFirst();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_isAfterLast() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.isAfterLast();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_beforeFirst() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.beforeFirst();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_afterLast() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.afterLast();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_insertRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.insertRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_deleteRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.deleteRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_refreshRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.refreshRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_cancelRowUpdates() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.cancelRowUpdates();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_moveToInsertRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.moveToInsertRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_moveToCurrentRow() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.moveToCurrentRow();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_rowInserted() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.rowInserted();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_rowUpdated() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.rowUpdated();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_rowDeleted() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.rowDeleted();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getCursorName() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getCursorName();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getHoldability() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getHoldability();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getNString1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getNString(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getNString2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getNString("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getNClob1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getNClob(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getNClob2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getNClob("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getNCharacterStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getNCharacterStream(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getNCharacterStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getNCharacterStream("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getRowId1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getRowId(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getRowId2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getRowId("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getUnicodeStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getUnicodeStream(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getUnicodeStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getUnicodeStream("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getCharacterStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getCharacterStream(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getCharacterStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getCharacterStream("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getClob1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getClob(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getClob2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getClob("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getSQLXML1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getSQLXML(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getSQLXML2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.getSQLXML("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_getWarnings() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          throw resultSet.getWarnings();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_clearWarnings() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.clearWarnings();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_last() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.last();
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBytes1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBytes(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBytes2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBytes("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBoolean1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBoolean(1, false);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBoolean2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBoolean("", false);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateByte1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateByte(1, (byte) 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateByte2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateByte("", (byte) 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateShort1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateShort(1, (short) 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateShort2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateShort("label", (short) 2);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateInt1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateInt(1, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateInt2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateInt("", 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateLong1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateLong(1, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateLong2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateLong("", 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateFloat1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateFloat(1, (float) 1.1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateFloat2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateFloat("", (float) 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNull1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNull(1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNull2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNull("");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateDouble1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateDouble(1, (double) 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateDouble2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateDouble("", (double) 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBigDecimal1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBigDecimal(1, new BigDecimal(1));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBigDecimal2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBigDecimal("", new BigDecimal(1));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateString1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateString(1, "");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateString2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateString("", "");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNString1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNString(1, "");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNString2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNString("", "");
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateDate1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateDate(1, new Date(System.currentTimeMillis()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateDate2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateDate("", new Date(System.currentTimeMillis()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateTime1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateTime(1, new Time(System.currentTimeMillis()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateTime2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateTime("", new Time(System.currentTimeMillis()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateTimestamp1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateTimestamp(1, new Timestamp(System.currentTimeMillis()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateTimestamp2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateTimestamp("", new Timestamp(System.currentTimeMillis()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateAsciiStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateAsciiStream(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateAsciiStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateAsciiStream(1, null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateAsciiStream3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateAsciiStream(1, null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateAsciiStream4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateAsciiStream("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateAsciiStream5() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateAsciiStream("", null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateAsciiStream6() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateAsciiStream("", null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBinaryStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBinaryStream(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBinaryStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBinaryStream(1, null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBinaryStream3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBinaryStream(1, null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBinaryStream4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBinaryStream("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBinaryStream5() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBinaryStream("", null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBinaryStream6() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBinaryStream("", null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateCharacterStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateCharacterStream(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateCharacterStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateCharacterStream(1, null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateCharacterStream3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateCharacterStream(1, null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateCharacterStream4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateCharacterStream("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateCharacterStream5() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateCharacterStream("", null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateCharacterStream6() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateCharacterStream("", null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNCharacterStream1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNCharacterStream(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNCharacterStream2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNCharacterStream(1, null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNCharacterStream3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNCharacterStream("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNCharacterStream4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNCharacterStream("", null, 1L);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateObject1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateObject(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateObject2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateObject(1, null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateObject3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateObject("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateObject4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateObject("", null, 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateRef1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateRef(1, null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateRef2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateRef("", null);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBlob1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBlob(1, new SerialBlob("".getBytes()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBlob2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBlob(1, new ByteArrayInputStream("".getBytes()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBlob3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBlob(1, new ByteArrayInputStream("".getBytes()), 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBlob4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBlob("", new SerialBlob("".getBytes()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBlob5() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBlob("", new ByteArrayInputStream("".getBytes()));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateBlob6() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateBlob("", new ByteArrayInputStream("".getBytes()), 1);
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateClob1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateClob(1, new SerialClob(new char[]{'1'}));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateClob2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateClob(1, new Reader() {
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
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateClob3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateClob(1, new Reader() {
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
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateClob4() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateClob("", new SerialClob(new char[]{'1'}));
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateClob5() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateClob("", new Reader() {
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
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateClob6() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateClob("", new Reader() {
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
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNClob2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNClob(1, new Reader() {
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
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNClob3() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNClob(1, new Reader() {
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
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNClob5() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNClob("", new Reader() {
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
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateNClob6() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateNClob("", new Reader() {
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
  }

  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateRowId1() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateRowId(1, new RowId() {
            @Override
            public byte[] getBytes() {
              return new byte[0];
            }
          });
        }
      }
    }
  }


  @Test(expected = SQLFeatureNotSupportedException.class)
  public void unsupported_updateRowId2() throws SQLException {
    try (
        Statement statement = connection.createStatement()
    ) {
      try (
          ResultSet resultSet = statement
              .executeQuery("select * from crm222.customer limit 0, 10 where id = 1");
      ) {
        // use first row
        if (resultSet.next()) {
          resultSet.updateRowId("", new RowId() {
            @Override
            public byte[] getBytes() {
              return new byte[0];
            }
          });
        }
      }
    }
  }

}
