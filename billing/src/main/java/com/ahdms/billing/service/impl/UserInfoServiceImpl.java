package com.ahdms.billing.service.impl;


import com.ahdms.billing.common.GridModel;
import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserInfoForService;
import com.ahdms.billing.model.WhiteIp;
import com.ahdms.billing.service.UserInfoService;
import com.ahdms.billing.service.WhiteIpService;
import com.ahdms.billing.utils.MD5Utils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import org.apache.commons.lang3.StringUtils;
import redis.clients.jedis.JedisCluster;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Service
public class UserInfoServiceImpl implements UserInfoService {

    private final static Logger logger = LoggerFactory.getLogger(UserInfoServiceImpl.class);
    @Autowired
    UserInfoMapper userInfoMapper;
    
    @Autowired
	private JedisCluster jedisCluster;

    @Autowired
    private WhiteIpService whiteIpService;

    @Override
    public Result<String> addUser(UserInfo userInfo,String whiteIp) {
    	UserInfo _userInfo = userInfoMapper.selectByUsernameOrUserAccountOrServiceAccount(userInfo.getUsername(),userInfo.getUserAccount(),userInfo.getServiceAccount());
        if(_userInfo != null){
        	if(_userInfo.getUsername().equals(userInfo.getUsername())){
        		return Result.error("用户名已存在！");
        	}
        	if(_userInfo.getServiceAccount().equals(userInfo.getServiceAccount())){
        		return Result.error("用户服务账号已存在！");
        	}
        	if(_userInfo.getUserAccount().equals(userInfo.getUserAccount())){
        		return Result.error("用户前台账号已存在！");
        	}
        }
        try {
            userInfoMapper.insertSelective(userInfo);
            whiteIpService.addUserWhiteIps(userInfo.getId(),whiteIp);
            return Result.ok("添加用户成功");
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return Result.error(e.getMessage());
        }
    }

    @Override
    public Result<UserInfo> getUserByUserId(String userId) {
        Result<UserInfo> result=new Result<>();

        UserInfo userInfo= userInfoMapper.selectByPrimaryKey(userId);

        result.setData(userInfo);

        return result;
    }

    @Override
    public void updateUserInfoById(UserInfo userInfo) {
        userInfoMapper.updateByPrimaryKeySelective(userInfo);
        UserInfo user = userInfoMapper.selectByPrimaryKey(userInfo.getId());
        // 服务信息添加至redis
        String s =  jedisCluster.hget(user.getId(), "USABLE");
        if(null != s) {
            jedisCluster.hset(user.getId(), "USABLE", user.getStatus().toString());
        }
    }

    @Override
    public Result<UserInfo> updateUserInfoById(UserInfo userInfo,String whiteIp) {
        Result<UserInfo> result=new Result<>();
        try {
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            whiteIpService.updateUserWhiteIps(userInfo.getId(),whiteIp);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result.setMessage(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        UserInfo user = userInfoMapper.selectByPrimaryKey(userInfo.getId());
		// 服务信息添加至redis  
		String s =  jedisCluster.hget(user.getId(), "USABLE");
		if(null != s) {
			jedisCluster.hset(user.getId(), "USABLE", user.getStatus().toString());
		}
        return result;
    }

    @Override
    public Result<UserInfo> getUserByUsernameAndPassword(String username, String password) {
        Result<UserInfo> result = new Result<>();
        UserInfo userInfo = userInfoMapper.selectByUsernameAndPassword(username, password);
        result.setData(userInfo);
        return result;
   }

    @Override
    public Result<UserInfo> getUserByUsername(String username) {
        Result<UserInfo> result = new Result<>();
        UserInfo userInfo = userInfoMapper.selectByUsername(username);
        result.setData(userInfo);
        return result;
    }

    @Override
    public Result<List<UserInfo>> findAll() {
        Result<List<UserInfo>> result;
        try {
            List<UserInfo> userInfos=userInfoMapper.findAll();
            result=Result.success(userInfos);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            result=Result.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return result;
    }

    @Override
    public List<UserInfoForService> queryReportAll() {
        List<UserInfoForService> userInfos = null;
        try {
            userInfos=userInfoMapper.queryAll();
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
        return userInfos;
    }

    @Override
    public Result<Object> selectLikeUserName(int page, int size,String userName) {
        GridModel<UserInfoForService> gridModel = new GridModel<UserInfoForService>();
        PageHelper.startPage(page, size);
        List<UserInfoForService> list = userInfoMapper.selectLikeUserName(userName);
        PageInfo<UserInfoForService> pageInfo = new PageInfo<>(list);
        gridModel.setPage(pageInfo.getPageNum());
        gridModel.setRecords((int) pageInfo.getTotal());
        gridModel.setTotal(pageInfo.getPages());
        gridModel.setRows(list);
        return Result.success(gridModel);
    }

    @Override
    public Result<UserInfo> resetUserById(String userId) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo != null){
            //redis密码输入错误计数器清零
            jedisCluster.set(userInfo.getUsername(), "0");
            userInfo.setUserPwd(MD5Utils.stringToMD5("000000"));//重置密码为000000
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            return Result.success(userInfo);
        }
        return Result.error("查询不到用户信息");
    }

    @Override
    public Result<UserInfo> updateUserInfoById(String userId, int status) {
        UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userId);
        if(userInfo != null){
            //redis密码输入错误计数器清零
            jedisCluster.set(userInfo.getUsername(), "0");
            userInfo.setStatus(status);
            userInfoMapper.updateByPrimaryKeySelective(userInfo);
            return Result.success(userInfo);
        }
        return Result.error("查询不到用户信息");
    }
}
