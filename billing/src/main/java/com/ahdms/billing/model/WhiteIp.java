package com.ahdms.billing.model;

/**
 * @author qinxiang
 * @date 2020-11-27 10:45
 */
public class WhiteIp {

    private String id;
    private String userId;
    private String ip;

    public WhiteIp() {
        super();
    }

    public WhiteIp(String id, String userId, String ip) {
        this.id = id;
        this.userId = userId;
        this.ip = ip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
