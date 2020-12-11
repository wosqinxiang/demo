package com.ahdms.framework.core.commom.util;

import com.ahdms.framework.core.env.DmsEnvironmentAware;

/**
 * 平台环境工具包
 *
 * @author Katrel.Zhou
 * @version 1.0.0
 * @date 2019/5/17 17:44
 */
public class EnvAutoUtils {
	private static DmsEnvironmentAware DMS_ENVIRONMENT;

	/**
	 * 应用缩写，三位
	 *
	 * @return
	 */
	public static String getServerName() {
		return getEnvironment().getServerName().toUpperCase();
	}

	/**
	 * 获取当前环境配置
	 *
	 * @return
	 */
	public static DmsEnvironmentAware getEnvironment() {
		if (DMS_ENVIRONMENT == null) {
			DMS_ENVIRONMENT = BeanLoaderUtils.getSpringBean(DmsEnvironmentAware.class);
		}
		return DMS_ENVIRONMENT;
	}

	/**
	 * 获取当前环境的属性值
	 *
	 * @param key
	 * @return
	 */
	public static String getProperty(String key) {
		return getEnvironment().getProperty(key);
	}

	/**
	 * 获取当前环境的属性值
	 *
	 * @param key
	 * @param targetType
	 * @return
	 */
	public static <T> T getProperty(String key, Class<T> targetType) {
		return getEnvironment().getProperty(key, targetType);
	}

}
