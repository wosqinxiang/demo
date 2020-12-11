package com.ahdms.framework.mybatis.injector.methods;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * 实体模型
 *
 * @author Katrel.zhou
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EntityItem implements Serializable {
	private String column;
	private Object field;
}
