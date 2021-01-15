package com.ahdms.ap.model;

import java.util.Date;

public class BoxType {
    private String id;

    private String province;

    private String city;

    private String typeName;

    private Date createTime;

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
		// TODO Auto-generated constructor stub
	}
    
    
}