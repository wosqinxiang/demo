package com.ahdms.svs.client.config;


import com.ahdms.svs.client.constants.HeaderConstants;
import com.ahdms.svs.client.util.JsonUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by on 2018/3/21.
 */
public class HttpSend {
    final static int threads = 200;

    final static HttpConnectionPool hcm = new HttpConnectionPool(threads);

    public String httpResponse(String postUrl, Object obj,String account) throws IOException {
        HttpPost post = new HttpPost(postUrl);
        post.setHeader(HeaderConstants.SVS_USER_ID,account);
        StringEntity myEntity = new StringEntity(JsonUtils.bean2Json(obj), ContentType.APPLICATION_JSON);
        post.setEntity(myEntity);
        CloseableHttpClient client = hcm.getClient();
        CloseableHttpResponse response = client.execute(post);
        HttpEntity entity = response.getEntity();

        if (response.getStatusLine().getStatusCode() == 200){
            String responContent = EntityUtils.toString(entity,"utf-8");
            return responContent;
        }
        return null;
    }

//    public <T> T httpResponse(String postUrl, Object obj,String account,Class<T> valueType) throws IOException {
//        HttpPost post = new HttpPost(postUrl);
//        post.setHeader(HeaderConstants.SVS_USER_ID,account);
//        StringEntity myEntity = new StringEntity(JsonUtils.bean2Json(obj), ContentType.APPLICATION_JSON);
//        post.setEntity(myEntity);
//        CloseableHttpClient client = hcm.getClient();
//        CloseableHttpResponse response = client.execute(post);
//        HttpEntity entity = response.getEntity();
//
//        if (response.getStatusLine().getStatusCode() == HttpServletResponse.SC_OK){
//            String responContent = EntityUtils.toString(entity,"utf-8");
//            return JsonUtils.json2Bean(responContent,valueType);
//        }
//        return null;
//    }
}
