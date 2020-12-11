####  异步及订阅发布
* 添加依赖

```xml
<dependency>
    <groupId>com.ahdms</groupId>
    <artifactId>omp-framework-stream</artifactId>
</dependency>
```

* yml配置

```yaml

spring:
  cloud:
    stream:
      kafka:
        bingdings:
          # 为input通道设置死信队列
          input:
            consumer:
              enable-dlq: true
              dlq-name: user-dead
        binder:
          # 实际的MQ地址
          brokers: 172.16.6.100:9092
      bindings:
        output:
          # 消息生产通道一 topic
          destination: sms
          contentType: application/json
        output1:
          # 消息生产通道二 topic
          destination: user
          contentType: application/json
        output-dead:
          # 死信队列生产通道 topic
          destination: user-dead
          contentType: application/json
        input:
          # 消息消费通道 topic
          destination: user
          contentType: application/json
          # 消息分组（保证多实例消息只会被消费一次）
          group: omp-demo
        input-dead:
          # 死信消息消费通道 topic
          destination: user-dead
          contentType: application/json
          # 消息分组（保证多实例消息只会被消费一次）
          group: omp-demo-dead
```
* 异步调用

> 异步接口调用，由消费端提供接口

step1-定义StreamClient

```java
@StreamClient("user")
public interface UserStreamClient {
    /**
    * handler 正常消费处理的beanName，必输
    * deadHandler 死信消费处理的beanName，可选，配置死信队列为true时必输
    */
    @Source(handler = "userStreamHandler", deadHandler="userStreamDeadHandler")
    void pushUser(UserMessage message);
}
```

step2-实现消费者handler

```java
@Component
public class UserStreamHandler implements StreamHandler<UserMessage, Message<UserMessage>> {
    @Override
    public void onMessageReceive(Message<UserMessage> message) {
        System.out.println("Receive message ==>" + message.getBody().toString());
    }
}
```

step3-实现消费者死信队列handler（可选）

```java
@Component
public class UserStreamDeadHandler implements StreamDeadHandler<UserMessage, Message<UserMessage>> {
    @Override
    public void onDeadMessageReceive(Message<UserMessage> message) {
        System.out.println("Receive dead message ==>" + message.getBody().toString());
    }
}
```

> 调用方引入消费端提供的pom依赖，注入client调用即可

```java
@Slf4j
@Validated
@RestController
@RequestMapping("/api/demo/user")
@Api("用户控制器")
public class UserController {
    @Autowired
    private SmsStreamClient smsStreamClient;
    
    @PostMapping("/message-user")
    @ApiOperation(value = "message-client", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public void message() {
        UserMessage userMessage = new UserMessage();
        userMessage.setUserName("katrel");
        userStreamClient.pushUser(userMessage);
    }
}

```

```java
@EnableStreamClients(clients = {
        UserStreamClient.class,
        SmsStreamClient.class
})
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
}
```

* 订阅发布
待完善