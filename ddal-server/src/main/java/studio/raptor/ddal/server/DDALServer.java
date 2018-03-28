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

package studio.raptor.ddal.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.common.ServerLifeCycle;
import studio.raptor.ddal.server.net.handler.ConnectEstablishedHandler;
import studio.raptor.ddal.server.net.handler.DataInBoundAwareHandler;

/**
 * Raptor ddal server
 * <p>
 * Created by Sam on 16/11/2016.
 */
public class DDALServer implements ServerLifeCycle {

    private static Logger LOGGER = LoggerFactory.getLogger(DDALServer.class);

    private int port;

    /**
     * NioEventLoopGroup is a multithreaded event loop that handles
     * I/O operation.
     * <p>
     * We are implementing a server-side application,and therefore two
     * NioEventLoopGroup will be used. The first one, often called 'boss',
     * accepts an incoming connection. The second one, often called 'worker',
     * handles the traffic of the accepted connection once the boss accepts
     * the connection and registers the accepted connection to the worker.
     */
    private EventLoopGroup bossGroup;
    private EventLoopGroup workerGroup;

    /**
     * Construct server with default strategy of calculating thread count of
     * boss and worker.
     *
     * @param port transport number
     */
    public DDALServer(int port) {
        this(port, 0, 0);
    }


    public DDALServer(int port, int bossThreadCount, int workerThreadCount) {
        this.port = port;
        this.bossGroup = new NioEventLoopGroup(bossThreadCount, new DefaultThreadFactory("boss-group", true));
        this.workerGroup = new NioEventLoopGroup(workerThreadCount, new DefaultThreadFactory("worker-group", true));
    }

    /**
     * Create and startup a server which is implemented by Netty.
     * <p>
     * ServerBootstrap is a helper class that sets up a server. We can set up
     * the server using a Channel directly. However, we note that this is a
     * tedious process, and we do not need to do that in most cases.
     *
     * @throws Exception failure message of startup progress
     */
    @Override
    public void startup() throws Exception {
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        public void initChannel(SocketChannel ch) throws Exception {
                            ch.pipeline().addLast(new ConnectEstablishedHandler());
//                            ch.pipeline().addLast(new ConnectionDisappearHandler());
                            ch.pipeline().addLast(new DataInBoundAwareHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true);

            // Bind and start to accept incoming connections.
            ChannelFuture f = b.bind(port).sync();

            LOGGER.info("> > > > > > > > > > > > > > > > > > > > > > > > > > > >");
            LOGGER.info("Congratulations! DDAL server is started, listening on {}", getPort());
            LOGGER.info("< < < < < < < < < < < < < < < < < < < < < < < < < < < <");

            // Wait until the server socket is closed.
            // In this example, this does not happen, but you can do that to gracefully
            // shut down your server.
            f.channel().closeFuture().sync();

        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }

    @Override
    public void shutdown() {

    }


    public int getPort() {
        return port;
    }
}
