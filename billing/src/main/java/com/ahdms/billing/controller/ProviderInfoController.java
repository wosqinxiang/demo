/**
 * Created on 2020年5月8日 by liuyipin
 */
package com.ahdms.billing.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ProviderInfo;
import com.ahdms.billing.service.ProviderService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.alibaba.fastjson.JSON;
import com.github.pagehelper.util.StringUtil;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author liuyipin
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
@RestController
@RequestMapping("api/provider")
public class ProviderInfoController {

	private static final Logger logger = LoggerFactory.getLogger(ProviderInfoController.class);

	@Autowired
	private ProviderService providerService;

	@RequestMapping(value = "/providerInfoList", method = { RequestMethod.GET })
	@ApiOperation(value = "供应商列表", notes = "分页列表")
	public GridModel<ProviderInfo> SysUserList(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int rows,
			@RequestParam(required = false) String providerName
			) {
		Map<String, Object> map = new HashMap<String, Object>();
		if(StringUtil.isNotEmpty(providerName)) {
			map.put("providerName", providerName);
		}

		return providerService.queryProviderPageList(map, page, rows, null);
	}

	@RequestMapping(value = "/addProvider", method = { RequestMethod.POST })
	@ApiOperation(value = "新增供应商", notes = "新增供应商")
    @SysLog(comment = "新增供应商")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "providerName", value = "供应商名称", required = true, dataType = "String")})
	public Result<String> addUser(HttpServletRequest request,
			@RequestParam(required = true) String providerName)
					throws Exception {
		Result<String> hb = new Result<>(); 
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR); 
		if(StringUtil.isEmpty(providerName)) {
			hb.setMessage("参数不能为空！");
			return hb;
		}
		ProviderInfo provider = new ProviderInfo(UUIDGenerator.getUUID(), providerName);
		try { 
			hb = providerService.addProvider(provider);
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	}

	@RequestMapping(value = "/editProvider", method = { RequestMethod.POST })
	@ApiOperation(value = "修改供应商", notes = "修改供应商")
    @SysLog(comment = "修改供应商")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "providerName", value = "供应商名称", required = true, dataType = "String"), 
		@ApiImplicitParam(paramType = "query", name = "id", value = "供应商id", required = true, dataType = "String")})
	public Result<String> editProvider(HttpServletRequest request,
			@RequestParam(required = true) String providerName,  
			@RequestParam(required = true) String id )
					throws Exception {
		Result<String> hb = new Result<>(); 
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR); 
		if(StringUtil.isEmpty(id) || StringUtil.isEmpty(providerName)) {
			hb.setMessage("参数不能为空！");
			return hb;
		}
		ProviderInfo provider = new ProviderInfo(id, providerName);
		try { 
			hb = providerService.eidtProvider(provider);
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	}

	@RequestMapping(value = "/getProvider", method = { RequestMethod.POST })
	@ApiOperation(value = "获取供应商", notes = "获取供应商") 
	public Result<List<ProviderInfo>> getProvider(HttpServletRequest request )
			throws Exception {
		Result<List<ProviderInfo>> hb = new Result<>(); 
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);  

		try { 
			hb = providerService.getProvider();
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	}
	
	@RequestMapping(value = "/getProviderByServiceId", method = { RequestMethod.POST })
	@ApiOperation(value = "根据服务ID获取供应商列表", notes = "根据服务ID获取供应商列表") 
	public Result<List<ProviderInfo>> getProviderByServiceId(@RequestParam String serviceId )
			throws Exception {
		return providerService.getProviderByServiceId(serviceId);
	}
	

}

