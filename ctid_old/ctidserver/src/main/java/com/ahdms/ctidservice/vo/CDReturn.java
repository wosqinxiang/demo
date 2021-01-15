/**
 * Created on 2018年7月9日 by caiming
 */
package com.ahdms.ctidservice.vo;

import java.io.Serializable;

import com.ahdms.api.model.CtidMessage;

/**
 * @Title 
 * @Description 
 * @author caiming
 * @version 1.0
 * @修改记录
 * @修改序号，修改日期，修改人，修改内容
 */
public class CDReturn implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final int RESULT_ERROR_DATA_TRUNC = 0x10;
	public static final int RESULT_ERROR_COMMUNICATION = 0x20;
	public static final int RESULT_ERROR_UNKNOWN = 0x40;
	
	public static final String AUTH_ERROR_RESPONSE = "YYYY";  
	public static final String AUTH_ERROR_COMMUNICATION = "JJJJ";
	public static final String AUTH_ERROR_VERIFYSIGN = "PPPP";  //验签不通过
	public static final String AUTH_ERROR_UNKNOWN = "QQQQ";
	
	/**
	 * 业务流水号
	 */
	private String streamNum;
	
	private String downloadResult;
	
	private String errorDesc;
	
	private CtidMessage ctidMessage;

	public String getStreamNum() {
		return streamNum;
	}

	public void setStreamNum(String streamNum) {
		this.streamNum = streamNum;
	}

	public String getDownloadResult() {
		return downloadResult;
	}

	public void setDownloadResult(String downloadResult) {
		this.downloadResult = downloadResult;
	}

	public CtidMessage getCtidMessage() {
		return ctidMessage;
	}

	public void setCtidMessage(CtidMessage ctidMessage) {
		this.ctidMessage = ctidMessage;
	}

	public String getErrorDesc() {
		return errorDesc;
	}

	public void setErrorDesc(String errorDesc) {
		this.errorDesc = errorDesc;
	}
	
}

