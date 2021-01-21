/**
 * Created on 2019年3月13日 by luotao
 */
package com.ahdms.billing.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.HttpResponseBody;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.config.security.AuthenctiationSuccessHandler;
import com.ahdms.billing.model.AdminUserInfo;
import com.ahdms.billing.service.SysUserService;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.utils.Util;
import com.ahdms.billing.vo.AdminUserInfoVO;
import com.alibaba.fastjson.JSON;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
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
@RequestMapping(value = "/api/AdminUserInfoLogin")
public class AdminUserInfoLoginController {
	private static final Logger logger = LoggerFactory.getLogger(AdminUserInfoLoginController.class);

	@Autowired
	private SysUserService sysUserService; 
	@Autowired
	AuthenticationProvider authenticationProvider;
	@Autowired
	AuthenctiationSuccessHandler authenctiationSuccessHandler;

	@RequestMapping(value = "/userNameCheck", method = { RequestMethod.GET })
	@ApiOperation(value = "用户名查重接口", notes = "根据userName")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "userName", value = "用户名查重", required = true, dataType = "string") })
	public HttpResponseBody<String> userNameCheck(HttpServletRequest request,
			@RequestParam(required = true) String userName) {
		HttpResponseBody<String> hb = new HttpResponseBody<String>();
		logger.debug("用户名查重接口userName=" + userName);
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		hb.setMessage("用户名已存在");
		try {
			boolean flag = sysUserService.queryAdminUserInfoNameExist(userName);
			if (!flag) {
				hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
				hb.setMessage("");
			}
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			hb.setMessage(e.getMessage());
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	}


	@GetMapping("/logout")
	@SysLog(comment = "用户退出登录")
	public void logout() {
		authenctiationSuccessHandler.loginout();
		SecurityContextHolder.getContext().setAuthentication(null);
	}

	@RequestMapping(value = "/login", method = { RequestMethod.POST })
	@ApiOperation(value = "用户登录", notes = "用户登录") 
//	    @SysLog(comment = "用户登录")
	public HttpResponseBody<String> login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = true) String userName, @RequestParam(required = true) String password)
					throws Exception { 

		logger.debug("用户登录userName=" + userName + ",password=" + password);
		HttpResponseBody<String> hb = new HttpResponseBody<>();
		Authentication authentication = null;
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);

		int i = sysUserService.queryAdminIsLock(userName);
		if( i > 0){
			hb.setMessage("账户已被锁定，请联系超级管理员解锁！"); 
			return hb;
		} 
		try {
			authentication = authenticationProvider
					.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
			SecurityContextHolder.getContext().setAuthentication(authentication);
			authenctiationSuccessHandler.onAuthenticationSuccess(request, response, authentication);
			hb.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage());
			hb.setMessage("用户登录失败！"); 
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb)); 
		return hb;
	}


	@RequestMapping(value = "/showSysUser", method = { RequestMethod.GET })
	@ApiOperation(value = "查看用户详情", notes = "查看用户详情")
	public HttpResponseBody<AdminUserInfoVO> showSysUser(HttpServletRequest request,
			@RequestParam(required = true) String id) {
		HttpResponseBody<AdminUserInfoVO> hi = new HttpResponseBody<AdminUserInfoVO>();
		logger.debug("查看用户详情id=" + id);
		try {
			AdminUserInfoVO aisv = sysUserService.getUserByUserId(id);
			hi.setData(aisv);
			hi.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			hi.setMessage(e.getMessage());
			hi.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hi));
		return hi;
	}

	@RequestMapping(value = "/SysUserList", method = { RequestMethod.GET })
	@ApiOperation(value = "用户管理列表", notes = "分页列表")
	public GridModel<AdminUserInfoVO> SysUserList(HttpServletRequest request,
			@RequestParam(required = false, defaultValue = "0") int page,
			@RequestParam(required = false, defaultValue = "10") int rows) {
		Map<String, Object> map = new HashMap<String, Object>();

		return sysUserService.querySysUserPageList(null, page, rows, null);
	}

	@RequestMapping(value = "/addUser", method = { RequestMethod.POST })
	@ApiOperation(value = "新增用户", notes = "新增用户")
	@SysLog(comment = "新增用户")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "userName", value = "用户名", required = true, dataType = "String"), 
		@ApiImplicitParam(paramType = "query", name = "authId", value = "权限类型：1-超级管理员 2-普通管理员", required = true, dataType = "Int")})
	public Result<String> addUser(HttpServletRequest request,
			@RequestParam(required = true) String userName, @RequestParam(required = true) Integer authId )
					throws Exception {
		Result<String> hb = new Result<>();
		logger.debug(
				"userName=" + userName + ",authId=" + authId );
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		AdminUserInfo adminUserInfo = new AdminUserInfo();
		adminUserInfo.setUsername(userName);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		adminUserInfo.setPassword(encoder.encode("000000")); //默认密码
		adminUserInfo.setSalt(UUIDGenerator.getUUID());
		adminUserInfo.setIsLock(0);
		adminUserInfo.setAuthId(authId); 
		adminUserInfo.setId(UUIDGenerator.getUUID());
		String  role =  "";
		if(authId.equals(1)) {
			role = BasicConstants.ROLE_SUPER_ADMIN ;
		}else {
			role = BasicConstants.ROLE_NORMAL_ADMIN ;
		}
		try { 
			hb = sysUserService.createUser(adminUserInfo, role);
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	}

	@RequestMapping(value = "/delUser", method = { RequestMethod.POST })
	@ApiOperation(value = "删除用户", notes = "删除用户")
	@SysLog(comment = "删除用户")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "userId", value = "userId", required = true, dataType = "String") 
	})
	public Result<String> delUser(HttpServletRequest request,
			@RequestParam(required = false) String userId )
					throws Exception {
		Result<String> hb = new Result<>(); 
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		if (Util.isEmpty(userId)) {
			hb.setMessage("userId is null");
			return hb;
		}  
		AdminUserInfo admin = sysUserService.getLoginUser();
		if(admin.getId().equals(userId)) {
			hb.setMessage("不能删除用户本身。");
			return hb;
		}
		try {  
			return sysUserService.delUser(userId);
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	} 


	@RequestMapping(value = "/unlockUser", method = { RequestMethod.POST })
	@ApiOperation(value = "解锁用户", notes = "解锁用户")
	@SysLog(comment = "解锁用户")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "userId", value = "userId", required = true, dataType = "String") 
	})
	public Result<String> unlockUser(HttpServletRequest request,
			@RequestParam(required = false) String userId )
					throws Exception {
		Result<String> hb = new Result<>(); 
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		if (Util.isEmpty(userId)) {
			hb.setMessage("userId is null");
			return hb;
		}   
		try {  
			return sysUserService.unlockUser(userId);
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	} 

	@RequestMapping(value = "/EditUserPassWord", method = { RequestMethod.POST })
	@ApiOperation(value = "修改用户密码", notes = "修改用户密码")
	@SysLog(comment = "修改用户密码")
	@ApiImplicitParams({
		@ApiImplicitParam(paramType = "query", name = "oldPassword", value = "oldPassword", required = true, dataType = "String") ,
		@ApiImplicitParam(paramType = "query", name = "newPassword", value = "newPassword", required = true, dataType = "String") 
	})
	public Result<String> EditUserPassWord(HttpServletRequest request,
			@RequestParam(required = true) String oldPassword,
			@RequestParam(required = true) String newPassword )
					throws Exception {
		Result<String> hb = new Result<>(); 
		hb.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		if (Util.isEmpty(oldPassword)|| Util.isEmpty(newPassword)) {
			hb.setMessage("参数不可为空！");
			return hb;
		}   
		try {  
			return sysUserService.EditUserPassWord(oldPassword,newPassword);
		} catch (Exception e) {
			hb.setMessage(e.getMessage());
			logger.error(e.getMessage(), e);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hb));
		return hb;
	} 

	@RequestMapping(value = "/showLoginUser", method = { RequestMethod.GET })
	@ApiOperation(value = "查看当前登录用户详情", notes = "查看当前登录用户详情")
	public Result<AdminUserInfoVO> showLoginUser() {
		Result<AdminUserInfoVO> hi = new Result<AdminUserInfoVO>();
//		logger.debug("查看用户详情id=" + id);
		try {
			AdminUserInfoVO aisv = sysUserService.showLoginUser();
			hi.setData(aisv);
			hi.setCode(BasicConstants.INTRETURN_CODE_SUCCESS);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			hi.setMessage(e.getMessage());
			hi.setCode(BasicConstants.INTRETURN_CODE_ERROR);
		}
		logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(hi));
		return hi;
	}

}
