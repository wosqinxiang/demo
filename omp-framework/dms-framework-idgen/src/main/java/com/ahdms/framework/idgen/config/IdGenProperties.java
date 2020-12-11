package com.ahdms.framework.idgen.config;

import com.ahdms.framework.idgen.constant.GenerateStrategy;
import com.ahdms.framework.idgen.constant.RedisMode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/***
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/13 15:56
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "dms.idgen")
public class IdGenProperties {
    private List<IdProperty> idProperties = new ArrayList<>();
    private RedisMode redisMode;

    @Getter
    @Setter
    public static class IdProperty {
        // ID名称
        private String name;
        // 每次申请存储到内存中的数量
        private Integer localStorage;
        // ID生成的最小值
        private Long minValue;
        // ID生成的最大值
        private Long maxValue;
        // ID生成策略
        private GenerateStrategy strategy;
    }
}
