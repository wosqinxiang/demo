/**
 * Created on 2019年8月5日 by liuyipin
 */
package com.ahdms.ap.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.ap.common.Contents;
import com.ahdms.ap.common.GridModel;
import com.ahdms.ap.common.HttpResponseBody;
import com.ahdms.ap.model.AuthRecord;
import com.ahdms.ap.service.ServerService;
import com.ahdms.ap.vo.AuthRecordVo;
import com.ahdms.ap.vo.CtidVO;
import com.ahdms.ap.vo.PersonInfoVO;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

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
@RequestMapping("/server")
public class OldServerController {

	@Autowired
	private ServerService serverService;

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
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "infoSource", value = "认证来源", required = true, dataType = "string"),
		@ApiImplicitParam(paramType = "query", name = "IDcard", value = "身份证", required = true, dataType = "string") })
	public HttpResponseBody<GridModel<AuthRecord>> recordsList(@RequestParam("infoSource") String infoSource,
			@RequestParam("IDcard") String IDcard,
			@RequestParam(value= "page",defaultValue = "0") int page,
			@RequestParam(value= "row",defaultValue = "10") int row) {
		HttpResponseBody<GridModel<AuthRecord>> hb = new HttpResponseBody<>(); 
		if(IDcard.equals("") || null == IDcard){
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("身份证不可为空。");
			return hb;
		}
		GridModel<AuthRecord> list = serverService.recordsList(infoSource, IDcard.toUpperCase(), page, row);                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                           
		hb.setCode(Contents.RETURN_CODE_SUCCESS);
		hb.setData(list);
		return hb;
	}



	@RequestMapping(value = "/getRecord", method = {RequestMethod.POST, RequestMethod.GET})
	@ApiOperation(value = "获取认证记录", notes = "获取认证记录")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "recordId", value = "认证id", required = true, dataType = "string")})
	public HttpResponseBody<AuthRecordVo> getRecord(@RequestParam("recordId") String recordId) {
		HttpResponseBody<AuthRecordVo> hb = new HttpResponseBody<>();
		AuthRecordVo recordVo;
		try {
			recordVo = serverService.getRecord(recordId);
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
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "openId", value = "openid", required = true, dataType = "string")})
	public HttpResponseBody<PersonInfoVO> getIdCard(@RequestParam("openId") String openId) {
		HttpResponseBody<PersonInfoVO> hb = new HttpResponseBody<>(); 
		PersonInfoVO vo = serverService.getIdCard(openId); 
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
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "IDcard", value = "IDcard", required = true, dataType = "string")})
	public HttpResponseBody<CtidVO> getCtidInfo(@RequestParam("IDcard") String IDcard) {
		HttpResponseBody<CtidVO> hb = new HttpResponseBody<>(); 
		CtidVO vo = serverService.getCtidInfo(IDcard.toUpperCase()); 
		if(null != vo){
			hb.setCode(Contents.RETURN_CODE_SUCCESS);
			hb.setData(vo);
		}else{
			hb.setCode(Contents.RETURN_CODE_ERROR);
			hb.setMessage("不存在网证信息。");
		}
		return hb;
	}
}
