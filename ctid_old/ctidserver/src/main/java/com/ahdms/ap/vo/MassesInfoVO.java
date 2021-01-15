package com.ahdms.ap.vo;

import java.io.Serializable;
import java.util.List;
/**
 * 主体人信息
 * @author PINZ
 *
 */
public class MassesInfoVO implements Serializable{
	 
	private static final long serialVersionUID = 1L;
	
		private String personId;

		private String name;
	
	 	private String idNo;

	    private String mobile;

	    private String address; 

	    private String openid;  
	    
	    private String outAddr;

	    private String backAddr;

	    private Byte plagueAreaFlag;

	    private String tripOutType;

	    private String tripOutNum;

	    private String tripBackType;

	    private String tripBackNum;
	    
	    private String carNum;
	    
	    private Byte temperature;  //体温  0 正常  1 异常
	    
	    private String workAddress;
	    
	    private String organization;
	    
	    private Byte backFromTowm;
	    
	    private Float temperatureNum;
	    
	    private String companyId; //企业ID
	    
	    private Byte isWork; //是否复工 （0,是 1.否）
	    
	    private Byte reason;  //未复工原因  (0.其他，1.隔离观察  2.治疗)
	    
	    private Byte location; //目前所在地  (0.本市  1.外地)
	    
	    private String towmAbbr; // 来源地信息  ( 0000 )
	    
	    private List<TripModeVO> tripMode; // 出行方式
	    
	    public List<TripModeVO> getTripMode() {
			return tripMode;
		}

		public void setTripMode(List<TripModeVO> tripMode) {
			this.tripMode = tripMode;
		}

		public String getTowmAbbr() {
			return towmAbbr;
		}

		public void setTowmAbbr(String towmAbbr) {
			this.towmAbbr = towmAbbr;
		}

		public Byte getIsWork() {
			return isWork;
		}

		public void setIsWork(Byte isWork) {
			this.isWork = isWork;
		}

		public Byte getReason() {
			return reason;
		}

		public void setReason(Byte reason) {
			this.reason = reason;
		}

		public Byte getLocation() {
			return location;
		}

		public void setLocation(Byte location) {
			this.location = location;
		}
	    
	    public String getCompanyId() {
			return companyId;
		}

		public void setCompanyId(String companyId) {
			this.companyId = companyId;
		}
	    
		public Float getTemperatureNum() {
			return temperatureNum;
		}

		public void setTemperatureNum(Float temperatureNum) {
			this.temperatureNum = temperatureNum;
		}

		public Byte getBackFromTowm() {
			return backFromTowm;
		}

		public void setBackFromTowm(Byte backFromTowm) {
			this.backFromTowm = backFromTowm;
		}

		public String getWorkAddress() {
			return workAddress;
		}

		public void setWorkAddress(String workAddress) {
			this.workAddress = workAddress;
		}

		public String getOrganization() {
			return organization;
		}

		public void setOrganization(String organization) {
			this.organization = organization;
		}

		public String getPersonId() {
			return personId;
		}

		public void setPersonId(String personId) {
			this.personId = personId;
		}

		public Byte getTemperature() {
			return temperature;
		}

		public void setTemperature(Byte temperature) {
			this.temperature = temperature;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getIdNo() {
			return idNo;
		}

		public void setIdNo(String idNo) {
			this.idNo = idNo;
		}

		public String getMobile() {
			return mobile;
		}

		public void setMobile(String mobile) {
			this.mobile = mobile;
		}

		public String getAddress() {
			return address;
		}

		public void setAddress(String address) {
			this.address = address;
		}

		public String getOpenid() {
			return openid;
		}

		public void setOpenid(String openid) {
			this.openid = openid;
		}

		public String getOutAddr() {
			return outAddr;
		}

		public void setOutAddr(String outAddr) {
			this.outAddr = outAddr;
		}

		public String getBackAddr() {
			return backAddr;
		}

		public void setBackAddr(String backAddr) {
			this.backAddr = backAddr;
		}

		public Byte getPlagueAreaFlag() {
			return plagueAreaFlag;
		}

		public void setPlagueAreaFlag(Byte plagueAreaFlag) {
			this.plagueAreaFlag = plagueAreaFlag;
		}

		public String getTripOutType() {
			return tripOutType;
		}

		public void setTripOutType(String tripOutType) {
			this.tripOutType = tripOutType;
		}

		public String getTripOutNum() {
			return tripOutNum;
		}

		public void setTripOutNum(String tripOutNum) {
			this.tripOutNum = tripOutNum;
		}

		public String getTripBackType() {
			return tripBackType;
		}

		public void setTripBackType(String tripBackType) {
			this.tripBackType = tripBackType;
		}

		public String getTripBackNum() {
			return tripBackNum;
		}

		public void setTripBackNum(String tripBackNum) {
			this.tripBackNum = tripBackNum;
		}
		

		public String getCarNum() {
			return carNum;
		}

		public void setCarNum(String carNum) {
			this.carNum = carNum;
		}

		public MassesInfoVO() {
			super();
			// TODO Auto-generated constructor stub
		}

		public MassesInfoVO(String name, String idNo, String mobile, String address, String openid, String outAddr,
				String backAddr, Byte plagueAreaFlag, String tripOutType, String tripOutNum, String tripBackType,
				String tripBackNum, String carNum) {
			super();
			this.name = name;
			this.idNo = idNo;
			this.mobile = mobile;
			this.address = address;
			this.openid = openid;
			this.outAddr = outAddr;
			this.backAddr = backAddr;
			this.plagueAreaFlag = plagueAreaFlag;
			this.tripOutType = tripOutType;
			this.tripOutNum = tripOutNum;
			this.tripBackType = tripBackType;
			this.tripBackNum = tripBackNum;
			this.carNum = carNum;
		}

		 
	    
		

}
