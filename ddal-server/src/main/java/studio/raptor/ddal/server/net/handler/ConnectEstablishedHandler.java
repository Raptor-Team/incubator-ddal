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

package studio.raptor.ddal.server.net.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.server.util.ChannelUtil;

/**
 *
 * When a channel is active, we think a connect from client to server is established.
 * A session to wrapper this connect will be created by next.
 *
 * Calls of {@link ChannelHandlerContext#fireChannelActive()} can trigger this
 * handler.
 *
 * @author Sam
 * @since 1.0
 */
public class ConnectEstablishedHandler extends AbstractChannelInBoundHandler {

    private static Logger LOGGER = LoggerFactory.getLogger(ConnectEstablishedHandler.class);

    /**
     * This method will be triggered when connection channel is active. see
     * {@link ChannelInboundHandler#channelActive(ChannelHandlerContext)} for
     * more information.
     *
     * @param ctx   channel handler context
     * @throws Exception channel active exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("======> channel {} is active", ChannelUtil.remoteConnectionInfo(ctx.channel()));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }
}
