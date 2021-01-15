package com.ahdms.ap.vo;

public class SearchPlaceVO {
	
	private String organization; //工作单位
	private String workAddress;  //单位地址
	
	
	public String getOrganization() {
		return organization;
	}
	public void setOrganization(String organization) {
		this.organization = organization;
	}
	public String getWorkAddress() {
		return workAddress;
	}
	public void setWorkAddress(String workAddress) {
		this.workAddress = workAddress;
	}
	
	
	public SearchPlaceVO() {
		super();
	}
	public SearchPlaceVO(String organization, String workAddress) {
		super();
		this.organization = organization;
		this.workAddress = workAddress;
	}
	
	
	
}
