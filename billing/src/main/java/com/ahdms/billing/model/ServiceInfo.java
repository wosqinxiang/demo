package com.ahdms.billing.model;

public class ServiceInfo {
    private String id;

    private String serviceName;

    private String serviceEncode;

    private String serviceType;
    
    private String typeName;

    private String specialLineCode;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName == null ? null : serviceName.trim();
    }

    public String getServiceEncode() {
        return serviceEncode;
    }

    public void setServiceEncode(String serviceEncode) {
        this.serviceEncode = serviceEncode == null ? null : serviceEncode.trim();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

    public String getSpecialLineCode() {
        return specialLineCode;
    }

    public void setSpecialLineCode(String specialLineCode) {
        this.specialLineCode = specialLineCode;
    }
}