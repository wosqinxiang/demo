package com.ahdms.billing.service.impl;

import java.util.List;
import java.util.Map;

import com.ahdms.billing.config.omp.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.ahdms.billing.common.Result;
import com.ahdms.billing.dao.SpecialLineInfoMapper;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.dao.UserServiceMapper;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserService;
import com.ahdms.billing.service.UserServiceService;
import com.ahdms.billing.vo.SpecialLineVO;
import com.ahdms.billing.vo.UserServiceDetailVO;

import redis.clients.jedis.JedisCluster;

@Service
public class UserServiceServiceImpl implements UserServiceService {
	private final static Logger logger = LoggerFactory.getLogger(UserServiceServiceImpl.class);
	@Autowired
	UserServiceMapper userServiceMapper;

	@Autowired
	private JedisCluster jedisCluster;

	@Autowired
	private UserInfoMapper userInfoDao;
	
	@Autowired
	private SpecialLineInfoMapper specialMapper;
	
	private static String JF = "JF:";

	@Override
	public Result<String> addUserService(UserService userService) {
		Result<String> result = new Result<>();
		try {
			//根据用户ID,渠道编码,服务编码 判断此服务是否已有
			UserService _userService = userServiceMapper.selectByUserIdAndServiceId(userService.getUserInfoId(),userService.getServiceId());
			if(null != _userService){
				_userService.setEndTime(userService.getEndTime());
				_userService.setIsExpired(0);
				_userService.setSpecialCode(userService.getSpecialCode());
				_userService.setProvider(userService.getProvider());
				_userService.setServiceCount(_userService.getServiceCount() + userService.getServiceCount());
				_userService.setTps(userService.getTps());
				userServiceMapper.updateByPrimaryKeySelective(_userService);
			}else{
				userService.setIsExpired(0);
				userServiceMapper.insert(userService);
			}

			insertToRedis(userService);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	public void insertToRedis(UserService userService){
		UserInfo user = userInfoDao.selectByPrimaryKey(userService.getUserInfoId());

		String hgetKey = RedisKey.getHashKeys(userService.getUserInfoId());
		// 服务信息添加至redis
		String fieldPrekey = new StringBuffer(JF).append(userService.getServiceId()).toString();
		Map<String, String> results = jedisCluster.hgetAll(hgetKey);
		results.put(RedisKey.getFieldUsable(fieldPrekey), "0");
		results.put(RedisKey.getFieldLimit(fieldPrekey), userService.getTps().toString());

		if(StringUtils.isNotBlank(userService.getSpecialCode())){
			results.put(RedisKey.getFieldSupplier(fieldPrekey), userService.getSpecialCode());
		}
		results.put(RedisKey.USABLE, user.getStatus().toString());
		//添加个key 判断是由中台添加的订单
//		results.put(RedisKey.OMP,RedisKey.OMP);

		String counts = jedisCluster.hget(hgetKey, RedisKey.getFieldCount(fieldPrekey));
		if (null != counts) {
			results.put(RedisKey.getFieldCount(fieldPrekey), Integer.toString(Integer.parseInt(counts) + userService.getServiceCount()));
		} else {
			results.put(RedisKey.getFieldCount(fieldPrekey), userService.getServiceCount().toString());
		}

		jedisCluster.hmset(hgetKey, results);
	}

	private String getKey(UserService userService) {
		StringBuffer key = new StringBuffer(); // 拼装redis key
		key.append(JF).append(userService.getServiceId());
		return key.toString();
	}

	@Override
	public Result<UserService> getUserServiceById(String serviceId) {
		Result<UserService> result = new Result<>();
		UserService userInfo = userServiceMapper.selectByPrimaryKey(serviceId);
		result.setData(userInfo);
		return result;
	}

	@Override
	public Result<UserService> updateUserServiceById(UserService userService) {
		Result<UserService> result = new Result<>();
		try {

			insertToRedis(userService);
			
			UserService selectByPrimaryKey = userServiceMapper.selectByPrimaryKey(userService.getId());
			userService.setServiceCount(selectByPrimaryKey.getServiceCount()+userService.getServiceCount());
			userServiceMapper.updateByPrimaryKey(userService);

		} catch (Exception e) {
			logger.error(e.getMessage(), e);
			result.setMessage(e.getMessage());
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
		}
		return result;
	}

	@Override
	public Result<List<UserService>> selectByUserId(String userId) {
		Result<List<UserService>> result = new Result<>();
		List<UserService> userInfo = userServiceMapper.selectByUserId(userId);
		result.setData(userInfo);
		return result;
	}

	@Override
	public Result<List<UserService>> queryServiceDetailByUserId(String userId, String typeId) {
		Result<List<UserService>> result = new Result<>();
		List<UserService> userInfo = userServiceMapper.queryServiceDetailByUserId(userId, typeId);
		result.setData(userInfo);
		return result;
	}

	@Override
	public Result<Object> findAll() {
		Result<Object> result = new Result<>();
		List<UserService> list = userServiceMapper.findAll();
		return Result.success(list);
	}

	private String getKey(UserInfo user, UserService userService) {
		StringBuffer key = new StringBuffer(); // 拼装redis key
		key.append(JF);
		key.append(user.getServiceAccount());
		key.append(":");
		key.append(userService.getServiceId());
//		if (userService.getChannelId().equals(ChannelInfoEnum.BOX.getCode())) {
//			key.append(JF);
//			key.append(user.getBoxId());
//			key.append(":");
//			key.append(userService.getServiceId());
//		} else if (userService.getChannelId().equals(ChannelInfoEnum.APP_SDK.getCode())
//				|| userService.getChannelId().equals(ChannelInfoEnum.WX_SDK.getCode())) {
//			key.append(JF);
//			key.append(user.getSdkEncode());
//			key.append(":");
//			key.append(userService.getServiceId());
//		} else {
//			key.append(JF);
//			key.append(user.getServiceAccount());
//			key.append(":");
//			key.append(userService.getServiceId());
//		}
		logger.info("-----计费参数key：" + key.toString());
		return key.toString();

	}

	@Override
	public Result<List<UserServiceDetailVO>> queryServiceDetailByUserIdAndType(String userId, String typeId) {
		List<UserServiceDetailVO> list = userServiceMapper.queryByUserIdAndType(userId,typeId);
		for (UserServiceDetailVO vo : list) {
			String specialName = vo.getSpecialName();
			SpecialLineVO sl = specialMapper.selectByCode(specialName);
			if(null != sl){
				vo.setSpecialName(sl.getName());
				vo.setProviderName(sl.getProviderName());
			}
		}
		return Result.ok(list);
	}

}
