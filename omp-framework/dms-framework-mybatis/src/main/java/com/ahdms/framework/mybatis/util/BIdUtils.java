package com.ahdms.framework.mybatis.util;

import com.ahdms.framework.mybatis.annotation.TableBId;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.core.toolkit.ReflectionKit;
import lombok.experimental.UtilityClass;

import java.lang.reflect.Field;
import java.util.List;

/**
 * 业务id 生成工具
 *
 * @author Katrel.zhou
 */
@UtilityClass
public class BIdUtils {

	/**
	 * 获取 bid 属性名
	 *
	 * @param modelClass 模型类型
	 * @return 属性名
	 */
	public static String getBIdFieldName(Class<?> modelClass) {
		List<Field> fieldList = ReflectionKit.getFieldList(modelClass);
		for (Field field : fieldList) {
			if (field.isAnnotationPresent(TableBId.class)) {
				return field.getName();
			}
		}
		return null;
	}

	/**
	 * 生成业务id
	 *
	 * @return 业务id
	 */
	public static Long getBId() {
		return IdWorker.getId();
	}
}
