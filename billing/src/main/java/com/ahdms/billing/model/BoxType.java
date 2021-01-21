package com.ahdms.billing.model;

import java.util.Date;

import com.alibaba.fastjson.annotation.JSONField;

public class BoxType {
    private String id;

    private String province;

    private String city;

    private String typeName;
    
    @JSONField(format="yyyy-MM-dd HH:mm:ss")
    private Date createTime;
    
    private String userServiceAccount;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName == null ? null : typeName.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    
	public String getUserServiceAccount() {
		return userServiceAccount;
	}

	public void setUserServiceAccount(String userServiceAccount) {
		this.userServiceAccount = userServiceAccount;
	}

	public BoxType(String id, String province, String city, String typeName, Date createTime) {
		super();
		this.id = id;
		this.province = province;
		this.city = city;
		this.typeName = typeName;
		this.createTime = createTime;
	}

	public BoxType() {
		super();
	}
    
    
}