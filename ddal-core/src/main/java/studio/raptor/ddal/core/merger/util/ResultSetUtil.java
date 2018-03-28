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

package studio.raptor.ddal.core.merger.util;



import studio.raptor.ddal.common.exception.GenericException;
import studio.raptor.ddal.core.parser.result.merger.OrderByColumn;

import java.math.BigDecimal;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Date;

/**
 * 结果集工具类.
 * 
 * @author jackcao
 * @since 3.0.0
 */
public final class ResultSetUtil {

    private ResultSetUtil(){}
    /**
     * 根据返回值类型返回特定类型的结果.
     * 
     * @param value 原始结果
     * @param convertType 返回值类型
     * @return 特定类型的返回结果
     */
    public static Object convertValue(final Object value, final Class<?> convertType) {
        if (null == value) {
            return convertNullValue(convertType);
        } 
        if (value.getClass() == convertType) {
            return value;
        }
        if (value instanceof Number) {
            return convertNumberValue(value, convertType);
        }
        if (value instanceof Date) {
            return convertDateValue(value, convertType);
        }
        if (String.class.equals(convertType)) {
            return value.toString();
        } else {
            return value;
        }    
    }
    
    private static Object convertNullValue(final Class<?> convertType) {
        switch (convertType.getName()) {
            case "byte":
                return (byte) 0;
            case "short":
                return (short) 0;
            case "int":
                return 0;
            case "long":
                return 0L;
            case "double":
                return 0D;
            case "float":
                return 0F;
            default:
                return null;
        }
    }
    
    private static Object convertNumberValue(final Object value, final Class<?> convertType) {
        Number number = (Number) value;
        switch (convertType.getName()) {
            case "byte":
                return number.byteValue();
            case "short":
                return number.shortValue();
            case "int":
                return number.intValue();
            case "long":
                return number.longValue();
            case "double":
                return number.doubleValue();
            case "float":
                return number.floatValue();
            case "java.math.BigDecimal":
                return new BigDecimal(number.toString());
            case "java.lang.Object":
                return value;
            case "java.lang.String":
                return value.toString();
            default:
                //todo
               // throw new GenericException();
                return null;
                //throw new ShardingJdbcException("Unsupported data type:%s", convertType);
        }
    }
    
    private static Object convertDateValue(final Object value, final Class<?> convertType) {
        Date date = (Date) value;
        switch (convertType.getName()) {
            case "java.sql.Date":
                return new java.sql.Date(date.getTime());
            case "java.sql.Time":
                return new Time(date.getTime());
            case "java.sql.Timestamp":
                return new Timestamp(date.getTime());
            default:
                //todo
                return null;
                //throw new ShardingJdbcException("Unsupported Date type:%s", convertType);
        }
    }
    
    /**
     * 根据排序类型比较大小.
     * 
     * @param thisValue 待比较的值
     * @param otherValue 待比较的值
     * @param orderByType 排序类型
     * @return 负数，零和正数分别表示小于，等于和大于
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static int compareTo(final Comparable thisValue, final Comparable otherValue, final OrderByColumn.OrderByType orderByType) {
        return OrderByColumn.OrderByType.ASC == orderByType ? thisValue.compareTo(otherValue) : -thisValue.compareTo(otherValue);
    }
}
