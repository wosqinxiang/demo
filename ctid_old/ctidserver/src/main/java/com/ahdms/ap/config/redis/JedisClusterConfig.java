/**
 * Created on 2016年9月18日 by lijiefeng
 */
package com.ahdms.ap.config.redis;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @Title 
 * @Description 
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @author lijiefeng
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */

@Configuration
public class JedisClusterConfig {
	
    @Autowired
    private RedisProperties redisProperties;

    /**
     * 注意：
     * 这里返回的JedisCluster是单例的，并且可以直接注入到其他类中去使用
     * @return
     */
    @Bean(name="ctidJedis")
    public JedisCluster getJedisCluster() {
        String[] serverArray = redisProperties.getClusterNodes().split(",");//获取服务器数组(这里要相信自己的输入，所以没有考虑空指针问题)
        Set<HostAndPort> nodes = new HashSet<HostAndPort>();

        for (String ipPort : serverArray) {
            String[] ipPortPair = ipPort.split(":");
            nodes.add(new HostAndPort(ipPortPair[0].trim(), Integer.valueOf(ipPortPair[1].trim())));
        }
        // Jedis连接池配置
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
//        // 最大空闲连接数, 默认8个
        jedisPoolConfig.setMaxIdle(redisProperties.getMaxIdle());
//        // 最大连接数, 默认8个
        jedisPoolConfig.setMaxTotal(redisProperties.getMaxTotal());
//        //最小空闲连接数, 默认0
        jedisPoolConfig.setMinIdle(redisProperties.getMinIdle());
//        // 获取连接时的最大等待毫秒数(如果设置为阻塞时BlockWhenExhausted),如果超时就抛异常, 小于零:阻塞不确定的时间,  默认-1
        jedisPoolConfig.setMaxWaitMillis(redisProperties.getMaxWaitMillis()); // 设置2秒
     // 每次逐出检查时 逐出的最大数目 如果为负数就是 : 1/abs(n), 默认3
        jedisPoolConfig.setNumTestsPerEvictionRun(100);//100
        // 对象空闲多久后逐出, 当空闲时间>该值 且 空闲连接>最大空闲数
        // 时直接逐出,不再根据MinEvictableIdleTimeMillis判断 (默认逐出策略)
        jedisPoolConfig.setSoftMinEvictableIdleTimeMillis(1000);//1000
//        //对拿到的connection进行validateObject校验
        jedisPoolConfig.setTestOnBorrow(true);
        // 在空闲时检查有效性, 默认false
        jedisPoolConfig.setTestWhileIdle(true);
     // 逐出扫描的时间间隔(毫秒) 如果为负数,则不运行逐出线程, 默认-1
        jedisPoolConfig.setTimeBetweenEvictionRunsMillis(1000);//1000
        return new JedisCluster(nodes, redisProperties.getCommandTimeout(), jedisPoolConfig);
    }
}

