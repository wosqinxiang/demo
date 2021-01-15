//package com.ahdms.ctidservice.util.ctid;
//
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//
//import org.apache.http.HttpEntity;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.config.RequestConfig;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.CloseableHttpResponse;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.entity.ContentType;
//import org.apache.http.entity.StringEntity;
//import org.apache.http.impl.client.CloseableHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//import org.apache.http.util.EntityUtils;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//
//import net.sf.json.JSON;
//
///**
// * Created by on 2018/3/21.
// */
//@Component
//public class HttpSend {
//	
//Logger logger = LoggerFactory.getLogger(HttpSend.class);
//	
//	@Autowired
//    private HttpPool httpPool;
//	
//	@Value("${timeout.value:10000}")
//	private Integer socketTimeout;
//	
//	@Value("${timeout.connectTimeout:30000}")
//	private Integer connectTimeout;
//	
//	@Value("${timeout.connectionRequestTimeout:20000}")
//	private Integer connectionRequestTimeout;
//    
//    public String HttpResponse(String postUrl, JSON jsonObj,Integer count){
//    	if(count == null || count ==0){
//    		count = 1;
//    	}
//    	String result = null;
//    	for(int i=1;i<=count;i++){
//    		try {
//				result = HttpResponse(postUrl,jsonObj);
//			} catch (IOException e) {
//				logger.error("请求超时，准备第"+(i+1)+"次连接..");
//				logger.error(e.getMessage());
//			}
//    		if(result!=null){
//    			break;
//    		}
//    	}
//    	return result;
//    	
//    }
//    
//    public String HttpResponse(String postUrl, JSON jsonObj) throws IOException {
//        HttpPost post = new HttpPost(postUrl);
//        StringEntity myEntity = new StringEntity(jsonObj.toString(), ContentType.APPLICATION_JSON);
//        post.setEntity(myEntity);
//        post.setConfig(getRequestConfig());
//        CloseableHttpResponse response =  httpPool.getClient().execute(post);
//        HttpEntity entity = response.getEntity();
//        if (response.getStatusLine().getStatusCode()==200){
//            String responContent = EntityUtils.toString(entity,"utf-8");
//            return responContent;
//        }
//        return null;
//    }
//    
//    public String HttpPostMap(String postUrl, Map<String,String> map) throws IOException {
//        HttpPost post = new HttpPost(postUrl);
//        
//        List<NameValuePair> pairs = new ArrayList<NameValuePair>();
//        for(Map.Entry<String,String> entry : map.entrySet())
//        {
//            pairs.add(new BasicNameValuePair(entry.getKey(),entry.getValue()));
//        }
//        post.setEntity(new UrlEncodedFormEntity(pairs,"UTF-8"));
//        CloseableHttpResponse response =  httpPool.getClient().execute(post);
//        HttpEntity entity = response.getEntity();
//        if (response.getStatusLine().getStatusCode()==200){
//            String responContent = EntityUtils.toString(entity,"utf-8");
//            return responContent;
//        }
//        return null;
//    }
//    
//    /**
//     * 获取请求配置
//     *
//     * @return
//     */
//    public RequestConfig getRequestConfig() {
//        return RequestConfig.custom()
//                .setSocketTimeout(socketTimeout)// 建立连接的超时时间
//                .setConnectTimeout(connectTimeout) // 连接建立后，传输数据时的超时时间
//                .setConnectionRequestTimeout(connectionRequestTimeout)// 从连接池中获取连接时的超时时间
//                .build();
//    }
//}
