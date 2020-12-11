package com.ahdms.framework.idgen.redis.piecemeal;

import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.exception.FrameworkException;
import com.ahdms.framework.idgen.config.IdGenProperties;
import com.ahdms.framework.idgen.constant.IdConstant;
import com.ahdms.framework.idgen.redis.AbstractRedisIdGenerator;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public abstract class AbstractPiecemealRedisIdGenerator extends AbstractRedisIdGenerator {

    private static final Map<String, IdStore> ID_STORE_MAP = new ConcurrentHashMap<>();

    private Lock lock = new ReentrantLock();

    public AbstractPiecemealRedisIdGenerator(RedisTemplate<String, Long> redisTemplate,
                                             String redisScriptFilePath) {
        super(redisTemplate, redisScriptFilePath);
    }

    @Override
    public String generateId(IdGenProperties.IdProperty idProperty, boolean global) {
        return this.generate(idProperty, global).toString();
    }

    /**
     * 生成ID
     *
     * @param idProperty ID Name
     * @param global     是否全局的 true ：全局 false : 钟对application
     * @return ID
     */
    protected Long generate(IdGenProperties.IdProperty idProperty, boolean global) {
        String idName = idProperty.getName();
        IdStore idStore = this.getIdStoreFromCache(idName);
        if (null == idStore) {
            lock.lock();
            try {
                idStore = this.getIdStoreFromCache(idName);
                if (null == idStore) {
                    idStore = this.createIdStore(idProperty, global);
                    this.putIdStoreToCache(idName, idStore);
                }
            } finally {
                lock.unlock();
            }
        }
        while (true) {
            Long value = idStore.nextValue();
            if (value < 0) {
                lock.lock();
                try {
                    idStore = this.getIdStoreFromCache(idName);
                    value = idStore.nextValue();
                    if (value < 0) {
                        idStore = this.createIdStore(idProperty, global);
                        value = idStore.nextValue();
                        this.putIdStoreToCache(idName, idStore);
                    }
                } finally {
                    lock.unlock();
                }
                //这个判断应该不会进去，除非脚本没有对maxValue进行控制，防御性代码
                if (value < 0) {
                    if (logger.isWarnEnabled()) {
                        logger.warn("IdGenerator script may be have a bug, generate id value is {}, defensive programming.", value);
                    }
                    idStore = this.getIdStoreFromCache(idName);
                    continue;
                }
            }
            if (logger.isDebugEnabled()) {
                logger.debug("Acquired id \"{}\" with name \"{}\".", value, idName);
            }
            return value;
        }
    }

    /**
     * 创建IDStore
     *
     * @param idProperty
     * @param global
     * @return
     */
    protected IdStore createIdStore(IdGenProperties.IdProperty idProperty, boolean global) {
        String idName = idProperty.getName();
        IdStore oldIdStore = this.getIdStoreFromCache(idName);
        Long maxValue = null != oldIdStore ? oldIdStore.maxValue : idProperty.getMaxValue();
        if (null == maxValue) {
            maxValue = -1L;
        }
        Long minValue = minValueFormat(idProperty.getMinValue());
        Integer delta = Optional.ofNullable(idProperty.getLocalStorage()).orElse(IdConstant.DEFAULT_DELTA);
        FrameworkException.throwOnFalse((maxValue == -1 || minValue < maxValue),
                StringUtils.format("The \"IdGen\" with name \"{}\" min value must be less than the max value.", idName));

        Long localMaxValue = this.acquireLocalMaxValue(idName, global, maxValue, minValue, delta);
        Long currentValue = localMaxValue - delta + 1;
        if (-1 != maxValue) {
            localMaxValue = localMaxValue <= maxValue ? localMaxValue : maxValue;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("New Id store with key \"{}\", currentId \"{}\", localMaxId \"{}\", maxValue \"{}\", delta \"{}\", minValue \"{}\".",
                    idName, currentValue, localMaxValue, maxValue, delta, minValue);
        }
        return IdStore.newInstance(currentValue, localMaxValue, maxValue);
    }

    protected IdStore getIdStoreFromCache(String idName) {
        return ID_STORE_MAP.get(idName);
    }

    protected void putIdStoreToCache(String idName, IdStore idStore) {
        ID_STORE_MAP.put(idName, idStore);
    }

    /**
     * 获取ID本地缓存最大值
     *
     * @param idName   ID 名称
     * @param global   是否全局唯一ID
     * @param maxValue ID最大值
     * @param minValue ID最小值
     * @param delta    每次在本地缓存的ID个数
     * @return
     */
    protected abstract Long acquireLocalMaxValue(String idName, boolean global, Long maxValue, Long minValue, Integer delta);

    /**
     * id store
     */
    protected static class IdStore {
        private AtomicLong localIdGen;
        private Long localMaxValue;
        //全局最大值，超过该值继续从minValue开始;该值为null则无穷自增，达到long最大值
        private Long maxValue;

        public IdStore(AtomicLong localIdGen, Long localMaxValue, Long maxValue) {
            this.localIdGen = localIdGen;
            this.localMaxValue = localMaxValue;
            this.maxValue = maxValue;
        }

        public static IdStore newInstance(Long currentValue, Long localMaxValue, Long maxValue) {
            return new IdStore(new AtomicLong(currentValue), localMaxValue, maxValue);
        }

        /**
         * 超出localMaxValue 返回-1
         * 超出maxValue返回-2
         *
         * @return
         */
        public Long nextValue() {
            Long value = localIdGen.getAndIncrement();
            if (value > localMaxValue) {
                return -1L;
            }
            if (-1 != maxValue && value > maxValue) {
                return -2L;
            }
            return value;
        }
    }

}

