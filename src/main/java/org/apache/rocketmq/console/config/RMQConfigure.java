/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.apache.rocketmq.console.config;

import java.io.File;
import org.apache.commons.lang3.StringUtils;
import org.apache.rocketmq.common.MixAll;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import static org.apache.rocketmq.client.ClientConfig.SEND_MESSAGE_WITH_VIP_CHANNEL_PROPERTY;

/**
 * mq的配置实体类
 */
@Configuration
@ConfigurationProperties(prefix = "rocketmq.config")
public class RMQConfigure {

    private Logger logger = LoggerFactory.getLogger(RMQConfigure.class);
    /**
     * volatile:同步机制，当运行内存（从内存）变量改变时，会立即通知到其他线程，性能优于synchronized 锁，但是使用的环境条件有限
     * 出于性能考虑使用volatile，而不使用synchronized 锁
     * volatile使用环境：
     * 1、volatile修饰的变量不依赖本身的结果，或者 只有单一线程改变其变量
     * 2、变量不依赖其他状态变量共同参与约束（状态变量可以简单理解成为开关的状态）
     * namesrvAddr 服务器地址
     */
    //rocketmq.namesrv.addr，如果它是空的，则使用system proerty或system env
    private volatile String namesrvAddr = System.getProperty(MixAll.NAMESRV_ADDR_PROPERTY, System.getenv(MixAll.NAMESRV_ADDR_ENV));
    //是否开启VIP通道
    private volatile String isVIPChannel = System.getProperty(SEND_MESSAGE_WITH_VIP_CHANNEL_PROPERTY, "true");


    private String dataPath;

    private boolean enableDashBoardCollect;

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    /**
     * 程序启动会set nameservAddr 初始化
     * @param namesrvAddr
     */
    public void setNamesrvAddr(String namesrvAddr) {
        if (StringUtils.isNotBlank(namesrvAddr)) {
            this.namesrvAddr = namesrvAddr;
            System.setProperty(MixAll.NAMESRV_ADDR_PROPERTY, namesrvAddr);
            logger.info("setNameSrvAddrByProperty nameSrvAddr={}", namesrvAddr);
        }
    }

    public String getRocketMqConsoleDataPath() {
        return dataPath;
    }

    public String getConsoleCollectData() {
        return dataPath + File.separator + "dashboard";
    }

    public void setDataPath(String dataPath) {
        this.dataPath = dataPath;
    }

    public String getIsVIPChannel() {
        return isVIPChannel;
    }

    public void setIsVIPChannel(String isVIPChannel) {
        if (StringUtils.isNotBlank(isVIPChannel)) {
            this.isVIPChannel = isVIPChannel;
            //vip 通道只有一个，所以用static final 修饰
            System.setProperty(SEND_MESSAGE_WITH_VIP_CHANNEL_PROPERTY, isVIPChannel);
            logger.info("setIsVIPChannel isVIPChannel={}", isVIPChannel);
        }
    }

    public boolean isEnableDashBoardCollect() {
        return enableDashBoardCollect;
    }

    public void setEnableDashBoardCollect(String enableDashBoardCollect) {
        this.enableDashBoardCollect = Boolean.valueOf(enableDashBoardCollect);
    }
}
