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

package studio.raptor.ddal.server.common;

import java.io.Serializable;

/**
 * Server running status
 * <p>
 * Created by Sam on 16/11/2016.
 */
public class Status implements Serializable {

    /**
     * Serializable object should declare a field represents
     * its version.
     */
    private static final long serialVersionUID = -8889878310964915592L;

    private final String name;
    private final int value;


    public static final Status STATUS_UNINITIALISED = new Status(0, "STATUS_UNINITIALISED"); // 未初始化

    public static final Status STATUS_RUNNING = new Status(1, "STATUS_RUNNING");       // 已启动运行

    public static final Status STATUS_SHUTDOWN = new Status(2, "STATUS_SHUTDOWN");      // 已关闭


    private Status(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public String toString() {
        return name;
    }

    public int value() {
        return value;
    }
}
