## RocketMQ-Console-Ng[![Build Status](https://travis-ci.org/rocketmq/rocketmq-console-ng.svg?branch=master)](https://travis-ci.org/rocketmq/rocketmq-console-ng) [![Coverage Status](https://coveralls.io/repos/github/rocketmq/rocketmq-console-ng/badge.svg?branch=master)](https://coveralls.io/github/rocketmq/rocketmq-console-ng?branch=master)
[![License](https://img.shields.io/badge/license-Apache%202-4EB1BA.svg)](https://www.apache.org/licenses/LICENSE-2.0.html)
## How To Install

### With Docker

* get docker image

```
mvn clean package -Dmaven.test.skip=true docker:build
```

or

```
docker pull styletang/rocketmq-console-ng
```
* run it (change namesvrAddr and port yourself)

```
docker run -e "JAVA_OPTS=-Drocketmq.namesrv.addr=127.0.0.1:9876 -Dcom.rocketmq.sendMessageWithVIPChannel=false" -p 8080:8080 -t styletang/rocketmq-console-ng
```

### Without Docker
require java 1.7
```
mvn spring-boot:run
```
or
```
mvn clean package -Dmaven.test.skip=true
java -jar target/rocketmq-console-ng-1.0.0.jar
```

#### Tips
* if you download package slow,you can change maven's mirror(maven's settings.xml)
  
  ```
  <mirrors>
      <mirror>
            <id>alimaven</id>
            <name>aliyun maven</name>
            <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
            <mirrorOf>central</mirrorOf>        
      </mirror>
  </mirrors>
  ```
  
* if you use the rocketmq < 3.5.8,please add -Dcom.rocketmq.sendMessageWithVIPChannel=false when you start rocketmq-console-ng(or you can change it in ops page)
* change the rocketmq.config.namesrvAddr in resource/application.properties.(or you can change it in ops page)

## UserGuide

[English](https://github.com/apache/incubator-rocketmq-externals/blob/master/rocketmq-console/doc/1_0_0/UserGuide_EN.md)

[中文](https://github.com/apache/incubator-rocketmq-externals/blob/master/rocketmq-console/doc/1_0_0/UserGuide_CN.md)

## Contact

* Issue / Pull Request
* You can join us and make a contribute for rocketmq-console.

[RocketMQ Contact](http://rocketmq.apache.org/about/contact/)
#### Mailing Lists

DEV dev@rocketmq.incubator.apache.org

USERS users@rocketmq.incubator.apache.org

---

源码阅读参考入口：

    初始化顺序：
    App(入口) ---> 
    RMQConfigure(加载mq配置文件)--->
    ClusterController(MQ集群控制器) ---> 
    ConsumerController(消费者控制器) --->
    DashboardController(控制面板控制器) ---> 
    MessageController(消息控制器) --->
    MonitorController(监控控制器) ---> 
    NamesvrController(MQ服务端配置控制器) ---> 
    OpsController(MQ相关的操作控制器) --->
    ProducerController(MQ生产者控制器) --->
    TopicController(MQ消息主题控制器) --->
    GlobalExceptionHandler( 全局异常处理程序) --->
    GlobalRestfulResponseBodyAdvice(全局的返回ResponseBody类) --->
    DashboardCollectTask(定时任务类) --->
    MonitorTask(监控任务类) --->
    
    接口：
    MultiMQAdminCmdMethod(?)
    OriginalControllerReturnValue(?)
    
    单独的admin class
    MQAdminAspect(?)


RocketMQ 核心概念：
![image](https://note.youdao.com/yws/api/personal/file/DCD5A467E0804A1DAC84116581CE13B6?method=download&shareKey=a3c509c527fd3a884dcbc2b6709d6b26)

```
消费者并发
消费者热点问题
消费者负载平衡
消息路由器
连接多路复用
金丝雀部署
```
- **Producer**：
  生产者，生产发送消息，提供多种例子：同步、异步和单向。
- **Producer Group**：
  生产者集合组，如果原始生产者在事务之后崩溃，则代理可以联系同一生产者组的不同生产者实例以提交或回滚事务。（**考虑到提供的生产者在发送消息方面足够强大，每个生产者组只允许一个实例，以避免不必要的生成器实例初始化，算是编码规则**）
- **Consumer**：
消费者从Producer那里获取消息并将其提供给应用程序。从用户应用的角度来看，提供了两种类型的消费者：**PullConsumer和PushConsumer**

- **PullConsumer**：
PullConsumer 积极地Producer 那里获取消息。一旦提取了批量消息，用户应用程序就会启动消费过程。
- **PushConsumer**：
 PushConsumer 封装消息提取，消费进度并维护其他内部工作，为最终用户留下回调接口以实现将在消息到达时执行。
- **Consumer Group**：
消费者组，与生产者组类似，完全相同角色的消费者被组合在一起并命名为消费者组。**消费者组是一个很好的概念，在消息消费方面实现负载平衡和容错目标非常容易。**

- **Topic**：
Topic 是 producer 传递消息和 consumer 提取消息的类别。**Topic主题与生产者和消费者的关系非常松散**。具体来说，一个主题可能有零个，一个或多个生成器向它发送消息; 相反，制作人可以发送不同主题的消息。从消费者的角度来看，主题可以由零个，一个或多个消费者群体订阅。类似地，消费者组可以订阅一个或多个主题，**只要该组的实例保持其订阅一致即可。**

- **Message**：
Message 是要传递的信息。消息必须有一个主题，可以将其解释为您要发送给的邮件地址。消息还可以具有可选标记和额外的键 - 值对。例如，您可以为消息设置业务密钥，并在代理服务器上查找消息以诊断开发期间的问题。

- **Message Queue**：
消息队列，Topic 被划分成了一个或者多个主题的消息队列。

- **Tag：**
Tag标签 子主题，为用户提供了额外的灵活性。对于tag ，来自同一业务模块的具有不同目的的消息可以具有相同的主题和不同的标记。**标签有助于保持代码的清晰和连贯，而标签也可以方便RocketMQ提供的查询系统。**

- **Broker**：
  它接收从生产者发送的消息，存储它们并准备处理来自消费者的拉取请求。它还存储与消息相关的元数据，包括消费者组，消耗进度偏移和主题/队列信息。

- **Name Server：**
MQ服务器充当路由信息提供者。生产者/消费者客户查找主题以查找相应的经纪人列表。

- **Message Model：**
消息模型分类**Clustering 和 Broadcasting**

- **Message Order：**
消息顺序，使用DefaultMQPushConsumer时，您可能决定按顺序或同时使用消息。
类型分为**Orderly和Concurrently**

```
Orderly：接收消息和发送消息的顺序一致
Concurrently ： 保证消息的实时性，但是不保证消息顺序 
```
