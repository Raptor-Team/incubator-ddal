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

package studio.raptor.ddal.server.net.nio;

import studio.raptor.ddal.common.ServerLifeCycle;
import studio.raptor.ddal.server.net.handler.AbstractChannelInBoundHandler;

import java.net.InetSocketAddress;

/**
 *  Server for accepting frontend connection.
 *
 * @author Sam
 * @since 1.0
 */
public class AsyncSocketServer implements ServerLifeCycle {

    /**
     * Connection established handler is triggered when connection is accept by
     * this server, and handles no data, only redirect inbound data buffer back
     * to the handler chain.
     */
    private AbstractChannelInBoundHandler connEstablishedHandler;

    /**
     *
     */
    private AbstractChannelInBoundHandler bufferInboundAwareHandler;


    /**
     * Remote address to bind and for data listening on.
     */
    private InetSocketAddress socketAddress;


    @Override
    public void startup() throws Exception {

    }

    @Override
    public void shutdown() {

    }
}
