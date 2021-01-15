package com.ahdms.svs.client.result;

import com.ahdms.svs.client.constants.ApiCode;

/**
 * @author qinxiang
 * @date 2020-12-24 14:44
 */

public class ApiResult<T> {

    private String code;
    private String message;
    private T data;

    public ApiResult() {
    }

    public ApiResult(String code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResult<T> error(String message){
        return new ApiResult<>("1",message,null);
    }

    public static <T> ApiResult<T> success(T data){
        return new ApiResult<>("0","",data);
    }

    public static <T> ApiResult<T> error(ApiCode apiCode){
        return new ApiResult<>(apiCode.getCode(),apiCode.getMessage(),null);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
