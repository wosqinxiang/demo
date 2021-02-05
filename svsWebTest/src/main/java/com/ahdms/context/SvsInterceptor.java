package com.ahdms.context;

import com.ahdms.bean.model.SvsConfig;
import com.ahdms.config.svs.SvsConfigCache;
import com.ahdms.config.svs.SvsProperties;
import com.ahdms.context.holder.SvsContextHolder;
import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.core.context.holder.ExtContextHolders;
import com.ahdms.result.ApiResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collection;

import static com.ahdms.code.ApiCode.CORE_ACCOUNT_ERROR;

/**
 * @author qinxiang
 * @date 2020-12-28 9:16
 */
@Component
@Slf4j
public class SvsInterceptor implements HandlerInterceptor {

    @Autowired
    private SvsConfigCache svsConfigCache;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String account = "";
        try {

            account = request.getHeader(HeaderConstants.SVS_USER_ID);
            boolean b = svsConfigCache.checkAccount(account);
            if(b){
                SvsConfig svsConfig = svsConfigCache.get(account);
                SvsContext svsContext = SvsContext.builder()
                        .account(account)
                        .svsConfig(svsConfig)
                        .build();
                SvsContextHolder.setContext(svsContext);
                return true;
            }

        } catch (Exception e){
            log.error(CORE_ACCOUNT_ERROR.getMessage()+"..."+account);
        }
        response.setCharacterEncoding("UTF-8");
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write(JsonUtils.toJson(ApiResult.error(CORE_ACCOUNT_ERROR)));
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        SvsContextHolder.removeContext();
        ExtContextHolders.clear();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
