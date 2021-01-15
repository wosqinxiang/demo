package com.ahdms.ctidservice.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ahdms.ap.vo.SearchPlaceVO;
import com.ahdms.ctidservice.bean.dto.FaceverifyBean;
import com.ahdms.ctidservice.vo.CtidResult;
import com.alibaba.fastjson.JSON;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import redis.clients.jedis.JedisCluster;

//@Component
public class BaiduApiUtils {
	Logger logger = LoggerFactory.getLogger(BaiduApiUtils.class);
	
	private String baiduTokenKey = "baiduTokenKey";
	
	@Value("${baidu.token.url}")
	private String baiduTokenUrl;
	
	@Value("${baidu.faceverify.url}")
	private String baiduFaceverifyUrl;
	
	@Value("${baidu.clientId}")
	private String clientId;
	
	@Value("${baidu.clientSecret}")
	private String clientSecret;
	
	@Value("${face.liveness.score:0.95}")
	private Double score;
	
	@Value("${baidu.carNum.url}")
	private String baiduCarNumUrl;
	
	@Resource(name="ctidJedis")
	private JedisCluster jedisCluster;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${baidu.location.url}")
	private String baiduLocationUrl;
	
	@Value("${baidu.searchPlace.url}")
	private String searchPlaceUrl;
	
	@Value("${baidu.suggestionPlace.url}")
	private String suggestionPlaceUrl;
	
	@Value("${baidu.location.ak}")
	private String baiduLocationAK;
	
	private String accessToken;
	
	public List<String> search(String keyword){
		//http://api.map.baidu.com/place/v2/suggestion?query=%E9%98%BF%E9%87%8C%E5%B7%B4%E5%B7%B4&region=%E5%8C%97%E4%BA%AC&city_limit=false&output=json&ak=Aih48Inty3pV28nCtaigq7P4nDhZLdl6&tag=%E5%85%AC%E5%8F%B8%E4%BC%81%E4%B8%9A
		try {
			String url = suggestionPlaceUrl + "?ak="+baiduLocationAK+"&output=json&city_limit=false&region=全国&query="+keyword;
			ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
			String body = forEntity.getBody();
			JSONObject json = JSONObject.fromObject(body);
			if(0 == json.getInt("status")){
				JSONArray resultArr = json.getJSONArray("result");
				if(resultArr != null && resultArr.size() > 0){
					List<String> list = new ArrayList<String>();
					for (int i=0;i<resultArr.size();i++) {
						JSONObject object = resultArr.getJSONObject(i);
						String name = object.getString("name");
						list.add(name);
					}
					return list;
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public List<String> searchPlace(String keyword){
		try {
			String url = searchPlaceUrl + "?ak="+baiduLocationAK+"&output=json&city_limit=false&region=全国&query="+keyword;
			ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
			String body = forEntity.getBody();
			JSONObject json = JSONObject.fromObject(body);
			if(0 == json.getInt("status")){
				JSONArray resultArr = json.getJSONArray("results");
				if(resultArr != null && resultArr.size() > 0){
					List<String> list = new ArrayList<String>();
					for (int i=0;i<resultArr.size();i++) {
						JSONObject object = resultArr.getJSONObject(i);
//						String name = object.getString("name");
						String address = object.getString("address");
						list.add(address);
					}
					return list;
				}
			}
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	
	public String getLocation(String location){
		//http://api.map.baidu.com/reverse_geocoding/v3/?ak=Aih48Inty3pV28nCtaigq7P4nDhZLdl6&output=json&coordtype=bd09ll&location=28.36121,112.8179
		try {
//			Map<String,String> params = new  HashMap<String, String>();
//			params.put("ak", baiduLocationAK);
//			params.put("output", "json");
//			params.put("coordtype", "bd09ll");
//			params.put("location", location);
			String url = baiduLocationUrl + "?ak="+baiduLocationAK+"&output=json&coordtype=bd09ll&location="+location;
			ResponseEntity<String> forEntity = restTemplate.getForEntity(url, String.class);
			String body = forEntity.getBody();
			JSONObject json = JSONObject.fromObject(body);
			String string = json.getJSONObject("result").getString("formatted_address");
			return string;
		} catch (RestClientException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public String licensePlate(String imageStr) {
        // 请求url
//        String url = "https://aip.baidubce.com/rest/2.0/ocr/v1/license_plate";
		//{"error_code":110,"error_msg":"Access token invalid or no longer valid"}
        try {
        	String encode = URLEncoder.encode(imageStr, "UTF-8");
            String param = "image=" + encode;
            String result = HttpUtil.post(baiduCarNumUrl, getAuth(0), param);
            JSONObject json = JSONObject.fromObject(result);
            String number = json.getJSONObject("words_result").getString("number");
//            String color = json.getJSONObject("words_result").getString("color");
            return number;
        } catch (Exception e) {
            e.printStackTrace();
        }
        getAuth(1);
        return null;
    }
	
	public CtidResult getFaceLiveness(List<FaceverifyBean> list){
		 // 请求url
//        String url = "https://aip.baidubce.com/rest/2.0/face/v3/faceverify";
        try {
        	String token = getAuth(0);
            String param = JSONArray.fromObject(list).toString();

            String result =  HttpUtil.post(baiduFaceverifyUrl,token,"application/json",param);
            JSONObject jo = JSONObject.fromObject(result);
            int error_code = jo.getInt("error_code");
            if( 0 == error_code){
            	JSONObject resultJson = jo.getJSONObject("result");
            	double face_liveness = resultJson.getDouble("face_liveness");
            	if(score <= face_liveness){
            		JSONArray faceList = resultJson.getJSONArray("face_list");
            		if(faceList != null && faceList.size()>0){
            			JSONObject faceJO = faceList.getJSONObject(0);
            			JSONObject qualityJO = faceJO.getJSONObject("quality");
            			JSONObject occlusionJO = qualityJO.getJSONObject("occlusion");
            			
            			int completeness = qualityJO.getInt("completeness");
            			if(completeness == 0){
            				logger.error("请正对手机，保持脸部完整completeness={}",completeness);
            				return CtidResult.error("请正对手机，保持脸部完整");
            			}
            			
            			double left_eye = occlusionJO.getDouble("left_eye");
            			double right_eye = occlusionJO.getDouble("right_eye");
            			double nose = occlusionJO.getDouble("nose");
            			double mouth = occlusionJO.getDouble("mouth");
            			double left_cheek = occlusionJO.getDouble("left_cheek");
            			double right_cheek = occlusionJO.getDouble("right_cheek");
            			double chin_contour  = occlusionJO.getDouble("chin_contour");
            			
            			if(left_eye > 0.6 || right_eye> 0.6){
            				logger.error("左右眼被遮挡的值：left({}),right({})",left_eye,right_eye);
            				return CtidResult.error("请正对手机，去除眼睛遮挡。");
            			}else if(nose > 0.6){
            				logger.error("鼻子被遮挡的值："+nose);
            				return CtidResult.error("请正对手机，去除鼻子遮挡。");
            			}else if(mouth > 0.6){
            				logger.error("嘴巴被遮挡的值："+mouth);
            				return CtidResult.error("请正对手机，去除嘴巴遮挡。");
            			}else if(left_cheek > 0.6 || right_cheek>0.6){
            				logger.error("左右脸颊被遮挡的值：left({}),right({})",left_cheek,right_cheek);
            				return CtidResult.error("请正对手机，去除脸部遮挡。");
            			}else if(chin_contour > 0.6){
            				logger.error("下巴被遮挡的值："+chin_contour);
            				return CtidResult.error("请正对手机，去除脸部遮挡。");
            			}else{
            				return CtidResult.ok(face_liveness);
            			}
            		}
            	}else{
            		logger.error("活体检测分数："+face_liveness);
            		return CtidResult.error("活体检测不通过，请重试！");
            	}
            	return CtidResult.error("活体检测不通过，请重试！");
            }else if( 100 == error_code || 110 == error_code || 111 == error_code){
            	accessToken = null;
            	return getFaceLiveness(list);
            }else if( 4 == error_code || 6 == error_code || 17 == error_code || 18 == error_code || 19 == error_code){
            	logger.error("活体检测QPS超限额"+result);
            	return CtidResult.error("活体检测不通过，请重试！");
            }else if( 222202 == error_code || 222203 == error_code ){
            	logger.error("请正对手机，保持脸部完整"+error_code);
            	return CtidResult.error("请正对手机，保持脸部完整");
            }else {
            	logger.error("活体检测异常"+result);
            	return CtidResult.error("活体检测不通过，请重试！");
            }
        } catch (Exception e) {
           logger.error(e.getMessage(), e);
        }
        return CtidResult.error("活体检测异常，请重试！");
	}
	
	 /**
     * 获取权限token
     * @return 返回示例：
     * {
     * "access_token": "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567",
     * "expires_in": 2592000
     * }
     */
    public String getAuth(int status) {
    	try {
			String baiduTokenValue = null;
			if(1 == status){
				baiduTokenValue = getAuth(clientId, clientSecret);
				jedisCluster.set(baiduTokenKey, baiduTokenValue);
				jedisCluster.expire(baiduTokenKey, 5400);
			}else{
				baiduTokenValue = jedisCluster.get(baiduTokenKey);
				if(StringUtils.isBlank(baiduTokenValue)){
					baiduTokenValue = getAuth(clientId, clientSecret);
					jedisCluster.set(baiduTokenKey, baiduTokenValue);
					jedisCluster.expire(baiduTokenKey, 5400);
				}
			}
			return baiduTokenValue;
		} catch (Exception e) {
			logger.error(e.getMessage(),e);
		}
		return null;
    }
   

    /**
     * 获取API访问token
     * 该token有一定的有效期，需要自行管理，当失效时需重新获取.
     * @param ak - 百度云官网获取的 API Key
     * @param sk - 百度云官网获取的 Securet Key
     * @return assess_token 示例：
     * "24.460da4889caad24cccdb1fea17221975.2592000.1491995545.282335-1234567"
     */
    public String getAuth(String ak, String sk) {
        // 获取token地址
//        String authHost = "https://aip.baidubce.com/oauth/2.0/token?";
        String getAccessTokenUrl = baiduTokenUrl
                // 1. grant_type为固定参数
                + "grant_type=client_credentials"
                // 2. 官网获取的 API Key
                + "&client_id=" + ak
                // 3. 官网获取的 Secret Key
                + "&client_secret=" + sk;
        try {
            URL realUrl = new URL(getAccessTokenUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
//            for (String key : map.keySet()) {
//                System.err.println(key + "--->" + map.get(key));
//            }
            // 定义 BufferedReader输入流来读取URL的响应
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String result = "";
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            /**
             * 返回结果示例
             */
//            System.err.println("result:" + result);
            JSONObject jsonObject = JSONObject.fromObject(result);
            String access_token = jsonObject.getString("access_token");
            return access_token;
        } catch (Exception e) {
            System.err.printf("获取token失败！");
            e.printStackTrace(System.err);
        }
        return null;
    }

}
