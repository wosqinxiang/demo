/**
 * Created on 2016年12月5日 by lijiefeng
 */
package com.ahdms.ap.common;

import java.io.Serializable;

/**
 * @author lijiefeng
 * @version 1.0
 * @Title
 * @Description
 * @Copyright <p>Copyright (c) 2015</p>
 * @Company <p>迪曼森信息科技有限公司 Co., Ltd.</p>
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class HttpResponseBody<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 成功或失败的响应码，自己定义常量 如：0:成功，1：失败
     */
    private int code;

    /**
     * 响应消息，自己定义常量，如：用户名已存在
     */
    private String message;

    /**
     * 具体的业务数据
     */
    private T data;

    public HttpResponseBody() {
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

}

