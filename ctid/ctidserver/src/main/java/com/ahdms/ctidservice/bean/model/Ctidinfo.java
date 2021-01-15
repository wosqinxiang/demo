package com.ahdms.ctidservice.bean.model;

import java.util.Date;

public class Ctidinfo {
    private String id;

    private String userId;

    private String ctidInfo;

    private String appId;

    private Date ctidDownTime;

    private Integer ctidStatus;

    private String ctidValidDate;

    private String ctidNum;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getCtidInfo() {
        return ctidInfo;
    }

    public void setCtidInfo(String ctidInfo) {
        this.ctidInfo = ctidInfo == null ? null : ctidInfo.trim();
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public Date getCtidDownTime() {
        return ctidDownTime;
    }

    public void setCtidDownTime(Date ctidDownTime) {
        this.ctidDownTime = ctidDownTime;
    }

    public Integer getCtidStatus() {
        return ctidStatus;
    }

    public void setCtidStatus(Integer ctidStatus) {
        this.ctidStatus = ctidStatus;
    }

    public String getCtidValidDate() {
        return ctidValidDate;
    }

    public void setCtidValidDate(String ctidValidDate) {
        this.ctidValidDate = ctidValidDate == null ? null : ctidValidDate.trim();
    }

    public String getCtidNum() {
        return ctidNum;
    }

    public void setCtidNum(String ctidNum) {
        this.ctidNum = ctidNum == null ? null : ctidNum.trim();
    }
}