package com.ahdms.aop;

import com.ahdms.result.AppResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author qinxiang
 * @date 2020-12-04 14:29
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseBody
    AppResponse handleException(Exception e){
        return AppResponse.error(e.getMessage());
    }


    /**
     * 处理所有接口数据验证异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    AppResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e){

        return AppResponse.error(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

}
