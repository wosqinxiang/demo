package com.ahdms.auth.common;

import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class RestTemplateUtils {
	
	@Autowired
	private RestTemplate restTemplate;
	
	public String postMapParams(String url,Map<String,String> params){
		
		try {
			MultiValueMap<String, String> map = new LinkedMultiValueMap<String, String>();
			for (Entry<String, String> entry : params.entrySet()) {
				map.add(entry.getKey(), entry.getValue());
			}
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
			HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(map, headers);

			ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, request, String.class);
			return postForEntity.getBody();
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	public String post(String url,Object params){
		try {
			HttpHeaders headers = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

			HttpEntity<String> request = new HttpEntity<String>(JsonUtils.bean2Json(params),headers);

			ResponseEntity<String> postForEntity = restTemplate.postForEntity(url, request, String.class);
			String httpResponse = postForEntity.getBody();

			return httpResponse;
		} catch (RestClientException e) {
			e.printStackTrace();
		}

		return null;
	}

}
