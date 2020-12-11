package com.ahdms.framework.idgen.constant;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/14 16:19
 */
public interface IdConstant {
   int DEFAULT_DELTA = 200;
    String DEFAULT_PREFIX_ID_GEN = "IDGEN:";
    String GLOBAL_REDIS_KEY = "GLOBAL";
    String PIECEMEAL_LUA_SCRIPT = "lua/piecemealGen.lua";
    String CONSECUTIVE_LUA_SCRIPT = "lua/consecutiveGen.lua";
}
