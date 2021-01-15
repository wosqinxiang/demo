package com.ahdms.ap.vo;

import java.io.Serializable;
import java.util.Date;
/**
 * 同行人信息
 * @author PINZ
 *
 */
public class RetinueInfoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L; 
	
	private String personId;
	
	private String id ;
	
	private String name;
    
    private String idNo;

    private String mobile;

    private String address; 

    private String relationShip;
    
    private String outAddr;

    private String backAddr;

    private Byte plagueAreaFlag;

    private String tripOutType;

    private String tripOutNum;

    private String tripBackType;

    private String tripBackNum;
    
    private Byte temperature;  //体温  0 正常  1 异常
    
    private Float temperatureNum; // 
    
    private String workAddress;
    
    private String organization;
    
    private Byte backFromTowm;
    
    private String companyId; //企业ID
    
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	 
	public String getRelationShip() {
		return relationShip;
	}

	public void setRelationShip(String relationShip) {
		this.relationShip = relationShip;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RetinueInfoVO() {
		super();
		// TODO Auto-generated constructor stub
	}
 

	public RetinueInfoVO(String id, String name, String idNo, String mobile, String address, String relationShip,
			String outAddr, String backAddr, Byte plagueAreaFlag, String tripOutType, String tripOutNum,
			String tripBackType, String tripBackNum) {
		super();
		this.id = id;
		this.name = name;
		this.idNo = idNo;
		this.mobile = mobile;
		this.address = address;
		this.relationShip = relationShip;
		this.outAddr = outAddr;
		this.backAddr = backAddr;
		this.plagueAreaFlag = plagueAreaFlag;
		this.tripOutType = tripOutType;
		this.tripOutNum = tripOutNum;
		this.tripBackType = tripBackType;
		this.tripBackNum = tripBackNum;
	}
	
    
}
