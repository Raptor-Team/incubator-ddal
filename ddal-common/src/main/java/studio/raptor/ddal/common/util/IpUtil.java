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

package studio.raptor.ddal.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip conversion
 *
 * @author jackcao, sam
 */
public class IpUtil {

    /**
     * 将127.0.0.1形式的IP地址转换成指定长度的十进制整数（10位）ip不能为0.0.0.0和1.1.1.1
     *
     * @param strIp 字符串形式的IP地址，例如"127.0.0.1"
     * @return IP地址的十进制整数
     */
    public static long ipToLong(String strIp) {
        long[] ip = new long[4];
        // 先找到IP地址字符串中.的位置
        int position1 = strIp.indexOf(".");
        int position2 = strIp.indexOf(".", position1 + 1);
        int position3 = strIp.indexOf(".", position2 + 1);
        // 将每个.之间的字符串转换成整型
        ip[0] = Long.parseLong(strIp.substring(0, position1));
        ip[1] = Long.parseLong(strIp.substring(position1 + 1, position2));
        ip[2] = Long.parseLong(strIp.substring(position2 + 1, position3));
        ip[3] = Long.parseLong(strIp.substring(position3 + 1));
        return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
    }

    /**
     * 将十进制整数形式转换成127.0.0.1形式的ip地址。
     *
     * Compiler will change this kind of string-plus style code to
     * {@link StringBuilder#append(String)}, so we don't need to write StringBuilder
     * in our code.
     *
     * The easiest way to see the compiled code is compile java code to smali-code.
     *
     * @param longIp 十进制整数
     * @return 字符串形式的IP地址。
     */
    public static String longToIP(long longIp) {
        String appender = "";

        // 直接右移24位
        appender += String.valueOf((longIp >>> 24));
        appender += ".";

        // 将高8位置0，然后右移16位
        appender += String.valueOf((longIp & 0x00FFFFFF) >>> 16);
        appender += ".";

        // 将高16位置0，然后右移8位
        appender += String.valueOf((longIp & 0x0000FFFF) >>> 8);
        appender += ".";

        // 将高24位置0
        appender += String.valueOf((longIp & 0x000000FF));

        return appender;
    }

    /**
     * 根据名称获取对应的物理IP
     *
     * @param hostName host name
     * @return an IP address for the given host name.
     * @throws UnknownHostException throw an exception if the given host name can't be verified.
     */
    public static String physicalIp(String hostName) throws UnknownHostException {
        return InetAddress.getByName(hostName).getHostAddress();
    }
}
