package com.ahdms.billing.task;

import java.time.LocalTime;
import java.util.List;

import com.ahdms.billing.config.omp.RedisKey;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.ChannelInfoEnum;
import com.ahdms.billing.config.redis.JedisClusterUtils;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.dao.UserServiceMapper;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

/**
 * 数据库同步redis中用户服务次数定时任务
 * 
 * @author Administrator
 *
 */
@Component
public class SyncCountJob implements SimpleJob {
	private final static Logger logger = LoggerFactory.getLogger(SyncCountJob.class);

	@Autowired
	private JedisClusterUtils jedisCluster;

	@Autowired
	private UserServiceMapper userServiceMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public void execute(ShardingContext shardingContext) {
		logger.info("同步计费次数定时任务执行..."+LocalTime.now());
		// 查询所有的用户服务
		List<UserService> findAll = userServiceMapper.findAll();
		for (UserService userService : findAll) {
			try {
				if (userService.getIsExpired() != BasicConstants.SERVICE_IS_EXPIRED) {
					String serviceId = userService.getServiceId();
					String userInfoId = userService.getUserInfoId();
					UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userInfoId);
					if(userInfo != null){
//						String key = userInfo.getServiceAccount();
						String hgetKey = RedisKey.getHashKeys(userService.getUserInfoId());
						if (StringUtils.isNotBlank(hgetKey)) {
//							String redisKeyFiled = "JF:" + key + ":" + serviceId;
							String fieldPrekey = new StringBuffer(RedisKey.JF).append(serviceId).toString();
							String count = jedisCluster.hget(hgetKey, RedisKey.getFieldCount(fieldPrekey)); // 查询次数
							if(StringUtils.isBlank(count)){
								userService.setServiceCount(0);
							}else{
								userService.setServiceCount(Integer.parseInt(count));
							}
							userServiceMapper.updateByPrimaryKeySelective(userService); // 修改数据库数据
						}
					}
				}
			} catch (Exception e) {
				logger.error("同步计费次数定时任务执行出错..."+e.getMessage());
			}
		}
	}
	
}
