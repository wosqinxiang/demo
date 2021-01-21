package com.ahdms.billing.vo;

import java.util.List;


public class ChannelDataReportVO {
	
	private String code;
	
	private String name;
	
	private int totalCount;
	
	private List<ServiceReport> channelList;
	
	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List<ServiceReport> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ServiceReport> channelList) {
		this.channelList = channelList;
	}

	public static class ServiceReport {
		private String serviceCode;
		
		private String serviceName;
		
		private int count;

		public String getServiceCode() {
			return serviceCode;
		}

		public void setServiceCode(String serviceCode) {
			this.serviceCode = serviceCode;
		}

		public String getServiceName() {
			return serviceName;
		}

		public void setServiceName(String serviceName) {
			this.serviceName = serviceName;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
		
	}

}
