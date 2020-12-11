package com.ahdms.framework.core.exception.advice;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.alert.translator.IAlertTranslator;
import com.ahdms.framework.core.commom.util.StringPool;
import com.ahdms.framework.core.constant.Constant;
import com.ahdms.framework.core.constant.SystemError;
import com.ahdms.framework.core.exception.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.ThreadContext;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * mvc 基础的异常拦截和处理器
 */
@Slf4j
@RestControllerAdvice
public class ControllerExceptionAdvice {

    private final IAlertTranslator alertTranslator;

    public ControllerExceptionAdvice(IAlertTranslator alertTranslator) {
        this.alertTranslator = alertTranslator;
    }

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Map<String, Object> handleException(HttpServletRequest request, HttpServletResponse response, Exception exception) {
        Map<String, Object> values = new HashMap<>();

        HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        IAlertAble alertAble = SystemError.INTERNAL_ERROR;
        String detailMessage = null;

        // 本地api异常
        if (IAlertAble.class.isAssignableFrom(exception.getClass())) {
            alertAble = (IAlertAble) exception;
        } else if (exception instanceof NoHandlerFoundException) {
            httpStatus = HttpStatus.NOT_FOUND;
            alertAble = SystemError.NO_HANDLER_FOUND_ERROR;
        } else if (exception instanceof IllegalArgumentException
                // 验证器异常
                || exception instanceof ValidationException
                // servlet异常等
                || exception instanceof ServletException) {

            httpStatus = HttpStatus.BAD_REQUEST;
            alertAble = SystemError.ILLEGAL_PARAM;
        } else if (exception instanceof MethodArgumentNotValidException) {
            httpStatus = HttpStatus.BAD_REQUEST;
            alertAble = SystemError.ILLEGAL_PARAM;
            MethodArgumentNotValidException ex = (MethodArgumentNotValidException) exception;
            detailMessage = Optional.ofNullable(ex.getBindingResult())
                    .map(BindingResult::getAllErrors)
                    .orElse(Collections.emptyList())
                    .stream().map(ObjectError::getDefaultMessage)
                    .collect(Collectors.joining(StringPool.COMMA));
        }

        values.put(Constant.DEFAULT_RESPONSE_CODE, alertAble.getCode());
        values.put(Constant.DEFAULT_RESPONSE_MESSAGE, Optional.ofNullable(detailMessage).orElse(alertAble.getMessage()));
//        values.put(Constant.DEFAULT_RESPONSE_DETAIL_MESSAGE, detailMessage);
        values.put(Constant.DEFAULT_RESPONSE_TIMESTAMP, new Date());
        Optional.ofNullable(ThreadContext.get(Constant.MDC_REQUEST_ID_KEY)).ifPresent(v -> values.put(Constant.DEFAULT_RESPONSE_REQUEST_ID, v));

        response.setStatus(httpStatus.value());
        logException(exception);
        translateException(alertAble, exception, values, detailMessage);
        return values;
    }

    protected void logException(Exception exception) {
        if (exception instanceof BusinessException) {
            // 业务异常不打印堆栈信息
            log.error("BusinessException: [{}]{}", ((BusinessException) exception).getCode(), exception.getMessage());
        } else {
            log.error("ex:", exception);
        }
    }

    /**
     * 处理异常信息(国际化等)
     *
     * @param values
     */
    protected void translateException(IAlertAble alertAble, Exception exception, Map<String, Object> values, String detailMessage) {
        if (!(alertAble.getCode() instanceof String)) {
            return;
        }
        if (((String) alertAble.getCode()).length() >= Constant.ERROR_TRANSLATED_LENGTH) {
            return;
        }
        Object[] i18Args = (exception instanceof BusinessException) ? ((BusinessException) exception).getArgs() : null;
        // 翻译错误码和错误信息
        IAlertTranslator.AlertInfo alertInfo = alertTranslator.translate(alertAble, i18Args);
        values.put(Constant.DEFAULT_RESPONSE_CODE, alertInfo.getCode());
        values.put(Constant.DEFAULT_RESPONSE_MESSAGE, Optional.ofNullable(detailMessage).orElse(alertInfo.getMessage()));
    }


}
