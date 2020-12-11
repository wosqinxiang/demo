//package com.ahdms.framework.idgen.redis.piecemeal;
//
//import com.ahdms.framework.core.commom.util.EnvAutoUtils;
//import com.ahdms.framework.idgen.config.IdGenProperties;
//import com.ahdms.framework.idgen.constant.GenerateStrategy;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.script.DefaultRedisScript;
//import org.springframework.data.redis.serializer.StringRedisSerializer;
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// *
// * @author zhoumin
// * @version 1.0.0
// * @date 2020/7/13 15:56
// */
//public class PiecemealRedisHashIdGenerator extends AbstractPiecemealRedisIdGenerator {
//
//    private static final String DEFAULT_IDGEN_LUA_FILE_PATH = "lua/hashIdGen.lua";
//    private static final Map<String, List<String>> CACHE_REDIS_SCRIPT_KEYS = new ConcurrentHashMap<>();
//
//    public PiecemealRedisHashIdGenerator(RedisTemplate<String, Long> redisTemplate, IdGenProperties idGenProperties) {
//        super(redisTemplate, idGenProperties, DEFAULT_IDGEN_LUA_FILE_PATH);
//    }
//
//    @Override
//    protected Long acquireLocalMaxValue(String idName, boolean global, Long maxValue, Long minValue, Integer delta) {
//        List<String> keys = CACHE_REDIS_SCRIPT_KEYS.get(idName);
//        if (null == keys) {
//            keys = new ArrayList<>();
//            keys.add(resolveIdGenRedisKey(global));
//            CACHE_REDIS_SCRIPT_KEYS.put(idName, keys);
//        }
//        return getRedisTemplate().execute(new DefaultRedisScript<>(getRedisIdGenScript(), Long.class),
//                new StringRedisSerializer(), new LongRedisSerializer(), keys, idName, String.valueOf(maxValue), String.valueOf(delta), String.valueOf(minValue));
//    }
//
//    /**
//     * @param global 是否全局共享的ID，如果是，生成redis key 为PREFIX，否则为PREFIX+AppName
//     * @return redis key（数据结构为Hash）
//     */
//    private String resolveIdGenRedisKey(boolean global) {
//        String prefix = getRedisKeyPrefix();
//        String appender = getGlobalRedisKey();
//        if (!global) {
//            appender = EnvAutoUtils.getServerName();
//        }
//        return new StringBuffer(prefix)
//                .append(appender)
//                .toString();
//    }
//
//    @Override
//    public boolean supports(GenerateStrategy strategy) {
//        return false;
//    }
//}
