/**
 * Created on 2019年8月5日 by liuyipin
 */
package com.ahdms.ap.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.common.GridModel;
import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.exception.tokenErrorException;
import com.ahdms.ap.model.AccessToken;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.service.ServerService;
import com.ahdms.ap.utils.IpUtil;
import com.ahdms.ap.utils.SortUtils;
import com.ahdms.ap.utils.TypeUtils;
import com.ahdms.ap.vo.AuthRecordVo;
import com.ahdms.ap.vo.CtidInfoReqVO;
import com.ahdms.ap.vo.CtidVO;
import com.ahdms.ap.vo.IDCardReqVO;
import com.ahdms.ap.vo.PersonInfoVO;
import com.ahdms.ap.vo.RecordBySerialReqVO;
import com.ahdms.ap.vo.RecordReqVO;
import com.ahdms.ap.vo.RequestListReqVO;
import com.ahdms.ctidservice.service.TokenCipherService;
import com.ahdms.util.SM3Digest;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import redis.clients.jedis.JedisCluster;

/**
 * @Title
 * @Description
 * @Copyright
 *            <p>
 *            Copyright (c) 2015
 *            </p>
 * @Company
 *          <p>
 *          迪曼森信息科技有限公司 Co., Ltd.
 *          </p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@RestController
@RequestMapping("/V216/server")
public class ServerController {

	private static final Logger LOGGER = LoggerFactory.getLogger(ServerController.class);
	@Autowired
	private ServerService serverService; 

	@Autowired
	private TokenCipherService cipherService;

	@Resource(name="ctidJedis")
	private JedisCluster jedisCluster;

	@RequestMapping(value = "/serverLogin", method = RequestMethod.POST)
	@ApiOperation(value = "服务商验证", notes = "服务商验证")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "account", value = "姓名", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "Password", value = "设备密码", required = true, dataType = "string") })
	public HttpResponseBody serverLogin(@RequestParam("account") String account,
			@RequestParam("Password") String Password) {
		HttpResponseBody hb = new HttpResponseBody<>(); 
		int result = serverService.login(account, Password); 
		if (result == 0) {
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
		} else {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("账号信息有误。");
		}
		return hb;
	}

	@RequestMapping(value = "/recordsList", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "获取认证记录", notes = "获取认证记录") 
	public HttpResponseBody<GridModel<AuthRecord>> recordsList(HttpServletRequest request,@RequestBody RequestListReqVO reqVO) { 
		HttpResponseBody<GridModel<AuthRecord>> hb = new HttpResponseBody<>(); 
		try { 
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("infoSource", reqVO.getInfoSource());
			map.put("page", String.valueOf(reqVO.getPage()));
			map.put("rows", String.valueOf(reqVO.getRows()));
			map.put("IDcard",reqVO.getIDcard());
			map.put("token", reqVO.getToken());
			map.put("reqTime", String.valueOf(reqVO.getReqTime()));
			checkUser(reqVO.getToken(),request,reqVO.getReqTime(),map,reqVO.getSignResult());
		} catch (tokenErrorException e) {
			hb.setCode(Contents.RETURN_CODE_TOKEN_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}
		catch(Exception e) { 
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}
		if(reqVO.getIDcard().equals("") || null == reqVO.getIDcard()){
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("身份证不可为空。");
			return hb;
		}
		GridModel<AuthRecord> list = serverService.recordsList(reqVO.getInfoSource(), reqVO.getIDcard().toUpperCase(),reqVO.getPage(),reqVO.getRows());                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
		hb.setCode(Contents.RETURN_CODE_SUCCESS);
		hb.setData(list);
		return hb;
	}



	@RequestMapping(value = "/getRecord", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "获取认证记录", notes = "获取认证记录") 
	public HttpResponseBody<AuthRecordVo> getRecord(HttpServletRequest request,@RequestBody RecordReqVO recordReqVO) {
		HttpResponseBody<AuthRecordVo> hb = new HttpResponseBody<>();
		AuthRecordVo recordVo;
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("recordId", recordReqVO.getRecordId()); 
			map.put("token", recordReqVO.getToken());
			map.put("reqTime", recordReqVO.getReqTime()+"");
			checkUser(recordReqVO.getToken(),request,recordReqVO.getReqTime(),map,recordReqVO.getSignResult());
		} catch (tokenErrorException e) {
			hb.setCode(Contents.RETURN_CODE_TOKEN_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}catch (Exception e) {  
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}
		try {
			recordVo = serverService.getRecord(recordReqVO.getRecordId());
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(recordVo);
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("图片获取失败");
			e.printStackTrace();
		}

		return hb;
	}

	@RequestMapping(value = "/getIdCard", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "获取身份信息", notes = "获取身份信息") 
	public HttpResponseBody<PersonInfoVO> getIdCard(HttpServletRequest request,@RequestBody IDCardReqVO ReqVO) {
		HttpResponseBody<PersonInfoVO> hb = new HttpResponseBody<>(); 
		AccessToken accessToken = null;
		try {
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("token", ReqVO.getToken());
			map.put("reqTime", String.valueOf(ReqVO.getReqTime()));
			accessToken = checkUser(ReqVO.getToken(),request,ReqVO.getReqTime(),map,ReqVO.getSignResult());
		} catch (tokenErrorException e) {
			hb.setCode(Contents.RETURN_CODE_TOKEN_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}catch (Exception e) { 
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}
		PersonInfoVO vo = serverService.getIdCard(accessToken.getOpenId()); 
		if(null != vo){
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(vo);
		}else{
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("不存在已有身份信息。");
		}
		return hb;
	}

	@RequestMapping(value = "/getCtidInfo", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "获取网证信息", notes = "获取网证信息") 
	public HttpResponseBody<CtidVO> getCtidInfo( HttpServletRequest request,@RequestBody CtidInfoReqVO ReqVO ) {
		HttpResponseBody<CtidVO> hb = new HttpResponseBody<>();  
		try {
			Map<String, Object> map = new HashMap<String, Object>(); 
			map.put("IDcard", ReqVO.getIDcard());
			map.put("token", ReqVO.getToken());
			map.put("reqTime", String.valueOf(ReqVO.getReqTime()));
			checkUser(ReqVO.getToken(),request,ReqVO.getReqTime(),map,ReqVO.getSignResult());
		} catch (tokenErrorException e) {
			hb.setCode(Contents.RETURN_CODE_TOKEN_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}catch (Exception e) { 
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}
		CtidVO vo = serverService.getCtidInfo(ReqVO.getIDcard().toUpperCase()); 
		if(null != vo){
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(vo);
		}else{
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("不存在网证信息。");
		}
		return hb;
	}

	@RequestMapping(value = "/getRecordBySerialNum", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "通过流水号获取认证记录", notes = "通过流水号获取认证记录") 
	public HttpResponseBody<AuthRecordVo> getRecordBySerialNum(HttpServletRequest request,@RequestBody RecordBySerialReqVO recordReqVO) {
		HttpResponseBody<AuthRecordVo> hb = new HttpResponseBody<>();
		AuthRecordVo recordVo;
		String openId = "";
		try {
			Map<String, Object> map = new HashMap<String, Object>();
			if(StringUtils.isBlank(recordReqVO.getSerialNum()) ||StringUtils.isBlank(recordReqVO.getToken()) ||StringUtils.isBlank(recordReqVO.getReqTime()+"") ) {
				throw new Exception("参数有误");
			}
			map.put("reqSerialNum", recordReqVO.getSerialNum()); 
			map.put("token", recordReqVO.getToken());
			map.put("reqTime", recordReqVO.getReqTime()+"");
			AccessToken at = checkUser(recordReqVO.getToken(),request,recordReqVO.getReqTime(),map,recordReqVO.getSignResult());
			openId = at.getOpenId();
		} catch (tokenErrorException e) {
			hb.setCode(Contents.RETURN_CODE_TOKEN_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}catch (Exception e) {  
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			return hb;
		}
		try {
			recordVo = serverService.getRecordBySerialNum(recordReqVO.getSerialNum(),openId);
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(recordVo);
		} catch (Exception e) {
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage(e.getMessage());
			e.printStackTrace();
		}

		return hb;
	}


	private AccessToken checkUser(String tokenid ,HttpServletRequest request, Long reqTime, Map<String, Object> map, String signResult) throws Exception,tokenErrorException{ 
		Date d = new Date();
		//		SimpleDateFormat sdf = new SimpleDateFormat( "yyyy-MM-dd hh:mm:ss");
		//		long l;
		try {  
			long l = d.getTime() - reqTime;
			if(l > 300000){ 
				throw new Exception("请求时间有误。"); 
			}
		} catch (ParseException e1) {
			throw new Exception("请求时间有误。"); 
		} 

		String token = jedisCluster.get(tokenid); 
		if(StringUtils.isBlank(token)){ 
			throw new tokenErrorException("无对应用户信息。");
		}
		AccessToken accessToken = cipherService.decodeAccess(token); 
		if(null == accessToken){
			throw new Exception("用户信息解密失败，请重试。");
		}
		map.put("serialNum", accessToken.getSerialNum());
		String result = SortUtils.formatUrlParam(map, "utf-8", false);
		SM3Digest sm3 = new SM3Digest();
		byte[] digit  =sm3.sm3Digest(result.getBytes());
		LOGGER.info("》》》》签名原文《《《《"+result);
		String finalResult = TypeUtils.byteToHex(digit);
		LOGGER.info("》》》》摘要值《《《《"+finalResult);
		if(!finalResult.equals(signResult)){
			throw new Exception("用户请求信息有误，请重试");
		}
		String ip = IpUtil.getClinetIpByReq(request);
		LOGGER.info("本次请求IP地址:{}"+ip);
		LOGGER.info("获取token时的IP地址:{}"+accessToken.getIp());

		if(!ip.equals(accessToken.getIp()) ){
			throw new Exception("用户IP信息有变更，请重新登录。");
		} 

		return accessToken;

	} 

	public static void main(String[] args) {

		SM3Digest sm3 = new SM3Digest();
		byte[] digit  =sm3.sm3Digest("reqSerialNum=076aZyb8hn0Arlh9KNS13pDVnkaI69sw&reqTime=1577957912660&serialNum=88aa58cdc48e4f89&token=1873be67eaee4d8d".getBytes()); 
		String finalResult = TypeUtils.byteToHex(digit);
		System.out.println(finalResult);
	}



}
