package com.ahdms.framework.core.stream;

import java.lang.annotation.*;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/16 15:24
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface StreamClient {
    /**
     * default channel name for stream client
     * @return
     */
    String value() default "";

    /**
     * @return the <code>@Qualifier</code> value for the feign client.
     */
    String qualifier() default "";

    /**
     * @return whether to mark the stream proxy as a primary bean. Defaults to true.
     */
    boolean primary() default true;
}
