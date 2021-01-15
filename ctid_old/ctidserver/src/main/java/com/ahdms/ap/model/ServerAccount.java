package com.ahdms.ap.model;

import java.io.Serializable;
import java.util.Date;

public class ServerAccount implements Serializable{
    /**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private String id;

    private String account;

    private String password;

    private Date createTime;

    private String type;

    private Integer isDel;
    
    private String ServerDesc; 
  

	public String getServerDesc() {
		return ServerDesc;
	}

	public void setServerDesc(String serverDesc) {
		ServerDesc = serverDesc;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account == null ? null : account.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public Integer getIsDel() {
        return isDel;
    }

    public void setIsDel(Integer isDel) {
        this.isDel = isDel;
    }
}