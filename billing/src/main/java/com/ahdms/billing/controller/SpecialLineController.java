package com.ahdms.billing.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.ProviderInfo;
import com.ahdms.billing.model.SpecialLineInfo;
import com.ahdms.billing.model.SpeciallineServiceinfo;
import com.ahdms.billing.service.SpecialLineService;
import com.ahdms.billing.vo.SpecialLineInfoVO;
import com.ahdms.billing.vo.SpecialLineVO;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping(value="/api",method={RequestMethod.GET,RequestMethod.POST})
public class SpecialLineController {
	
	@Autowired
	private SpecialLineService specialLineService;
	
	@SysLog(comment="新增专线")
	@RequestMapping(value = "/addSpecialLine", method = { RequestMethod.POST })
	@ApiOperation(value = "新增专线", notes = "新增专线") 
	public Result addSpecialLine(HttpServletRequest request,@RequestBody SpecialLineInfo specialLineInfo)
			throws Exception {
		if(specialLineInfo == null || StringUtils.isBlank(specialLineInfo.getName()) || StringUtils.isBlank(specialLineInfo.getName())){
			return Result.error("名称或编码不能为空");
		}
		return specialLineService.addSpecialLine(specialLineInfo);
	}
	
	@SysLog(comment="修改专线")
	@RequestMapping(value = "/updateSpecialLine", method = { RequestMethod.POST })
	@ApiOperation(value = "修改专线", notes = "修改专线") 
	public Result updateSpecialLine(HttpServletRequest request,@RequestBody SpecialLineInfo specialLineInfo)
			throws Exception {
		if(specialLineInfo == null || StringUtils.isBlank(specialLineInfo.getName()) || StringUtils.isBlank(specialLineInfo.getName())){
			return Result.error("名称或编码不能为空");
		}
		return specialLineService.updateSpecialLine(specialLineInfo);
	}
	
	@RequestMapping(value = "/findSpecialLine", method = { RequestMethod.POST })
	@ApiOperation(value = "条件查询专线", notes = "条件查询专线")
	 @ApiImplicitParams({
         @ApiImplicitParam(paramType = "query", name = "providerId", value = "供应商ID", required = false, dataType = "String"),
         @ApiImplicitParam(paramType = "query", name = "name", value = "专线名称", required = false, dataType = "String"),
         @ApiImplicitParam(paramType = "query", name = "code", value = "专线编码", required = false, dataType = "String")})
	public Result<List<SpecialLineVO>> findSpecialLine(HttpServletRequest request,
			@RequestParam(required = false) String providerId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String code)
			throws Exception {
		
		return specialLineService.findSpecialLine(providerId,name,code);
	}
	
	
	@SysLog(comment="删除专线")
	@RequestMapping(value = "/deleteSpecialLine", method = { RequestMethod.POST })
	@ApiOperation(value = "删除专线", notes = "删除专线")
	 @ApiImplicitParams({
         @ApiImplicitParam(paramType = "query", name = "id", value = "专线ID", required = true, dataType = "String")})
	public Result deleteSpecialLine(@RequestParam String id)
			throws Exception {
		
		return specialLineService.deleteSpecialLine(id);
	}
	
	@SysLog(comment="新增专线服务")
	@RequestMapping(value = "/addSpecialLineService", method = { RequestMethod.POST })
	@ApiOperation(value = "新增专线服务", notes = "新增专线服务") 
	public Result addSpecialLineService(HttpServletRequest request,@RequestBody SpecialLineInfoVO specialLineInfoVO)
			throws Exception {
		if(specialLineInfoVO == null || StringUtils.isBlank(specialLineInfoVO.getSpecialId()) || StringUtils.isBlank(specialLineInfoVO.getServiceId())){
			return Result.error("专线或服务不能为空");
		}
		return specialLineService.addSpecialLineService(specialLineInfoVO);
	}
	
	@SysLog(comment="删除专线服务")
	@RequestMapping(value = "/deleteSpecialLineService", method = { RequestMethod.POST })
	@ApiOperation(value = "删除专线服务", notes = "删除专线服务")
	 @ApiImplicitParams({
         @ApiImplicitParam(paramType = "query", name = "id", value = "专线服务ID", required = true, dataType = "String")})
	public Result deleteSpecialLineService(@RequestParam String id)
			throws Exception {
		return specialLineService.deleteSpecialLineService(id);
	}
	
	@RequestMapping(value = "/findSpecialLineService", method = { RequestMethod.POST })
	@ApiOperation(value = "查询专线服务", notes = "查询专线服务") 
	 @ApiImplicitParams({
         @ApiImplicitParam(paramType = "query", name = "specialLineId", value = "专线ID", required = true, dataType = "String")})
	public Result findSpecialLineService(@RequestParam String specialLineId)
			throws Exception {
		return specialLineService.findSpecialLineService(specialLineId);
	}
	
	@RequestMapping(value = "/findSpecial", method = { RequestMethod.POST })
	@ApiOperation(value = "根据服务ID和供应商ID查询专线", notes = "根据服务ID和供应商ID查询专线") 
	 @ApiImplicitParams({
         @ApiImplicitParam(paramType = "query", name = "providerId", value = "专线ID", required = true, dataType = "String"),
         @ApiImplicitParam(paramType = "query", name = "serviceId", value = "专线ID", required = true, dataType = "String")})
	public Result findSpecial(@RequestParam String providerId,@RequestParam String serviceId)
			throws Exception {
		return specialLineService.findSpecial(providerId,serviceId);
	}

}
