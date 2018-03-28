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

//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package oracle.sql;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;

public abstract class Datum implements Serializable {
  private byte[] data;
  static final long serialVersionUID = 4645732484621936751L;

  public Datum() {
  }

  public Datum(byte[] var1) {
    this.data = var1;
  }

  public boolean equals(Object var1) {
    if(this == var1) {
      return true;
    } else if(var1 != null && var1 instanceof Datum) {
      if(this.getClass() == var1.getClass()) {
        Datum var2 = (Datum)var1;
        if(this.data == null && var2.data == null) {
          return true;
        } else if(this.data == null && var2.data != null || this.data != null && var2.data == null) {
          return false;
        } else if(this.data.length != var2.data.length) {
          return false;
        } else {
          for(int var3 = 0; var3 < this.data.length; ++var3) {
            if(this.data[var3] != var2.data[var3]) {
              return false;
            }
          }

          return true;
        }
      } else {
        return false;
      }
    } else {
      return false;
    }
  }

  public byte[] shareBytes() {
    return this.data;
  }

  public long getLength() {
    return null == this.data?0L:(long)this.data.length;
  }

  public void setBytes(byte[] var1) {
    int var2 = var1.length;
    this.data = new byte[var2];
    System.arraycopy(var1, 0, this.data, 0, var2);
  }

  public void setShareBytes(byte[] var1) {
    this.data = var1;
  }

  public byte[] getBytes() {
    byte[] var1 = new byte[this.data.length];
    System.arraycopy(this.data, 0, var1, 0, this.data.length);
    return var1;
  }

  public InputStream getStream() {
    return new ByteArrayInputStream(this.data);
  }

  public String stringValue() throws SQLException {
    throw new SQLException("Conversion to String failed");
  }

  public boolean booleanValue() throws SQLException {
    throw new SQLException("Conversion to boolean failed");
  }

  public int intValue() throws SQLException {
    throw new SQLException("Conversion to integer failed");
  }

  public long longValue() throws SQLException {
    throw new SQLException("Conversion to long failed");
  }

  public float floatValue() throws SQLException {
    throw new SQLException("Conversion to float failed");
  }

  public double doubleValue() throws SQLException {
    throw new SQLException("Conversion to double failed");
  }

  public byte byteValue() throws SQLException {
    throw new SQLException("Conversion to byte failed");
  }

  public BigDecimal bigDecimalValue() throws SQLException {
    throw new SQLException("Conversion to BigDecimal failed");
  }

  public Date dateValue() throws SQLException {
    throw new SQLException("Conversion to Date failed");
  }

  public Time timeValue() throws SQLException {
    throw new SQLException("Conversion to Time failed");
  }

  public Timestamp timestampValue() throws SQLException {
    throw new SQLException("Conversion to Timestamp failed");
  }

  public Reader characterStreamValue() throws SQLException {
    throw new SQLException("Conversion to character stream failed");
  }

  public InputStream asciiStreamValue() throws SQLException {
    throw new SQLException("Conversion to ascii stream failed");
  }

  public InputStream binaryStreamValue() throws SQLException {
    throw new SQLException("Conversion to binary stream failed");
  }

  public abstract boolean isConvertibleTo(Class var1);

  public abstract Object toJdbc() throws SQLException;

  public abstract Object makeJdbcArray(int var1);

  protected static int compareBytes(byte[] var0, byte[] var1) {
    int var2 = var0.length;
    int var3 = var1.length;
    int var4 = 0;
    int var5 = Math.min(var2, var3);
    boolean var6 = false;

    for(boolean var7 = false; var4 < var5; ++var4) {
      int var8 = var0[var4] & 255;
      int var9 = var1[var4] & 255;
      if(var8 != var9) {
        if(var8 < var9) {
          return -1;
        }

        return 1;
      }
    }

    if(var2 == var3) {
      return 0;
    } else if(var2 > var3) {
      return 1;
    } else {
      return -1;
    }
  }
}
