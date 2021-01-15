package com.ahdms.ctidservice.config;

import com.ahdms.ctidservice.wrapper.CtidRequestWrapper;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 自定义 DispatcherServlet 来分派 CtidRequestWrapper
 * @author qinxiang
 * @date 2021-01-04 15:53
 */
public class CtidDispatcherServlet extends DispatcherServlet {

    /**
     * 包装成我们自定义的request
     * @param request
     * @param response
     * @throws Exception
     */
    @Override
    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {
        super.doDispatch(new CtidRequestWrapper(request), response);
    }

}
