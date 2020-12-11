package com.ahdms.framework.core.copier;

import java.lang.annotation.*;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CopyProperty {

    /**
     * 属性名，用于指定别名，默认使用：field name
     *
     * @return 属性名
     */
    String value() default "";

    /**
     * 忽略：默认为 false
     *
     * @return 是否忽略
     */
    boolean ignore() default false;
}
