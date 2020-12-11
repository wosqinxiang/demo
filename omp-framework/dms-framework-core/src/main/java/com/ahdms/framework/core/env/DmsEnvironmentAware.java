package com.ahdms.framework.core.env;

import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.exception.FrameworkException;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;

/**
 * 当前系统环境变量及配置文件
 *
 * @author Katrel.Zhou
 * @version 1.0.0
 * @date 2019/5/17 17:44
 */
public class DmsEnvironmentAware implements EnvironmentAware {

    private Environment environment;
    private String serverName;
    private DmsServerProperties dmsServerProperties;

    public DmsEnvironmentAware(DmsServerProperties dmsServerProperties) {
        this.dmsServerProperties = dmsServerProperties;
    }

    public String getServerName() {
        return this.serverName;
    }

    public String getProperty(String key) {
        return this.environment.getProperty(key);
    }

    public <T> T getProperty(String key, Class<T> targetType) {
        return this.environment.getProperty(key, targetType);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }


    public DmsServerProperties getDmsServerProperties() {
        return this.dmsServerProperties;
    }

    @PostConstruct
    public void postConstruct() {
        String propertyServerName = this.environment.getProperty("dms.server.short-name");
        if (StringUtils.isBlank(propertyServerName)) {
            throw new FrameworkException("No server name was found, please configure it with property key dms.server.short-name = ${shortName}");
        }
        this.serverName = propertyServerName.toLowerCase();
    }

}
