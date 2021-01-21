package com.ahdms.billing.controller;

import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.aop.SysLog;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.ChannelInfoMapper;
import com.ahdms.billing.dao.ProviderInfoMapper;
import com.ahdms.billing.dao.ServiceInfoMapper;
import com.ahdms.billing.dao.SpecialLineInfoMapper;
import com.ahdms.billing.dao.UserServiceMapper;
import com.ahdms.billing.model.*;
import com.ahdms.billing.service.UserInfoService;
import com.ahdms.billing.service.UserServiceRecordService;
import com.ahdms.billing.service.UserServiceService;
import com.ahdms.billing.service.WhiteIpService;
import com.ahdms.billing.utils.DateUtils;
import com.ahdms.billing.utils.MD5Utils;
import com.ahdms.billing.utils.SessionUtil;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.UserInfoRspVo;
import com.ahdms.billing.vo.UserServiceDetailVO;
import com.ahdms.billing.vo.UserServiceRecordVO;
import com.alibaba.fastjson.JSON;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping(value = "/api/user")
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private WhiteIpService whiteIpService;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserServiceService userServiceService;

    @Autowired
    private UserServiceRecordService userServiceRecordService;

    @Autowired
    UserServiceMapper userServiceMapper;

    @Autowired
    ServiceInfoMapper serviceInfoMapper;

    @Autowired
    ProviderInfoMapper providerInfoMapper;
    
    @Autowired
    private SpecialLineInfoMapper specialMapper;

    @RequestMapping(value = "/addUser", method = {RequestMethod.POST})
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @SysLog(comment="新增客户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceAccount", value = "服务账户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "servicePwd", value = "服务密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userAccount", value = "前台账户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "whiteIp", value = "白名单列表", required = false, dataType = "String")})
    public Result<String> addUser(HttpServletRequest request,
                                  @RequestParam(required = true) String username, @RequestParam(required = true) String serviceAccount,
                                  @RequestParam(required = false) String servicePwd, @RequestParam(required = false) String userAccount
                                    , @RequestParam(required = false) String whiteIp)
            throws Exception {
        Result<String> result = new Result<>();
        logger.debug(
                "username=" + username + ",serviceAccount=" + serviceAccount + ",servicePwd=" + servicePwd + ",userAccount=" + userAccount);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(UUIDGenerator.getUUID());
        userInfo.setServiceAccount(serviceAccount);
        userInfo.setServicePwd(servicePwd);
        userInfo.setUsername(username);
        userInfo.setUserAccount(userAccount);
        userInfo.setSalt(UUIDGenerator.getUUID());
        userInfo.setStatus(0);
        userInfo.setUserPwd(MD5Utils.stringToMD5("000000"));//默认密码000000
        try {
            result = userInfoService.addUser(userInfo,whiteIp);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryUserById", method = {RequestMethod.POST})
    @ApiOperation(value = "查询用户", notes = "查询用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String")})
    public Result<UserInfoRspVo> queryUserById(HttpServletRequest request,
                                  @RequestParam(required = true) String userId)throws Exception{
        try {
            UserInfo userInfo = userInfoService.getUserByUserId(userId).getData();
            List<String> whiteIps = whiteIpService.selectByUserId(userId);
            String ips = StringUtils.join(whiteIps,",");
            UserInfoRspVo userInfoRspVo = UserInfoRspVo.convert(userInfo,ips);
            return Result.success(userInfoRspVo);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return Result.error(e.getMessage());
        }

    }
    
    @SysLog(comment="修改客户")
    @RequestMapping(value = "/updateUser", method = {RequestMethod.POST})
    @ApiOperation(value = "修改用户", notes = "修改用户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "username", value = "用户名称", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceAccount", value = "服务账户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "servicePwd", value = "服务密码", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userAccount", value = "前台账户", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "whiteIp", value = "白名单列表", required = false, dataType = "String")})
    public Result<UserInfo> update(HttpServletRequest request,
                                   @RequestParam(required = true) String userId,
                                  @RequestParam(required = true) String username, @RequestParam(required = true) String serviceAccount,
                                  @RequestParam(required = false) String servicePwd,
                                   @RequestParam(required = false) String userAccount
                                    ,@RequestParam(required = false) String whiteIp)
            throws Exception {
        Result<UserInfo> result = new Result<>();
        logger.debug(
                "username=" + username + ",serviceAccount=" + serviceAccount + ",servicePwd=" + servicePwd  + ",userAccount=" + userAccount);
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userId);
        userInfo.setServiceAccount(serviceAccount);
        userInfo.setServicePwd(servicePwd);
        userInfo.setUsername(username);
        userInfo.setUserAccount(userAccount);
        try {
            result = userInfoService.updateUserInfoById(userInfo,whiteIp);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
    
    @SysLog(comment="前台客户重置")
    @RequestMapping(value = "/resetUserById", method = {RequestMethod.POST})
    @ApiOperation(value = "前台用户重置", notes = "前台用户重置")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String")})
    public Result<UserInfo> resetUserById(HttpServletRequest request,
                                          @RequestParam(required = true) String userId)throws Exception{
        Result<UserInfo> result = new Result<>();
        try {
            result = userInfoService.resetUserById(userId);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return result;
    }
    
    @SysLog(comment="修改客户状态")
    @RequestMapping(value = "/updateUserStatusById", method = {RequestMethod.POST})
    @ApiOperation(value = "修改用户状态", notes = "修改用户状态")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "status", value = "状态：0启用、1禁用", required = true, dataType = "Int")})
    public Result<UserInfo> updateUserStatusById(HttpServletRequest request,
                                                 @RequestParam(required = true) String userId, @RequestParam(required = true) int status)throws Exception{
        Result<UserInfo> result = new Result<>();
        try {
            result = userInfoService.updateUserInfoById(userId,status);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        return result;
    }
    
    @SysLog(comment="添加客户服务")
    @RequestMapping(value = "/addUserService", method = {RequestMethod.POST})
    @ApiOperation(value = "添加客户服务", notes = "添加客户服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userInfoId", value = "用户id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceId", value = "服务编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "specialId", value = "专线编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "provider", value = "供应商", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceCount", value = "服务次数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tps", value = "并发数", required = true, dataType = "int")})
    public Result<String> addUserService(HttpServletRequest request,
                                              @RequestParam(required = true) String userInfoId, @RequestParam(required = true) String serviceId,
                                         @RequestParam(required = false) String specialId
    , @RequestParam(required = true) int serviceCount, @RequestParam(required = true) String endTime, @RequestParam(required = false) String provider,
    @RequestParam(required = true) int tps)throws Exception{
        
    	ServiceInfo serviceInfo = serviceInfoMapper.selectByPrimaryKey(serviceId);
    	String specialCode = null;
    	if(StringUtils.isNoneBlank(specialId)){
    		SpecialLineInfo specialInfo = specialMapper.selectByPrimaryKey(specialId);
    		specialCode = specialInfo.getCode();
    	}
    	
    	Result<String> result = new Result<>();
        UserService userService=new UserService();
        userService.setId(UUIDGenerator.getUUID());
        userService.setUserInfoId(userInfoId);
        userService.setServiceId(serviceInfo.getServiceEncode());
        userService.setSpecialCode(specialCode);
        userService.setServiceCount(serviceCount);
        userService.setTps(tps);
        userService.setCreateTime(new Date());
        userService.setEndTime(new Date(Long.parseLong(endTime)));
        userService.setProvider(provider);
        userService.setIsExpired(0);
        //添加操作记录
        UserServiceRecord userServiceRecord=new UserServiceRecord();
        userServiceRecord.setUserService(userService.getId());
        userServiceRecord.setCount(serviceCount);
        userServiceRecord.setCreateTime(userService.getCreateTime());
        userServiceRecord.setEndTime(userService.getEndTime());
        userServiceRecord.setOperationUser(SessionUtil.getUserName(request.getSession()));//预留 管理系统管理员
        userServiceRecord.setTps(tps);
        userServiceRecord.setId(UUIDGenerator.getUUID());
        userServiceRecord.setSpecialCode(specialCode);
        userServiceRecord.setProviderId(provider);
        
        int code=userServiceRecordService.addUserServiceRecord(userServiceRecord);//操作记录 数据库操作code
        try {
            result = userServiceService.addUserService(userService);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @SysLog(comment="修改客户服务")
    @RequestMapping(value = "/updateUserService", method = {RequestMethod.POST})
    @ApiOperation(value = "修改客户服务", notes = "修改客户服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userServiceId", value = "用户服务id", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "serviceCount", value = "服务次数", required = true, dataType = "int"),
            @ApiImplicitParam(paramType = "query", name = "endTime", value = "结束时间", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "specialId", value = "专线编码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "provider", value = "供应商", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "tps", value = "并发数", required = true, dataType = "int")})
    public Result<UserService> updateUserService(HttpServletRequest request,
                                                 @RequestParam(required = true) String userServiceId
            , @RequestParam(required = true) int serviceCount, @RequestParam(required = false) String specialId,
            @RequestParam(required = false) String provider,
            @RequestParam(required = true) Long endTime, @RequestParam(required = true) int tps)throws Exception{
        logger.debug(
                "userServiceId"+userServiceId+",serviceCount=" + serviceCount + ",endTime=" + endTime+ ",tps=" + tps);
        Result<UserService> result=userServiceService.getUserServiceById(userServiceId);
        String specialCode = null;
    	if(StringUtils.isNoneBlank(specialId)){
    		SpecialLineInfo specialInfo = specialMapper.selectByPrimaryKey(specialId);
    		specialCode = specialInfo.getCode();
    	}
        
        UserService userService=result.getData();
        userService.setServiceCount(serviceCount);
        userService.setEndTime(new Date(endTime));
        userService.setTps(tps);
        userService.setSpecialCode(specialCode);
        userService.setProvider(provider);
        userService.setIsExpired(0);
        //添加操作记录
        UserServiceRecord userServiceRecord_update=new UserServiceRecord();
        userServiceRecord_update.setUserService(userService.getId());
        userServiceRecord_update.setCount(serviceCount);
        userServiceRecord_update.setCreateTime(new Date());
        userServiceRecord_update.setEndTime(new Date(endTime));
        userServiceRecord_update.setOperationUser(SessionUtil.getUserName(request.getSession()));//预留 管理系统管理员
        userServiceRecord_update.setTps(tps);
        userServiceRecord_update.setId(UUIDGenerator.getUUID());
        userServiceRecord_update.setProviderId(provider);
        userServiceRecord_update.setSpecialCode(specialCode);
        int code=userServiceRecordService.addUserServiceRecord(userServiceRecord_update);//操作记录 数据库操作code
        try {
            result = userServiceService.updateUserServiceById(userService);
            return result;
        } catch (Exception e) {
            result=Result.error(e.getMessage());
            logger.error(e.getMessage(), e);
        }

        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    
    @RequestMapping(value = "/queryUserService", method = {RequestMethod.POST})
    @ApiOperation(value = "查询客户服务", notes = "查询客户服务")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = false, dataType = "String")})
    public Result<List<UserServiceDetailVO>> queryUserServiceByUserId(HttpServletRequest request,
                                                   @RequestParam(required = false) String userId
                                                   ,@RequestParam(required = false) String typeId)throws Exception{
        Result<List<UserServiceDetailVO>> queryServiceDetailByUserId = userServiceService.queryServiceDetailByUserIdAndType(userId, typeId);
        	
        return queryServiceDetailByUserId;
    }
    

    @RequestMapping(value = "/queryUserServiceRecordByUserId", method = {RequestMethod.POST})
    @ApiOperation(value = "查询用户服务操作记录", notes = "查询用户服务操作记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户id", required = true, dataType = "String")})
    public Result<List<UserServiceRecordVO>> queryUserServiceRecordByUserId(HttpServletRequest request,
                                                                     @RequestParam(required = true) String userId)throws Exception{
        Result<List<UserServiceRecordVO>> result = new Result<>();
        try {
            result = userServiceRecordService.selectByUserId(userId);
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryUserList", method = {RequestMethod.POST})
    @ApiOperation(value = "查询客戶列表(包含搜索)", notes = "查询客戶列表(包含搜索)")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页页码", required = true, dataType = "Int"),
            @ApiImplicitParam(paramType = "query", name = "size", value = "分页大小", required = true, dataType = "Int"),
            @ApiImplicitParam(paramType = "query", name = "serviceType", value = "服务类型 1:身份认证服务 2:电子认证服务", required = false, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "userName", value = "客户名称", required = false, dataType = "String")})
    public Result<Object> queryUserList(HttpServletRequest request,
                                  @RequestParam(required = true) int page,
                                  @RequestParam(required = true) int size,
                                  @RequestParam(required = false) String serviceType,
                                  @RequestParam(required = false) String userName)
            throws Exception {
        Result<Object> result = new Result<>();
        try {
            result = userInfoService.selectLikeUserName(page, size, userName);

            result.setCode(0);
            result.setMessage("查询成功");

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
}
