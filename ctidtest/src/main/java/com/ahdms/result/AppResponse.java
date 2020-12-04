package com.ahdms.result;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author qinxiang
 * @date 2020-12-04 14:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppResponse<T> {

    private String code;
    private String message;
    private T data;

    public static <T> AppResponse<T> success(T data){
        return new AppResponse("200","success",data);
    }

    public static <T> AppResponse<T> error(String message){
        return new AppResponse("500",message,null);
    }

}
