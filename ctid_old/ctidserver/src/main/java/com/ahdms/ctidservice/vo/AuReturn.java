/**
 * Created on 2018年7月11日 by caiming
 */
package com.ahdms.ctidservice.vo;

import java.io.Serializable;

/**
 * @Title
 * @Description
 * @author caiming
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class AuReturn implements Serializable {

	private static final long serialVersionUID = 1L;
	
	public static final String AUTH_ERROR_RESPONSE = "XXXX";  //响应错误
	public static final String AUTH_ERROR_COMMUNICATION = "JJJJ"; //
	public static final String AUTH_ERROR_VERIFYSIGN = "PPPP";  //验签不通过
	public static final String AUTH_ERROR_UNKNOWN = "QQQQ";

	private String streamNumber;

	private String auResult;
	
	private String errorDesc;

	public String getStreamNumber() {
		return streamNumber;
	}

	public void setStreamNumber(String streamNumber) {
		this.streamNumber = streamNumber;
	}

	public String getAuResult() {
		return auResult;
	}

	public void setAuResult(String auResult) {
		this.auResult = auResult;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}

}
