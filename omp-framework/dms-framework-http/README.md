#### otter-framework-http

* 添加依赖
```xml
<dependency>
  <groupId>com.ahdms</groupId>
  <artifactId>dms-framework-http</artifactId>
</dependency>
```

* 使用API

```java
// 设置全局的日志级别，多用于调试
HttpRequest.setGlobalLog(LogLevel.BODY);

// 1. 同步方法

// http 方法和 url，目前支持 get、post、patch、put、delete
HttpRequest.get("https://www.baidu.com")
    .log(LogLevel.BASIC)  // 设定日志级别
    .addHeader("test", "test") // 添加 Header，可多次 add
    .addCookie(new Cookie.Builder() // 添加 cookie，可多次 add
        .name("sid")
        .value("123123123")
        .build()
    )
    .query("q", "sunvalley") // query 参数会做url编码，同类方法 queryEncoded、queryMap
    .formBuilder() // 表单构造器，同类方法 multipartFormBuilder（文件上传表单）
    .add("name", "")
    .build()
    .bodyJson(user)         //请求 json 对象
    .bodyString("{}")       // 与 bodyJson 互斥，不能同时使用，会被后面的替换
    .execute()              // 执行请求
    .headers(headers -> {   // 相应的 headers 消费

    })
    .cookies(cookies -> {   // 相应的 cookies 消费

    })
    .asString();             
// 将相应数据转成字符串（网路等异常会抛出）
// 同类的有 asBytes、asStream、toFile
// asJsonNode、asObject、asList、asMap （json 转换）
// asDocument （jsoup html、xml 处理）

// 2. 同步方法2
// 同步，异常返回 null
String html = HttpRequest.post("https://www.baidu.com")
    .bodyString("Important stuff")
    .execute()
    .onResponse(ResponseSpec::asDocument)   // 有响应时处理，异常返回 null
    .onSuccess(ResponseSpec::asString);     // 与上面互斥，响应成功时处理（http code 200~300），失败返回 null

// 3. 异步方法
HttpRequest.get("https://www.baidu.com")
    .async()                  // 开启异步
    .onResponse(response -> { 
        // 异步有响应时的处理，注意：相应的流只能读一次
        // 请不要再 onSuccessful 重复读。

    })
    .onSuccessful(response -> {  // 异步成功时的处理
        // 异步结果处理
        String text = response.asString();
        System.out.println(text);
    })
    .onFailed((request, e) -> {  // 失败时的处理，可选
        // 失败时的处理
        e.printStackTrace();
    })
    .execute();                  // 必须，异步最后执行。
```

* 代码示例

```java
private String getUserEmail(String accessToken) {
    return HttpRequest.get("https://api.linkedin.com/v2/emailAddress?q=members&projection=(elements*(handle~))")
            .addHeader("Host", "api.linkedin.com")
            .addHeader("Connection", "Keep-Alive")
            .addHeader("Authorization", "Bearer " + accessToken)
            .execute()
            .asJsonNode()
            .at("/elements/0/handle~0/emailAddress")
            .asText();
}
```

```java
// 异步
HttpRequest.get("https://www.baidu.com")
    .connectTimeout(Duration.ofSeconds(1000))
    .query("name", "張三")
    .queryEncoded("name1", "張三")
    .async()
    .onSuccessful(responseSpec -> {
        // 异步保存结果为文件
        responseSpec.toFile(new File("D:\\test.html"));
    })
    .onFailed((request, e) -> {
        // 失败时的处理
        e.printStackTrace();
    })
    .execute();

// 同步，异常返回 null
String html = HttpRequest.post("https://www.baidu.com/do-stuff")
    .bodyString("Important stuff")
    .formBuilder()
    .add("a", "b")
    .execute()
    .onSuccess(ResponseSpec::asString);
System.out.println(html);

// 同步，异常时直接抛出
String text = HttpRequest.get("https://xxxx.baidu.com")
    .addHeader("X-Custom-header", "stuff")
    .execute()
    .asString();

```