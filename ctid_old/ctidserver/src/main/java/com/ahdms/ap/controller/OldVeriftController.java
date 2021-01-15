/**
 * Created on 2019年8月2日 by liuyipin
 */
package com.ahdms.ap.controller;

import java.util.Map;

import com.ahdms.ctidservice.common.IpUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.service.VerifyService;
import com.ahdms.ap.vo.AddUserVO;
import com.ahdms.ap.vo.CtidOfflineRequestVO;
import com.ahdms.ap.vo.CtidOnlineRequestVO;
import com.ahdms.ap.vo.OfflineRequestVO;
import com.ahdms.ap.vo.OnlineRequestVO;
import com.ahdms.ap.vo.OnlineVO;
import com.ahdms.ap.vo.SignResponseVo;
import com.ahdms.ctidservice.service.AuthCtidService;
import com.ahdms.ctidservice.vo.CtidResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import javax.servlet.http.HttpServletRequest;

/**
 * @author liuyipin
 * @version 1.0
 * @Title
 * @Description
 * @Copyright <p>
 * Copyright (c) 2015
 * </p>
 * @Company <p>
 * 迪曼森信息科技有限公司 Co., Ltd.
 * </p>
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@RestController
@RequestMapping("/verify")
public class OldVeriftController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OldVeriftController.class);

	@Autowired
	private VerifyService verifyService; 

	@Value("${transfer.service.signUrl}")
	private String signUrl;

	//    @Value("${ctid.auth.url}")
	//    private String authUrl;

	@Value("${transfer.skip.url}")
	private String skipUrl;

	@Autowired
	private AuthCtidService authCtidService;

	@RequestMapping(value = "/simpleVerify", method = RequestMethod.POST)
	@ApiOperation(value = "便捷认证", notes = "便捷认证")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "Name", value = "姓名", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "IDcard", value = "身份证", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "facePic", value = "人像信息", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "devicName", value = "设备识别码", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "Password", value = "设备密码", required = true, dataType = "string")})
	public HttpResponseBody<OnlineVO> simpleVerify(@RequestParam("Name") String name,
												   @RequestParam("IDcard") String IDcard, @RequestParam("facePic") String facePic,
												   @RequestParam("devicName") String devicName, @RequestParam("Password") String Password, HttpServletRequest request) {
		HttpResponseBody<OnlineVO> hb = new HttpResponseBody<>(); 
		OnlineVO vo = null;
		try {
			if(StringUtils.isBlank(name) || StringUtils.isBlank(IDcard) || StringUtils.isBlank(devicName) || StringUtils.isBlank(Password) ){
				throw new Exception("参数不可为空");
			}
			if(StringUtils.isNotBlank(facePic)){
				facePic = facePic.replaceAll(" ", "+");
			}
			vo = verifyService.simpleVerify(name, IDcard.toUpperCase(), facePic, devicName, Password, IpUtils.getIpAddress(request));
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(vo);
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			LOGGER.error(e.getMessage()); 
		} 
		return hb;

	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/onlineVerify", method = RequestMethod.POST)
	@ApiOperation(value = " 在线认证", notes = "在线认证")
	public HttpResponseBody onlineVerify(@RequestBody OnlineRequestVO onlineRequestVO,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			if(StringUtils.isBlank(onlineRequestVO.getName()) || StringUtils.isBlank(onlineRequestVO.getIdCard().toUpperCase()) 
					|| StringUtils.isBlank(onlineRequestVO.getFacePic()) || StringUtils.isBlank(onlineRequestVO.getOpenID())
					|| StringUtils.isBlank(onlineRequestVO.getSerialNum())){
				throw new Exception("参数不可为空");
			}
			if( onlineRequestVO.getType() < 1 || onlineRequestVO.getType() >4){
				throw new Exception("参数有误");
			}
			
			Map<String, Object> b = verifyService.onlineVerify(onlineRequestVO.getName(), onlineRequestVO.getIdCard().toUpperCase(), onlineRequestVO.getFacePic(),
					onlineRequestVO.getType(), onlineRequestVO.getOpenID(), onlineRequestVO.getSerialNum(), onlineRequestVO.getLocation(), IpUtils.getIpAddress(request));
			
			if((boolean) b.get("result")){
				hb.setCode(Contents.RETURN_CODE_SUCCESS);
			}else{
				hb.setCode(Contents.RETURN_CODE_ERROR);
				hb.setMessage( (String) b.get("authDesc"));
			}
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
			return hb;
		}
		return hb;

	}

	@RequestMapping(value = "/offlineVerify", method = RequestMethod.POST)
	@ApiOperation(value = "离线认证", notes = "离线认证")
	public HttpResponseBody offLineVerify(@RequestBody OfflineRequestVO offlineRequestVO,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			LOGGER.debug("signUrl: {}",signUrl);
			//            LOGGER.debug("authUrl: {}",authUrl);
			if(StringUtils.isBlank(offlineRequestVO.getName()) || StringUtils.isBlank(offlineRequestVO.getIdCard().toUpperCase()) 
					|| StringUtils.isBlank(offlineRequestVO.getFacePic()) || StringUtils.isBlank(offlineRequestVO.getOpenID())
					|| StringUtils.isBlank(offlineRequestVO.getSerialNum()) ){
				throw new Exception("参数不可为空");
			}
			if( offlineRequestVO.getType() < 1 || offlineRequestVO.getType() >4){
				throw new Exception("参数有误");  
			}
			SignResponseVo srVo = verifyService.offlineVerify(offlineRequestVO.getName(),
					offlineRequestVO.getIdCard().toUpperCase(), offlineRequestVO.getFacePic(),
					offlineRequestVO.getOpenID(), offlineRequestVO.getSerialNum(),
					offlineRequestVO.getType(), signUrl, IpUtils.getIpAddress(request));
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(srVo);
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return hb;
	}

	@RequestMapping(value = "/getSerialNum", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "getSerialNum", notes = "在线认证，后台产生流水号")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "deviceName", value = "设备账户", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "password", value = "密码", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "url", value = "回调地址", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "location", value = "是否需要位置信息", required = false, dataType = "int")})
	public HttpResponseBody<String> getSerialNum(@RequestParam("deviceName") String deviceName, @RequestParam("password") String password,
			@RequestParam("url") String url,
			@RequestParam(value = "location",required = false ,defaultValue = "1") int location) {
		HttpResponseBody<String> ret = new HttpResponseBody<String>();
		try {
			if(StringUtils.isBlank(deviceName) || StringUtils.isBlank(password) || StringUtils.isBlank(url)  ){
				throw new Exception("参数不可为空");
			}
			if(location != 0 && location != 1 ){
				throw new Exception("参数有误");
			}
			ret.setCode(Contents.RETURN_CODE_SUCCESS);
			ret.setData(verifyService.generateSerialNum(deviceName, password, url, location, skipUrl));
		} catch (Exception e) {
			ret.setCode(Contents.RETURN_CODE_ERROR);
			ret.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
		}
		return ret;
	}


	@RequestMapping(value = "/addUser", method = RequestMethod.POST)
	@ApiOperation(value = "绑定用户", notes = "绑定用户")
	//    @ApiImplicitParams({
	//            @ApiImplicitParam(paramType = "query", name = "Name", value = "姓名", required = true, dataType = "string"),
	//            @ApiImplicitParam(paramType = "query", name = "IDcard", value = "身份证", required = true, dataType = "string"),
	//            @ApiImplicitParam(paramType = "query", name = "facePic", value = "人像信息", required = true, dataType = "string")})
	public HttpResponseBody addUser(@RequestBody AddUserVO vo,HttpServletRequest request) {
		HttpResponseBody  hb = new HttpResponseBody<>();
		boolean b = false;

		if(StringUtils.isBlank(vo.getName()) || StringUtils.isBlank(vo.getIdCard())|| StringUtils.isBlank( vo.getFacePic()) || StringUtils.isBlank(vo.getOpenID())  ){
			hb.setCode(Contents.RETURN_CODE_ERROR); 
			hb.setMessage("参数不能为空！");
			return hb;
		}
		if( vo.getType() < 1 || vo.getType() >4){
			hb.setCode(Contents.RETURN_CODE_ERROR); 
			hb.setMessage("参数有误！");
			return hb;
		}
		try { 
			b = verifyService.addUser(vo.getName(), vo.getIdCard().toUpperCase(), vo.getFacePic(),vo.getOpenID(),vo.getType(), IpUtils.getIpAddress(request));
			if(b){
				hb.setCode(Contents.RETURN_CODE_SUCCESS);
			}else{
				hb.setCode(Contents.RETURN_CODE_ERROR);
			}
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR); 
			LOGGER.error(e.getMessage());
			hb.setMessage(e.getMessage());
		} 
		return hb;

	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/onlineVerifyByCtid", method = RequestMethod.POST)
	@ApiOperation(value = " 有网证在线认证", notes = "有网证在线认证")
	public HttpResponseBody onlineVerifyByCtid(@RequestBody CtidOnlineRequestVO onlineRequestVO,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			if(StringUtils.isBlank(onlineRequestVO.getCtid()) || StringUtils.isBlank(onlineRequestVO.getBusinessSerialNumber() ) 
					|| StringUtils.isBlank(onlineRequestVO.getFacePic()) || StringUtils.isBlank(onlineRequestVO.getOpenID())
					|| StringUtils.isBlank(onlineRequestVO.getSerialNum()) || 0 == onlineRequestVO.getType()){
				throw new Exception("参数不可为空");
			}

			if( onlineRequestVO.getType() < 1 || onlineRequestVO.getType() >4){
				throw new Exception("参数有误");
			}
			Map<String, Object> b = verifyService.onlineVerifyByCtid(onlineRequestVO.getCtid(),onlineRequestVO.getFacePic(),onlineRequestVO.getLocation(),
					onlineRequestVO.getOpenID(),onlineRequestVO.getSerialNum(),onlineRequestVO.getType(),onlineRequestVO.getBusinessSerialNumber(), IpUtils.getIpAddress(request));
			if((boolean) b.get("result")){
				hb.setCode(Contents.RETURN_CODE_SUCCESS);
			}else{
				hb.setCode(Contents.RETURN_CODE_ERROR);
				hb.setMessage( (String) b.get("authDesc"));
			}
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage()); 
			LOGGER.error(e.getMessage(),e);
			return hb;
		}
		return hb;

	}

	@RequestMapping(value = "/offlineVerifyByCtid", method = RequestMethod.POST)
	@ApiOperation(value = "有网证离线认证", notes = "有网证离线认证")
	public HttpResponseBody offlineVerifyByCtid(@RequestBody CtidOfflineRequestVO offlineRequestVO,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			if(StringUtils.isBlank(offlineRequestVO.getCtid()) || StringUtils.isBlank(offlineRequestVO.getBusinessSerialNumber() ) 
					|| StringUtils.isBlank(offlineRequestVO.getFacePic()) || StringUtils.isBlank(offlineRequestVO.getOpenID())
					|| StringUtils.isBlank(offlineRequestVO.getSerialNum()) || 0 == offlineRequestVO.getType() ){
				throw new Exception("参数不可为空");
			}
			if( offlineRequestVO.getType() < 1 || offlineRequestVO.getType() >4){
				throw new Exception("参数有误");
			}
			LOGGER.debug("signUrl: {}",signUrl);
			//            LOGGER.debug("authUrl: {}",authUrl);
			SignResponseVo srVo = verifyService.offlineVerifyByCtid(offlineRequestVO.getCtid(),offlineRequestVO.getFacePic(),offlineRequestVO.getOpenID(),
					offlineRequestVO.getSerialNum(),offlineRequestVO.getType(),offlineRequestVO.getBusinessSerialNumber(), IpUtils.getIpAddress(request));
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(srVo);
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return hb;
	}


	@RequestMapping(value = "/VerifyAuthCtidApply", method = {RequestMethod.POST,RequestMethod.GET})
	@ApiOperation(value = "app获取网证认证随机数", notes = "app获取网证认证随机数")
	public HttpResponseBody<Object> VerifyAuthCtidApply() {
		HttpResponseBody<Object> hb = new HttpResponseBody<>();
		try {
			CtidResult result = authCtidService.authCtidApply("0x06","");
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(result);
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			LOGGER.error(e.getMessage());
			e.printStackTrace();
		}
		return hb;
	}
}

