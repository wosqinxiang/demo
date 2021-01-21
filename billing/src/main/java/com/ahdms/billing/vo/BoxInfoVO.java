package com.ahdms.billing.vo;

import java.io.Serializable;
import java.util.Date;

public class BoxInfoVO implements Serializable { 
	/**
	 * Comment for <code>serialVersionUID</code>
	 */
	private static final long serialVersionUID = 1L;

	private String boxNum;

    private String boxId;

    private String boxLocation;

    private String createTime;

    private String boxType;

    private String serverDepartment;
    
    private String updateTime;
    
    private String TypeId; 
    
    private String TypeName;
    
    private Integer counts;
    

    public Integer getCounts() {
		return counts;
	}

	public void setCounts(Integer counts) {
		this.counts = counts;
	}

	public String getTypeName() {
		return TypeName;
	}

	public void setTypeName(String typeName) {
		TypeName = typeName;
	}

	public String getTypeId() {
		return TypeId;
	}

	public void setTypeId(String typeId) {
		TypeId = typeId;
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

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
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
 
	
    public String getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(String updateTime) {
		this.updateTime = updateTime;
	}
 

	public BoxInfoVO(String boxNum, String boxId, String boxLocation, String createTime, String boxType,
			String serverDepartment, String updateTime, String typeId) {
		super();
		this.boxNum = boxNum;
		this.boxId = boxId;
		this.boxLocation = boxLocation;
		this.createTime = createTime;
		this.boxType = boxType;
		this.serverDepartment = serverDepartment;
		this.updateTime = updateTime;
		TypeId = typeId;
	}

	public BoxInfoVO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}