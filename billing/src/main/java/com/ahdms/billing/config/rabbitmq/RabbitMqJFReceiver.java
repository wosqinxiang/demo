package com.ahdms.billing.config.rabbitmq;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ahdms.billing.common.ChannelInfoEnum;
import com.ahdms.billing.common.JFResultEnum;
import com.ahdms.billing.dao.ServiceLogMapper;
import com.ahdms.billing.dao.UserInfoMapper;
import com.ahdms.billing.model.ServiceLog;
import com.ahdms.billing.model.UserInfo;
import com.ahdms.billing.utils.UUIDGenerator;
import com.ahdms.billing.vo.ServiceLogVO;
import com.alibaba.fastjson.JSON;

import redis.clients.jedis.JedisCluster;

@Component
public class RabbitMqJFReceiver {
	
	private static final Logger logger = LoggerFactory.getLogger(RabbitMqJFReceiver.class);

	@Autowired
	private ServiceLogMapper serviceLogMapper;

	@Autowired
	private UserInfoMapper userInfoMapper;

	@Autowired
	private JedisCluster jedisCluster;

	@RabbitListener(queues = "JF_LOG", containerFactory="jfFactory")
	public void process(String jsonStr) {

		try {
			ServiceLogVO parse = JSON.parseObject(jsonStr, ServiceLogVO.class);
			ServiceLog serviceLog = new ServiceLog();
			serviceLog.setId(UUIDGenerator.getUUID());
			serviceLog.setMessage(parse.getMessage());
			serviceLog.setChannelEncode(parse.getChannelMode());
			serviceLog.setOperationtime(new Date());
			serviceLog.setResult(Integer.parseInt(parse.getResult()));
			serviceLog.setServiceEncode(parse.getSerivceMode());
			serviceLog.setTypeId(parse.getServiceType());
			serviceLog.setSpecialCode(parse.getSpecialCode());
			serviceLog.setSerialNum(parse.getSerialNum());
			serviceLog.setComment(parse.getComment());
			
			//根据渠道编码，服务编码， 查询客户名称
			UserInfo userInfo = userInfoMapper.selectByServiceAccount(parse.getKey());
			
			if(null != userInfo){
				serviceLog.setUsername(userInfo.getUsername());
			}
			serviceLogMapper.insertSelective(serviceLog);
			
			//服务次数减一
			String countKey = "JF:"+parse.getKey()+":"+parse.getSerivceMode();
			Long decr = jedisCluster.hincrBy(userInfo.getId(), countKey+":COUNT", -1);
			if(decr <= 0){ //修改为不可用
				jedisCluster.hset(userInfo.getId(), countKey+":USABLE", JFResultEnum.NOCOUNT.getCode());
			}
		} catch (Exception e) {
			logger.error("业务日志入库失败.."+e.getMessage());
		}
		
	}

}
