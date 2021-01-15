package com.ahdms.ctidservice.code;

import com.ahdms.framework.core.alert.IAlertAble;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author qinxiang
 * @date 2021-01-04 11:40
 */
@Getter
@AllArgsConstructor
public enum ApiCode implements IAlertAble {

    MD5_VAERFIY_ERROR("","请求数据验签失败"),

    ;

    private String code;
    private String message;

}
