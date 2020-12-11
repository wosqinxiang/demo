package com.ahdms.framework.core.stream;


import java.lang.annotation.*;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 15:33
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
@Documented
public @interface Source {
    /**
     * Designate channel name, get @StreamClient value if blank
     *
     * @return
     */
    String output() default "";

    /**
     * The spring bean name of message listener handler
     *
     * @return
     */
    String handler() default "";

    /**
     * The spring bean name of DLQ message listener handler
     *
     * @return
     */
    String deadHandler() default "";

    /**
     * Unique name that the binding belongs to (applies to producer only).
     * A empty String value indicates that binding with exchange and queue are not created, consumer must be creating binding before producer using it.
     *
     * @return
     */
    String group() default "";

    /**
     * @return
     */
    String prefix() default "";
}
