package com.ahdms.framework.core.context;

import com.ahdms.framework.core.constant.Constant;
import com.ahdms.framework.core.context.holder.ExtContextHolders;
import com.ahdms.framework.core.context.holder.DmsContextHolders;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

/**
 * header解析，存储上下文
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@Slf4j
public class DmsContextFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            String requestId = request.getHeader(Constant.MDC_REQUEST_ID_KEY);
            String token = request.getHeader(HeaderConstants.DMS_TOKEN);
            Long userId = Optional.ofNullable(request.getHeader(HeaderConstants.DMS_USER_ID))
                    .map(Long::valueOf).orElse(null);
            String role = request.getHeader(HeaderConstants.DMS_ROLE_ID);
            String clientIp = request.getHeader(HeaderConstants.DMS_CLIENT_IP);
            String userAgent = request.getHeader(HeaderConstants.DMS_USER_AGENT);
            DmsContext dmsContext = DmsContext.builder()
                    .requestId(requestId)
                    .token(token)
                    .userId(userId)
                    .role(role)
                    .clientIp(clientIp)
                    .userAgent(userAgent)
                    .build();

            if (log.isDebugEnabled()) {
                log.debug("Context initialized >>>>>>", dmsContext.toString());
            }
            DmsContextHolders.setContext(dmsContext);
            chain.doFilter(request, response);
        } finally {
            DmsContextHolders.removeContext();
            ExtContextHolders.clear();
        }
    }
}
