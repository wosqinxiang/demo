/**
 * Created on 2019年8月2日 by liuyipin
 */
package com.ahdms.ap.controller;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import com.ahdms.ctidservice.common.IpUtils;
import com.ahdms.ctidservice.util.StringUtils;
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
import com.ahdms.ap.utils.IpUtil;
import com.ahdms.ap.vo.AddAuthRecordVO;
import com.ahdms.ap.vo.AddUserVO;
import com.ahdms.ap.vo.CtidOfflineRequestVO;
import com.ahdms.ap.vo.CtidOnlineRequestVO;
import com.ahdms.ap.vo.FTFVerifyVO;
import com.ahdms.ap.vo.LongDistanceVerifyVO;
import com.ahdms.ap.vo.OfflineRequestVO;
import com.ahdms.ap.vo.OnlineRequestVO;
import com.ahdms.ap.vo.OnlineVO;
import com.ahdms.ap.vo.SignResponseVo;
import com.ahdms.ap.vo.WechatSerialVO;
import com.ahdms.ctidservice.service.AuthCtidService;
import com.ahdms.ctidservice.util.WxFaceApiUtils;
import com.ahdms.ctidservice.vo.CtidResult;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/V216/verify")
public class VerifyController {

	private static final Logger LOGGER = LoggerFactory.getLogger(VerifyController.class);

	@Autowired
	private VerifyService verifyService; 

	@Autowired
	private WxFaceApiUtils wxFaceApiUtils;

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
			@RequestParam("devicName") String devicName, @RequestParam("Password") String Password,HttpServletRequest request) {
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
			Integer type = onlineRequestVO.getType();
			String pic = onlineRequestVO.getFacePic();
			if(1 == type){
				pic = wxFaceApiUtils.getImageBase64(pic);
				if(null == pic){
					throw new Exception("照片获取失败");
				}
			}
			Map<String, Object> b = verifyService.onlineVerify(onlineRequestVO.getName(), onlineRequestVO.getIdCard().toUpperCase(), pic,
					type, onlineRequestVO.getOpenID(), onlineRequestVO.getSerialNum(), onlineRequestVO.getLocation(), IpUtils.getIpAddress(request));

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

			Integer type = offlineRequestVO.getType();
			String pic = offlineRequestVO.getFacePic();
			if(1 == type){
				pic = wxFaceApiUtils.getImageBase64(pic);
				if(null == pic){
					throw new Exception("照片获取失败");
				}
			}
			SignResponseVo srVo = verifyService.offlineVerify(offlineRequestVO.getName(),
					offlineRequestVO.getIdCard().toUpperCase(), pic,
					offlineRequestVO.getOpenID(), offlineRequestVO.getSerialNum(),
					type, signUrl, IpUtils.getIpAddress(request));
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
	public HttpResponseBody<String> addUser(@RequestBody AddUserVO vo,HttpServletRequest request) {
		HttpResponseBody<String>  hb = new HttpResponseBody<>();
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
			Integer type = vo.getType();
			String pic = vo.getFacePic();
			if(1 == type){
				pic = wxFaceApiUtils.getImageBase64(pic);
				if(null == pic){
					throw new Exception("照片获取失败");
				}
			}
			b = verifyService.addUser(vo.getName(), vo.getIdCard().toUpperCase(), pic,vo.getOpenID(),type, IpUtils.getIpAddress(request));
			if(b){
				hb.setCode(Contents.RETURN_CODE_SUCCESS);
			}else{
				hb.setCode(Contents.RETURN_CODE_ERROR); 
				hb.setMessage("用户信息验证失败！");
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

	@RequestMapping(value = "/userLogin", method = RequestMethod.POST)
	@ApiOperation(value = "用户登录验证", notes = "用户商验证")
	public HttpResponseBody userLogin(@RequestBody AddUserVO vo,HttpServletRequest request) {
		HttpResponseBody  hb = new HttpResponseBody<>(); 
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
		String ip = IpUtil.getClinetIpByReq(request);
		try { 
			hb = verifyService.userLogin(vo.getName(), vo.getIdCard().toUpperCase(), vo.getFacePic(),vo.getOpenID(),vo.getType(),ip); 
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR); 
			LOGGER.error(e.getMessage());
			hb.setMessage(e.getMessage());
		} 
		return hb;

	}


	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/FTOFVerify", method = RequestMethod.POST)
	@ApiOperation(value = " 面对面认证", notes = "面对面认证")
	public HttpResponseBody FTOFVerify(@RequestBody FTFVerifyVO vo,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			if(StringUtils.isBlank(vo.getName()) || StringUtils.isBlank(vo.getIdcard() ) 
					|| StringUtils.isBlank(vo.getOpenID()) || StringUtils.isBlank(vo.getPic())
					|| StringUtils.isBlank(vo.getSerialNum())){
				throw new Exception("参数不可为空");
			} 
			if(isSpecialChar(vo.getSerialNum())) {
				throw new Exception("参数有误");
			} 
			if(!vo.getSerialNum().substring(0, 2).equals("06")) {
				throw new Exception("无效二维码。");
			}
			String pic = vo.getPic();
			String imageBase64 = wxFaceApiUtils.getImageBase64(pic);
			if(null == imageBase64){
				throw new Exception("照片获取失败"); 
			}
			Map<String, Object> b = verifyService.FTOFVerify(vo.getName(),vo.getIdcard(),vo.getOpenID(),imageBase64,vo.getSerialNum(), IpUtils.getIpAddress(request));
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

	@RequestMapping(value = "/getWechatSerialNum", method = {RequestMethod.POST})
	@ApiOperation(value = "getWechatSerialNum", notes = "面对面，分享认证后台产生流水号") 
	public HttpResponseBody<String> getWechatSerialNum(@RequestBody WechatSerialVO vo) {
		HttpResponseBody<String> ret = new HttpResponseBody<String>();
		try {
			if( StringUtils.isBlank(vo.getOpenid()) ){
				throw new Exception("参数不可为空");
			} 
			if(vo.getAuthType() != 1 && vo.getAuthType() != 2){
				throw new Exception("参数有误。");
			}
			if(vo.getAuthType() == 1 && StringUtils.isBlank(vo.getSocketId()) ) {
				throw new Exception("参数不可为空");
			}
			ret.setCode(Contents.RETURN_CODE_SUCCESS);
			ret.setData(verifyService.getWechatSerialNum(vo.getSocketId(), vo.getOpenid(), vo.getAuthType()));
		} catch (Exception e) {
			ret.setCode(Contents.RETURN_CODE_ERROR);
			ret.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
		}
		return ret;
	}



	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/longDistanceVerify", method = RequestMethod.POST)
	@ApiOperation(value = " 远程分享认证", notes = "远程分享认证")
	public HttpResponseBody longDistanceVerify(@RequestBody LongDistanceVerifyVO vo,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			if(StringUtils.isAnyBlank(vo.getName(),vo.getIdcard(),vo.getOpenID(),vo.getPic(),vo.getSerialNum())){
				throw new Exception("参数不可为空");
			}
			if(isSpecialChar(vo.getSerialNum())) {
				throw new Exception("参数有误");
			}
			if(!vo.getSerialNum().substring(0, 2).equals("07")) {
				throw new Exception("参数有误");
			}
			String pic = vo.getPic();
			String imageBase64 = wxFaceApiUtils.getImageBase64(pic);
			if(null == imageBase64){
				throw new Exception("照片获取失败");
			}
			Map<String, Object> b = verifyService.longDistanceVerify(vo.getName(),vo.getIdcard(),vo.getOpenID(),imageBase64,vo.getSerialNum(), IpUtils.getIpAddress(request));
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

	public static boolean isSpecialChar(String str) { 
		String regEx = "[ _`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]|\n|\r|\t";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(str);
		return m.find(); 
	}  


	@RequestMapping(value = "/SerialNumIsValid", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "SerialNumIsValid", notes = "面对面中二维码是否有效") 
	public HttpResponseBody SerialNumIsValid(@RequestParam(value = "serialNum" , required = true)String serialNum) {
		HttpResponseBody<String> ret = new HttpResponseBody<String>();
		try {
			if( StringUtils.isBlank(serialNum) ){
				throw new Exception("参数不可为空");
			}  
			if(isSpecialChar(serialNum)) {
				throw new Exception("参数有误");
			}
			boolean b = verifyService.SerialNumIsValid(serialNum);
			if(b) {
				ret.setCode(Contents.RETURN_CODE_SUCCESS); 
			}else {
				ret.setCode(Contents.RETURN_CODE_ERROR);
			}
		} catch (Exception e) {
			ret.setCode(Contents.RETURN_CODE_ERROR);
			ret.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
		}
		return ret;
	}

	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/addAuthRecord", method = RequestMethod.POST)
	@ApiOperation(value = "活体检测失败添加认证记录", notes = "远程分享认证")
	public HttpResponseBody addAuthRecord(@RequestBody AddAuthRecordVO vo,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {

			if(StringUtils.isAnyBlank(vo.getName(),vo.getIdcard(),vo.getOpenID(),vo.getSerialNum(),vo.getFailDesc())
				|| vo.getAuthType() == 0){
				throw new Exception("参数不可为空");
			}
			if(isSpecialChar(vo.getSerialNum())) {
				throw new Exception("参数有误");
			}
			verifyService.addAuthRecord(vo.getName(),vo.getIdcard(),vo.getOpenID(),null,vo.getSerialNum(),vo.getAuthType(),vo.getFailDesc(), IpUtils.getIpAddress(request));

		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage()); 
			LOGGER.error(e.getMessage(),e);
			return hb;
		}
		return hb;

	}
	
	@RequestMapping(value = "/getWPOSSerialNum", method = {RequestMethod.POST})
	@ApiOperation(value = "getWPOSSerialNum", notes = "获取旺pos流水号") 
	public HttpResponseBody<String> getWPOSSerialNum(@RequestParam String wposEN) {
		HttpResponseBody<String> ret = new HttpResponseBody<String>();
		try {
			if( StringUtils.isBlank(wposEN)){
				throw new Exception("参数不可为空");
			}  
			ret.setCode(Contents.RETURN_CODE_SUCCESS);
			ret.setData(verifyService.getWPOSSerialNum(wposEN));
		} catch (Exception e) {
			ret.setCode(Contents.RETURN_CODE_ERROR);
			ret.setMessage(e.getMessage());
			LOGGER.error(e.getMessage());
		}
		return ret;
	}
	
	@SuppressWarnings("rawtypes")
	@RequestMapping(value = "/WPOSVerify", method = RequestMethod.POST)
	@ApiOperation(value = " 旺pos认证", notes = "旺pos认证")
	public HttpResponseBody WPOSVerify(@RequestBody OnlineRequestVO onlineRequestVO,HttpServletRequest request) {
		HttpResponseBody hb = new HttpResponseBody<>();
		try {
			if(StringUtils.isAnyBlank(onlineRequestVO.getName(),onlineRequestVO.getIdCard()
					,onlineRequestVO.getFacePic(),onlineRequestVO.getOpenID(),onlineRequestVO.getSerialNum())){
				throw new Exception("参数不可为空");
			}
			if( onlineRequestVO.getType() < 1 || onlineRequestVO.getType() >4){
				throw new Exception("参数有误");
			}
			Integer type = onlineRequestVO.getType();
			String pic = onlineRequestVO.getFacePic();
			if(1 == type){
				pic = wxFaceApiUtils.getImageBase64(pic);
				if(null == pic){
					throw new Exception("照片获取失败");
				}
			}
			Map<String, Object> b = verifyService.WPOSVerify(onlineRequestVO.getName(), onlineRequestVO.getIdCard().toUpperCase(), pic,
					type, onlineRequestVO.getOpenID(), onlineRequestVO.getSerialNum(), IpUtils.getIpAddress(request) );

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

	public static void main(String[] args) {

	}

}

