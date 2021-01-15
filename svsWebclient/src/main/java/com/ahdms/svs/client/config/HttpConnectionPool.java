package com.ahdms.svs.client.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
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
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import java.io.IOException;

/**
 * @author qinxiang
 */
public class HttpConnectionPool {

    private PoolingHttpClientConnectionManager cm;
    private HttpClientBuilder httpClientBuilder;
    private int max;

    public HttpConnectionPool(int maxTotal){
        this.max = maxTotal;
        cm = poolingBuild();
        httpClientBuilder = initHttpClientBuilder();
    }

    public PoolingHttpClientConnectionManager poolingBuild() {
        PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
        try {
            SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, (arg0, arg1) -> true).build();
            HostnameVerifier hostnameVerifier = NoopHostnameVerifier.INSTANCE;
            SSLConnectionSocketFactory sslConnectionSocketFactory = new SSLConnectionSocketFactory(sslContext, hostnameVerifier);

            Registry<ConnectionSocketFactory> socketFactoryRegistry = RegistryBuilder.<ConnectionSocketFactory>create()
                    // 注册http和https请求
                    .register("http", PlainConnectionSocketFactory.getSocketFactory())
                    .register("https", sslConnectionSocketFactory).build();
            poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(socketFactoryRegistry);
        }catch (Exception e){
            poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager();
        }
        // 最大连接数
        poolingHttpClientConnectionManager.setMaxTotal(max);
        // 同路由并发数
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(max);

        return poolingHttpClientConnectionManager;
    }

    public HttpClientBuilder initHttpClientBuilder(){
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        //配置连接池
        httpClientBuilder.setConnectionManager(cm);
        // 重试次数
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(2, true));

        //设置长连接保持策略
        httpClientBuilder.setKeepAliveStrategy(connectionKeepAliveStrategy());
        return httpClientBuilder;
    }

    public CloseableHttpClient getClient() {

        return httpClientBuilder.build();
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
            return 60 * 1000;
        };
    }

    public int getActiveNum(){
        return this.max - cm.getTotalStats().getAvailable();
    }

    public String getRequest(String url) throws IOException {

        CloseableHttpClient hc = this.getClient();

        HttpGet httpget = new HttpGet(url);
        CloseableHttpResponse response;

        try {
            response = hc.execute(httpget);
            HttpEntity et = response.getEntity();
            if(response.getStatusLine().getStatusCode() >= 400) {
                return "失败";
            }else{
                return EntityUtils.toString(et);
            }
        } catch (IOException e) {
           throw e;
        }

    }
}
