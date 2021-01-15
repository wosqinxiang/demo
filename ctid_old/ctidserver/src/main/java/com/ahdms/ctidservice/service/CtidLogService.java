package com.ahdms.ctidservice.service;

import com.ahdms.ctidservice.bean.CtidRequestBean;
import com.ahdms.ctidservice.vo.CtidResult;
import com.ahdms.ctidservice.vo.SfxxBean;

import javax.servlet.http.HttpServletRequest;

public interface CtidLogService {

	/**
	 * <p>Title: </p>  
	 * <p>Description: </p>  
	 * @param dcaBean
	 * @param result
	 * @param ipAddress
	 */
	void insertSelective(CtidRequestBean dcaBean, CtidResult result, String ipAddress,int type,Object obj);

	void insertSelective(CtidRequestBean dcaBean, CtidResult result, String ipAddress,int type);

	void insertAuthCardLog(String appId, String businessSerialNumber, String authMode, int code, String getxM,
						   String getgMSFZHM, HttpServletRequest request, String message);

	void insertAuthCardLog(String appId, String businessSerialNumber, String authMode, int code, SfxxBean getsFXX,
						   HttpServletRequest request, String message);

    void insertAuthCtidLog(String ret, String appId, String mode, String ipAddress, String bsn);
}
