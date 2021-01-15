package com.ahdms.ctidservice.bean.model;

/**
 * @author qinxiang
 * @date 2020-11-26 16:49
 */
public class CtidPidInfos {

    private String id;
    private String ctidInfosId;
    private String pid;
    private String speciallineCode;

    public CtidPidInfos() {
        super();
    }

    public CtidPidInfos(String id, String ctidInfosId, String pid, String speciallineCode) {
        this.id = id;
        this.ctidInfosId = ctidInfosId;
        this.pid = pid;
        this.speciallineCode = speciallineCode;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCtidInfosId() {
        return ctidInfosId;
    }

    public void setCtidInfosId(String ctidInfosId) {
        this.ctidInfosId = ctidInfosId;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getSpeciallineCode() {
        return speciallineCode;
    }

    public void setSpeciallineCode(String speciallineCode) {
        this.speciallineCode = speciallineCode;
    }
}
