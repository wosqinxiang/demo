package com.ahdms.framework.core.logger.env;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * 环境对用的日志级别
 *
 * @author Katrel.zhou
 */
@Getter
@AllArgsConstructor
public enum EnvLogLevel {
	/**
	 * 各环境日志配置
	 */
	dev("INFO", "INFO"),
	test("INFO", "OFF"),
	pre("INFO", "OFF"),
	pro("INFO", "OFF");

	/**
	 * root 日志级别
	 */
	@NonNull
	private String root;
	/**
	 * 控制台日志级别
	 */
	@NonNull
	private String console;

	/**
	 * 环境与日志级别关联
	 *
	 * @param env 环境
	 * @return 日志级别
	 */
	public static EnvLogLevel of(String env) {
		EnvLogLevel[] logLevels = EnvLogLevel.values();
		for (EnvLogLevel logLevel : logLevels) {
			if (logLevel.name().equals(env.toLowerCase())) {
				return logLevel;
			}
		}
		return EnvLogLevel.dev;
	}
}
