package com.ahdms.framework.core.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Getter
@Setter
@RefreshScope
@ConfigurationProperties("dms.context")
public class DmsContextProperties {

    /**
     * RestTemplate 和 Fegin 透传到下层的 Headers 名称列表
     */
    private List<String> allowedHeaders = new ArrayList<>();

}
