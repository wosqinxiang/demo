package com.ahdms.ap.vo;

public class TripModeVO {
	
	private String time;  //时间
	
	private String fromPlace; //出发地
	
	private String toPlace;   //目的地
	
	private String tripType;   //出行交通工具
	
	private String tripNum;   // 班次/车牌

	public String getFromPlace() {
		return fromPlace;
	}

	public void setFromPlace(String fromPlace) {
		this.fromPlace = fromPlace;
	}

	public String getToPlace() {
		return toPlace;
	}

	public void setToPlace(String toPlace) {
		this.toPlace = toPlace;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getTripNum() {
		return tripNum;
	}

	public void setTripNum(String tripNum) {
		this.tripNum = tripNum;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
	
}
