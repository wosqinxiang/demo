package com.ahdms.ctidservice.aop;

import com.ahdms.jf.model.JFChannelEnum;
import com.ahdms.jf.model.JFServiceEnum;

import java.lang.annotation.*;

/**
 * @author qinxiang
 * @date 2021-01-13 10:36
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Billing {

    JFServiceEnum service();

    JFChannelEnum channel();

}
