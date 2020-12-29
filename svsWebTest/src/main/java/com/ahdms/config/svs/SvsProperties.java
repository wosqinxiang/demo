package com.ahdms.config.svs;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author qinxiang
 * @date 2020-12-21 11:19
 */
@Getter
@Setter
@ConfigurationProperties("svs")
public class SvsProperties {

    private Map<String,SvsServer> appCodes = new LinkedHashMap();
    private Collection<String> accounts = new ArrayList<>();

    @Getter
    @Setter
    public static class SvsServer {
        private String ip;
        private int port;
        private int keyIndex;
        private String keyValue;
        private String serialNumber;
        private String encryptKey;

    }

}
