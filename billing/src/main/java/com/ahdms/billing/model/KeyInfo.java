package com.ahdms.billing.model;

public class KeyInfo {
    private Integer id;

    private String appId;

    private String secretKey;
    
    private String serverAccount;
    
    private String appPackage;
    
    private String userServiceAccount;
    
    private String organizeId;
    
    public String getUserServiceAccount() {
		return userServiceAccount;
	}

	public void setUserServiceAccount(String userServiceAccount) {
		this.userServiceAccount = userServiceAccount;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId == null ? null : appId.trim();
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey == null ? null : secretKey.trim();
    }
    
    public String getServerAccount() {
        return serverAccount;
    }

    public void setServerAccount(String serverAccount) {
        this.serverAccount = serverAccount == null ? null : serverAccount.trim();
    }

	public String getAppPackage() {
		return appPackage;
	}

	public void setAppPackage(String appPackage) {
		this.appPackage = appPackage == null ? null : appPackage.trim();
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}
	
}