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

package studio.raptor.ddal.jdbc;

import java.sql.Types;

/**
 * <P>Defines the constants that are used to identify generic
 * SQL types, called JDBC types.
 * <p>
 */
public enum JDBCType {

  /**
   * Identifies the generic SQL type {@code BIT}.
   */
  BIT(Types.BIT),
  /**
   * Identifies the generic SQL type {@code TINYINT}.
   */
  TINYINT(Types.TINYINT),
  /**
   * Identifies the generic SQL type {@code SMALLINT}.
   */
  SMALLINT(Types.SMALLINT),
  /**
   * Identifies the generic SQL type {@code INTEGER}.
   */
  INTEGER(Types.INTEGER),
  /**
   * Identifies the generic SQL type {@code BIGINT}.
   */
  BIGINT(Types.BIGINT),
  /**
   * Identifies the generic SQL type {@code FLOAT}.
   */
  FLOAT(Types.FLOAT),
  /**
   * Identifies the generic SQL type {@code REAL}.
   */
  REAL(Types.REAL),
  /**
   * Identifies the generic SQL type {@code DOUBLE}.
   */
  DOUBLE(Types.DOUBLE),
  /**
   * Identifies the generic SQL type {@code NUMERIC}.
   */
  NUMERIC(Types.NUMERIC),
  /**
   * Identifies the generic SQL type {@code DECIMAL}.
   */
  DECIMAL(Types.DECIMAL),
  /**
   * Identifies the generic SQL type {@code CHAR}.
   */
  CHAR(Types.CHAR),
  /**
   * Identifies the generic SQL type {@code VARCHAR}.
   */
  VARCHAR(Types.VARCHAR),
  /**
   * Identifies the generic SQL type {@code LONGVARCHAR}.
   */
  LONGVARCHAR(Types.LONGVARCHAR),
  /**
   * Identifies the generic SQL type {@code DATE}.
   */
  DATE(Types.DATE),
  /**
   * Identifies the generic SQL type {@code TIME}.
   */
  TIME(Types.TIME),
  /**
   * Identifies the generic SQL type {@code TIMESTAMP}.
   */
  TIMESTAMP(Types.TIMESTAMP),
  /**
   * Identifies the generic SQL type {@code BINARY}.
   */
  BINARY(Types.BINARY),
  /**
   * Identifies the generic SQL type {@code VARBINARY}.
   */
  VARBINARY(Types.VARBINARY),
  /**
   * Identifies the generic SQL type {@code LONGVARBINARY}.
   */
  LONGVARBINARY(Types.LONGVARBINARY),
  /**
   * Identifies the generic SQL value {@code NULL}.
   */
  NULL(Types.NULL),
  /**
   * Indicates that the SQL type
   * is database-specific and gets mapped to a Java object that can be
   * accessed via the methods getObject and setObject.
   */
  OTHER(Types.OTHER),
  /**
   * Indicates that the SQL type
   * is database-specific and gets mapped to a Java object that can be
   * accessed via the methods getObject and setObject.
   */
  JAVA_OBJECT(Types.JAVA_OBJECT),
  /**
   * Identifies the generic SQL type {@code DISTINCT}.
   */
  DISTINCT(Types.DISTINCT),
  /**
   * Identifies the generic SQL type {@code STRUCT}.
   */
  STRUCT(Types.STRUCT),
  /**
   * Identifies the generic SQL type {@code ARRAY}.
   */
  ARRAY(Types.ARRAY),
  /**
   * Identifies the generic SQL type {@code BLOB}.
   */
  BLOB(Types.BLOB),
  /**
   * Identifies the generic SQL type {@code CLOB}.
   */
  CLOB(Types.CLOB),
  /**
   * Identifies the generic SQL type {@code REF}.
   */
  REF(Types.REF),
  /**
   * Identifies the generic SQL type {@code DATALINK}.
   */
  DATALINK(Types.DATALINK),
  /**
   * Identifies the generic SQL type {@code BOOLEAN}.
   */
  BOOLEAN(Types.BOOLEAN),

    /* JDBC 4.0 Types */

  /**
   * Identifies the SQL type {@code ROWID}.
   */
  ROWID(Types.ROWID),
  /**
   * Identifies the generic SQL type {@code NCHAR}.
   */
  NCHAR(Types.NCHAR),
  /**
   * Identifies the generic SQL type {@code NVARCHAR}.
   */
  NVARCHAR(Types.NVARCHAR),
  /**
   * Identifies the generic SQL type {@code LONGNVARCHAR}.
   */
  LONGNVARCHAR(Types.LONGNVARCHAR),
  /**
   * Identifies the generic SQL type {@code NCLOB}.
   */
  NCLOB(Types.NCLOB),
  /**
   * Identifies the generic SQL type {@code SQLXML}.
   */
  SQLXML(Types.SQLXML);

  /**
   * The Integer value for the JDBCType.  It maps to a value in
   * {@code Types.java}
   */
  private Integer type;

  /**
   * Constructor to specify the data type value from {@code Types) for
   * this data type.
   *
   * @param type The value from {@code Types) for this data type
   */
  JDBCType(final Integer type) {
    this.type = type;
  }

  /**
   * Returns the {@code JDBCType} that corresponds to the specified
   * {@code Types} value
   *
   * @param type {@code Types} value
   * @return The {@code JDBCType} constant
   * @throws IllegalArgumentException if this enum type has no constant with the specified {@code
   *                                  Types} value
   * @see Types
   */
  public static JDBCType valueOf(int type) {
    for (JDBCType sqlType : JDBCType.class.getEnumConstants()) {
      if (type == sqlType.type)
        return sqlType;
    }
    throw new IllegalArgumentException("Type:" + type + " is not a valid "
            + "Types.java value.");
  }

  /**
   * {@inheritDoc }
   *
   * @return The name of this {@code SQLType}.
   */
  public String getName() {
    return name();
  }

  /**
   * Returns the name of the vendor that supports this data type.
   *
   * @return The name of the vendor for this data type which is {@literal java.sql} for JDBCType.
   */
  public String getVendor() {
    return "java.sql";
  }

  /**
   * Returns the vendor specific type number for the data type.
   *
   * @return An Integer representing the data type. For {@code JDBCType}, the value will be the same
   * value as in {@code Types} for the data type.
   */
  public Integer getVendorTypeNumber() {
    return type;
  }
}
