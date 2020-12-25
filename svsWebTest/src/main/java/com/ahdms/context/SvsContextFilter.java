package com.ahdms.context;

import com.ahdms.context.holder.SvsContextHolder;
import com.ahdms.framework.core.context.holder.ExtContextHolders;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author qinxiang
 * @date 2020-12-25 14:34
 */
@Slf4j
public class SvsContextFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String account = request.getHeader(HeaderConstants.SVS_USER_ID);
            SvsContext svsContext = SvsContext.builder()
                    .account(account)
                    .build();

            if (log.isDebugEnabled()) {
                log.debug("Context initialized >>>>>>", svsContext.toString());
            }
            SvsContextHolder.setContext(svsContext);
            chain.doFilter(request, response);
        } finally {
            SvsContextHolder.removeContext();
            ExtContextHolders.clear();
        }
    }


}
