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

package studio.raptor.ddal.server.util;

import com.google.common.base.Preconditions;
import io.netty.channel.Channel;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * A utility for netty {@link io.netty.channel.Channel}.
 *
 * @author Sam
 * @since 1.0
 */
public class ChannelUtil {

    /**
     * See deeply into {@link Channel} to find remote address and local address.
     *
     * @param channel Nio connection channel. See {@link Channel} for more.
     * @return "Remote[ip:port] <-> Local[ip:port]"
     */
    public static String connectionInfoPairOf(Channel channel) {
        Preconditions.checkArgument((null != channel), "");
        InetSocketAddress localAddress = downCast(channel.localAddress()), remoteAddress = downCast(channel.remoteAddress());
        return String.format(
                "Remote[%s:%s]<->Local[%s:%s]",
                textualIpOf(remoteAddress), textualPortOf(remoteAddress),
                // <-->
                textualIpOf(localAddress), textualPortOf(localAddress));
    }

    /**
     * See deeply into {@link Channel} to find remote address information.
     *
     * @param channel Nio connection channel
     * @return Remote connection info. for example, "Connection[127.0.0.1:58321]"
     */
    public static String remoteConnectionInfo(Channel channel) {
        Preconditions.checkArgument((null != channel), "");
        InetSocketAddress remoteAddress = downCast(channel.remoteAddress());
        return String.format("[%s:%s]", textualIpOf(remoteAddress), textualPortOf(remoteAddress));
    }


    private static InetSocketAddress downCast(SocketAddress socketAddress) {
        return (InetSocketAddress) socketAddress;
    }

    public static String textualIpOf(InetSocketAddress address) {
        if (null != address && null != address.getAddress()) {
            return address.getAddress().getHostAddress();
        }
        return "NVL";
    }

    public static String textualPortOf(InetSocketAddress address) {
        if (null != address) {
            return String.valueOf(address.getPort());
        }
        return "NVL";
    }
}
