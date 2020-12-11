package com.ahdms.framework.core.logger.util;

/**
 * log4j2配置的定义key
 *
 * @author Katrel.zhou
 */
public interface LoggerConstants {

    /**
     * 日志目录变量名
     */
    String LOGGER_DIR = "logging.dir";
	/**
	 * 应用名
	 */
	String LOGGER_APP_NAME = "logging.appName";
	/**
	 * root日志级别
	 */
    String LOGGER_ROOT_LEVEL = "logging.level.root";
	/**
	 * 日志配置文件目录
	 */
	String LOGGER_CONFIG = "logging.config";

	String LOGGER_DEFAULT_DIR = "logs";
}
