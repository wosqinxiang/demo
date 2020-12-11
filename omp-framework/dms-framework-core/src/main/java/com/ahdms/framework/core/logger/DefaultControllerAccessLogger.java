package com.ahdms.framework.core.logger;

import com.ahdms.framework.core.commom.util.DateUtils;
import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.core.commom.util.DmsContextUtils;
import com.ahdms.framework.core.commom.util.WebUtils;
import com.ahdms.framework.core.web.RequestInfo;
import com.ahdms.framework.core.web.ResponseInfo;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/8/4 9:38
 */
@Slf4j
@AllArgsConstructor
public class DefaultControllerAccessLogger implements ControllerAccessLogger {

    private boolean logAble;
    private static List<Class<?>> NO_PARSE_TYPE;

    static {
        NO_PARSE_TYPE = Stream.of(HttpServletRequest.class, HttpServletResponse.class, MultipartFile.class)
                .collect(Collectors.toList());
    }


    @Override
    public void request(ProceedingJoinPoint point) {
        HttpServletRequest request = WebUtils.getRequest();
        if (logAble) {
            RequestInfo requestInfo = RequestInfo.builder()
                    .requestId(DmsContextUtils.getRequestId())
                    .method(request.getMethod())
                    .uri(request.getRequestURI())
                    .target(point.getSignature().getName())
                    .clientIp(DmsContextUtils.getClientIP())
                    .requestTime(DateUtils.getCurrentDateTime(DateUtils.STANDARD_FORMATTER))
                    .body(Stream.of(point.getArgs()).filter(arg ->
                            NO_PARSE_TYPE.stream().noneMatch(c -> c.isAssignableFrom(arg.getClass())))
                            .collect(Collectors.toList()))
                    .build();
            log.info("[{}] - {}", Thread.currentThread().getId(), JsonUtils.writeValueAsString(requestInfo));
        }
    }

    @Override
    public void response(ProceedingJoinPoint point, Object responseObject, Date startTime) {
        if (logAble) {
            ResponseInfo responseInfo = ResponseInfo.builder()
                    .requestId(DmsContextUtils.getRequestId())
                    .duration(DateUtils.between(startTime, new Date()).toMillis())
                    .result(responseObject)
                    .build();
            log.info("[{}] - {}", Thread.currentThread().getId(), JsonUtils.writeValueAsString(responseInfo));
        }
    }
}
