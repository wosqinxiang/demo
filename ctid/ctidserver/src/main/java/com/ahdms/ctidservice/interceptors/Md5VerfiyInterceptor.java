package com.ahdms.ctidservice.interceptors;

import com.ahdms.ctidservice.bean.AppSdkBasicBean;
import com.ahdms.ctidservice.bean.CtidResult;
import com.ahdms.ctidservice.bean.model.KeyInfo;
import com.ahdms.ctidservice.bean.vo.CtidRequestBean;
import com.ahdms.ctidservice.context.CtidContext;
import com.ahdms.ctidservice.context.holder.CtidContextHolder;
import com.ahdms.ctidservice.exception.ApiException;
import com.ahdms.ctidservice.service.IKeyInfoService;
import com.ahdms.ctidservice.util.MD5Utils;
import com.ahdms.framework.core.commom.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author qinxiang
 * @date 2021-01-04 11:27
 */
@Component
@Slf4j
public class Md5VerfiyInterceptor implements HandlerInterceptor {

    @Autowired
    private IKeyInfoService keyInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            //获取请求body
            byte[] bodyBytes = StreamUtils.copyToByteArray(request.getInputStream());
            String body = new String(bodyBytes, request.getCharacterEncoding());
            CtidRequestBean ctidRequestBean = JsonUtils.readValue(body, CtidRequestBean.class);
            KeyInfo keyInfo = validSignData(ctidRequestBean);
            CtidContext ctidContext = CtidContext.builder().keyInfo(keyInfo).build();
            CtidContextHolder.setContext(ctidContext);
            return true;
        }catch (ApiException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        response.setStatus(HttpServletResponse.SC_OK);
        response.addHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE);
        JsonUtils.writeValue(response.getWriter(), CtidResult.error("ERROR"));
        return false;
    }

    private KeyInfo validSignData(CtidRequestBean dcaBean) {
        String sign = dcaBean.getSign();
        String bizPackageStr = dcaBean.getBizPackage();
        AppSdkBasicBean appSdkBasicBean = JsonUtils.readValue(bizPackageStr, AppSdkBasicBean.class);
        KeyInfo keyInfo = keyInfoService.selectByAppSdkBean(appSdkBasicBean);

        if (null != keyInfo) {
            String key = keyInfo.getSecretKey();
            boolean verify = MD5Utils.verify(bizPackageStr, key, sign);
            if (verify) {
                return keyInfo;
            } else {
                log.error("MD5验证失败！");
                throw new ApiException("请求数据验签失败!");
            }
        } else {
            log.error("AppId与APP包名不匹配！");
            throw new ApiException("AppId与APP包名不匹配！");
        }
}

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        CtidContextHolder.removeContext();
    }
}
