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

package studio.raptor.ddal.common.helper;

import java.io.File;

/**
 * 系统文件路径
 *
 * @author jackcao, bruce, Sam
 */
public class PathHelper {

    /*
     * 存放ddalserver根路径的系统属性名称
     * 在vm启动命令中通过"-Dddal.sever.home=xxxx"设置ddalserver的运行时根路径
     */
    private static final String DDAL_SERVER_HOME = "ddal.sever.home";

    /*
     * ddalserver存放配置文件目录名
     */
    private static final String DDAL_SERVER_CONF_DIR = "conf";

    /*
     * ddalserver存放二进制脚本目录名
     */
    private static final String DDAL_SERVER_BIN_DIR = "bin";

    /**
     * Base directory of ddalserver.
     *
     * @return Base directory of ddalserver.
     */
    public static String getDdalServerHome() {
        return System.getProperty(DDAL_SERVER_HOME);
    }

    /**
     * Get directory of configuration files. Generally this directory is appended
     * to java path.
     *
     * @return configuration path
     */
    public static String getDdalServerConfDir() {
        String ddalServerHome = getDdalServerHome();
        return null != ddalServerHome ? (ddalServerHome + File.separator + DDAL_SERVER_CONF_DIR) : null;
    }

    /**
     * Executable file path of ddalserver, and it is kind of like system executable bin
     * path and can be added to system path for immediately execute.
     *
     * @return execute file path
     */
    public static String getDdalServerBinDir() {
        String ddalServerHome = getDdalServerHome();
        return null != ddalServerHome ? (ddalServerHome + File.separator + DDAL_SERVER_BIN_DIR) : null;
    }
}
