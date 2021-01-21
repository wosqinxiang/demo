package com.ahdms.billing.vo.query;

public class SpecialLineQueryVO {
	
	private String name;
	
	private String code;
	
	private String providerId;

	public SpecialLineQueryVO() {
		super();
	}

	public SpecialLineQueryVO(String providerId,String name, String code) {
		super();
		this.providerId = providerId;
		this.name = name;
		this.code = code;
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

	public String getProviderId() {
		return providerId;
	}

	public void setProviderId(String providerId) {
		this.providerId = providerId;
	}
	
	
	
}
