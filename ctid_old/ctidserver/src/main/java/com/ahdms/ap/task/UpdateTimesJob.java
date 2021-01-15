package com.ahdms.ap.task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.ahdms.ap.dao.AuthCountDao;
import com.ahdms.ap.model.AuthCount;
import com.ahdms.ap.utils.DateUtils;
import com.ahdms.ap.utils.UUIDGenerator;
import com.ahdms.ctidservice.db.core.CTIDProperties;
import com.dangdang.ddframe.job.api.ShardingContext;
import com.dangdang.ddframe.job.api.simple.SimpleJob;

import redis.clients.jedis.JedisCluster;


@Component
public class UpdateTimesJob implements SimpleJob {
    Logger logger = LoggerFactory.getLogger(UpdateTimesJob.class);
    @Autowired
    CTIDProperties ctidProperties;
    @Resource(name="ctidJedis")
    JedisCluster jedisCluster;
    @Autowired
    AuthCountDao countDao;
    
	@Override
    public void execute(ShardingContext context) {
        logger.debug("用户使用次数从redis中记录到数据库" + DateUtils.getNowTime());
        try {
//			JdbcTemplate jdbcTemplate = applicationContext.getBean(JdbcTemplate.class);
			String deviceCount = context.getJobParameter();
			Map<String, String> results = jedisCluster.hgetAll(deviceCount);
			if(null != results){
				for (Map.Entry<String, String> item : results.entrySet()) {
					System.out.println(item.getKey() + " : " + item.getValue());
					String deviceName = item.getKey();
					String result = item.getValue(); 
					if (null != result) {
						String date = result.substring(0, result.indexOf("+"));
						int count = Integer.parseInt(result.substring(result.indexOf("+") + 1));
						AuthCount authCount = countDao.selectByServer(deviceName);
						if (null == authCount) {
							AuthCount acount = new AuthCount(UUIDGenerator.getSerialId(), deviceName, date, count);
							countDao.insert(acount);
						} else {
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							String nowdate = sdf.format(new Date());
							if (nowdate.equals(date)) {
								authCount.setCount(count);
								authCount.setNowDay(date);
								countDao.updateByPrimaryKeySelective(authCount);
							}
						} 
						jedisCluster.hdel(deviceCount, deviceName);
					}
				} 
			}
		}  catch (Exception e) {
			logger.error("备份SQL失败 - {}", e.getMessage());
		}
	} 
}