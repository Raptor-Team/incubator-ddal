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

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

public class TIMESTAMP extends Datum implements Serializable {
  private static final int CENTURY_DEFAULT = 119;
  private static final int DECADE_DEFAULT = 100;
  private static final int MONTH_DEFAULT = 1;
  private static final int DAY_DEFAULT = 1;
  private static final int DECADE_INIT = 170;
  private static final int JAVA_YEAR = 1970;
  private static final int JAVA_MONTH = 0;
  private static final int JAVA_DATE = 1;
  private static final int SIZE_TIMESTAMP = 11;
  private static final int SIZE_TIMESTAMP_NOFRAC = 7;
  private static final int SIZE_DATE = 7;
  private static final int MINYEAR = -4712;
  private static final int MAXYEAR = 9999;
  private static final int JANMONTH = 1;
  private static final int DECMONTH = 12;
  private static final int MINDAYS = 1;
  private static final int MAXDAYS = 31;
  private static final int MINHOURS = 1;
  private static final int MAXHOURS = 24;
  private static final int MINMINUTES = 1;
  private static final int MAXMINUTES = 60;
  private static final int MINSECONDS = 1;
  private static final int MAXSECONDS = 60;
  private static final int[] daysInMonth = new int[]{31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
  static final long serialVersionUID = -7964732752952728545L;

  public TIMESTAMP() {
    super(initTimestamp());
  }

  public TIMESTAMP(byte[] var1) {
    super(var1);
  }

  public TIMESTAMP(Time var1) {
    super(toBytes(var1));
  }

  public TIMESTAMP(Date var1) {
    super(toBytes(var1));
  }

  public TIMESTAMP(Timestamp var1) {
    super(toBytes(var1));
  }

  public TIMESTAMP(String var1) {
    super(toBytes(var1));
  }

  public static Date toDate(byte[] var0) throws SQLException {
    int var2 = var0.length;
    int[] var1;
    if(var2 == 11) {
      var1 = new int[11];
    } else {
      var1 = new int[7];
    }

    int var3;
    for(var3 = 0; var3 < var0.length; ++var3) {
      var1[var3] = var0[var3] & 255;
    }

    var3 = (var1[0] - 100) * 100 + (var1[1] - 100);
    Calendar var4 = Calendar.getInstance();
    var4.set(1, var3);
    var4.set(2, var1[2] - 1);
    var4.set(5, var1[3]);
    var4.set(11, var1[4] - 1);
    var4.set(12, var1[5] - 1);
    var4.set(13, var1[6] - 1);
    var4.set(14, 0);
    long var5 = var4.getTime().getTime();
    return new Date(var5);
  }

  public static Time toTime(byte[] var0) throws SQLException {
    int var1 = var0[4] & 255;
    int var2 = var0[5] & 255;
    int var3 = var0[6] & 255;
    return new Time(var1 - 1, var2 - 1, var3 - 1);
  }

  public static Timestamp toTimestamp(byte[] var0) throws SQLException {
    int var2 = var0.length;
    int[] var1;
    if(var2 == 11) {
      var1 = new int[11];
    } else {
      var1 = new int[7];
    }

    int var3;
    for(var3 = 0; var3 < var0.length; ++var3) {
      var1[var3] = var0[var3] & 255;
    }

    var3 = (var1[0] - 100) * 100 + (var1[1] - 100);
    Calendar var4 = Calendar.getInstance();
    var4.set(1, var3);
    var4.set(2, var1[2] - 1);
    var4.set(5, var1[3]);
    var4.set(11, var1[4] - 1);
    var4.set(12, var1[5] - 1);
    var4.set(13, var1[6] - 1);
    var4.set(14, 0);
    long var5 = var4.getTime().getTime();
    Timestamp var7 = new Timestamp(var5);
    if(var2 == 11) {
      int var8 = var1[7] << 24;
      var8 |= var1[8] << 16;
      var8 |= var1[9] << 8;
      var8 |= var1[10] & 255;
      var7.setNanos(var8);
    } else {
      var7.setNanos(0);
    }

    return var7;
  }

  public static Timestamp toTimestamp(byte[] var0, Calendar var1) throws SQLException {
    int var3 = var0.length;
    int[] var2;
    if(var3 == 11) {
      var2 = new int[11];
    } else {
      var2 = new int[7];
    }

    int var4;
    for(var4 = 0; var4 < var0.length; ++var4) {
      var2[var4] = var0[var4] & 255;
    }

    var4 = (var2[0] - 100) * 100 + (var2[1] - 100);
    if(var1 == null) {
      var1 = Calendar.getInstance();
    }

    var1.clear();
    var1.set(1, var4);
    var1.set(2, var2[2] - 1);
    var1.set(5, var2[3]);
    var1.set(11, var2[4] - 1);
    var1.set(12, var2[5] - 1);
    var1.set(13, var2[6] - 1);
    var1.set(14, 0);
    long var5 = var1.getTime().getTime();
    Timestamp var7 = new Timestamp(var5);
    if(var3 == 11) {
      int var8 = var2[7] << 24;
      var8 |= var2[8] << 16;
      var8 |= var2[9] << 8;
      var8 |= var2[10] & 255;
      var7.setNanos(var8);
    } else {
      var7.setNanos(0);
    }

    return var7;
  }

  public Timestamp timestampValue() throws SQLException {
    return toTimestamp(this.getBytes());
  }

  public Timestamp timestampValue(Calendar var1) throws SQLException {
    return toTimestamp(this.getBytes(), var1);
  }

  public static String toString(byte[] var0) {
    int var1 = 0;
    int var3 = var0.length;
    int[] var2;
    if(var3 == 11) {
      var2 = new int[11];
    } else {
      var2 = new int[7];
    }

    for(int var4 = 0; var4 < var0.length; ++var4) {
      if(var0[var4] < 0) {
        var2[var4] = var0[var4] + 256;
      } else {
        var2[var4] = var0[var4];
      }
    }

    --var2[4];
    --var2[5];
    --var2[6];
    int var5 = (var2[0] - 100) * 100 + (var2[1] - 100);
    if(var3 == 11) {
      var1 = (var2[7] & 255) << 24;
      var1 |= (var2[8] & 255) << 16;
      var1 |= (var2[9] & 255) << 8;
      var1 |= var2[10] & 255 & 255;
    }

    return var5 + "-" + var2[2] + "-" + var2[3] + " " + var2[4] + "." + var2[5] + "." + var2[6] + "." + var1;
  }

  public byte[] toBytes() {
    return this.getBytes();
  }

  public static byte[] toBytes(Time var0) {
    if(var0 == null) {
      return null;
    } else {
      byte[] var1 = new byte[7];
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      var1[0] = 119;
      var1[1] = 100;
      var1[2] = 1;
      var1[3] = 1;
      var1[4] = (byte)(var2.get(11) + 1);
      var1[5] = (byte)(var2.get(12) + 1);
      var1[6] = (byte)(var2.get(13) + 1);
      return var1;
    }
  }

  public static byte[] toBytes(Date var0) {
    if(var0 == null) {
      return null;
    } else {
      byte[] var1 = new byte[7];
      Calendar var2 = Calendar.getInstance();
      var2.setTime(var0);
      var1[0] = (byte)(var2.get(1) / 100 + 100);
      var1[1] = (byte)(var2.get(1) % 100 + 100);
      var1[2] = (byte)(var2.get(2) + 1);
      var1[3] = (byte)var2.get(5);
      var1[4] = 1;
      var1[5] = 1;
      var1[6] = 1;
      return var1;
    }
  }

  public static byte[] toBytes(Timestamp var0) {
    if(var0 == null) {
      return null;
    } else {
      int var1 = var0.getNanos();
      byte[] var2;
      if(var1 == 0) {
        var2 = new byte[7];
      } else {
        var2 = new byte[11];
      }

      Calendar var3 = Calendar.getInstance();
      var3.setTime(var0);
      var2[0] = (byte)(var3.get(1) / 100 + 100);
      var2[1] = (byte)(var3.get(1) % 100 + 100);
      var2[2] = (byte)(var3.get(2) + 1);
      var2[3] = (byte)var3.get(5);
      var2[4] = (byte)(var3.get(11) + 1);
      var2[5] = (byte)(var3.get(12) + 1);
      var2[6] = (byte)(var3.get(13) + 1);
      if(var1 != 0) {
        var2[7] = (byte)(var1 >> 24);
        var2[8] = (byte)(var1 >> 16 & 255);
        var2[9] = (byte)(var1 >> 8 & 255);
        var2[10] = (byte)(var1 & 255);
      }

      return var2;
    }
  }

  public static byte[] toBytes(String var0) {
    return toBytes(Timestamp.valueOf(var0));
  }

  public Object toJdbc() throws SQLException {
    return this.timestampValue();
  }

  public Object makeJdbcArray(int var1) {
    Timestamp[] var2 = new Timestamp[var1];
    return var2;
  }

  public boolean isConvertibleTo(Class var1) {
    return var1.getName().compareTo("java.sql.Date") == 0 || var1.getName().compareTo("java.sql.Time") == 0 || var1.getName().compareTo("java.sql.Timestamp") == 0 || var1.getName().compareTo("java.lang.String") == 0;
  }

  public String stringValue() {
    return toString(this.getBytes());
  }

  public Date dateValue() throws SQLException {
    return toDate(this.getBytes());
  }

  public Time timeValue() throws SQLException {
    return toTime(this.getBytes());
  }

  private static byte[] initTimestamp() {
    byte[] var0 = new byte[11];
    var0[0] = 119;
    var0[1] = -86;
    var0[2] = 1;
    var0[3] = 1;
    var0[4] = 1;
    var0[5] = 1;
    var0[6] = 1;
    return var0;
  }

  private boolean isLeapYear(int var1) {
    boolean var10000;
    label28: {
      if(var1 % 4 == 0) {
        if(var1 <= 1582) {
          if(var1 != -4712) {
            break label28;
          }
        } else if(var1 % 100 != 0 || var1 % 400 == 0) {
          break label28;
        }
      }

      var10000 = false;
      return var10000;
    }

    var10000 = true;
    return var10000;
  }

  private boolean isValid() {
    byte[] var1 = this.getBytes();
    if(var1.length != 11 && var1.length != 7) {
      return false;
    } else {
      int var2 = ((var1[0] & 255) - 100) * 100 + ((var1[1] & 255) - 100);
      if(var2 >= -4712 && var2 <= 9999) {
        if(var2 == 0) {
          return false;
        } else {
          int var3 = var1[2] & 255;
          if(var3 >= 1 && var3 <= 12) {
            int var4 = var1[3] & 255;
            if(var4 >= 1 && var4 <= 31) {
              if(var4 <= daysInMonth[var3 - 1] || this.isLeapYear(var2) && var3 == 2 && var4 == 29) {
                if(var2 == 1582 && var3 == 10 && var4 >= 5 && var4 < 15) {
                  return false;
                } else {
                  int var5 = var1[4] & 255;
                  if(var5 >= 1 && var5 <= 24) {
                    int var6 = var1[5] & 255;
                    if(var6 >= 1 && var6 <= 60) {
                      int var7 = var1[6] & 255;
                      return var7 >= 1 && var7 <= 60;
                    } else {
                      return false;
                    }
                  } else {
                    return false;
                  }
                }
              } else {
                return false;
              }
            } else {
              return false;
            }
          } else {
            return false;
          }
        }
      } else {
        return false;
      }
    }
  }

  private void readObject(ObjectInputStream var1) throws IOException, ClassNotFoundException {
    var1.defaultReadObject();
    if(!this.isValid()) {
      throw new IOException("Invalid TIMESTAMP");
    }
  }
}
