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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import studio.raptor.ddal.server.common.Status;

/**
 * DDAL server startup entry point. In general this class
 * is called from shell script.
 * <p>
 * Created by Sam on 16/11/2016.
 */
public class DDALServerEntry {

    private static final Logger LOGGER = LoggerFactory.getLogger(DDALServerEntry.class);

    private DDALServer ddalServer;

    public void startup() throws Exception {
        // use netty default thread count strategy
        ddalServer = new DDALServer(3307, 0, 0);
        ddalServer.startup();
    }

    public static void main(String[] args) {
        DDALServerEntry sep = new DDALServerEntry();
        try {
            sep.startup();
        } catch (Exception e) {
            LOGGER.error("x x x x x x x x x x x x x x x x x x x x x x x x x x x x x");
            LOGGER.error("Sorry, DDAL server will exit because it encounters an error");
            LOGGER.error("x x x x x x x x x x x x x x x x x x x x x x x x x x x x x");
            System.exit(-1);
        }
    }
}
