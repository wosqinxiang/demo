package com.ahdms.billing.vo;

public class SpecialLineVO {
	
	private String id;
	
	private String name;
	
	private String code;
	
	private String providerName;
	
	private String providerId;
	
	public SpecialLineVO() {
		super();
	}

	public SpecialLineVO(String id, String name, String code, String providerName,String providerId) {
		super();
		this.id = id;
		this.name = name;
		this.code = code;
		this.providerName = providerName;
		this.providerId = providerId;
	}
	
	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	
}
