package com.ahdms.billing.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.KeyInfo;
import com.ahdms.billing.service.KeyInfoService;

@RestController
@RequestMapping(value={"/api/keyInfo"},method={RequestMethod.GET,RequestMethod.POST})
public class KeyInfoController {
	
	@Autowired
	private KeyInfoService keyInfoService;
	
	
	@RequestMapping("add")
	@SysLog(comment="添加SDK密钥")
	public Result add(@RequestBody KeyInfo keyInfo,HttpServletRequest request){
//		String identity = SessionUtils.getAuthUserIdentity(request);
//		if (null == opinfoService.queryByIdentity(identity)) {
//			return CtidResult.error("当前登录用户已被删除。");
//		}
		Result result = keyInfoService.add(keyInfo);
		return result;
	}
	
	@RequestMapping("selectAll")
	public Result selectAll(HttpServletRequest request){
//		String identity = SessionUtils.getAuthUserIdentity(request);
//		if (null == opinfoService.queryByIdentity(identity)) {
//			return CtidResult.error("当前登录用户已被删除。");
//		}
		return keyInfoService.selectAll();
	}
	
	@RequestMapping("update")
	@SysLog(comment="修改SDK密钥数据")
	public Result update(@RequestBody KeyInfo keyInfo,HttpServletRequest request){
//		String identity = SessionUtils.getAuthUserIdentity(request);
//		if (null == opinfoService.queryByIdentity(identity)) {
//			return CtidResult.error("当前登录用户已被删除。");
//		}
		Result result = keyInfoService.update(keyInfo);
		return result;
	}
	
	@RequestMapping("delete")
	@SysLog(comment="删除SDK密钥数据")
	public Result delete(@RequestParam int id,HttpServletRequest request){
//		String identity = SessionUtils.getAuthUserIdentity(request);
//		if (null == opinfoService.queryByIdentity(identity)) {
//			return CtidResult.error("当前登录用户已被删除。");
//		}
		Result result = keyInfoService.delete(id);
		return result;
	}

}
