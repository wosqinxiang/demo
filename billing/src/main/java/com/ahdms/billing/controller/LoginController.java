package com.ahdms.billing.controller;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserService;
import com.ahdms.billing.model.UserServiceRecord;
import com.ahdms.billing.service.UserInfoService;
import com.ahdms.billing.service.UserServiceRecordService;
import com.ahdms.billing.service.UserServiceService;
import com.ahdms.billing.service.WhiteIpService;
import com.ahdms.billing.utils.MD5Utils;
import com.ahdms.billing.utils.Util;
import com.ahdms.billing.vo.UserInfoRspVo;
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
import javax.servlet.http.HttpSession;

import java.util.List;

@RestController
@RequestMapping(value={"/api/"})
public class LoginController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);

    @Autowired
    private JedisCluster jediscluster;

    @Autowired
    private UserInfoService userInfoService;

    @Autowired
    private UserServiceService userServiceService;

    @Autowired
    private UserServiceRecordService userServiceRecordService;

    @Autowired
    private WhiteIpService whiteIpService;

    @RequestMapping(value = "/login", method = {RequestMethod.POST})
    @ApiOperation(value = "用户登录", notes = "用户登录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "user_account", value = "用户帐号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "user_pwd", value = "用户密码", required = true, dataType = "String")})

    public Result<UserInfo> queryUserById(HttpServletRequest request,
                                          @RequestParam(required = true) String username,
                                          @RequestParam(required = true) String password)throws Exception{
        Result<UserInfo> result = new Result<>();
        try {

            UserInfo userInfo = userInfoService.getUserByUsername(username).getData();

            if (userInfo == null) {
                result.setCode(401);
                result.setMessage("帐号不存在");
                return result;

            } else if (userInfo.getStatus() == 1) {
                result.setCode(401);
                result.setMessage("账号被锁定，请联系客服解锁！");
                return result;

            } else if (userInfo.getUserPwd().equals(MD5Utils.stringToMD5(password))) {
                jediscluster.set(username, "0");
//                userInfo.setStatus(0);
//                userInfoService.updateUserInfoById(userInfo);

                result.setCode(0);
                result.setMessage("登录成功");
                result.setData(userInfo);
                return result;

            } else {

                jediscluster.incr(username);
                if(Integer.parseInt(jediscluster.get(username)) > 4) {
//                    userInfo.setStatus(1);
                    userInfoService.updateUserInfoById(userInfo.getId(),1);
                }
                result.setCode(401);
                result.setMessage("密码错误");
                return result;
            }

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }


    @RequestMapping(value = "/updatePassword", method = {RequestMethod.POST})
    @ApiOperation(value = "修改用户密码", notes = "修改用户密码")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "user_account", value = "用户帐号", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "oldPassword", value = "用户原来的密码", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "newPassword", value = "用户设置的新密码", required = true, dataType = "String")})

    public Result<UserInfo> queryUserById(HttpServletRequest request,
                                          @RequestParam(required = true) String username,
                                          @RequestParam(required = true) String oldPassword,
                                          @RequestParam(required = true) String newPassword)throws Exception{
        Result<UserInfo> result = new Result<>();
        try {

            if (Util.isEmpty(oldPassword)|| Util.isEmpty(newPassword)) {
                result.setCode(1);
                result.setMessage("参数不可为空");

                return result;
            }

            UserInfo userInfo = userInfoService.getUserByUsername(username).getData();

            if (MD5Utils.stringToMD5(oldPassword).equals(userInfo.getUserPwd())) {

                //redis密码输入错误计数器清零
                jediscluster.set(userInfo.getUsername(), "0");

                userInfo.setUserPwd(MD5Utils.stringToMD5(newPassword));
                userInfoService.updateUserInfoById(userInfo);
                result.setCode(0);
                result.setMessage("密码修改成功");
                result.setData(userInfo);
            } else {
                result.setCode(1);
                result.setMessage("旧密码输入错误");
            }

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryServiceDetail", method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务明细", notes = "查询服务明细")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "typeId", value = "服务类型ID(1:身份认证服务 2:电子认证服务)", required = true, dataType = "String")})

    public Result<List<UserService>> queryServiceDetail(HttpServletRequest request,
                                                        @RequestParam(required = true) String userId,
                                                        @RequestParam(required = true) String typeId)throws Exception{
        Result<List<UserService>> result = new Result<>();
        try {
            result = userServiceService.queryServiceDetailByUserId(userId, typeId);
            result.setCode(0);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryServiceHistoryRecord", method = {RequestMethod.POST})
    @ApiOperation(value = "查询服务历史记录", notes = "查询服务历史记录")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "String")})
    public Result<List<UserServiceRecord>> queryServiceHistoryRecord(HttpServletRequest request,
                                                                     @RequestParam(required = true) String userId)throws Exception{
        Result<List<UserServiceRecord>> result = new Result<>();
        try {
            result = userServiceRecordService.queryServiceHistoryRecord(userId);
            result.setCode(0);
            result.setMessage("查询成功");
        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/queryUserById", method = {RequestMethod.POST})
    @ApiOperation(value = "查询帐号信息", notes = "查询帐号信息")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "userId", value = "用户ID", required = true, dataType = "String")})

    public Result<UserInfoRspVo> queryServiceDetail(HttpServletRequest request,
                                                        @RequestParam(required = true) String userId)throws Exception{
        Result<UserInfoRspVo> result = new Result<>();
        try {
            UserInfo userInfo = userInfoService.getUserByUserId(userId).getData();
            List<String> whiteIps = whiteIpService.selectByUserId(userId);
            String ips = StringUtils.join(whiteIps,",");
            UserInfoRspVo userInfoRspVo = UserInfoRspVo.convert(userInfo,ips);
            return Result.success(userInfoRspVo);
        } catch (Exception e) {
            result.setMessage("查询失败");
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }

    @RequestMapping(value = "/userLogout", method = {RequestMethod.POST})
    @ApiOperation(value = "退出登录", notes = "退出登录")
    public Result<Object> logout(HttpServletRequest request)throws Exception{
        Result<Object> result = new Result<>();
        try {
            HttpSession session = request.getSession();
            session.removeAttribute("user");
            result.setCode(0);
            result.setMessage("退出成功");

        } catch (Exception e) {
            result.setMessage(e.getMessage());
            logger.error(e.getMessage(), e);
        }
        logger.debug("接口返回结果Json.hb={}", JSON.toJSONString(result));
        return result;
    }
}
