package com.ahdms.ap.vo;

/**
 * WxMiniProgramVo class
 *
 * @author hetao
 * @date 2019/08/01
 */
public class WxMiniProgramVo {

    /**
     * 微信小程序用户登录的openid
     */
    private String openid;

    /**
     * 微信小程序用户登录的session_key
     */
    private String sessionKey;

    /**
     * 成功或失败的响应码，自己定义常量 如：0:成功，1：失败
     */
    private int code;

    /**
     * 响应消息，自己定义常量，如：用户名已存在
     */
    private String message;
    
    private String token;

    public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getOpenid() {
        return openid;
    }

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
