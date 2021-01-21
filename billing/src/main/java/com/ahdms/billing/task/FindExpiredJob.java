package com.ahdms.billing.task;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.billing.common.BasicConstants;
import com.ahdms.billing.common.ChannelInfoEnum;
import com.ahdms.billing.common.JFResultEnum;
import com.ahdms.billing.config.redis.JedisClusterUtils;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.dao.UserServiceMapper;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.model.UserService;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;
/**
 * 查询用户购买服务过期定时任务
 * @author Administrator
 *
 */
@Component
public class FindExpiredJob implements SimpleJob{
	private final static Logger logger = LoggerFactory.getLogger(FindExpiredJob.class);
	
	@Autowired
	private JedisClusterUtils jedisCluster;
	
	@Autowired
	private UserServiceMapper userServiceMapper;
	
	@Autowired
	private UserInfoMapper userInfoMapper;

	@Override
	public void execute(ShardingContext shardingContext) {
		logger.info("查询服务过期定时任务执行..."+LocalTime.now());
		//查询所有的用户服务
		List<UserService> findAll = userServiceMapper.findAll();
		Date nowDate = new Date();
		for (UserService userService : findAll) {
			try {
				if(userService.getIsExpired() != BasicConstants.SERVICE_IS_EXPIRED){
					Date endTime = userService.getEndTime();
					if(nowDate.after(endTime)){ //已过期
						//修改redis对应的数据(服务次数清零)
						String userInfoId = userService.getUserInfoId();
						UserInfo userInfo = userInfoMapper.selectByPrimaryKey(userInfoId);
						if(userInfo != null){
							String key = userInfo.getServiceAccount();
							String serviceId = userService.getServiceId();
							if(StringUtils.isNotBlank(key)){
								String redisKeyFiled = "JF:"+key+":"+serviceId; //是否可用
								jedisCluster.hset(userInfoId, redisKeyFiled+":USABLE","5"); //修改为 已过期
								jedisCluster.hset(userInfoId, redisKeyFiled+":COUNT", "0");
							}
							userService.setServiceCount(0);
							userService.setIsExpired(BasicConstants.SERVICE_IS_EXPIRED); //设置为已过期
							userServiceMapper.updateByPrimaryKeySelective(userService); //修改数据库数据
						}
					}
				}
			} catch (Exception e) {
				logger.error("查询服务过期定时任务执行出错..."+e.getMessage());
			}
		}
	}
	
}
