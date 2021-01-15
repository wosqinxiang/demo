package com.ahdms.exception;

import com.ahdms.code.ApiCode;
import lombok.Getter;

/**
 * @author qinxiang
 * @date 2020-12-21 14:00
 */
public class ApiException extends RuntimeException{

    @Getter
    private String code;

    public ApiException(String code,String message){
        super(message);
        this.code = code;
    }

    public ApiException(ApiCode apiCode){
        super(apiCode.getMessage());
        this.code = apiCode.getCode();
    }

    public ApiException(String message){
        super(message);
    }

}
