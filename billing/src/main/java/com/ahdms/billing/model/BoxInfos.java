package com.ahdms.billing.model;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;

import com.alibaba.fastjson.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;

public class BoxInfos { 
	private String boxNum;

    private String boxId;

    private String boxLocation;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date createTime; 
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss", locale = "zh", timezone = "GMT+8")
    private Date updateTime;

    private String boxType;

    private String serverDepartment;
    
    private String typeId;
    
    private String typeName;
    
    private String province;

    private String city;
    
    private Integer counts;

    public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public String getBoxNum() {
        return boxNum;
    }

    public void setBoxNum(String boxNum) {
        this.boxNum = boxNum == null ? null : boxNum.trim();
    }

    public String getBoxId() {
        return boxId;
    }

    public void setBoxId(String boxId) {
        this.boxId = boxId == null ? null : boxId.trim();
    }

    public String getBoxLocation() {
        return boxLocation;
    }

    public void setBoxLocation(String boxLocation) {
        this.boxLocation = boxLocation == null ? null : boxLocation.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getBoxType() {
        return boxType;
    }

    public void setBoxType(String boxType) {
        this.boxType = boxType == null ? null : boxType.trim();
    }

    public String getServerDepartment() {
        return serverDepartment;
    }

    public void setServerDepartment(String serverDepartment) {
        this.serverDepartment = serverDepartment == null ? null : serverDepartment.trim();
    } 
 
	
    public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}

	 

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	} 

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BoxInfos() {
		super();
		// TODO Auto-generated constructor stub
	} 

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public BoxInfos(String boxNum, String boxId, String boxLocation, Date createTime, Date updateTime, String boxType,
			String serverDepartment, String typeId, String typeName, String province, String city, Integer counts) {
		super();
		this.boxNum = boxNum;
		this.boxId = boxId;
		this.boxLocation = boxLocation;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.boxType = boxType;
		this.serverDepartment = serverDepartment;
		this.typeId = typeId;
		this.typeName = typeName;
		this.province = province;
		this.city = city;
		this.counts = counts;
	}

	 

	 
    
}