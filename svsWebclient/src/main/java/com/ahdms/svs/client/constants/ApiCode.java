package com.ahdms.svs.client.constants;


/**
 * @author qinxiang
 * @date 2020-12-25 17:29
 */

public enum ApiCode {
    SVS_CONNECT_ERROR("CORE20000", "密码服务平台连接失败"),
    ;

    private String code;
    private String message;

    ApiCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
