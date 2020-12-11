package com.ahdms.framework.core.web.response;

/**
 * @Author: Simms.shi
 * @Date: 2019/7/29 12:01
 * @Desc: ****
 */

import com.ahdms.framework.core.alert.IAlertAble;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/2 13:22
 */
public interface ResponseMessageResolver {

    /**
     * 成功返回
     */
    void successResolve(HttpServletRequest request, HttpServletResponse response, Object data);

    /**
     * 失败返回
     */
    void failResolve(HttpServletRequest request, HttpServletResponse response,
                     String code, String message, Object... args);

    /**
     * 失败返回
     */
    void failResolve(HttpServletRequest request, HttpServletResponse response,
                     IAlertAble alertAble, Object... args);

}
