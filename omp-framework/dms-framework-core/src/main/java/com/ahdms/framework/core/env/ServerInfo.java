package com.ahdms.framework.core.env;

import com.ahdms.framework.core.commom.util.CharPool;
import com.ahdms.framework.core.commom.util.NetUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.SmartInitializingSingleton;
import org.springframework.boot.autoconfigure.web.ServerProperties;

/**
 * 服务 ip 信息，启动之后几乎不会再变动
 *
 * @author Katrel.Zhou
 * @version 1.0.0
 * @date 2019/5/17 17:44
 */
@Slf4j
@Getter
public class ServerInfo implements SmartInitializingSingleton {

    @Setter
    private ServerProperties serverProperties;

    private String hostName;
    private String ip;
    private String ipSuffix;
    private Integer port;
    private String ipWithPort;

    @Override
    public void afterSingletonsInstantiated() {
        this.hostName = NetUtils.getHostName();
        this.ip = NetUtils.getHostIp();
        this.ipSuffix = getIpSuffix(this.ip);
        this.port = serverProperties.getPort();
        this.ipWithPort = String.format("%s:%d", this.ip, this.port);
    }

    private static String getIpSuffix(String ip) {
        String ipSuffix = ip.substring(ip.lastIndexOf(CharPool.DOT) + 1);
        if (ipSuffix.length() > 2) {
            return ipSuffix;
        }
        // ip 后缀小于 3 位的补零
        return String.format("%03d", Integer.valueOf(ipSuffix));
    }

}
