package com.ahdms.ap.model;

import java.util.Date;

public class AuthRecord {
    private String id;

    private Integer infoSource;

    private String name;

    private String idcard;

    private Integer authType;

    private Integer ctidType;

    private Integer authResult;

    private String serverAccount;

    private Date createTime;

    private String serialNum;

    private String pic;
    
    private String signData;

    private String openid;
    
    private String location;
    
    private String authDesc; 
    
    private Integer authObject;
    
    private String certifiedId;
    
    

    public Integer getAuthObject() {
		return authObject;
	}

	public void setAuthObject(Integer authObject) {
		this.authObject = authObject;
	}

	public String getCertifiedId() {
		return certifiedId;
	}

	public void setCertifiedId(String certifiedId) {
		this.certifiedId = certifiedId;
	}

	public String getAuthDesc() {
		return authDesc;
	}

	public void setAuthDesc(String authDesc) {
		this.authDesc = authDesc;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getOpenid() {
		return openid;
	}

	public void setOpenid(String openid) {
		this.openid = openid;
	}

	public String getSignData() {
		return signData;
	}

	public void setSignData(String signData) {
		this.signData = signData;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getInfoSource() {
        return infoSource;
    }

    public void setInfoSource(Integer infoSource) {
        this.infoSource = infoSource;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard == null ? null : idcard.trim();
    }

    public Integer getAuthType() {
        return authType;
    }

    public void setAuthType(Integer authType) {
        this.authType = authType;
    }

    public Integer getCtidType() {
        return ctidType;
    }

    public void setCtidType(Integer ctidType) {
        this.ctidType = ctidType;
    }

    public Integer getAuthResult() {
        return authResult;
    }

    public void setAuthResult(Integer authResult) {
        this.authResult = authResult;
    }

    public String getServerAccount() {
        return serverAccount;
    }

    public void setServerAccount(String serverAccount) {
        this.serverAccount = serverAccount == null ? null : serverAccount.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getSerialNum() {
        return serialNum;
    }

    public void setSerialNum(String serialNum) {
        this.serialNum = serialNum == null ? null : serialNum.trim();
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic == null ? null : pic.trim();
    }

	public AuthRecord() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AuthRecord(String id, Integer infoSource, String name, String idcard, Integer authType, Integer ctidType,
			Integer authResult, String serverAccount, Date createTime, String serialNum, String pic, String signData,
			String openid, String location, String authDesc, Integer authObject, String certifiedId) {
		super();
		this.id = id;
		this.infoSource = infoSource;
		this.name = name;
		this.idcard = idcard;
		this.authType = authType;
		this.ctidType = ctidType;
		this.authResult = authResult;
		this.serverAccount = serverAccount;
		this.createTime = createTime;
		this.serialNum = serialNum;
		this.pic = pic;
		this.signData = signData;
		this.openid = openid;
		this.location = location;
		this.authDesc = authDesc;
		this.authObject = authObject;
		this.certifiedId = certifiedId;
	}

	 
    
}