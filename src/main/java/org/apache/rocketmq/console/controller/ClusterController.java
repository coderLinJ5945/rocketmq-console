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
package org.apache.rocketmq.console.controller;

import org.apache.rocketmq.console.service.ClusterService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * MQ集群控制器：
 * 加载完mq配置文件之后，就初始化集群代码
 */
@Controller
@RequestMapping("/cluster")
public class ClusterController {

    @Resource
    private ClusterService clusterService;

    @RequestMapping(value = "/list.query", method = RequestMethod.GET)
    @ResponseBody
    public Object list() {
        return clusterService.list();
    }

    /**
     * 获取代理的配置
     * broker作用：它接收从生产者发送的消息，存储它们并准备处理来自消费者的拉取请求。
     *             它还存储与消息相关的元数据，包括消费者组，消耗进度偏移和主题/队列信息。
     * @param brokerAddr
     * @return
     */
    @RequestMapping(value = "/brokerConfig.query", method = RequestMethod.GET)
    @ResponseBody
    public Object brokerConfig(@RequestParam String brokerAddr) {
        return clusterService.getBrokerConfig(brokerAddr);
    }
}
