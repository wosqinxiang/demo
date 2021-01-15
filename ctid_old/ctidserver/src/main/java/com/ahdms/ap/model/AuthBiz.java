package com.ahdms.ap.model;

import java.util.Date;

public class AuthBiz {
    private String id;

    private String serialNum;

    private String serverAccount;

    private Integer infoSource;

    private String url;

    private Integer isCallback;

    private Date createTime;
    
    private String serverDesc;
    
    private int authType; 

    public int getAuthType() {
		return authType;
	}

	public void setAuthType(int authType) {
		this.authType = authType;
	}

	public String getServerDesc() {
		return serverDesc;
	}

	public void setServerDesc(String serverDesc) {
		this.serverDesc = serverDesc;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public String getServerAccount() {
        return serverAccount;
    }

    public void setServerAccount(String serverAccount) {
        this.serverAccount = serverAccount == null ? null : serverAccount.trim();
    }

    public Integer getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(Integer infoSource) {
        this.infoSource = infoSource;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url == null ? null : url.trim();
    }

    public Integer getIsCallback() {
        return isCallback;
    }

    public void setIsCallback(Integer isCallback) {
        this.isCallback = isCallback;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}