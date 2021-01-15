package com.ahdms.ctidservice.db.model;

import java.util.Date;

public class CtidLog {
    public static final Integer DOWNLOAD_TYPE = 1;
    public static final Integer AUTH_TYPE = 2;
    public static final Integer CREATE_CODE_TYPE = 3;
    public static final Integer VALIDATE_CODE_TYPE = 4;

    private String id;

    private String appId;

    private Integer type;

    private String mode;

    private Integer result;

    private String errorDesc;

    private Date createTime;

    private String ip;

    private String ctidBsn;

    public CtidLog() {
    }

    public CtidLog(String id, String appId, Integer type, String mode, Integer result, String errorDesc, Date createTime, String ip, String ctidBsn) {
        this.id = id;
        this.appId = appId;
        this.type = type;
        this.mode = mode;
        this.result = result;
        this.errorDesc = errorDesc;
        this.createTime = createTime;
        this.ip = ip;
        this.ctidBsn = ctidBsn;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode == null ? null : mode.trim();
    }

    public Integer getResult() {
        return result;
    }

    public void setResult(Integer result) {
        this.result = result;
    }

    public String getErrorDesc() {
        return errorDesc;
    }

    public void setErrorDesc(String errorDesc) {
        this.errorDesc = errorDesc == null ? null : errorDesc.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip == null ? null : ip.trim();
    }

    public String getCtidBsn() {
        return ctidBsn;
    }

    public void setCtidBsn(String ctidBsn) {
        this.ctidBsn = ctidBsn == null ? null : ctidBsn.trim();
    }
}