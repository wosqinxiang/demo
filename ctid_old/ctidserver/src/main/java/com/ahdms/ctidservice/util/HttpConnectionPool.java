package com.ahdms.ctidservice.util;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

public class HttpConnectionPool {

    private PoolingHttpClientConnectionManager cm;
    private int max;

    public HttpConnectionPool(int maxTotal){

        this.max = maxTotal;
        cm = new PoolingHttpClientConnectionManager();
        cm.setMaxTotal(maxTotal);
        cm.setDefaultMaxPerRoute(maxTotal);

    }

    public CloseableHttpClient getClient() {
        CloseableHttpClient httpClient = HttpClients.custom().setConnectionManager(cm).build();
        return httpClient;
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
