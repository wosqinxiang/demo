package com.ahdms.billing.config.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

@Component
public class JedisClusterUtils {
	
	@Autowired
	private JedisCluster jedisCluster;

	public Long hset(String key, String field, String value) {
		return jedisCluster.hset(key, field, value);
	}

	public String hget(String key, String field) {
		return jedisCluster.hget(key, field);
	}
	
	

}
