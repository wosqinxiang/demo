package com.ahdms.framework.core.logger;

import com.ahdms.framework.core.logger.env.EnvLogLevel;
import com.ahdms.framework.core.logger.util.LoggerConstants;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Properties;
import java.util.stream.Stream;

/**
 * 应用日志处理
 *
 * @author Katrel.zhou
 */
public class LoggerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        if (applicationContext.getParent() != null) {// 非root容器
            return;
        }
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        String[] profiles = environment.getActiveProfiles();
        String activeProfile = Stream.of(profiles).findFirst().orElse("");
        EnvLogLevel envLogLevel = EnvLogLevel.of(activeProfile);

        Properties properties = System.getProperties();
        // 日志服务名
        String shortName = environment.getProperty("dms.server.short-name").toLowerCase();
        properties.setProperty(LoggerConstants.LOGGER_APP_NAME,
                environment.getProperty(LoggerConstants.LOGGER_APP_NAME, shortName));
        // 日志目录
        String logPath = environment.getProperty(LoggerConstants.LOGGER_DIR, LoggerConstants.LOGGER_DEFAULT_DIR);
        properties.setProperty(LoggerConstants.LOGGER_DIR, logPath);

        // 兼容 spring boot 默认的 logging.level.root: debug，支持 bootstrap.yml 或 -Dlogging.level.root=debug
        String rootLevel = environment.getProperty(LoggerConstants.LOGGER_ROOT_LEVEL, envLogLevel.getRoot());
        properties.setProperty(LoggerConstants.LOGGER_ROOT_LEVEL, rootLevel);
        // 日志配置文件路径
        properties.setProperty(LoggerConstants.LOGGER_CONFIG, "classpath:properties/logback-spring.xml");
    }
}
