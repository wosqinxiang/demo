//package com.ahdms.ctidservice.service.impl;
//
//import java.io.IOException;
//import java.util.HashMap;
//import java.util.Map;
//
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import com.ahdms.ctidservice.service.PictureAuthService;
//import com.ahdms.ctidservice.util.ctid.HttpSend;
//
//import net.sf.json.JSONObject;
//
//@Service
//public class PictureAuthServiceImpl implements PictureAuthService {
//	@Value("${picture.server.url}")
//	private String picServerUrl;
//	
//	@Autowired
//	private HttpSend hs;
//
//	@Override
//	public String comparePicture(String pic1, String pic2) {
//		String result = null;
//		Map<String,String> params = new HashMap<>();
//		params.put("image1", pic1);
//		params.put("image2", pic2);
//		try {
//			result = hs.HttpResponse(picServerUrl, JSONObject.fromObject(params));
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
// 		return result;
//	}
//
//}
