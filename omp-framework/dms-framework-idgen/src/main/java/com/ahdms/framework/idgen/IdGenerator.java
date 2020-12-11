package com.ahdms.framework.idgen;

import com.ahdms.framework.idgen.config.IdGenProperties;
import com.ahdms.framework.idgen.constant.GenerateStrategy;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
public interface IdGenerator {
    /**
     * 生成ID
     * @param idProperty 配置的ID属性
     * @param global 是否全局唯一
     * @return ID
     */
    String generateId(IdGenProperties.IdProperty idProperty, boolean global);

    /**
     * 支持策略
     * @param strategy
     * @return
     */
    boolean supports(GenerateStrategy strategy);
}
