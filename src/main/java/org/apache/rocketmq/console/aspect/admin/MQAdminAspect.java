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
package org.apache.rocketmq.console.aspect.admin;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.rocketmq.console.aspect.admin.annotation.MultiMQAdminCmdMethod;
import org.apache.rocketmq.console.service.client.MQAdminInstance;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * 监控客户端初始化类？
 * todo : learning
 */
@Aspect
@Service
public class MQAdminAspect {
    private Logger logger = LoggerFactory.getLogger(MQAdminAspect.class);

    public MQAdminAspect() {
    }
    //@Pointcut : 是指那些方法需要被执行"AOP",是由"Pointcut Expression"来描述的.
    //todo learning
    @Pointcut("execution(* org.apache.rocketmq.console.service.client.MQAdminExtImpl..*(..))")
    public void mQAdminMethodPointCut() {

    }

    @Pointcut("@annotation(org.apache.rocketmq.console.aspect.admin.annotation.MultiMQAdminCmdMethod)")
    public void multiMQAdminMethodPointCut() {

    }
    //@Around是可以同时在所拦截方法的前后执行一段逻辑
    //todo ，用于初始化MQAdmin？，添加了Spring日志管理

    /**
     * 个人理解，AOP编程，这里是用来计算每个接口调用的时长
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around(value = "mQAdminMethodPointCut() || multiMQAdminMethodPointCut()")
    public Object aroundMQAdminMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        logger.info("now>>>>>>>:{}",sdf.format(new Date()));
        long start = System.currentTimeMillis();
        Object obj = null;
        try {
            MethodSignature signature = (MethodSignature)joinPoint.getSignature();
            Method method = signature.getMethod();
            MultiMQAdminCmdMethod multiMQAdminCmdMethod = method.getAnnotation(MultiMQAdminCmdMethod.class);
            //初始化MQ监控客户端 MQAdmin start 的入口
            if (multiMQAdminCmdMethod != null && multiMQAdminCmdMethod.timeoutMillis() > 0) {

                MQAdminInstance.initMQAdminInstance(multiMQAdminCmdMethod.timeoutMillis());
            }
            else {
                MQAdminInstance.initMQAdminInstance(0);
            }
            obj = joinPoint.proceed();
        }
        finally {
            MQAdminInstance.destroyMQAdminInstance();
            logger.debug("op=look method={} cost={}", joinPoint.getSignature().getName(), System.currentTimeMillis() - start);
        }
        return obj;
    }
}