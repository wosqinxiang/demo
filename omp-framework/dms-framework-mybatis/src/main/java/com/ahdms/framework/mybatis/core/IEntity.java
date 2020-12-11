package com.ahdms.framework.mybatis.core;

import java.util.Date;

/**
 * @author Katrel.Zhou
 * @date 2019/5/31 11:27
 */
public interface IEntity {

	/**
	 * 主键id
	 */
	Long getId();

	/**
	 * 创建时间
	 */
	Date getCreatedAt();

	/**
	 * 更新时间
	 */
	Date getUpdatedAt();
}
