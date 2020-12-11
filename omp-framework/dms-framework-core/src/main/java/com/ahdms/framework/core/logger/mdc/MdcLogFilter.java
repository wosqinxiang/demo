package com.ahdms.framework.core.logger.mdc;

import com.ahdms.framework.core.commom.util.StringUtils;
import com.ahdms.framework.core.constant.Constant;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Spring boot Mdc Log Filter
 *
 * @author Katrel.zhou
 */
@Slf4j
public class MdcLogFilter extends HttpFilter {

	/**
	 * Mdc Log Filter
	 *
	 * @param request  request
	 * @param response response
	 * @param chain    chain
	 * @throws IOException      Exception
	 * @throws ServletException Exception
	 */
	@Override
	protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		try {
			String requestId = request.getHeader(Constant.MDC_REQUEST_ID_KEY);
			if (StringUtils.isBlank(requestId)) {
				requestId = StringUtils.getUUID();
				log.info("{} no request id found, create request id >>> {}", request.getRequestURI(), requestId);
			}
			ThreadContext.put(Constant.MDC_REQUEST_ID_KEY, requestId);
			chain.doFilter(request, response);
		} finally {
			ThreadContext.remove(Constant.MDC_REQUEST_ID_KEY);
		}
	}
}
