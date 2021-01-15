package com.ahdms.ctidservice.bean.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("key_info")
public class KeyInfo {
    private Integer id;

    private String appId;

    private String secretKey;
    
    private String serverAccount;
    
    private String appPackage;
    
    private String sdkCode;

    @TableField("organizeId")
    private String organizeId;

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

	public String getSdkCode() {
		return sdkCode;
	}

	public void setSdkCode(String sdkCode) {
		this.sdkCode = sdkCode;
	}

	public String getOrganizeId() {
		return organizeId;
	}

	public void setOrganizeId(String organizeId) {
		this.organizeId = organizeId;
	}
	
}