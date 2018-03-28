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

package studio.raptor.ddal.server.mx;

import com.google.common.base.Strings;
import studio.raptor.ddal.common.helper.PathHelper;
import studio.raptor.ddal.common.util.IpUtil;
import studio.raptor.ddal.server.common.IMBeanName;

import java.io.File;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

import static java.lang.String.format;

/**
 * Some thing about the comb server
 *
 * @author bruce
 */
public class ServerInfo implements ServerInfoMBean, IMBeanName {

    private String sysId;                // 标识server进程

    private String ip;                   // 绑定IP

    private int port;                    // 绑定端口

    private final String startupTime;    // 启动时间

    private String startedTime;          // 启动完成时间

    private final long startupDateTime;

    private final RunMode runMode;       // 运行模式

    private final InitLevel initLevel;   // 启动启动级别

    public final File shutdownFile;      // 安全关闭标识文件

    public final CountDownLatch shutdownLatch = new CountDownLatch(1);

    public final AtomicBoolean shuttingDown = new AtomicBoolean(false);

    public final AtomicBoolean dirtyShutdown = new AtomicBoolean(true);

    public ServerInfo(String ip, int port, String safetyShutdownFileName, InitLevel initLevel) throws UnknownHostException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        this.startupTime = format.format(new Date());
        this.startupDateTime = System.currentTimeMillis();
        this.ip = ip;
        this.port = port;
        this.sysId = IpUtil.physicalIp("ddalserver") + "-" + port;
        this.runMode = Strings.isNullOrEmpty(PathHelper.getDdalServerHome()) ? RunMode.DEV : RunMode.PROD;
        this.initLevel = initLevel;
        this.shutdownFile = getShutdownFile(safetyShutdownFileName);
        this.dirtyShutdown.set(checkDirtyShutdown(this.shutdownFile));
    }

    @Override
    public String getSysId() {
        return sysId;
    }

    @Override
    public String getIp() {
        return this.ip;
    }

    @Override
    public int getPort() {
        return this.port;
    }

    @Override
    public String getVersion() {
        String version = null;
        try {
            version = ServerInfo.class.getPackage().getImplementationVersion();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return version != null ? version : "-.-.-";
    }

    @Override
    public String getStartupTime() {
        return startupTime;
    }

    public void started() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        startedTime = format.format(new Date());
    }

    @Override
    public String getRunningTime() {
        long time = System.currentTimeMillis() - startupDateTime;
        final long days = time / (1000L * 60 * 60 * 24L);
        time = time % (1000L * 60 * 60 * 24L);
        final long hours = time / (1000L * 60 * 60);
        time = time % (1000L * 60 * 60);
        final long minutes = time / (1000L * 60);
        return format("%d Days %d Hours %d Minutes", days, hours, minutes);
    }

    @Override
    public String getStartedTime() {
        return startedTime != null ? startedTime : "--";
    }

    @Override
    public String getMBeanName() {
        return "ddalserver:type=ddalserver.ServerInfo";
    }

    @Override
    public String getRunMode() {
        return runMode.getMode();
    }

    @Override
    public InitLevel getInitLevel() {
        return initLevel;
    }

    /**
     * 获取关闭标识文件
     *
     * @param safetyShutdownFilename 关闭表示文件
     * @return 关闭标识文件
     */
    public File getShutdownFile(String safetyShutdownFilename) {
        String shutdownFilePath;
        if (RunMode.PROD.toString().equals(this.getRunMode())) {
            // 生产模式
            shutdownFilePath = PathHelper.getDdalServerHome();
        } else {
            // 开发模式
            shutdownFilePath = System.getProperty("user.dir");
        }
        return new File(new File(shutdownFilePath), safetyShutdownFilename);
    }

    /**
     * 检查server是否异常关闭
     *
     * @param shutdownFile
     * @return
     */
    private boolean checkDirtyShutdown(File shutdownFile) {
        boolean dirty = true;
        if (null != shutdownFile && shutdownFile.exists()) {
            // &&safetyShutDownFile.delete()安全关闭标识文件存在则删除，无需进行数据恢复
            dirty = false;
        }
        return dirty;
    }

    public static enum RunMode {

        DEV("development"), PROD("production");

        private String mode;

        private RunMode(String mode) {
            this.mode = mode;
        }

        public String getMode() {
            return mode;
        }

        public void setMode(String mode) {
            this.mode = mode;
        }
    }

    public static enum InitLevel {
        NormalModel(0, "正常模式"), ForceModel(1, "强制模式"), DiagnosticModel(2, "诊断模式"), NoBackModel(3, "无后端模式");

        private int level;

        private String desc;

        private InitLevel(int level, String desc) {
            this.level = level;
            this.desc = desc;
        }

        public int getLevel() {
            return level;
        }

        public String getDesc() {
            return desc;
        }
    }

}
