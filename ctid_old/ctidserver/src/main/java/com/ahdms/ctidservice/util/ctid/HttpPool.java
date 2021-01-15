//package com.ahdms.ctidservice.util.ctid;
//
//import javax.annotation.PostConstruct;
//
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.config.Registry;
//import org.apache.http.config.RegistryBuilder;
//import org.apache.http.config.SocketConfig;
//import org.apache.http.conn.socket.ConnectionSocketFactory;
//import org.apache.http.conn.socket.PlainConnectionSocketFactory;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.impl.client.HttpClients;
//import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//@Component
//public class HttpPool {
//	private PoolingHttpClientConnectionManager cm;
//
//	Logger logger = LoggerFactory.getLogger(HttpPool.class);
//    
//    @Value("${http.pool.max:20}")
//    private int max;
//    @Value("${http.pool.maxPerRoute:5}")
//    private int maxPerRoute;
//    
//    @PostConstruct
//    public void init() {
//        Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory> create()
//                 .register("http", new PlainConnectionSocketFactory())
//                 .build();
//        cm =new PoolingHttpClientConnectionManager(socketFactoryRegistry);
//        cm.setMaxTotal(max);
//        cm.setDefaultMaxPerRoute(maxPerRoute);
//    }
//
//
//
//  /*public HttpPool(int maxTotal){
//       this.max = maxTotal;
//       cm = new PoolingHttpClientConnectionManager();
//       cm.setMaxTotal(maxTotal);
//       cm.setDefaultMaxPerRoute(maxTotal);
//  }*/
//    public SocketConfig getDefaultSocketConfig(){
//    	return SocketConfig.custom().setTcpNoDelay(true)     //是否立即发送数据，设置为true会关闭Socket缓冲，默认为false
//                .setSoReuseAddress(true) //是否可以在一个进程关闭Socket后，即使它还没有释放端口，其它进程还可以立即重用端口
//                .setSoTimeout(500)       //接收数据的等待超时时间，单位ms
//                .setSoLinger(1)         //关闭Socket时，要么发送完所有数据，要么等待60s后，就关闭连接，此时socket.close()是阻塞的
//                .setSoKeepAlive(true)    //开启监视TCP连接是否有效
//                .build();
//    }
//    
//    public RequestConfig getDefaultRequestConfig(){
//    	return RequestConfig.custom()
//                .setConnectTimeout(1800)         //连接超时时间
//                .setSocketTimeout(100)          //读超时时间（等待数据超时时间）
//                .setConnectionRequestTimeout(100)    //从池中获取连接超时时间
//                .build();
//    }
//    
//    public CloseableHttpClient getClient() {
//        CloseableHttpClient httpClient =  HttpClients.custom().setConnectionManager(cm).build();
//        return httpClient;
//    }
//    
//    public int getActiveNum(){
//        return this.max - cm.getTotalStats().getAvailable();
//    }
//}
