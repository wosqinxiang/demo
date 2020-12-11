package com.ahdms.framework.job;

import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.logger.util.LoggerConstants;
import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import com.xxl.job.core.util.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.Assert;

import java.util.Optional;

/**
 * xxl-job config
 *
 * @author xuxueli 2017-04-28
 */
@Slf4j
@Configuration
@EnableConfigurationProperties(XxlJobProperties.class)
public class XxlJobConfiguration implements InitializingBean, EnvironmentAware {

    private final XxlJobProperties xxlJobProperties;

    private Environment environment;

    public XxlJobConfiguration(XxlJobProperties xxlJobProperties) {
        this.xxlJobProperties = xxlJobProperties;
    }

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor() {
        log.info(">>>>>>>>>>> xxl-job config init.");
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(xxlJobProperties.getAdmin().getAddresses());
        xxlJobSpringExecutor.setAccessToken(xxlJobProperties.getAccessToken());
        XxlJobProperties.Executor executor = xxlJobProperties.getExecutor();
        xxlJobSpringExecutor.setAppname(executor.getAppName());
        xxlJobSpringExecutor.setAddress(executor.getAddress());
        xxlJobSpringExecutor.setIp(executor.getIp());
        xxlJobSpringExecutor.setPort(executor.getPort());
        xxlJobSpringExecutor.setLogPath(executor.getLogPath());
        xxlJobSpringExecutor.setLogRetentionDays(executor.getLogRetentionDays());
        String ip_port_address = IpUtil.getIpPort(executor.getIp(), executor.getPort());
        log.info(">>>>>>>>>>> xxl-job executor address : {}", ip_port_address);
        return xxlJobSpringExecutor;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Assert.notNull(xxlJobProperties.getAdmin(), "xxl.job.admin.addresses missing your property");
//        Assert.notNull(xxlJobProperties.getExecutor(), "xxl.job.admin.executor.xxx missing your property");
        XxlJobProperties.Executor executor = Optional.ofNullable(xxlJobProperties.getExecutor())
                .orElseGet(() -> new XxlJobProperties.Executor());
        String appName = environment.getProperty("spring.application.name");
        if (StringUtils.isBlank(executor.getAppName())) {
            executor.setAppName(appName);
        }
        if (executor.getPort() == 0) {
            executor.setPort(9999);
        }
        if (executor.getLogRetentionDays() == 0) {
            executor.setLogRetentionDays(14);
        }
        if (StringUtils.isBlank(executor.getLogPath())) {
            executor.setLogPath(environment.getProperty(LoggerConstants.LOGGER_DIR, LoggerConstants.LOGGER_DEFAULT_DIR));
        }
        xxlJobProperties.setExecutor(executor);
    }

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

}