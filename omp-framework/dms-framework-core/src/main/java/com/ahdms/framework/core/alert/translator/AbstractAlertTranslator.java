package com.ahdms.framework.core.alert.translator;

import com.ahdms.framework.core.alert.IAlertAble;
import com.ahdms.framework.core.commom.util.ObjectUtils;
import com.ahdms.framework.core.env.DmsServerProperties;
import com.ahdms.framework.core.env.ServerInfo;
import lombok.RequiredArgsConstructor;

/**
 * @author zhoumin
 * @version 1.0.0
 * @date 2020/7/3 9:41
 */
@RequiredArgsConstructor
public abstract class AbstractAlertTranslator implements IAlertTranslator {

    private static final int CODE_MAX_LENGTH = 11;
    private final ServerInfo serverInfo;
    private final DmsServerProperties properties;

    private static final String SYSTEM_PREFIX = "SYS";
    private static final String SUCCESS_CODE = "1";

    /**
     * 组装完整的 11 位 code
     *
     * @param alertAble 返回的code
     * @return 完整的code
     */
    protected String getFullCode(IAlertAble alertAble) {
        return getFullCode((String) alertAble.getCode());
    }

    protected String getFullCode(String code) {
        if (ObjectUtils.nullSafeEquals(SUCCESS_CODE, code)
                || code.length() >= 11) {
            return code;
        }
        StringBuilder codeBuilder = new StringBuilder(11);
        // 系统 code 已经添加了前缀
        if (!code.startsWith(SYSTEM_PREFIX)) {
            // 三位服务名缩写（大写）
            String shortName = properties.getServer().getShortName();
            codeBuilder.append(shortName.toUpperCase());
        }
        codeBuilder.append(code);
        codeBuilder.append(serverInfo.getIpSuffix());
        return codeBuilder.toString();
    }

    @Override
    public AlertInfo translate(IAlertAble alertAble, Object[] args) {
        return translate((String) alertAble.getCode(), alertAble.getMessage(), args);
    }

    /**
     * code 翻译
     *
     * @param code
     * @param message
     * @param args
     * @return
     */
    @Override
    public AlertInfo translate(String code, String message, Object[] args) {
        if (code.length() >= CODE_MAX_LENGTH) {
            message = doTranslation(code, message, args);
            return AlertInfo.builder().code(code).message(message).build();
        }
        // 系统 code 已经添加了前缀
        if (!code.startsWith(SYSTEM_PREFIX)) {
            // 三位服务名缩写（大写）
            String shortName = properties.getServer().getShortName();
            code = shortName + code;
        }
        message = doTranslation(code, message, args);
        code = code + serverInfo.getIpSuffix();
        return AlertInfo.builder().code(code).message(message).build();
    }

    /**
     * 翻译API定义
     *
     * @param code
     * @param message
     * @param args
     * @return
     */
    public abstract String doTranslation(String code, String message, Object[] args);
}
