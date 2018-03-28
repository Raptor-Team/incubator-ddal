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
 * @author jackcao
 * @since 3.0.0
 */
public class FieldType {

    //FIELD TYPE
    public static final int FIELD_TYPE_DECIMAL = 0x00;
    public static final int FIELD_TYPE_TINY = 0x01;
    public static final int FIELD_TYPE_SHORT = 0x02;
    public static final int FIELD_TYPE_LONG = 0x03;
    public static final int FIELD_TYPE_FLOAT = 0x04;
    public static final int FIELD_TYPE_DOUBLE = 0x05;
    public static final int FIELD_TYPE_NULL = 0x06;
    public static final int FIELD_TYPE_TIMESTAMP = 0x07;
    public static final int FIELD_TYPE_LONGLONG = 0x08;
    public static final int FIELD_TYPE_INT24 = 0x09;
    public static final int FIELD_TYPE_DATE = 0x0a;
    public static final int FIELD_TYPE_TIME = 0x0b;
    public static final int FIELD_TYPE_DATETIME = 0x0c;
    public static final int FIELD_TYPE_YEAR = 0x0d;
    public static final int FIELD_TYPE_NEWDATE = 0x0e;
    public static final int FIELD_TYPE_VARCHAR = 0x0f;
    public static final int FIELD_TYPE_BIT = 0x10;
    public static final int FIELD_TYPE_TIMESTAMP2 = 0x11;
    public static final int FIELD_TYPE_DATETIME2 = 0x12;
    public static final int FIELD_TYPE_TIME2 = 0x13;
    public static final int FIELD_TYPE_NEWDECIMAL = 0xf6;
    public static final int FIELD_TYPE_ENUM = 0xf7;
    public static final int FIELD_TYPE_SET = 0xf8;
    public static final int FIELD_TYPE_TINY_BLOB = 0xf9;
    public static final int FIELD_TYPE_MEDIUM_BLOB = 0xfa;
    public static final int FIELD_TYPE_LONG_BLOB = 0xfb;
    public static final int FIELD_TYPE_BLOB = 0xfc;
    public static final int FIELD_TYPE_VAR_STRING = 0xfd;
    public static final int FIELD_TYPE_STRING = 0xfe;
    public static final int FIELD_TYPE_GEOMETRY = 0xff;
}
