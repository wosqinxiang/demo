package com.ahdms.ctidservice.config;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpHost;
import org.apache.http.client.HttpClient;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

@Configuration
@Component
@ConditionalOnClass(value = {RestTemplate.class, CloseableHttpClient.class})
public class HttpClientConfig {
//	@Bean
//  public RestTemplate restTemplate() {
//    return new RestTemplate();
//  }
	
	/**
	   * 创建HTTP客户端工厂
	   */
	  @Bean(name = "clientHttpRequestFactory")
	  public ClientHttpRequestFactory clientHttpRequestFactory() {
	    /**
	     *  maxTotalConnection 和 maxConnectionPerRoute 必须要配
	     */
	    HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(httpClient());
	    // 连接超时
	    clientHttpRequestFactory.setConnectTimeout(3000);
	    // 数据读取超时时间，即SocketTimeout
	    clientHttpRequestFactory.setReadTimeout(3000);
	    // 从连接池获取请求连接的超时时间，不宜过长，必须设置，比如连接不够用时，时间过长将是灾难性的
	    clientHttpRequestFactory.setConnectionRequestTimeout(2000);
	    return clientHttpRequestFactory;
	  }
	  
	  /**
	   * 初始化RestTemplate,并加入spring的Bean工厂，由spring统一管理
	   */
	  @Bean(name = "httpClientTemplate")
	  public RestTemplate restTemplate(ClientHttpRequestFactory factory) {
	    return createRestTemplate(factory);
	  }
	  
	  /**
	   * 配置httpClient
	   *
	   * @return
	   */
	  @Bean
	  public HttpClient httpClient() {
	    HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
	    try {
	      //设置信任ssl访问
	      SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();

	      httpClientBuilder.setSSLContext(sslContext);
	      HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
	      SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);
	      Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
	              // 注册http和https请求
	              .register("http", PlainConnectionSocketFactory.getSocketFactory())
	              .register("https", sslConnectionSocketFactory).build();

	      //使用Httpclient连接池的方式配置(推荐)，同时支持netty，okHttp以及其他http框架
	      PoolingHttpClientConnectionManager poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
	      // 最大连接数
	      poolingHttpClientConnectionManager.setMaxTotal(1000);
	      // 同路由并发数
	      poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
	      //配置连接池
	      httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);
	      // 重试次数
	      httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

	      //设置默认请求头
//	      List<Header> headers = getDefaultHeaders();
//	      httpClientBuilder.setDefaultHeaders(headers);
	      //设置长连接保持策略
	      httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy());
	      return httpClientBuilder.build();
	    } catch (KeyManagementException | NoSuchAlgorithmException | KeyStoreException e) {
	    }
	    return null;
	  }
	  
	  /**
	   * 配置长连接保持策略
	   * @return
	   */
	  public ConnectionKeepAliveStrategy connectionKeepAliveStrategy(){
	    return (response, context) -> {
	      // Honor 'keep-alive' header
	      HeaderElementIterator it = new BasicHeaderElementIterator(
	              response.headerIterator(HTTP.CONN_KEEP_ALIVE));
	      while (it.hasNext()) {
	        HeaderElement he = it.nextElement();
	        String param = he.getName();
	        String value = he.getValue();
	        if (value != null && "timeout".equalsIgnoreCase(param)) {
	          try {
	            return Long.parseLong(value) * 1000;
	          } catch(NumberFormatException ignore) {
	          }
	        }
	      }
//	      HttpHost target = (HttpHost) context.getAttribute(
//	              HttpClientContext.HTTP_TARGET_HOST);
	      //如果请求目标地址,单独配置了长连接保持时间,使用该配置
//	      Optional<Map.Entry<String, Integer>> any = httpClientPoolConfig.getKeepAliveTargetHost().entrySet().stream().filter(
//	              e -> e.getKey().equalsIgnoreCase(target.getHostName())).findAny();
	      //否则使用默认长连接保持时间
	      return 60 * 1000;
	    };
	  }
	  
	  private RestTemplate createRestTemplate(ClientHttpRequestFactory factory) {
		    RestTemplate restTemplate = new RestTemplate(factory);

		    //我们采用RestTemplate内部的MessageConverter
		    //重新设置StringHttpMessageConverter字符集，解决中文乱码问题
//		    modifyDefaultCharset(restTemplate);

		    //设置错误处理器
		    restTemplate.setErrorHandler(new DefaultResponseErrorHandler());

		    return restTemplate;
		  }
}
