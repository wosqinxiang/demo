package com.ahdms.framework.cache.annotation;

import java.lang.annotation.*;

/**
 * 标记为redis cache
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface RedisMergedAnnotation {
}
