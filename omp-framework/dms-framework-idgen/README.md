####  ID生成器
* 添加依赖

```xml
<dependency>
    <groupId>com.ahdms</groupId>
    <artifactId>omp-framework-idgen</artifactId>
</dependency>
```

* yml配置
[IdGenProperties](src/main/java/com/ahdms/framework/idgen/config/IdGenProperties.java)

```yaml
omp:
  idgen:
    redis:
      host: 172.16.7.100
          #    password:
      port: 6379
    id-properties:
        # ID名称，必填
      - name: USER
        # ID生成策略（连续），必填
        strategy: CONSECUTIVE
        # 允许的最大值，必填
        maxValue: 10
        # 序号的最小值，可选
        minValue: 4
      - name: ORDER
        # ID生成策略（分片不连续），必填，分布式的ID生成推荐此策略，性能最高
        strategy: PIECEMEAL
        maxValue: 10
        # 每次申请存储在本地的数量，生成策略为PIECEMEAL时可选
        localStorage: 300
      - name: PRODUCT
        # ID生成策略（单机本地生成，连续），必填
        strategy: LOCAL
        maxValue: 10
```

* 使用方式
[IdGenerateUtils](../../blob/master/omp-framework-core/src/main/java/com/ahdms/framework/core/commom/util/IdGenerateUtils.java)

```java
public class TestIdGen {

    public static void main(String[] args){
        // 生成序号 格式：1
        String id = IdGenerateUtils.generateId("USER");
        // 生成带前缀的序号 格式：U1
        String prefixId = IdGenerateUtils.generateId("USER", "U");
        // 生成带日期时间的序号，可设置序号部分的长度（向左补0） 格式：yyyyMMddHHmmss00001
        String datetimeId = IdGenerateUtils.generateIdWithDateTime("USER", 5);
        // 生成带前缀和短日期时间的序号，可设置序号部分的长度（向左补0） 格式：OyyMMddHHmmss001  
        String datetimePrefixId = IdGenerateUtils.generateGlobalIdWithShortDateTime("ORDER", "O", 3);
    }

}
```

