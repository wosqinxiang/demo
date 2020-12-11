####  XXL-JOB
* 添加依赖

```xml
<dependency>
    <groupId>com.ahdms</groupId>
    <artifactId>omp-framework-job</artifactId>
</dependency>
```

* yml配置

```yaml
xxl:
  job:
    admin:
      #  xxl-job admin地址
      addresses: http://127.0.0.1:6006/xxl-job-admin
    executor:
      # xxl-job admin配置的执行器名称
      app-name: demo-executor
```

* 使用方式

```java
@Slf4j
@Component
public class DemoJob {

    @XxlJob("demoJobHandler")
    public ReturnT<String> demoJobHandler(String param) {
        log.info("DemoJobHandler invoked >>>> {}", param);
        return ReturnT.SUCCESS;
    }
}
```

