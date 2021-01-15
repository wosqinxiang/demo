package com.ahdms.ctidservice.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisCluster;

/**
 * @author qinxiang
 * @date 2020-09-03 14:03
 */
@Component
public class RedisOpsClient {

    @Autowired
    private JedisCluster jedisCluster;

    public String set(String key,String value){

        return jedisCluster.set(key, value);
    }

    public Long expire(String key,int seconds){
        return jedisCluster.expire(key, seconds);
    }

    public String set(String key,String value,int seconds){
        String set = jedisCluster.set(key, value);
        expire(key, seconds);
        return set;
    }

    public String get(String key){
        return jedisCluster.get(key);
    }

    public Long hset(String key, String field, String value) {
        return jedisCluster.hset(key, field, value);
    }

    public Long del(String key) {
        return jedisCluster.del(key);
    }
}
