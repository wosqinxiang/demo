package com.ahdms.ap.vo;

import java.io.Serializable;
import java.util.List;

public class PlagueRecordShowVO implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;

	private String id_no;

	private String mobile;

	private Float temperatureNum;

	private Byte isWork;

	private String companyarea;

	private List<DiagnosisVO> personInfos; //确诊人员

	private List<DiagnosisVO> suspectedPerson; //疑似人员 
    
    private Byte fourteenDay;
    
    private Byte touchSuspected;
    

	public Byte getFourteenDay() {
		return fourteenDay;
	}

	public void setFourteenDay(Byte fourteenDay) {
		this.fourteenDay = fourteenDay;
	}

	public Byte getTouchSuspected() {
		return touchSuspected;
	}

	public void setTouchSuspected(Byte touchSuspected) {
		this.touchSuspected = touchSuspected;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getId_no() {
		return id_no;
	}

	public void setId_no(String id_no) {
		this.id_no = id_no;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public Float getTemperatureNum() {
		return temperatureNum;
	}

	public void setTemperatureNum(Float temperatureNum) {
		this.temperatureNum = temperatureNum;
	}

	public Byte getIsWork() {
		return isWork;
	}

	public void setIsWork(Byte isWork) {
		this.isWork = isWork;
	}

	

	public String getCompanyarea() {
		return companyarea;
	}

	public void setCompanyarea(String companyarea) {
		this.companyarea = companyarea;
	}

	public String getCompanyname() {
		return companyname;
	}

	public void setCompanyname(String companyname) {
		this.companyname = companyname;
	}

	public List<DiagnosisVO> getPersonInfos() {
		return personInfos;
	}

	public void setPersonInfos(List<DiagnosisVO> personInfos) {
		this.personInfos = personInfos;
	}

	public List<DiagnosisVO> getSuspectedPerson() {
		return suspectedPerson;
	}

	public void setSuspectedPerson(List<DiagnosisVO> suspectedPerson) {
		this.suspectedPerson = suspectedPerson;
	}

	public String getCompanybranch() {
		return companybranch;
	}

	public void setCompanybranch(String companybranch) {
		this.companybranch = companybranch;
	}

	public String getLeader() {
		return leader;
	}

	public void setLeader(String leader) {
		this.leader = leader;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	private String companyname;

	private String companybranch;

	private String leader;

	private String phone;

	public PlagueRecordShowVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public PlagueRecordShowVO(String name, String id_no, String mobile, Float temperatureNum, Byte isWork,
			String companyarea, List<DiagnosisVO> personInfos, List<DiagnosisVO> suspectedPerson, String companyname,
			String companybranch, String leader, String phone) {
		super();
		this.name = name;
		this.id_no = id_no;
		this.mobile = mobile;
		this.temperatureNum = temperatureNum;
		this.isWork = isWork;
		this.companyarea = companyarea;
		this.personInfos = personInfos;
		this.suspectedPerson = suspectedPerson;
		this.companyname = companyname;
		this.companybranch = companybranch;
		this.leader = leader;
		this.phone = phone;
	}
	
	
	

}
