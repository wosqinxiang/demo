package com.ahdms.ctidservice.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ahdms.ap.utils.HttpRequestUtils;
import com.ahdms.ctidservice.common.Base64Utils;
import com.ahdms.ctidservice.common.FileUtils;

import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

@Component
public class WxFaceApiUtils {
	Logger logger = LoggerFactory.getLogger(WxFaceApiUtils.class);
	
	private String picTokenKey = "picTokenKey";
	
	@javax.annotation.Resource(name="ctidJedis")
	private JedisCluster jedisCluster;
	
	@Value("${wx.mini.program.appId}")
	private String appId;
	
	@Value("${wx.mini.program.secretKey}")
	private String secret;
	
	@Value("${wx.mini.program.picTokenUrl}")
	private String picTokenUrl;
	
	@Value("${wx.mini.program.getImageUrl}")
	private String getImageUrl;
	
	@Value("${wx.mini.program.getVideoUrl}")
	private String getVideoUrl;
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String getImageBase64(String verifyResult){
		try {
			String url = getImageUrl + getAccessToken(0);
			Map<String,String> params = new HashMap<>();
			params.put("verify_result", verifyResult);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.fromObject(params).toString(), httpHeaders);
			ResponseEntity<Resource> postForEntity = restTemplate.postForEntity(url,formEntity,Resource.class);
			Resource body = postForEntity.getBody();
			HttpHeaders headers = postForEntity.getHeaders();
			MediaType contentType = headers.getContentType();
			if(MediaType.IMAGE_JPEG.equals(contentType)){
				InputStream inputStream = body.getInputStream();
				byte[] byteArray = FileUtils.toByteArray(inputStream);
				return Base64Utils.encode(byteArray);
			}else if(contentType.toString().contains("application/json")){
				InputStream inputStream = body.getInputStream();
				byte[] byteArray = FileUtils.toByteArray(inputStream);
				String result = new String(byteArray);
				JSONObject resultJO = JSONObject.fromObject(result);
				String errcode = resultJO.getString("errcode");
				String errmsg = resultJO.getString("errmsg");
				logger.error("微信小程序API获取人脸图片失败："+resultJO.toString());
				if("42001".equals(errcode) || "40001".equals(errcode)){
					logger.error("token已过期>>"+errmsg);
					getAccessToken(1);
					return null;
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}
	
	public String getAccessToken(Integer status){
		
		try {
			String picTokenValue = null;
			if(1 == status){
				picTokenValue = getPicToken();
				jedisCluster.set(picTokenKey, picTokenValue);
				jedisCluster.expire(picTokenKey, 5400);
			}else{
				picTokenValue = jedisCluster.get(picTokenKey);
				if(StringUtils.isBlank(picTokenValue)){
					picTokenValue = getPicToken();
					jedisCluster.set(picTokenKey, picTokenValue);
					jedisCluster.expire(picTokenKey, 5400);
				}
			}
			return picTokenValue;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	}

	private String getPicToken() {
		StringBuffer stb = new StringBuffer(picTokenUrl);
		stb.append("&appid=").append(appId).append("&secret=").append(secret);
		String results = HttpRequestUtils.sendGetReq(stb.toString());
		JSONObject jo = JSONObject.fromObject(results);
		String accessToken = jo.getString("access_token");
		return accessToken;
	}
	
	
	public String getVideo(String verifyResult){
		try {
			String url = getVideoUrl + getAccessToken(0);
			Map<String,String> params = new HashMap<>();
			params.put("verify_result", verifyResult);
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> formEntity = new HttpEntity<String>(JSONObject.fromObject(params).toString(), httpHeaders);
			ResponseEntity<Resource> postForEntity = restTemplate.postForEntity(url,formEntity,Resource.class);
			Resource body = postForEntity.getBody();
			HttpHeaders headers = postForEntity.getHeaders();
			MediaType contentType = headers.getContentType();
			if(contentType.toString().equals("video/mpeg4")){
				InputStream inputStream = body.getInputStream();
				byte[] byteArray = FileUtils.toByteArray(inputStream);
				return Base64Utils.encode(byteArray);
			}else if(contentType.toString().contains("application/json")){
				InputStream inputStream = body.getInputStream();
				byte[] byteArray = FileUtils.toByteArray(inputStream);
				String result = new String(byteArray);
				JSONObject resultJO = JSONObject.fromObject(result);
				String errcode = resultJO.getString("errcode");
				String errmsg = resultJO.getString("errmsg");
				logger.error("微信小程序API获取人脸图片失败："+resultJO.toString());
				if("42001".equals(errcode) || "40001".equals(errcode)){
					logger.error("token已过期>>"+errmsg);
					getAccessToken(1);
					return getVideo(verifyResult);
				}
			}
		} catch (IOException e) {
			logger.error(e.getMessage(),e);
		}
		return null;
	} 
 
}
