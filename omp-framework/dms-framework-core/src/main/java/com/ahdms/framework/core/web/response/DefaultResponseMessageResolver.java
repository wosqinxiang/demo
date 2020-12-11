package com.ahdms.framework.core.web.response;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.alert.translator.IAlertTranslator;
import com.ahdms.framework.core.commom.util.JsonUtils;
import com.ahdms.framework.core.commom.util.DmsContextUtils;
import com.ahdms.framework.core.constant.Constant;
import com.ahdms.framework.core.constant.SystemError;
import com.ahdms.framework.core.exception.FrameworkException;
import lombok.AllArgsConstructor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Katrel.Zhou
 * @date 2019/5/28 9:49
 * @desc
 */
@AllArgsConstructor
public class DefaultResponseMessageResolver implements ResponseMessageResolver {

    private final IAlertTranslator alertTranslator;

    @Override
    public void successResolve(HttpServletRequest request, HttpServletResponse response, Object data) {
        resolve(request, response, data);
    }

    @Override
    public void failResolve(HttpServletRequest request, HttpServletResponse response, String code, String message,
                            Object... args) {
        Map<String, Object> result = new HashMap<>();
        IAlertTranslator.AlertInfo alertInfo = alertTranslator.translate(code, message, args);
        result.put(Constant.DEFAULT_RESPONSE_CODE, alertInfo.getCode());
        result.put(Constant.DEFAULT_RESPONSE_MESSAGE, alertInfo.getMessage());
        result.put(Constant.DEFAULT_RESPONSE_TIMESTAMP, new Date());
        result.put(Constant.DEFAULT_RESPONSE_REQUEST_ID, DmsContextUtils.getRequestId());
        resolve(request, response, result);
    }

    @Override
    public void failResolve(HttpServletRequest request, HttpServletResponse response, IAlertAble alertAble,
                            Object... args) {
        failResolve(request, response, (String) alertAble.getCode(), alertAble.getMessage(), args);
    }

    private void resolve(HttpServletRequest request, HttpServletResponse response, Object data) {
        try (PrintWriter writer = response.getWriter()) {
            JsonUtils.writeValue(writer, data);
            writer.flush();
        } catch (IOException e) {
            throw new FrameworkException(SystemError.INTERNAL_ERROR);
        }
    }
}

