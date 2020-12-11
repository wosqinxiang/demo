package com.ahdms.framework.idgen.constant;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/14 13:54
 */
public enum GenerateStrategy {
    // 本地自增策略
    LOCAL,
    // 基于Redis的分片自增策略
    PIECEMEAL,
    // 基于Redis的连续自增策略
    CONSECUTIVE;
}
