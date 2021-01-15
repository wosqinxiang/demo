/**
 * <p>Title: PCControlApiUtils.java</p>  
 * <p>Description: </p>  
 * <p>Copyright: Copyright (c) 2019</p>  
 * <p>Company: www.ahdms.com</p>  
 * @author qinxiang  
 * @date 2019年9月20日  
 * @version 1.0  
*/
package com.ahdms.auth.common;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.ahdms.api.model.AuthCtidValidDateReturnBean;

import net.sf.json.JSONObject;

/**
 * <p>Title: PCControlApiUtils</p>  
 * <p>Description: 调用PC端控件接口工具类</p>  
 * @author qinxiang  
 * @date 2019年9月20日  
 */
@Component
public class PCControlApiUtils {
	private static final Logger LOG = LoggerFactory.getLogger(PCControlApiUtils.class);
	
//	@Autowired
//	private HttpSend hs;
	
	@Value("${ctid.pc.address}")
	private String address;
	
	@Autowired
	private RestTemplateUtils restTempalteUtils;
	
	
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: 获取认证码加密数据</p>  
	 * @param randomNum  随机数
	 * @param authCode   认证码原码
	 * @return
	 */
	public String getAuthCodeData(String randomNum,String authCode){
		try {
			if(!authCode.matches("[\\d]{8}")){
				LOG.error("认证码输入不规范！认证码: "+authCode);
				return null;
			}
			Map<String,String> params = new HashMap<>();
			params.put("randomNum", randomNum);
			params.put("authCode", authCode);
			
			String postResult = restTempalteUtils.postMapParams(address+"/GetAuthCodeData", params);
			
			JSONObject jo = JSONObject.fromObject(postResult);
			String result = jo.getString("result");
			if("TRUE".equalsIgnoreCase(result)){
				JSONObject jsonObject = jo.getJSONObject("data");
				String vCode = jsonObject.getString("authCode");
				return vCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: 获取ID验证数据</p>  
	 * @param randomNum   随机数
	 * @param ctid   网证数据 (可为空)
	 * @param type   类型
	 * @return
	 */
	public String getIDAuthData(String randomNum,String ctid,String type){
		try {
			Map<String,String> params = new HashMap<>();
			params.put("randomNum", randomNum);
			params.put("ctid", ctid);
			params.put("type", type);
			String postResult = restTempalteUtils.postMapParams(address+"/GetIDCardAuthData", params);
			JSONObject jo = JSONObject.fromObject(postResult);
			String result = jo.getString("result");
			if("TRUE".equalsIgnoreCase(result)){
				JSONObject jsonObject = jo.getJSONObject("data");
				String vCode = jsonObject.getString("idData");
				return vCode;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: 获取网证编号及网证有效期</p>  
	 * @param ctid  网证数据
	 * @return
	 */
	public AuthCtidValidDateReturnBean getCtidNumAndValidDate(String ctid){
		AuthCtidValidDateReturnBean bean = new AuthCtidValidDateReturnBean();
		try {
			Map<String,String> params = new HashMap<>();
			params.put("ctid", ctid);
			
			String postResult = restTempalteUtils.postMapParams(address+"/GetCtidNumAndValidDate", params);
			
			JSONObject jo = JSONObject.fromObject(postResult);
			String result = jo.getString("result");
			if("TRUE".equalsIgnoreCase(result)){
				JSONObject jsonObject = jo.getJSONObject("data");
				String ctidNum = jsonObject.getString("ctidNum");
				String validDate = jsonObject.getString("validDate");
				bean.setCtidNum(ctidNum);
				bean.setCtidValidDate(validDate);
				return bean;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bean;
	}
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: 获取二维码申请数据</p>  
	 * @param ctid
	 * @return
	 */
	public String getQRCodeApplyData(String type){
		try {
			Map<String,String> params = new HashMap<>();
			params.put("type", type);
			String postResult = restTempalteUtils.postMapParams(address+"/GetQRCodeApplyData", params);
			JSONObject jo = JSONObject.fromObject(postResult);
			String result = jo.getString("result");
			if("TRUE".equalsIgnoreCase(result)){
				JSONObject jsonObject = jo.getJSONObject("data");
				String applyData = jsonObject.getString("applyData");
				return applyData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: 获取二维码赋码数据</p>  
	 * @param randomNum  随机数
	 * @param ctid  网证数据
	 * @return
	 */
	public String getReqQRCodeData(String randomNum,String ctid){
		try {
			Map<String,String> params = new HashMap<>();
			params.put("randomNum", randomNum);
			params.put("ctid", ctid);
			String postResult = restTempalteUtils.postMapParams(address+"/GetReqQRCodeData", params);
			JSONObject jo = JSONObject.fromObject(postResult);
			String result = jo.getString("result");
			if("TRUE".equalsIgnoreCase(result)){
				JSONObject jsonObject = jo.getJSONObject("data");
				String reqData = jsonObject.getString("reqData");
				return reqData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 
	 * <p>Title: </p>  
	 * <p>Description: 获取二维码验码数据</p>  
	 * @param randomNum  随机数
	 * @param qrCode  二维码数据
	 * @return
	 */
	public String getAuthQRCodeData(String randomNum,String qrCode){
		try {
			Map<String,String> params = new HashMap<>();
			params.put("randomNum", randomNum);
			params.put("qrCode", qrCode);
			String postResult = restTempalteUtils.postMapParams(address+"/GetAuthQRCodeData", params);
			JSONObject jo = JSONObject.fromObject(postResult);
			String result = jo.getString("result");
			if("TRUE".equalsIgnoreCase(result)){
				JSONObject jsonObject = jo.getJSONObject("data");
				String qrData = jsonObject.getString("qrData");
				return qrData;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
}
