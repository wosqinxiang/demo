package com.ahdms.framework.idgen.local;

import com.ahdms.framework.idgen.IdGenerator;
import com.ahdms.framework.idgen.config.IdGenProperties;
import com.ahdms.framework.idgen.constant.GenerateStrategy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 本地ID生成
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public class LocalIdGenerator implements IdGenerator {

    private Map<String, AtomicLong> idCountMap = new ConcurrentHashMap<>();

    @Override
    public String generateId(IdGenProperties.IdProperty idProperty, boolean global) {
        return String.valueOf(this.incrementAndGet(idProperty));
    }

    @Override
    public boolean supports(GenerateStrategy strategy) {
        return GenerateStrategy.LOCAL.equals(strategy);
    }

    private Long incrementAndGet(IdGenProperties.IdProperty idProperty) {
        String idName = idProperty.getName();
        AtomicLong idCount = this.idCountMap.get(idName);
        if (null == idCount) {
            synchronized (this.idCountMap) {
                idCount = this.idCountMap.get(idName);
                if (null == idCount) {
                    idCount = new AtomicLong(minValueFormat(idProperty.getMinValue()));
                    this.idCountMap.put(idName, idCount);
                }
            }
        }
        long value = idCount.incrementAndGet();
        long maxValue = idProperty.getMaxValue();
        if (-1 != maxValue && value > maxValue) {
            synchronized (this.idCountMap) {
                idCount = this.idCountMap.get(idName);
                value = idCount.longValue();
                if (-1 != maxValue && value > maxValue) {
                    AtomicLong newIdCount = new AtomicLong(minValueFormat(idProperty.getMinValue()));
                    value = newIdCount.incrementAndGet();
                    this.idCountMap.put(idName, newIdCount);
                }

            }
        }
        return value;
    }

    protected Long minValueFormat(Long minValue) {
        return null != minValue ? (minValue <= 0 ? 0 : minValue - 1) : 0;
    }
}
