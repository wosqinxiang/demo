package com.ahdms.billing.model;

import java.util.Date;

public class ServiceLogQuery {

    private Date beginDate;

    private Date endDate;

    private String username;

    private String typeId;

    private String serviceEncode;

    private String channelEncode;
    
    private String dateStr;
    
    private String monthStr;
    
    public String getMonthStr() {
		return monthStr;
	}

	public void setMonthStr(String monthStr) {
		this.monthStr = monthStr;
	}

	public String getDateStr() {
		return dateStr;
	}

	public void setDateStr(String dateStr) {
		this.dateStr = dateStr;
	}

	public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getServiceEncode() {
        return serviceEncode;
    }

    public void setServiceEncode(String serviceEncode) {
        this.serviceEncode = serviceEncode;
    }

    public String getChannelEncode() {
        return channelEncode;
    }

    public void setChannelEncode(String channelEncode) {
        this.channelEncode = channelEncode;
    }

    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
}
