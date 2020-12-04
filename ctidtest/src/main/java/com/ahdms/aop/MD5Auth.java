package com.ahdms.aop;

import java.lang.annotation.*;

/**
 * @author qinxiang
 * @date 2020-12-04 14:26
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MD5Auth {

    String value() default "";

}
