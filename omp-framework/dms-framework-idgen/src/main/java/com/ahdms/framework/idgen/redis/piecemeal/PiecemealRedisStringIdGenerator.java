package com.ahdms.framework.idgen.redis.piecemeal;

import com.ahdms.framework.core.commom.util.EnvAutoUtils;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.idgen.constant.GenerateStrategy;
import com.ahdms.framework.idgen.constant.IdConstant;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public class PiecemealRedisStringIdGenerator extends AbstractPiecemealRedisIdGenerator {

    private static final Map<String, List<String>> CACHE_REDIS_SCRIPT_KEYS = new ConcurrentHashMap<>();

    public PiecemealRedisStringIdGenerator(RedisTemplate<String, Long> redisTemplate) {
        super(redisTemplate, IdConstant.PIECEMEAL_LUA_SCRIPT);
    }

    @Override
    protected Long acquireLocalMaxValue(String idName, boolean global, Long maxValue, Long minValue, Integer delta) {
        List<String> keys = CACHE_REDIS_SCRIPT_KEYS.get(idName);
        if (null == keys) {
            keys = new ArrayList<>();
            keys.add(resolveIdGenRedisKey(global, idName, GenerateStrategy.PIECEMEAL));
            CACHE_REDIS_SCRIPT_KEYS.put(idName, keys);
        }
        return getRedisTemplate().execute(new DefaultRedisScript<>(getRedisIdGenScript(), Long.class),
                new StringRedisSerializer(), new LongRedisSerializer(), keys, String.valueOf(maxValue), String.valueOf(delta), String.valueOf(minValue));
    }


    @Override
    public boolean supports(GenerateStrategy strategy) {
        return GenerateStrategy.PIECEMEAL.equals(strategy);
    }
}
