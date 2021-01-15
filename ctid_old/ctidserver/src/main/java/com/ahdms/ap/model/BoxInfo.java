package com.ahdms.ap.model;

import java.util.Date;

public class BoxInfo { 
	private String boxNum;

    private String boxId;

    private String boxLocation;

    private Date createTime; 

    private Date updateTime;

    private String boxType;

    private String serverDepartment;
    
    private String typeId;

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

	 

	public BoxInfo(String boxNum, String boxId, String boxLocation, Date createTime, Date updateTime, String boxType,
			String serverDepartment, String typeId) {
		super();
		this.boxNum = boxNum;
		this.boxId = boxId;
		this.boxLocation = boxLocation;
		this.createTime = createTime;
		this.updateTime = updateTime;
		this.boxType = boxType;
		this.serverDepartment = serverDepartment;
		this.typeId = typeId;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public BoxInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}