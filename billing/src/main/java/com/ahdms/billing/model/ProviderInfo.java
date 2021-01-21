package com.ahdms.billing.model;

public class ProviderInfo {
    private String id;

    private String providerName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName == null ? null : providerName.trim();
    }


	public ProviderInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public ProviderInfo(String id, String providerName) {
		super();
		this.id = id;
		this.providerName = providerName;
	}
    
    
}