package com.ahdms.svs.client.constants;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qinxiang
 * @date 2020-12-25 17:29
 */
@Getter
@AllArgsConstructor
public enum ApiCode {
    SVS_CONNECT_ERROR("CORE20000","密码服务平台连接失败"),
    ;

    private String code;
    private String message;
}
