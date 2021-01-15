package com.ahdms.ap.vo;

import java.util.Date;

public class CountSearchVO {
	
	private String companyId; //根据企业Id统计
	
	private String companyArea; //根据 区域统计
	
	private String companytype; // 企业行业0快递
	
	private Date countDate; //根据时间统计
	
//	private List<String> townAbbrs; // 根据来源地查询
	
	public String getCompanytype() {
		return companytype;
	}

	public void setCompanytype(String companytype) {
		this.companytype = companytype;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyArea() {
		return companyArea;
	}

	public void setCompanyArea(String companyArea) {
		this.companyArea = companyArea;
	}

	public Date getCountDate() {
		return countDate;
	}

	public void setCountDate(Date countDate) {
		this.countDate = countDate;
	}
	
}
