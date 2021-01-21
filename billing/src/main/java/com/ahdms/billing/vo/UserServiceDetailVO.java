package com.ahdms.billing.vo;

import java.util.Date;

import com.ahdms.billing.model.ProviderInfo;
import com.ahdms.billing.model.ServiceInfo;
import com.ahdms.billing.model.SpecialLineInfo;

public class UserServiceDetailVO {
	
	private String id;

    private Integer serviceCount;

    private Date endTime;

    private Integer tps;

    private String providerName;
    
    private String specialName; //专线编码
    
    private SpecialLineVO specialInfo;
    
    private int isExpired;  //是否已过期(0.否  1. 是)

    private String serviceName;
    
    private ServiceInfo serviceInfo;
    
	public SpecialLineVO getSpecialInfo() {
		return specialInfo;
	}

	public void setSpecialInfo(SpecialLineVO specialInfo) {
		this.specialInfo = specialInfo;
	}

	public ServiceInfo getServiceInfo() {
		return serviceInfo;
	}

	public void setServiceInfo(ServiceInfo serviceInfo) {
		this.serviceInfo = serviceInfo;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Integer getServiceCount() {
		return serviceCount;
	}

	public void setServiceCount(Integer serviceCount) {
		this.serviceCount = serviceCount;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Integer getTps() {
		return tps;
	}

	public void setTps(Integer tps) {
		this.tps = tps;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}

	public String getSpecialName() {
		return specialName;
	}

	public void setSpecialName(String specialName) {
		this.specialName = specialName;
	}

	public int getIsExpired() {
		return isExpired;
	}

	public void setIsExpired(int isExpired) {
		this.isExpired = isExpired;
	}

	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
    
}
