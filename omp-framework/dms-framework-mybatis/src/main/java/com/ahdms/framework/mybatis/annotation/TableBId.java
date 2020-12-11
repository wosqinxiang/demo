package com.ahdms.framework.mybatis.annotation;

import java.lang.annotation.*;

/**
 * 业务主键标识
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface TableBId {}
