package com.ahdms.framework.feign.logger;

import com.ahdms.framework.core.commom.util.DateUtils;
import feign.Logger;
import feign.Request;
import feign.Response;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

/**
 * 参考 feign.slf4j.Slf4jLogger，将日志级别改为 info
 *
 * @author Katrel.zhou
 */
@Slf4j
public class FeignLogger extends Logger {

	@Override
	protected void logRequest(String configKey, Level logLevel, Request request) {
		if (log.isDebugEnabled()) {
			super.logRequest(configKey, logLevel, request);
		}
	}

	@Override
	protected Response logAndRebufferResponse(String configKey, Level logLevel, Response response, long elapsedTime) throws IOException {
		if (log.isDebugEnabled()) {
			return super.logAndRebufferResponse(configKey, logLevel, response, elapsedTime);
		}
		return response;
	}

	@Override
	protected void log(String configKey, String format, Object... args) {
		if (log.isDebugEnabled()) {
			log.debug(String.format(methodTag(configKey) + format, args));
		}
	}
}
