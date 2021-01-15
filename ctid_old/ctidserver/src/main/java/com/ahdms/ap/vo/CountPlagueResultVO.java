package com.ahdms.ap.vo;

import java.io.Serializable;

public class CountPlagueResultVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String  companyArea;
	
	private String companyName;
	
	private Integer companySize;
	
	private int countAllRegist;
	
	private int countAllIsWork;
	
	private int countAffirm ;
	
	private int countSuspected ;
	
	private int countTemperatureWrong;
	
	private String leader;
	
	private String phone;

	public String getCompanyArea() {
		return companyArea;
	}

	public void setCompanyArea(String companyArea) {
		this.companyArea = companyArea;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Integer getCompanySize() {
		return companySize;
	}

	public void setCompanySize(Integer companySize) {
		this.companySize = companySize;
	}

	public int getCountAllRegist() {
		return countAllRegist;
	}

	public void setCountAllRegist(int countAllRegist) {
		this.countAllRegist = countAllRegist;
	}

	public int getCountAllIsWork() {
		return countAllIsWork;
	}

	public void setCountAllIsWork(int countAllIsWork) {
		this.countAllIsWork = countAllIsWork;
	}

	public int getCountAffirm() {
		return countAffirm;
	}

	public void setCountAffirm(int countAffirm) {
		this.countAffirm = countAffirm;
	}

	public int getCountSuspected() {
		return countSuspected;
	}

	public void setCountSuspected(int countSuspected) {
		this.countSuspected = countSuspected;
	}

	public int getCountTemperatureWrong() {
		return countTemperatureWrong;
	}

	public void setCountTemperatureWrong(int countTemperatureWrong) {
		this.countTemperatureWrong = countTemperatureWrong;
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

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public CountPlagueResultVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public CountPlagueResultVO(String companyArea, String companyName, Integer companySize, int countAllRegist,
			int countAllIsWork, int countAffirm, int countSuspected, int countTemperatureWrong, String leader,
			String phone) {
		super();
		this.companyArea = companyArea;
		this.companyName = companyName;
		this.companySize = companySize;
		this.countAllRegist = countAllRegist;
		this.countAllIsWork = countAllIsWork;
		this.countAffirm = countAffirm;
		this.countSuspected = countSuspected;
		this.countTemperatureWrong = countTemperatureWrong;
		this.leader = leader;
		this.phone = phone;
	}
	

}
