package com.ahdms.ap.vo;

import java.io.Serializable;
import java.util.List;

public class TravelerInfoVO implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MassesInfoVO massesInfoVO;
	
	private List<RetinueInfoVO> retinueInfoVOs;
	
	private String serialNum;

	public MassesInfoVO getMassesInfoVO() {
		return massesInfoVO;
	}

	public void setMassesInfoVO(MassesInfoVO massesInfoVO) {
		this.massesInfoVO = massesInfoVO;
	}

	public List<RetinueInfoVO> getRetinueInfoVOs() {
		return retinueInfoVOs;
	}

	public void setRetinueInfoVOs(List<RetinueInfoVO> retinueInfoVOs) {
		this.retinueInfoVOs = retinueInfoVOs;
	}

	public String getSerialNum() {
		return serialNum;
	}

	public void setSerialNum(String serialNum) {
		this.serialNum = serialNum;
	}
	
	

}
