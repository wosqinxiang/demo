package com.ahdms.framework.idgen.redis.consecutive;

import com.ahdms.framework.idgen.config.IdGenProperties;
import com.ahdms.framework.idgen.constant.GenerateStrategy;
import com.ahdms.framework.idgen.redis.AbstractRedisIdGenerator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 连续的ID生成器
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
@Slf4j
public abstract class AbstractConsecutiveRedisIdGenerator extends AbstractRedisIdGenerator {

    public AbstractConsecutiveRedisIdGenerator(RedisTemplate<String, Long> redisTemplate,
                                               String redisScriptFilePath) {
        super(redisTemplate, redisScriptFilePath);
    }

    @Override
    public String generateId(IdGenProperties.IdProperty idProperty, boolean global) {
        Long value = acquireNextValue(idProperty.getName(), global, idProperty.getMaxValue(), minValueFormat(idProperty.getMinValue()));
        return value.toString();
    }



    /**
     * 获取ID本地缓存最大值
     *
     * @param idName   ID 名称
     * @param global   是否全局唯一ID
     * @param maxValue ID最大值
     * @param minValue ID最小值
     * @return
     */
    protected abstract Long acquireNextValue(String idName, boolean global, Long maxValue, Long minValue);
}
