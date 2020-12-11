package com.ahdms.framework.idgen.redis.consecutive;

import com.ahdms.framework.idgen.constant.GenerateStrategy;
import com.ahdms.framework.idgen.constant.IdConstant;
import com.ahdms.framework.idgen.redis.piecemeal.LongRedisSerializer;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/14 17:34
 */
public class ConsecutiveRedisStringIdGenerator extends AbstractConsecutiveRedisIdGenerator {
    private static final Map<String, List<String>> CACHE_REDIS_SCRIPT_KEYS = new ConcurrentHashMap<>();

    public ConsecutiveRedisStringIdGenerator(RedisTemplate<String, Long> redisTemplate) {
        super(redisTemplate, IdConstant.CONSECUTIVE_LUA_SCRIPT);
    }

    @Override
    protected Long acquireNextValue(String idName, boolean global, Long maxValue, Long minValue) {
        List<String> keys = CACHE_REDIS_SCRIPT_KEYS.get(idName);
        if (null == keys) {
            keys = new ArrayList<>();
            keys.add(resolveIdGenRedisKey(global, idName, GenerateStrategy.CONSECUTIVE));
            CACHE_REDIS_SCRIPT_KEYS.put(idName, keys);
        }
        return getRedisTemplate().execute(new DefaultRedisScript<>(getRedisIdGenScript(), Long.class),
                new StringRedisSerializer(), new LongRedisSerializer(), keys, String.valueOf(++maxValue), String.valueOf(minValue));
    }

    @Override
    public boolean supports(GenerateStrategy strategy) {
        return GenerateStrategy.CONSECUTIVE.equals(strategy);
    }
}
