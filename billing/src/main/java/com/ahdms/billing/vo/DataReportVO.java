package com.ahdms.billing.vo;

import java.util.List;

public class DataReportVO {
	
	private String code;
	
	private String name;
	
	private int totalCount;
	
	private List<ChannelReport> channelList;
	
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

	public List<ChannelReport> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<ChannelReport> channelList) {
		this.channelList = channelList;
	}

	public static class ChannelReport{
		private String channelCode;
		
		private String channelName;
		
		private int count;

		public String getChannelCode() {
			return channelCode;
		}

		public void setChannelCode(String channelCode) {
			this.channelCode = channelCode;
		}

		public String getChannelName() {
			return channelName;
		}

		public void setChannelName(String channelName) {
			this.channelName = channelName;
		}

		public int getCount() {
			return count;
		}

		public void setCount(int count) {
			this.count = count;
		}
		
	}

}
