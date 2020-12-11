package com.ahdms.framework.idgen.redis;

import com.ahdms.framework.core.commom.util.EnvAutoUtils;
import com.ahdms.framework.core.commom.util.ResourceUtils;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.idgen.IdGenerator;
import com.ahdms.framework.idgen.constant.GenerateStrategy;
import com.ahdms.framework.idgen.constant.IdConstant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.IOException;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/14 17:19
 */
public abstract class AbstractRedisIdGenerator implements IdGenerator {
    protected static final Logger logger = LoggerFactory.getLogger(AbstractRedisIdGenerator.class);
    private RedisTemplate<String, Long> redisTemplate;
    private String idgenScript;

    public AbstractRedisIdGenerator(RedisTemplate<String, Long> redisTemplate,
                                    String redisScriptFilePath) {
        this.redisTemplate = redisTemplate;
        loadRedisScript(redisScriptFilePath);
    }

    /**
     * 加载脚本，只是为了检查脚本是否存在
     *
     * @param redisScriptFilePath
     */
    protected void loadRedisScript(String redisScriptFilePath) {
        try {
            this.idgenScript = ResourceUtils.getFileContent(redisScriptFilePath);
        } catch (IOException e) {
            FrameworkException.throwFail(e.getMessage());
        }
        FrameworkException.throwOnFalse(StringUtils.isNotBlank(this.idgenScript),
                StringUtils.format("Redis hash id generator script {} is not found.", redisScriptFilePath));
        if (logger.isDebugEnabled()) {
            logger.debug("Load IdGen lua script {}.", redisScriptFilePath);
            logger.debug("{}", this.idgenScript);
        }
    }

    protected Long minValueFormat(Long minValue) {
        return null != minValue ? (minValue  <= 0 ? 0 : minValue - 1 ) : 0;
    }

    protected String getRedisIdGenScript() {
        return this.idgenScript;
    }

    protected RedisTemplate<String, Long> getRedisTemplate() {
        return redisTemplate;
    }

    protected void setRedisTemplate(RedisTemplate<String, Long> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getGlobalRedisKey() {
        return IdConstant.GLOBAL_REDIS_KEY;
    }

    private String getRedisKeyPrefix(GenerateStrategy strategy) {
        return IdConstant.DEFAULT_PREFIX_ID_GEN + strategy.name() + StringPool.COLON;
    }

    protected String resolveIdGenRedisKey(boolean global, String idName, GenerateStrategy strategy) {
        String prefix = getRedisKeyPrefix(strategy);
        String appender = getGlobalRedisKey();
        if (!global) {
            appender = EnvAutoUtils.getServerName();
        }
        return new StringBuffer(prefix)
                .append(appender)
                .append(StringPool.DOT)
                .append(idName)
                .toString();
    }
}
