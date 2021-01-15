package com.ahdms.ctidservice.config.svs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @author qinxiang
 * @date 2020-12-21 11:19
 */
@Getter
@Setter
@ConfigurationProperties("svtool.source")
public class SvsProperties {

    private String svIp;
    private int svPort;
    private int keyIndex;
    private String keyValue;
    private String svSubject;

}
